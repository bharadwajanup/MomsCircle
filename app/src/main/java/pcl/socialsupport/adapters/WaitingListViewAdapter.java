package pcl.socialsupport.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pcl.socialsupport.Utils.AcceptListItem;
import pcl.socialsupport.R;

/**
 * Created by Anup on 11/18/2015.
 */
public class WaitingListViewAdapter extends ArrayAdapter {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<AcceptListItem> mData = new ArrayList<AcceptListItem>();

    public WaitingListViewAdapter(Context context, int layoutResId, ArrayList<AcceptListItem> mobileData)
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



            holder.choreName = (TextView) row.findViewById(R.id.chore_name_waiting);
            holder.supportTime = (TextView) row.findViewById(R.id.chore_time_waiting);
            holder.supportDate = (TextView) row.findViewById(R.id.chore_date);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        AcceptListItem item = mData.get(position);

        holder.supportTime.setText(Html.fromHtml(item.getSupport_time()));
        holder.choreName.setText(Html.fromHtml(item.getChore_name()));
        holder.supportDate.setText(Html.fromHtml(item.getSupport_date()));

        //Uncomment this to implement images...
        //holder.idView.setText(Html.fromHtml(item.getID()));
        //String image_url =    "http://149.160.253.196/saware/"+item.getImage();
        //Picasso.with(mContext).load(image_url).into(holder.imageView);

        return row;
    }

    static class ViewHolder {
        TextView supportTime;
        TextView choreName;
        TextView supportDate;
    }
}
