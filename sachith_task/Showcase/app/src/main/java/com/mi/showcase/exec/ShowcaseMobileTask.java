package com.mi.showcase.exec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mi.showcase.R;
import com.mi.showcase.util.AndroidFriendlyFeatures;
import com.mi.showcase.util.MessageHandler;
import com.mi.showcase.view.dto.MobileDataListModel;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Sachith Dickwella
 */
public abstract class ShowcaseMobileTask<E, V, T> extends AsyncTask<E, V, T> {

    private final static String WEB_TARGET = "http://mobilesandboxdev.azurewebsites.net/";
    private final static String ANDROID_PATH = "android";
    private final static String DEVICES_PATH = "devices";

    private Context context;
    /**
     * Only use in MobileDataLoadTask class
     */
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static ImageView imageView;
    private static ProgressBar progressBar;
    private static ProgressBar progressBarSave;
    private static ScrollView scrollView;
    private static TextView[] textViews;

    public static class MobileDataLoadTask extends ShowcaseMobileTask<Void, Void, List<?>> {

        public MobileDataLoadTask(Context context, SwipeRefreshLayout swipeRefreshLayout) {
            ShowcaseMobileTask.swipeRefreshLayout = swipeRefreshLayout;
            super.context = context;
        }

        @Override
        protected List<MobileDataListModel> doInBackground(Void... params) {
            final Client client = ClientBuilder.newClient(new ClientConfig().register(new AndroidFriendlyFeatures()));
            List<MobileDataListModel> mobileDataList = new LinkedList<>();
            try {
                Callable<String> androidCallable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return client.target(WEB_TARGET).path(ANDROID_PATH)
                                .request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
                    }
                };

