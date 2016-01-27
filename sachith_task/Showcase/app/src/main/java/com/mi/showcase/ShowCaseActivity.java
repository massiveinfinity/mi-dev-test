package com.mi.showcase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mi.showcase.exec.ShowcaseMobileTask;
import com.mi.showcase.util.DialogTypes;
import com.mi.showcase.util.MessageHandler;
import com.mi.showcase.util.ShowcaseUtils;
import com.mi.showcase.view.MobileDataAdapter;
import com.mi.showcase.view.dto.MobileDataListModel;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Sachith Dickwella
 */
public class ShowCaseActivity extends AppCompatActivity {

    private static SwipeRefreshLayout swipToRefreshLayout;
    private ShowcaseUtils.ShowcaseUtilDialogs connectionDialog;
    private ShowcaseUtils.ShowcaseUtilDialogs informationDialog;
    private static ListView dataListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_case);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.app_subtitle);
        setSupportActionBar(toolbar);

        dataListView = (ListView) findViewById(R.id.dataList);

        connectionDialog = new ShowcaseUtils.ShowcaseUtilDialogs(this, DialogTypes.CONNECTION_DIALOG, new ShowcaseUtils.ShowcaseUtilDialogs.InterceptionBeforeExecutor() {
            @Override
            public void run() {
                swipToRefreshLayout.setRefreshing(false);
            }
        }, null);
        informationDialog = new ShowcaseUtils.ShowcaseUtilDialogs(this, DialogTypes.INFORMATION_DIALOG, null, null);

        swipToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean isConnected = ShowcaseUtils.isConnected(getApplicationContext());
                if (!isConnected) {
                    connectionDialog.showDialog(null);
                } else {
                    try {
                        MessageHandler.executeHandler(loadData);
                    } catch (InterruptedException ex) {
                        Log.e(ex.getMessage(), ex.toString());
                    }
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insertDataIntent = new Intent(ShowCaseActivity.this, InsertDataActivity.class);
                startActivity(insertDataIntent);
            }
        });

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isConnected = ShowcaseUtils.isConnected(getApplicationContext());
                if (!isConnected) {
                    connectionDialog.showDialog(null);
                } else {
                    MobileDataListModel mobileDataListModel = ((MobileDataAdapter) parent.getAdapter()).getItem(position);
                    informationDialog.showDialog(mobileDataListModel);
                }
            }
        });

        List<MobileDataListModel> mobileDataListModels = new LinkedList<>();
        try (BufferedInputStream fin = new BufferedInputStream(openFileInput(getString(R.string.data_store)))) {
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);

            JSONObject jsonObject = new JSONObject(new String(buffer));
            ShowcaseMobileTask.MobileDataLoadTask mobileDataLoadTask
                    = new ShowcaseMobileTask.MobileDataLoadTask(this, null);
            mobileDataLoadTask.selectJSON(mobileDataListModels,
                    jsonObject.getJSONArray(getString(R.string.android)),
                    jsonObject.getJSONArray(getString(R.string.devices)));

        } catch (Exception ex) {
            Log.e(ex.getMessage(), ex.toString());
        } finally {
            System.gc();
        }
        MobileDataAdapter mobileDataAdapter = new MobileDataAdapter(this, mobileDataListModels);
        dataListView.setAdapter(mobileDataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_showcase, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                boolean isConnected = ShowcaseUtils.isConnected(getApplicationContext());
                if (!isConnected) {
                    connectionDialog.showDialog(null);
                } else {
                    try {
                        MessageHandler.executeHandler(loadData);
                    } catch (InterruptedException ex) {
                        Log.e(ex.getMessage(), ex.toString());
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final Runnable loadData = new Runnable() {
        @Override
        public void run() {
            ShowcaseMobileTask.MobileDataLoadTask dataLoadTask
                    = new ShowcaseMobileTask.MobileDataLoadTask(ShowCaseActivity.this, swipToRefreshLayout);
            AsyncTask<Void, Void, List<?>> asyncTask = dataLoadTask.execute((Void) null);
            try {
                @SuppressWarnings("unchecked")
                List<MobileDataListModel> mobileData = (List<MobileDataListModel>) asyncTask.get();
                final MobileDataAdapter mobileDataAdapter = new MobileDataAdapter(ShowCaseActivity.this, mobileData);

                MessageHandler.sendMessageHandler(new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                        dataListView.setAdapter(mobileDataAdapter);
                    }
                }, null, null);
            } catch (InterruptedException | ExecutionException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
    };
}
