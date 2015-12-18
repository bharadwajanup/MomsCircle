package pcl.socialsupport.adapters;

/**
 * Created by Anup on 11/9/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pcl.socialsupport.Utils.GridItem;
import pcl.socialsupport.R;

public class GridViewAdapter extends ArrayAdapter<GridItem> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData) {
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
    public void setGridData(ArrayList<GridItem> mGridData) {
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
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);

            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            holder.idView = (TextView) row.findViewById(R.id.grid_item_id);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));
        holder.idView.setText(Html.fromHtml(item.getID()));
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(getContext().getString(R.string.protocol))
                .authority(getContext().getString(R.string.server_ip))
                .appendPath(getContext().getString(R.string.pathname))
                .appendEncodedPath(item.getImage());


        String image_url =   builder.build().toString();
        //getContext().getString(R.string.hostname)+item.getImage();
        Picasso.with(mContext).load(image_url).into(holder.imageView);

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        TextView idView;
    }
}