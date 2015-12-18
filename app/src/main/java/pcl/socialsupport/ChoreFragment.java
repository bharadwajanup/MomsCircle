package pcl.socialsupport;

/**
 * Created by Anup on 11/15/2015.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pcl.socialsupport.Utils.GetServerResponse;
import pcl.socialsupport.Utils.GridItem;
import pcl.socialsupport.adapters.GridViewAdapter;


public class ChoreFragment extends Fragment{

    private static final String TAG = ChoreFragment.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    //private String FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";
    private String FEED_URL = "";
    private String server_script = "getchores.php";

    public ChoreFragment() {
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
        View v = inflater.inflate(R.layout.chores, container, false);
        mGridView = (GridView) v.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.chore_item, mGridData);

        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(getActivity(), ChoreInsertActivity.class);

                TextView name = (TextView) v.findViewById(R.id.grid_item_title);

                TextView choreId = (TextView) v.findViewById(R.id.grid_item_id);
                intent.putExtra("name", name.getText());
                intent.putExtra("id", choreId.getText());
                if(name.getText().toString().equals("Add a Task"))
                    return;
                else if(name.getText().toString().equals("SOS"))
                {
                    ViewPager cp = (ViewPager) getActivity().findViewById(R.id.viewpager);

                    if(cp != null)
                    {
                        cp.setCurrentItem(1,true);
                    }
                    else {
                    Intent fragIntent = new Intent(getActivity(),MainActivity.class);
                    fragIntent.putExtra("Fragment",1);
                    startActivity(fragIntent);
                    }
                    return;
                }
                //Start details activity

                startActivity(intent);
            }
        });

        new processServerResponse().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    private class processServerResponse extends GetServerResponse {

        @Override
        protected void onPostExecute(String result) {
            parseResult(result);
            mGridAdapter.setGridData(mGridData);
            mProgressBar.setVisibility(View.GONE);
        }
        private void parseResult(String result) {
            try {
                //JSONObject response = new JSONObject(result);

                JSONArray posts = new JSONArray(result);//response.optJSONArray("");//response.optJSONArray("posts");
                GridItem item;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    String title = post.optString("chore_name");
                    item = new GridItem();
                    item.setTitle(title);
                    // JSONArray attachments = post.getJSONArray("attachments");
                    //if (null != attachments && attachments.length() > 0) {
                    //  JSONObject attachment = attachments.getJSONObject(0);
                    //if (attachment != null)
                    //  item.setImage(attachment.getString("url"));
                    //}
                    item.setImage(post.optString("chore_image_name"));
                    item.setId(post.optString("chore_id"));
                    mGridData.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
