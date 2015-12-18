package pcl.socialsupport.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pcl.socialsupport.HelperMessageFragment;
import pcl.socialsupport.R;
import pcl.socialsupport.Utils.GetServerResponse;
import pcl.socialsupport.Utils.HelperMessageItem;

/**
 * Created by Anup on 11/27/2015.
 */
public class HelperMessageViewAdapter extends ArrayAdapter<HelperMessageItem> implements View.OnClickListener{


    private Context mContext;
    private int layoutResourceId;
    private ArrayList<HelperMessageItem> mGridData = new ArrayList<HelperMessageItem>();
    private HelperMessageFragment helperMessageFragment;



    ImageButton acceptButton;
    ImageButton ignoreButton;

    public HelperMessageViewAdapter(Context mContext, int layoutResourceId, ArrayList<HelperMessageItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<HelperMessageItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.chore_description);

            holder.message = (TextView) row.findViewById(R.id.chore_message);

            holder.id = (TextView) row.findViewById(R.id.chore_activity_track_id);

            holder.buttonLayout = (LinearLayout) row.findViewById(R.id.buttonPanel);

            holder.choreTime = (TextView) row.findViewById(R.id.chore_time);
            holder.choreDate = (TextView) row.findViewById(R.id.chore_date);

            acceptButton = (ImageButton) row.findViewById(R.id.accept_button);
            ignoreButton = (ImageButton) row.findViewById(R.id.ignore_button);

            holder.notify = (TextView) row.findViewById(R.id.notify);

            acceptButton.setOnClickListener(this);
            ignoreButton.setOnClickListener(this);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        HelperMessageItem item = mGridData.get(position);

        String titleText = "",date="",time="",messageText="";


        String[] optionArray = item.getOptions().split("\\|");

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
            time = optionKeyValues.get("time");
        if(optionKeyValues.containsKey("date"))
            date = optionKeyValues.get("date");

        if(item.getAssigned_to() != 0)
        {
            titleText = item.getName();//+" has been assigned to you";

            holder.buttonLayout.setVisibility(View.GONE);
            //holder.message.setVisibility(View.GONE);
            messageText = "Assigned to you.";
            holder.notify.setText(messageText);
            holder.notify.setVisibility(View.VISIBLE);
            messageText = item.getMessage();
        }
        else
        {
            titleText = item.getName();//+" task for "+date+" at "+time;
            messageText = item.getMessage();
            holder.notify.setVisibility(View.GONE);

        }

        holder.choreDate.setText(date);
        holder.choreTime.setText(time);
        holder.title.setText(titleText);
        holder.message.setText(messageText);

        holder.id.setText(Integer.toString(item.getChoreActivityTrackId()));
        if(item.getAssigned_to() == 0)
        {
            //TextView ignoreMessageBox = (TextView) row.findViewById(R.id.ignoreText);
            if(!messageText.equals("Assigned to you.")) {
                holder.buttonLayout.setVisibility(View.VISIBLE);
                holder.message.setVisibility(View.VISIBLE);
            }
        }
        return row;
    }

    @Override
    public void onClick(View v) {

        View vp = (View)v.getParent().getParent();

        switch (v.getId())
        {
            case R.id.ignore_button:



                LinearLayout ll = (LinearLayout)vp.findViewById(R.id.buttonPanel);
                TextView message = (TextView) vp.findViewById(R.id.notify);

                ll.setVisibility(View.GONE);

                message.setText("You have Ignored this Task.");
                message.setVisibility(View.VISIBLE);
               /* TextView tv = (TextView) vp.findViewById(R.id.ignoreText);
                if(ll != null){

                    tv.setVisibility(View.VISIBLE);
                }*/
                break;

            case R.id.accept_button:
                TextView hiddenId = (TextView) vp.findViewById(R.id.chore_activity_track_id);
                String chore_activity_track_id = hiddenId.getText().toString();
                setTaskToUser(chore_activity_track_id);
                break;
        }

    }

    private void setTaskToUser(String chore_activity_track_id) {
        String FEED_URL="";
        String server_script = "set_task.php";
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getContext().getString(R.string.protocol))
                .authority(getContext().getString(R.string.server_ip))
                .appendPath(getContext().getString(R.string.pathname))
                .appendPath(server_script)
                .appendQueryParameter("user_id", getContext().getString(R.string.user_id))
                .appendQueryParameter("chore_activity_track_id", chore_activity_track_id);

        FEED_URL = builder.build().toString();

        new processServerResponse().execute(FEED_URL);


    }


    private class processServerResponse extends GetServerResponse {

        @Override
        protected void onPostExecute(String result) {
            if(result.contentEquals("Update Failed!") || result.contentEquals("Task already assigned")) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
            else {
                ArrayList<HelperMessageItem> mData = parseResult(result);
                mGridData = mData;
                notifyDataSetChanged();
                Toast.makeText(getContext(),"You accepted this task",Toast.LENGTH_SHORT).show();
            }


        }
        private ArrayList<HelperMessageItem> parseResult(String result) {
            try {
                //JSONObject response = new JSONObject(result);
                ArrayList<HelperMessageItem> mData = new ArrayList<HelperMessageItem>();
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
                return mData;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    static class ViewHolder {
        TextView title;
        TextView message;
        TextView id;
        LinearLayout buttonLayout;
        TextView choreDate;
        TextView choreTime;

        TextView notify;

    }
}
