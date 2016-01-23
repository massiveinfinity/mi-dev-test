package com.dharashah.showcaseandroidapp.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler defaultUEH;
    public static String sendErrorLogsTo = "dhara.shah@credencys.com" ;

    Activity activity;

    public CustomExceptionHandler(Activity activity) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.activity = activity;
    }

    public void uncaughtException(Thread t, Throwable e) {

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = "error" + System.nanoTime() + ".stacktrace";

        sendToServer(stacktrace, filename);

        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString() + "\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i = 0; i < arr.length; i++) {
            report += "    " + arr[i].toString() + "\n";
        }
        report += "-------------------------------\n\n";

        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if (cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report += "    " + arr[i].toString() + "\n";
            }
        }
        report += "-------------------------------\n\n";

        defaultUEH.uncaughtException(t, e);
    }

    private void sendToServer(String stacktrace, String filename) {
        AsyncTaskClass async = new AsyncTaskClass(stacktrace, filename,
                getAppLable(activity));
        async.execute("");
    }

    public String getAppLable(Context pContext) {
        PackageManager lPackageManager = pContext.getPackageManager();
        ApplicationInfo lApplicationInfo = null;
        try {
            lApplicationInfo = lPackageManager.getApplicationInfo(
                    pContext.getApplicationInfo().packageName, 0);
        } catch (final NameNotFoundException e) {
        }
        return (String) (lApplicationInfo != null ? lPackageManager
                .getApplicationLabel(lApplicationInfo) : "Unknown");
    }

    public class AsyncTaskClass extends AsyncTask<String, String, InputStream> {
        InputStream is = null;
        String stacktrace;
        final String filename;
        String applicationName;

        AsyncTaskClass(final String stacktrace, final String filename,
                String applicationName) {
            this.applicationName = applicationName;
            this.stacktrace = stacktrace;
            this.filename = filename;
        }

        @Override
        protected InputStream doInBackground(String... params) 
        {
            HttpURLConnection connection = null;
            try {
                String urlMain ="http://suo-yang.com/books/sendErrorLog/sendErrorLogs.php";
                URL url = new URL("http://suo-yang.com/books/sendErrorLog/sendErrorLogs.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                String strParamsToSend = "data=" + URLEncoder.encode(stacktrace, "UTF-8") +
                                "&to=" + URLEncoder.encode(sendErrorLogsTo, "UTF-8") +
                                "&subject=" + URLEncoder.encode(applicationName, "UTF-8");

                if(strParamsToSend != null) {
                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(strParamsToSend);
                    out.flush();
                    out.close();
                }

                is = connection.getInputStream();
                getStringFromInputStream(is);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(InputStream result) {
            super.onPostExecute(result);
        }
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            //Mint.logException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                //Mint.logException(e);
            }
        }
        return buffer.toString();

    }
}