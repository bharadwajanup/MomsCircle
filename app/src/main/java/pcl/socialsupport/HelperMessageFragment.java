package pcl.socialsupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pcl.socialsupport.Utils.GetServerResponse;
import pcl.socialsupport.Utils.HelperMessageItem;
import pcl.socialsupport.adapters.HelperMessageViewAdapter;


/**
 * Created by Anup on 11/27/2015.
 */
public class HelperMessageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = HelperMessageFragment.class.getSimpleName();
    private ListView mListView;
    private HelperMessageViewAdapter mListAdapter;
    private ArrayList<HelperMessageItem> mData;
    private String FEED_URL = "";
    private ProgressBar mProgressBar;
    private String server_script="get_helper_tasks.php";
    private BroadcastReceiver mMessageReceiver;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getString(R.string.protocol))
                .authority(getString(R.string.server_ip))
                .appendEncodedPath(getString(R.string.pathname))
                .appendPath(server_script)
                .appendQueryParameter("user_id", getString(R.string.user_id));

        FEED_URL = builder.build().toString();

    }

    @Override
    public void onResume() {
        super.onResume();
        getContext() .registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
    }

    //Must unregister onPause()
    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mMessageReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.helper_message, container, false);

        //FEED_URL = getString(R.string.hostname)+"status.php?user_id=1&type=A";

        mListView = (ListView) v.findViewById(R.id.helperMessage);
        //mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Initialize with empty data
        mData = new ArrayList<HelperMessageItem>();
        mListAdapter = new HelperMessageViewAdapter(getActivity(), R.layout.helper_message_item, mData);
        if(mListView != null)
            mListView.setAdapter(mListAdapter);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar_accept);
        //Start download
        new processServerResponse().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);


        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String message = intent.getStringExtra("message");

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                mData = new ArrayList<HelperMessageItem>();
                mListAdapter = new HelperMessageViewAdapter(getActivity(), R.layout.helper_message_item, mData);
                if(mListView != null)
                    mListView.setAdapter(mListAdapter);
                //Start download
                new processServerResponse().execute(FEED_URL);
                mProgressBar.setVisibility(View.VISIBLE);

            }
        };



        return v;
    }

    public void refresh()
    {
        mData = new ArrayList<HelperMessageItem>();
        new processServerResponse().execute(FEED_URL);


    }

    @Override
    public void onClick(View v) {

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
                HelperMessageItem item;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    String choreName = post.optString("chore_name");
                    String message = post.optString("message");
                    String options = post.optString("options");
                    int chore_activity_id = post.optInt("chore_activity_track_id");
                    int assigned_to = post.optInt("assigned_to");


                    item = new HelperMessageItem();

                    item.setName(choreName);
                    item.setOptions(options);
                    item.setAssigned_to(assigned_to);
                    item.setMessage(message);
                    item.setChoreActivityTrackId(chore_activity_id);

                    //Process the options and get the time...



                    mData.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
