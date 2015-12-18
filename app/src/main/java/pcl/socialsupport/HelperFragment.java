package pcl.socialsupport;

/**
 * Created by Anup on 11/15/2015.
 */
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pcl.socialsupport.Utils.GetServerResponse;
import pcl.socialsupport.Utils.GridItem;
import pcl.socialsupport.adapters.supportListViewAdapter;


public class HelperFragment extends Fragment{

    private static final String TAG = HelperFragment.class.getSimpleName();
    private ListView mListView;
    private supportListViewAdapter mListAdapter;
    private ArrayList<GridItem> mData;
    private String FEED_URL = "";
    private ProgressBar mProgressBar;
    private String server_script="get_helpers.php";
    public HelperFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getString(R.string.protocol))
                .authority(getString(R.string.server_ip))
                .appendPath(getString(R.string.pathname))
                .appendPath(server_script)
                .appendQueryParameter("user_id",getString(R.string.user_id));

        FEED_URL = builder.build().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.helpers, container, false);
        // Inflate the layout for this fragment

        mListView = (ListView) v.findViewById(R.id.supportlist);
        //mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        mData = new ArrayList<>();
        mListAdapter = new supportListViewAdapter(getActivity(), R.layout.helper_list_item, mData);
        if(mListView != null)
            mListView.setAdapter(mListAdapter);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar_supportList);


        //Start download
        new processServerResponse().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);

        return v;
    }


    private class processServerResponse extends GetServerResponse {

        @Override
        protected void onPostExecute(String result) {
            parseResult(result);
            mListAdapter.setGridData(mData);
            mProgressBar.setVisibility(View.GONE);
        }
        private void parseResult(String result) {
            try {
                //JSONObject response = new JSONObject(result);

                JSONArray posts = new JSONArray(result);//response.optJSONArray("");//response.optJSONArray("posts");
                GridItem item;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    String title = post.optString("user_name");
                    item = new GridItem();
                    item.setTitle(title);
                    // JSONArray attachments = post.getJSONArray("attachments");
                    //if (null != attachments && attachments.length() > 0) {
                    //  JSONObject attachment = attachments.getJSONObject(0);
                    //if (attachment != null)
                    //  item.setImage(attachment.getString("url"));
                    //}
                    item.setImage(post.optString("image_location"));
                    item.setId(post.optString("user_id"));
                    item.setPhone(post.optInt("phone"));
                    item.setEmail(post.optInt("email_id"));
                    mData.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

