package pcl.socialsupport.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pcl.socialsupport.R;
import pcl.socialsupport.Utils.GridItem;

/**
 * Created by Anup on 11/12/2015.
 */
public class supportListViewAdapter extends ArrayAdapter implements View.OnClickListener{

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mData = new ArrayList<GridItem>();
    private ImageButton callButton;
    private ImageButton messageButton;


    public supportListViewAdapter(Context context, int layoutResId, ArrayList<GridItem> mobileData)
    {
        super(context,layoutResId,mobileData);

        layoutResourceId = layoutResId;
        mContext = context;
        mData = mobileData;

    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mData = mGridData;
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
            holder.titleTextView = (TextView) row.findViewById(R.id.contact_name);

            holder.phone  = (TextView) row.findViewById(R.id.phone_number);

            holder.imageView = (ImageView) row.findViewById(R.id.contact_image);
           callButton = (ImageButton) row.findViewById(R.id.phone);
            messageButton = (ImageButton) row.findViewById(R.id.message);

            callButton.setOnClickListener(this);
            messageButton.setOnClickListener(this);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));
        holder.phone.setText(Integer.toString(item.getPhone()));

        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getContext().getString(R.string.protocol))
                .authority(getContext().getString(R.string.server_ip))
                .appendPath(getContext().getString(R.string.pathname))
                .appendEncodedPath(item.getImage());


        String image_url =   builder.build().toString();


        //Uncomment this to implement images...
        //holder.idView.setText(Html.fromHtml(item.getID()));

        Picasso.with(mContext).load(image_url).into(holder.imageView);

        return row;
    }

    @Override
    public void onClick(View v) {
        View parent = (View)v.getParent();
        TextView phone_view = (TextView) parent.findViewById(R.id.phone_number);
        String phone_number = phone_view.getText().toString();

        switch (v.getId())
        {
            case R.id.phone: Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                getContext().startActivity(callIntent);
                break;
            case R.id.message: Intent messageIntent = new Intent(Intent.ACTION_VIEW);
                messageIntent.setData(Uri.parse("sms:" + phone_number));
                messageIntent.putExtra("sms_body","Hey there!");
                getContext().startActivity(messageIntent);
                break;
        }

    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        TextView phone;
       // TextView idView;
    }

}
