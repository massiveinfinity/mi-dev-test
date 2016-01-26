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

import com.mi.androidarsenal.R;
import com.mi.androidarsenal.utility.AppConstants;
import com.mi.androidarsenal.utility.AppUtils;
import com.mi.androidarsenal.utility.DeleteItemOnClickListener;

/**
 * This is the adapter for the versions list
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class VersionsAdapter extends CursorAdapter implements AppConstants {
    private final Context mContext;
    private LayoutInflater mInflater;
    private int lastPosition;

    public VersionsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.version_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        // Find fields to populate in inflated template
        holder.nameView = (TextView) view.findViewById(R.id.versions_name);
        holder.versionView = (TextView) view.findViewById(R.id.versions_version);
        holder.versionDelete = (ImageView) view.findViewById(R.id.versions_delete);
        holder.versionEdit = (ImageView) view.findViewById(R.id.versions_edit);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        // Extract properties from cursor
        String id = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VERSION_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VERSION_NAME));
        String version = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VERSION));
        String codename = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CODENAME));
        String distribution = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESTRIBUTION));
        String target = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TARGET));

       // Populate fields with extracted properties
        holder.nameView.setText(name);
        holder.versionView.setText(version);
        holder.versionEdit.setOnClickListener(new EditItemOnClickListener(id, name, version, codename, target, distribution));
        holder.versionDelete.setOnClickListener(new DeleteItemOnClickListener(mContext, id, false));

        listScrollAnimation(view, cursor);
    }

    /**
     * When the list is scrolled, the item show up with an animation
     *
     * @param view
     * @param cursor
     */
    private void listScrollAnimation(View view, Cursor cursor){
        Animation animation = AnimationUtils.loadAnimation(mContext, (cursor.getPosition() > lastPosition) ? R.anim.list_up_from_bottom : R.anim.list_down_from_top);
        view.startAnimation(animation);
        lastPosition = cursor.getPosition();
    }

    /**
     * Edit button click listener
     */
    public class EditItemOnClickListener implements View.OnClickListener
    {
        private String mName;
        private String mVersion;
        private String mCodename;
        private String mTarget;
        private String mDistribution;
        private String mId;

        public EditItemOnClickListener(String id, String name, String version, String codename, String target, String distribution) {
            mId = id;
            mName = name;
            mVersion = version;
            mCodename = codename;
            mTarget = target;
            mDistribution = distribution;
        }

        @Override
        public void onClick(View v)
        {
            editItem(mId, mName, mVersion, mCodename, mTarget, mDistribution);

        }
    };


    /**
     * Calls the edit functionality
     *
     * @param id
     * @param name
     * @param version
     * @param codename
     * @param target
     * @param distribution
     */
    private void editItem(String id, String name, String version, String codename, String target, String distribution) {
        if(AppUtils.mOnEditItemListener != null){
            Bundle editBundle = new Bundle();
            editBundle.putBoolean(EDIT_BUNDLE_DB_TYPE, false);
            editBundle.putString(KEY_VERSION_ID, id);
            editBundle.putString(KEY_VERSION_NAME, name);
            editBundle.putString(KEY_VERSION, version);
            editBundle.putString(KEY_CODENAME, codename);
            editBundle.putString(KEY_TARGET, target);
            editBundle.putString(KEY_DESTRIBUTION, distribution);
            AppUtils.mOnEditItemListener.onEditItem(editBundle);
        }
    }

    /**
     * view holder for listview item.
     */
    static class ViewHolder {
        TextView nameView;
        TextView versionView;
        ImageView versionDelete;
        ImageView versionEdit;
    }
}
