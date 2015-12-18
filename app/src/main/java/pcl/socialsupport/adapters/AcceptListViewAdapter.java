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
import pcl.socialsupport.Utils.AcceptListItem;

/**
 * Created by Anup on 11/18/2015.
 */
public class AcceptListViewAdapter extends ArrayAdapter implements View.OnClickListener{

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<AcceptListItem> mData = new ArrayList<AcceptListItem>();
    private ImageButton phoneIcon;
    private ImageButton messageIcon;

    public AcceptListViewAdapter(Context context, int layoutResId, ArrayList<AcceptListItem> mobileData)
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
    public void setGridData(ArrayList<AcceptListItem> mGridData) {
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
            holder.supportName = (TextView) row.findViewById(R.id.support_name);

            holder.supportImageView = (ImageView) row.findViewById(R.id.support_image);
            holder.choreName = (TextView) row.findViewById(R.id.chore_name);
            holder.supportTime = (TextView) row.findViewById(R.id.chore_time);
            holder.phone_view = (TextView) row.findViewById(R.id.phone_number);
            holder.supportDate = (TextView) row.findViewById(R.id.chore_date);

            phoneIcon = (ImageButton) row.findViewById(R.id.callButton);
            messageIcon = (ImageButton) row.findViewById(R.id.textButton);

            phoneIcon.setOnClickListener(this);
            messageIcon.setOnClickListener(this);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        AcceptListItem item = mData.get(position);
        holder.supportName.setText(Html.fromHtml(item.getSupport_name()));
        holder.supportTime.setText(Html.fromHtml(item.getSupport_time()));
        holder.choreName.setText(Html.fromHtml(item.getChore_name()));
        holder.phone_view.setText(Integer.toString(item.getPhone()));
        holder.supportDate.setText(Html.fromHtml(item.getSupport_date()));


        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getContext().getString(R.string.protocol))
                .authority(getContext().getString(R.string.server_ip))
                .appendPath(getContext().getString(R.string.pathname))
                .appendEncodedPath(item.getSupport_image());


        String image_url =   builder.build().toString();


        //Uncomment this to implement images...
        //holder.idView.setText(Html.fromHtml(item.getID()));
        //String image_url =    "http://149.160.253.196/saware/"+item.getImage();
        Picasso.with(mContext).load(image_url).into(holder.supportImageView);

        return row;
    }

    @Override
    public void onClick(View v) {
        View parent = (View)v.getParent();
        TextView phone_view = (TextView) parent.findViewById(R.id.phone_number);
        String phone_number = phone_view.getText().toString();

        switch (v.getId())
        {
            case R.id.callButton: Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                getContext().startActivity(callIntent);
                break;
            case R.id.textButton: Intent messageIntent = new Intent(Intent.ACTION_VIEW);
                messageIntent.setData(Uri.parse("sms:" + phone_number));
                messageIntent.putExtra("sms_body","Hey there!");
                getContext().startActivity(messageIntent);
                break;
        }
    }

    static class ViewHolder {
        TextView supportName;
        TextView supportTime;
        TextView supportDate;
        TextView choreName;
        ImageView supportImageView;
        TextView phone_view;
    }
}
