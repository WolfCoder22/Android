package alexwolf.cs.dartmouth.edu.stressmeter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by alexwolf on 1/23/16. Used code from
 * http://developer.android.com/guide/topics/ui/layout/gridview.html#example
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    PSM psm= new PSM();
    int mGridId;

    //constructor for the Image adaptor taking
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 16;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(175, 175));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1,4,1,4);
        } else {
            imageView = (ImageView) convertView;
        }


        imageView.setImageResource(psm.getGridById(mGridId)[position]);
        return imageView;
    }

}