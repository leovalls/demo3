package com.leovalls.demo3.data;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.leovalls.demo3.R;
import com.leovalls.demo3.activities.MainActivity;
import com.leovalls.demo3.utility.BitmapLRUCache;

public class ImageAdapter extends BaseAdapter {

	private ImageLoader imageLoader;
	private ArrayList<Image> dataArray;
    private LayoutInflater inflater;
    
	public ImageAdapter(Context context, ArrayList<Image> dataArray) {
        this.dataArray = dataArray;
	    this.inflater = LayoutInflater.from(context);
	    this.imageLoader = new ImageLoader(MainActivity.resquestQueue, new BitmapLRUCache());
	}
        
    @Override
    public int getCount() {
            return dataArray.size();
    }

    @Override
    public Object getItem(int position) {
            return null;
    }

    @Override
    public long getItemId(int position) {
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Image current = dataArray.get(position);
    if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_image, null);

            holder = new ViewHolder();
            holder.imgFlag = (NetworkImageView) convertView.findViewById(R.id.imageFlag);
            holder.txtName = (TextView) convertView.findViewById(R.id.textName);
            
            convertView.setTag(holder);                
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
    
    	holder.imgFlag.setImageUrl(current.getImageUrl(), imageLoader);

        holder.txtName.setText(current.getUserName());
    
        return convertView;
    }
    
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
            int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    
    public static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth) {
		
		    final int heightRatio = Math.round((float) height / (float) reqHeight);
		    final int widthRatio = Math.round((float) width / (float) reqWidth);
		
		    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		
		return inSampleSize;
	}        
    
    static class ViewHolder {
            public NetworkImageView imgFlag;
            public TextView txtName;
    }

}