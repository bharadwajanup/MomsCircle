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
import java.util.HashMap;

import pcl.socialsupport.Utils.AcceptListItem;
import pcl.socialsupport.Utils.GetServerResponse;
import pcl.socialsupport.adapters.AcceptListViewAdapter;


public class StatusAcceptedNestedFragment extends Fragment{

    private static final String TAG = StatusAcceptedNestedFragment.class.getSimpleName();
    private ListView mListView;
    private AcceptListViewAdapter mListAdapter;
    private ArrayList<AcceptListItem> mData;
    private String FEED_URL = "";
    private ProgressBar mProgressBar;
    private String server_script="status.php";
    private BroadcastReceiver mMessageReceiver;

    public StatusAcceptedNestedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getString(R.string.protocol))
                .authority(getString(R.string.server_ip))
                .appendEncodedPath(getString(R.string.pathname))
                .appendEncodedPath(server_script)
                .appendQueryParameter("user_id", getString(R.string.user_id))
                .appendQueryParameter("type","A");

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
        View v = inflater.inflate(R.layout.status_accepted, container, false);

        //FEED_URL = getString(R.string.hostname)+"status.php?user_id=1&type=A";

        mListView = (ListView) v.findViewById(R.id.acceptList);
        //mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        mData = new ArrayList<>();
        mListAdapter = new AcceptListViewAdapter(getActivity(), R.layout.status_accepted_item, mData);
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
                mData = new ArrayList<>();
                mListAdapter = new AcceptListViewAdapter(getActivity(), R.layout.status_accepted_item, mData);
                if(mListView != null)
                    mListView.setAdapter(mListAdapter);
                //Start download
                new processServerResponse().execute(FEED_URL);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        };

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
                AcceptListItem item;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    String supportName = post.optString("support_name");
                    String choreName = post.optString("chore_name");
                    String options = post.optString("options");
                    String image = post.optString("image");
                    int phone = post.optInt("phone");

                    item = new AcceptListItem();

                    item.setChore_name(choreName);
                    item.setSupport_name(supportName);
                    item.setSupport_image(image);
                    item.setPhone(phone);

                    //Process the options and get the time...

                    String[] optionArray = options.split("\\|");

                    HashMap<String,String> optionKeyValues = new HashMap<String,String>();

                    for (String opt : optionArray )
                    {
                        String[] optFrag = opt.split("=");
                        if(optFrag.length>0)
                        {
                            optionKeyValues.put(optFrag[0],optFrag[1]);
                        }
                    }

                    if(optionKeyValues.containsKey("time"))
                        item.setSupport_time(optionKeyValues.get("time"));
                    else
                        item.setSupport_time("");

                    if(optionKeyValues.containsKey("date"))
                        item.setSupport_date(optionKeyValues.get("date"));
                    else
                        item.setSupport_date("");



                    mData.add(item);
                    
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
