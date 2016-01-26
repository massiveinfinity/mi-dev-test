package com.mi.androidarsenal.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.mi.androidarsenal.R;
import com.mi.androidarsenal.fragment.AndroidVersionsFragment;
import com.mi.androidarsenal.fragment.DeviceDetailFragment;
import com.mi.androidarsenal.fragment.DevicesFragment;
import com.mi.androidarsenal.fragment.VersionDetailFragment;
import com.mi.androidarsenal.network.RequestOperation;
import com.mi.androidarsenal.utility.AndroidResponseObserver;
import com.mi.androidarsenal.utility.AppConstants;
import com.mi.androidarsenal.utility.AppUtils;
import com.mi.androidarsenal.utility.OfflineUtil;
import com.mi.androidarsenal.utility.OnEditItemListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the activity which hosts all the required fragment and is basically the base of the application
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements AndroidResponseObserver, DevicesFragment.OnDeviceSelectedListener, AppConstants, OnEditItemListener, RequestOperation.OnRefreshDbListener, AndroidVersionsFragment.OnVersionSelectedListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;
    private LinearLayout mTabContainer;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabContainer = (LinearLayout) findViewById(R.id.tab_container);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(null);
            }
        });

        AppUtils.setOnEditItemListener(this);
        RequestOperation.setOnRefreshDbListener(this);

        //fetch the data from server
        fetchDataFromServer();
    }

    /**
     * initiates the network request for all the data from server
     */
    private void fetchDataFromServer() {
        //check network availability
        if (AppUtils.isNetworkAvailable(MainActivity.this.getApplicationContext())) {
            RequestOperation requestOperation = RequestOperation.getRequestOperationInstance(MainActivity.this);
            requestOperation.getAllData(MainActivity.this);
        }
    }

    /**
     * sets up the view pager for the application
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DevicesFragment(), "Android Devices");
        adapter.addFragment(new AndroidVersionsFragment(), "Android Versions");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAllDataGetResponse(String jsonResponse) {
        if (!TextUtils.isEmpty(jsonResponse)) {
            OfflineUtil offlineUtil = new OfflineUtil(MainActivity.this);
            offlineUtil.populateResponseInMemory(jsonResponse);
        }
    }

    @Override
    public void onEditItem(Bundle editBundle) {
        //show the edit dialog
        showDialog(editBundle);
    }

    @Override
    public void onRefreshDb() {
        fetchDataFromServer();
    }

    /**
     * viewpager adapter class
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     * Implements the edit and add dialog (for POST and PUT requests)
     *
     * @param editBundle
     */
    private void showDialog(final Bundle editBundle) {
        //initialize views
        final View dialogView = View.inflate(MainActivity.this, R.layout.info_field, null);
        final View view = dialogView.findViewById(R.id.reveal_view);
        final LinearLayout deviceLayout = (LinearLayout) dialogView.findViewById(R.id.device_layout);
        final EditText nameView = (EditText) dialogView.findViewById(R.id.device_dialog_name_field);
        final EditText androidIdView = (EditText) dialogView.findViewById(R.id.device_dialog_id_field);
        final EditText imageView = (EditText) dialogView.findViewById(R.id.device_dialog_image_field);
        final EditText carrierView = (EditText) dialogView.findViewById(R.id.device_dialog_carrier_field);
        final EditText descriptionView = (EditText) dialogView.findViewById(R.id.device_dialog_snippet_field);
        final LinearLayout versionLayout = (LinearLayout) dialogView.findViewById(R.id.version_layout);
        final EditText versionNameView = (EditText) dialogView.findViewById(R.id.version_dialog_name_field);
        final EditText codenameView = (EditText) dialogView.findViewById(R.id.version_dialog_codename_field);
        final EditText destributionView = (EditText) dialogView.findViewById(R.id.version_dialog_distribution_field);
        final EditText targetView = (EditText) dialogView.findViewById(R.id.version_dialog_target_field);
        final EditText versionView = (EditText) dialogView.findViewById(R.id.version_dialog_version_field);
        Button saveBtn = (Button) dialogView.findViewById(R.id.btn_save);
        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.dialog_spinner);

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.android));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_dropdown, list);
        spinner.setAdapter(adapter);

        //check for add or edit(POST or PUT)
        if (editBundle == null) {
            //Add operation
            saveBtn.setText(getResources().getString(R.string.add));
            spinner.setSelection(mViewPager.getCurrentItem());
        } else {
            spinner.setEnabled(false);

            //edit operation
            saveBtn.setText(getResources().getString(R.string.update));

            boolean isDevice = editBundle.getBoolean(EDIT_BUNDLE_DB_TYPE);
            if (isDevice) {
                //select the device option
                spinner.setSelection(0);

                String carrier = editBundle.getString(KEY_CARRIER);
                String snippet = editBundle.getString(KEY_SNIPPET);
                String imageUrl = editBundle.getString(KEY_IMAGE_URL);
                String androidId = editBundle.getString(KEY_ANDROID_ID);
                String deviceName = editBundle.getString(KEY_NAME);

                nameView.setText(deviceName);
                carrierView.setText(carrier);
                descriptionView.setText(snippet);
                imageView.setText(imageUrl);
                androidIdView.setText(androidId);

            } else {
                //select the version option
                spinner.setSelection(1);
                String versionName = editBundle.getString(KEY_VERSION_NAME);
                String version = editBundle.getString(KEY_VERSION);
                String target = editBundle.getString(KEY_TARGET);
                String destribution = editBundle.getString(KEY_DESTRIBUTION);
                String codename = editBundle.getString(KEY_CODENAME);
                versionView.setText(version);
                versionNameView.setText(versionName);
                targetView.setText(target);
                destributionView.setText(destribution);
                codenameView.setText(codename);
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    deviceLayout.setVisibility(View.VISIBLE);
                    versionLayout.setVisibility(View.GONE);
                } else {
                    deviceLayout.setVisibility(View.GONE);
                    versionLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView)
                .setCancelable(true);


        mDialog = builder.create();
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    revealShow(view, true, null);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialogManeuver(view);
            }
        });

        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dismissDialogManeuver(view);
                    return true;
                }
                return false;
            }
        });

        destributionView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submitRequest(editBundle, view, spinner, nameView, descriptionView, carrierView, androidIdView, imageView, versionNameView, versionView, codenameView, targetView, destributionView);
                    return true;
                }
                return false;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           submitRequest(editBundle, view, spinner, nameView, descriptionView, carrierView, androidIdView, imageView, versionNameView, versionView, codenameView, targetView, destributionView);
                                       }
                                   }
        );

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    /**
     * Submits the request to the server after verifying whether its for POST or PUT
     *
     * @param editBundle
     * @param view
     * @param spinner
     * @param nameView
     * @param descriptionView
     * @param carrierView
     * @param androidIdView
     * @param imageView
     * @param versionNameView
     * @param versionView
     * @param codenameView
     * @param targetView
     * @param destributionView
     */
    private void submitRequest(Bundle editBundle, View view, Spinner spinner, EditText nameView, EditText descriptionView, EditText carrierView, EditText androidIdView, EditText imageView, EditText versionNameView, EditText versionView, EditText codenameView, EditText targetView, EditText destributionView) {
        if (AppUtils.isNetworkAvailable(getApplicationContext())) {
            RequestOperation requestOperation = RequestOperation.getRequestOperationInstance(MainActivity.this);
            if (editBundle == null) {
                //Add operation
                if (spinner.getSelectedItemPosition() == 0) {
                    boolean isSomethingThere = validate(new EditText[]{nameView, descriptionView, carrierView, androidIdView, imageView});
                    if (isSomethingThere) {
                        requestOperation.postDeviceInfo(nameView.getText().toString(), descriptionView.getText().toString(), carrierView.getText().toString(), androidIdView.getText().toString(), imageView.getText().toString());
                        dismissDialogManeuver(view);
                    } else
                        Toast.makeText(MainActivity.this, EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                } else {
                    boolean isSomethingThere = validate(new EditText[]{versionNameView, versionView, codenameView, targetView, destributionView});
                    if (isSomethingThere) {
                        requestOperation.postVersionInfo(versionNameView.getText().toString(), versionView.getText().toString(), codenameView.getText().toString(), targetView.getText().toString(), destributionView.getText().toString());
                        dismissDialogManeuver(view);
                    } else
                        Toast.makeText(MainActivity.this, EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                }

            } else {
                //Update operation
                boolean isDevice = editBundle.getBoolean(EDIT_BUNDLE_DB_TYPE);

                if (isDevice) {
                    boolean isSomethingThere = validate(new EditText[]{nameView, descriptionView, carrierView, androidIdView, imageView});
                    if (isSomethingThere) {
                        requestOperation.putDeviceInfo(editBundle.getString(KEY_DEVICE_ID), nameView.getText().toString(), descriptionView.getText().toString(), carrierView.getText().toString(), androidIdView.getText().toString(), imageView.getText().toString());
                        dismissDialogManeuver(view);
                    } else
                        Toast.makeText(MainActivity.this, EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                } else {
                    boolean isSomethingThere = validate(new EditText[]{versionNameView, versionView, codenameView, targetView, destributionView});
                    if (isSomethingThere) {
                        requestOperation.putVersionInfo(editBundle.getString(KEY_VERSION_ID), versionNameView.getText().toString(), versionView.getText().toString(), codenameView.getText().toString(), targetView.getText().toString(), destributionView.getText().toString());
                        dismissDialogManeuver(view);
                    } else
                        Toast.makeText(MainActivity.this, EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Validate for all empty fields
     * returns false if all the fields are empty.
     *
     * @param fields
     * @return
     */
    private boolean validate(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dismiss dialog with animation
     *
     * @param view
     */
    private void dismissDialogManeuver(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            revealShow(view, false, mDialog);
        } else {
            mDialog.dismiss();
            view.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * reveal animation for dialog
     *
     * @param view
     * @param reveal
     * @param dialog
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealShow(final View view, boolean reveal, final AlertDialog dialog) {
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if (reveal) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                    w / 2, h / 2, 0, maxRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.start();

        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        }

    }

    @Override
    public void onVersionItemSelected(String id, String name, String version, String codename, String target, String distribution) {
        VersionDetailFragment fragment = new VersionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_VERSION_ID, id);
        bundle.putString(KEY_VERSION_NAME, name);
        bundle.putString(KEY_VERSION, version);
        bundle.putString(KEY_CODENAME, codename);
        bundle.putString(KEY_TARGET, target);
        bundle.putString(KEY_DESTRIBUTION, distribution);
        fragment.setArguments(bundle);
        addFragment(fragment);
    }

    @Override
    public void onDeviceItemSelected(String id, String name, String imageUrl, String snippet, String carrier, String android_id) {
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_DEVICE_ID, id);
        bundle.putString(KEY_NAME, name);
        bundle.putString(KEY_IMAGE_URL, imageUrl);
        bundle.putString(KEY_SNIPPET, snippet);
        bundle.putString(KEY_CARRIER, carrier);
        bundle.putString(KEY_ANDROID_ID, android_id);

        fragment.setArguments(bundle);
        addFragment(fragment);
    }

    /**
     * Adds fragment
     *
     * @param fragment
     */
    private void addFragment(Fragment fragment) {
        hideFabAndTab();
        FragmentManager fm = MainActivity.this.getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        if (fragment != null) {
            ft.add(R.id.detail_fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    /**
     * hides Floating action button and Tabs for fragment to show up
     */
    private void hideFabAndTab() {
        mFab.setVisibility(View.GONE);
        mTabContainer.setVisibility(View.GONE);
    }

    /**
     * shows Floating action button and Tabs
     */
    private void showFabAndTab() {
        mFab.setVisibility(View.VISIBLE);
        mTabContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        int backCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backCount > 0) {
            showFabAndTab();
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }
}