                Callable<String> devicesCallable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return client.target(WEB_TARGET).path(DEVICES_PATH)
                                .request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
                    }
                };

                ExecutorService executorService = Executors.newCachedThreadPool();
                Future<String> androidFuture = executorService.submit(androidCallable);
                Future<String> devicesFuture = executorService.submit(devicesCallable);

                String json = "{ \"android\":" + androidFuture.get() + ", \"devices\":" + devicesFuture.get() + "}";

                try (BufferedOutputStream fos = new BufferedOutputStream(
                        super.context.openFileOutput(super.context.getString(R.string.data_store), Context.MODE_PRIVATE))) {
                    fos.write(json.getBytes());
                    fos.flush();
                } catch (Exception e) {
                    Log.e(e.getMessage(), e.toString());
                }

                JSONObject jsonObject = new JSONObject(json);
                mobileDataList = selectJSON(mobileDataList,
                        jsonObject.getJSONArray(super.context.getString(R.string.android)),
                        jsonObject.getJSONArray(super.context.getString(R.string.devices)));

            } catch (Exception ex) {
                Log.e(ex.getMessage(), ex.toString());
                Snackbar.make(swipeRefreshLayout, super.context.getResources()
                        .getText(R.string.service_unavailable), Snackbar.LENGTH_LONG).show();
            } finally {
                client.close();
                System.gc();
            }
            return mobileDataList;
        }

        @Override
        protected void onPreExecute() {
            MessageHandler.sendMessageHandler(ShowcaseMobileTask.swipeRefreshingHandler, null, null);
        }

        @Override
        protected void onPostExecute(List<?> list) {
            swipeRefreshLayout.setRefreshing(false);
        }

        public List<MobileDataListModel> selectJSON(List<MobileDataListModel> dataList,
                                                    JSONArray androidArray, JSONArray devicesArray) throws JSONException {
            for (int i = 0; i < androidArray.length(); i++) {
                JSONObject androidObject = (JSONObject) androidArray.get(i);
                for (int x = 0; x < devicesArray.length(); x++) {
                    JSONObject devicesObject = (JSONObject) devicesArray.get(x);
                    try {
                        if (androidObject.getInt("id") == devicesObject.getInt("androidId")) {
                            MobileDataListModel listModel = new MobileDataListModel();

                            try {
                                listModel.setId(androidObject.getInt("id"));
                                listModel.setAndroidName(androidObject.getString("name"));
                                listModel.setVersion(androidObject.getString("version"));
                                listModel.setDeviceName(devicesObject.getString("name"));
                                listModel.setCodeName(androidObject.getString("codename"));
                                listModel.setDistribution(androidObject.getString("distribution"));
                                try {
                                    listModel.setCarrier(devicesObject.getString("carrier"));
                                } catch (JSONException ex) {
                                    listModel.setCarrier(super.context.getString(R.string.none));
                                }
                                listModel.setSnippet(devicesObject.getString("snippet"));
                                listModel.setImageUrl(devicesObject.getString("imageUrl"));
                            } catch (JSONException ex) {
                                Log.e(ex.getMessage(), ex.toString());
                            }
                            dataList.add(listModel);
                        }
                    } catch (JSONException ex) {
                        Log.e(ex.getMessage(), ex.toString());
                    }
                }
            }
            return dataList;
        }
    }

    /**
     * Declare in outer class to avoid memory-leaks.
     */
    @SuppressLint("HandlerLeak")
    private static Handler swipeRefreshingHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            swipeRefreshLayout.setRefreshing(true);
        }
    };

    public static class LoadImageTask extends ShowcaseMobileTask<String, Void, Void> {

        public LoadImageTask(Context context, View... view) {
            ShowcaseMobileTask.imageView = (ImageView) view[0];
            ShowcaseMobileTask.progressBar = (ProgressBar) view[1];
            super.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(super.context.getResources(), bmp);
                roundedBitmapDrawable.setCornerRadius(Math.max(bmp.getWidth(), bmp.getHeight()) / 2.0f);

                @SuppressLint("HandlerLeak") final
                Handler imageSetHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        imageView.setImageDrawable(roundedBitmapDrawable);
                    }
                };

                MessageHandler.sendMessageHandler(imageSetHandler, null, null);
            } catch (Exception ex) {
                Log.e(ex.getMessage(), ex.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            MessageHandler.sendMessageHandler(startLoadHandler, null, null);
        }

        @Override
        protected void onPostExecute(Void none) {
            MessageHandler.sendMessageHandler(endLoadHandler, null, null);
        }
    }

    @SuppressLint("HandlerLeak")
    private static Handler startLoadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    };

    @SuppressLint("HandlerLeak")
    private static Handler endLoadHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
    };

    public static class AddDeviceTask extends ShowcaseMobileTask<MobileDataListModel, Void, Void> {

        public AddDeviceTask(Context context, Object... objs) {
            super.context = context;
            ShowcaseMobileTask.progressBarSave = (ProgressBar) objs[0];
            ShowcaseMobileTask.scrollView = (ScrollView) objs[1];
            ShowcaseMobileTask.textViews = (TextView[]) objs[2];
        }

        @Override
        protected Void doInBackground(MobileDataListModel... params) {
            final Client client = ClientBuilder.newClient(new ClientConfig().register(new AndroidFriendlyFeatures()));
            try {
                Callable<String> androidCallable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return client.target(WEB_TARGET).path(ANDROID_PATH)
                                .request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
                    }
                };

                Callable<String> devicesCallable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return client.target(WEB_TARGET).path(DEVICES_PATH)
                                .request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
                    }
                };

                ExecutorService executorService = Executors.newCachedThreadPool();
                Future<String> androidFuture = executorService.submit(androidCallable);
                Future<String> devicesFuture = executorService.submit(devicesCallable);

                JSONArray androidArray = new JSONArray(androidFuture.get());
                JSONArray devicesArray = new JSONArray(devicesFuture.get());

                int _MAX_androidId = 0, _MAX_devicesId = 0;
                for (int i = 0; i < androidArray.length(); i++) {
                    JSONObject obj = androidArray.getJSONObject(i);
                    if (obj.getInt("id") > _MAX_androidId) {
                        _MAX_androidId = obj.getInt("id");
                    }
                }
                for (int i = 0; i < devicesArray.length(); i++) {
                    JSONObject obj = devicesArray.getJSONObject(i);
                    if (obj.getInt("id") > _MAX_devicesId) {
                        _MAX_devicesId = obj.getInt("id");
                    }
                }

                MobileDataListModel mobileDataListModel = params[0];

                final JSONObject androidObject = new JSONObject();
                androidObject.put("id", _MAX_androidId + 1)
                        .put("name", mobileDataListModel.getAndroidName())
                        .put("version", mobileDataListModel.getVersion())
                        .put("codename", mobileDataListModel.getCodeName())
                        .put("target", mobileDataListModel.getTarget())
                        .put("distribution", mobileDataListModel.getDistribution());

                final JSONObject devicesObject = new JSONObject();
                devicesObject.put("id", _MAX_devicesId + 1)
                        .put("androidId", _MAX_androidId + 1)
                        .put("carrier", mobileDataListModel.getCarrier())
                        .put("imageUrl", mobileDataListModel.getImageUrl())
                        .put("name", mobileDataListModel.getDeviceName())
                        .put("snippet", mobileDataListModel.getSnippet());

                Callable<Response> androidSaveCall = new Callable<Response>() {
                    @Override
                    public Response call() throws Exception {
                        return client.target(WEB_TARGET).path(ANDROID_PATH)
                                .request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(androidObject.toString(),
                                        MediaType.APPLICATION_JSON_TYPE));
                    }
                };

                final Callable<Response> devicesSaveCall = new Callable<Response>() {
                    @Override
                    public Response call() throws Exception {
                        return client.target(WEB_TARGET).path(DEVICES_PATH)
                                .request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(devicesObject.toString(), MediaType.APPLICATION_JSON_TYPE));
                    }
                };

                ExecutorService executorSaveService = Executors.newSingleThreadExecutor();
                Future<Response> androidFutureResponse = executorSaveService.submit(androidSaveCall);
                Future<Response> devicesFutureResponse = executorSaveService.submit(devicesSaveCall);

                Response androidResponse = androidFutureResponse.get();
                Response devicesResponse = devicesFutureResponse.get();

                if (androidResponse.getStatus() == 201
                        && devicesResponse.getStatus() == 201) {
                    MessageHandler.sendMessageHandler(clearTextFields, null, null);
                    Snackbar.make(scrollView, super.context.getString(R.string.saving_process_succes), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(scrollView, super.context.getString(R.string.saving_process_failed), Snackbar.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Log.e(ex.getMessage(), ex.toString());
            } finally {
                client.close();
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            MessageHandler.sendMessageHandler(saveStartHandler, null, null);
        }

        @Override
        public void onPostExecute(Void none) {
            MessageHandler.sendMessageHandler(saveEndHandler, null, null);
        }
    }

    @SuppressLint("HandlerLeak")
    private static Handler saveStartHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            scrollView.setVisibility(View.INVISIBLE);
            progressBarSave.setVisibility(View.VISIBLE);
        }
    };

    @SuppressLint("HandlerLeak")
    private static Handler saveEndHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            scrollView.setVisibility(View.VISIBLE);
            progressBarSave.setVisibility(View.INVISIBLE);
        }
    };

    @SuppressLint("HandlerLeak")
    private static Handler clearTextFields = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            for (TextView textView : textViews) {
                textView.setText("");
            }
        }
    };
}
