package com.mi.androidarsenal.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mi.androidarsenal.R;
import com.mi.androidarsenal.network.VolleyHelper;
import com.mi.androidarsenal.utility.AppConstants;
import com.mi.androidarsenal.utility.AppUtils;
import com.mi.androidarsenal.utility.DeleteItemOnClickListener;

/**
 * This is the adapter for the device list
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class DevicesAdapter extends CursorAdapter implements AppConstants {
    private final Context mContext;
    private LayoutInflater mInflater;
    private int lastPosition;

    public DevicesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.devices_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        // Find fields to populate in inflated template
        holder.thumbnail = (NetworkImageView) view.findViewById(R.id.device_thumbnail);
        holder.nameView = (TextView) view.findViewById(R.id.device_name);
        holder.carrierView = (TextView) view.findViewById(R.id.device_carrier);
        holder.deviceDelete = (ImageView) view.findViewById(R.id.device_delete);
        holder.deviceEdit = (ImageView) view.findViewById(R.id.device_edit);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        // Extract properties from cursor
        String id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEVICE_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL));
        String snippet = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SNIPPET));
        String carrier = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CARRIER));
        String android_id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANDROID_ID));

        // Populate fields with extracted properties
        holder.nameView.setText(name);
        holder.carrierView.setText(carrier);
        holder.thumbnail.setImageUrl(imageUrl,
                VolleyHelper.getInstance(context).getImageLoader());
        holder.deviceEdit.setOnClickListener(new EditItemOnClickListener(id, name, snippet, carrier, android_id, imageUrl));
        holder.deviceDelete.setOnClickListener(new DeleteItemOnClickListener(mContext, id, true));

        listScrollAnimation(view, cursor);
    }

    /**
     * When the list is scrolled, the item show up with an animation
     *
     * @param view
     * @param cursor
     */
    private void listScrollAnimation(View view, Cursor cursor) {
        Animation animation = AnimationUtils.loadAnimation(mContext, (cursor.getPosition() > lastPosition) ? R.anim.list_up_from_bottom : R.anim.list_down_from_top);
        view.startAnimation(animation);
        lastPosition = cursor.getPosition();
    }

    /**
     * Edit button click listener
     */
    public class EditItemOnClickListener implements View.OnClickListener {
        private String mName;
        private String mSnippet;
        private String mCarrier;
        private String mAndroidId;
        private String mImageUrl;
        private String mId;

        public EditItemOnClickListener(String id, String name, String snippet, String carrier, String android_id, String imageUrl) {
            mId = id;
            mName = name;
            mSnippet = snippet;
            mCarrier = carrier;
            mAndroidId = android_id;
            mImageUrl = imageUrl;
        }

        @Override
        public void onClick(View v) {
            editItem(mId, mName, mSnippet, mCarrier, mAndroidId, mImageUrl);
        }
    }

    /**
     * Calls the edit functionality
     *
     * @param id
     * @param name
     * @param snippet
     * @param carrier
     * @param android_id
     * @param imageUrl
     */
    private void editItem(String id, String name, String snippet, String carrier, String android_id, String imageUrl) {
        if (AppUtils.mOnEditItemListener != null) {
            Bundle editBundle = new Bundle();
            editBundle.putBoolean(EDIT_BUNDLE_DB_TYPE, true);
            editBundle.putString(KEY_DEVICE_ID, id);
            editBundle.putString(KEY_NAME, name);
            editBundle.putString(KEY_SNIPPET, snippet);
            editBundle.putString(KEY_CARRIER, carrier);
            editBundle.putString(KEY_ANDROID_ID, android_id);
            editBundle.putString(KEY_IMAGE_URL, imageUrl);
            AppUtils.mOnEditItemListener.onEditItem(editBundle);
        }
    }

    /**
     * view holder for listview item.
     */
    static class ViewHolder {
        TextView nameView;
        TextView carrierView;
        NetworkImageView thumbnail;
        ImageView deviceDelete;
        ImageView deviceEdit;
    }
}
