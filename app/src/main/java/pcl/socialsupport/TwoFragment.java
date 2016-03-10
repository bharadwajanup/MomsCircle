package pcl.socialsupport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pcl.socialsupport.Utils.AcceptListItem;
import pcl.socialsupport.Utils.GetServerResponse;


public class TwoFragment extends Fragment implements View.OnClickListener{

    private TextView mom_name;
    private TextView  mom_address;
    private TextView mom_phone;
    private ImageView mom_image;
    private String FEED_URL;
    private String server_script="get_mom_info.php";
    private ImageButton phone;
    private ImageButton message;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mom_profile, container, false);// Inflate the layout for this fragment

        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getString(R.string.protocol))
                .authority(getString(R.string.server_ip))
                .appendPath(getString(R.string.pathname))
                .appendPath(server_script)
                .appendQueryParameter("user_id", getString(R.string.user_id));

        FEED_URL = builder.build().toString();

        mom_name = (TextView) v.findViewById(R.id.mom_name);
        mom_address =  (TextView) v.findViewById(R.id.mom_address);
        mom_phone = (TextView) v.findViewById(R.id.mom_phone_number);
        mom_image = (ImageView) v.findViewById(R.id.mom_image);

        phone = (ImageButton) v.findViewById(R.id.mom_phone);
        message = (ImageButton) v.findViewById(R.id.mom_message);

        phone.setOnClickListener(this);
        message.setOnClickListener(this);



        new processServerResponse().execute(FEED_URL);
        return v;
    }

    @Override
    public void onClick(View v) {
        View parent = (View)v.getParent();
        TextView phone_view = (TextView) parent.findViewById(R.id.mom_phone_number);
        String phone_number = phone_view.getText().toString();

        switch (v.getId())
        {
            case R.id.mom_phone: Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                getContext().startActivity(callIntent);
                break;
            case R.id.mom_message: Intent messageIntent = new Intent(Intent.ACTION_VIEW);
                messageIntent.setData(Uri.parse("sms:" + phone_number));
                messageIntent.putExtra("sms_body","Hey there!");
                getContext().startActivity(messageIntent);
                break;
        }

    }


    private class processServerResponse extends GetServerResponse {

        @Override
        protected void onPostExecute(String result) {
            parseResult(result);
        }

        private void parseResult(String result) {
            try {
                //JSONObject response = new JSONObject(result);

                JSONArray posts = new JSONArray(result);//response.optJSONArray("");//response.optJSONArray("posts");
                AcceptListItem item;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    String userName = post.optString("user_name");
                    String userAddress = post.optString("user_address");
                    String userPhone = post.optString("phone");
                    String userImage = post.optString("image_location");

                    Uri.Builder builder = new Uri.Builder();

                    mom_name.setText(userName);
                    mom_address.setText(userAddress);
                    mom_phone.setText(userPhone);

                    builder.scheme(getContext().getString(R.string.protocol))
                            .authority(getContext().getString(R.string.server_ip))
                            .appendEncodedPath(getContext().getString(R.string.pathname))
                            .appendEncodedPath(userImage);


                    String image_url =   builder.build().toString();
                    //getContext().getString(R.string.hostname)+item.getImage();
                    Picasso.with(getContext()).load(image_url).into(mom_image);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
