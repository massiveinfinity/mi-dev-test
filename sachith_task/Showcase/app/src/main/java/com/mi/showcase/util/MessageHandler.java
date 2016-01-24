package com.mi.showcase.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author Sachith Dickwella
 */
public class MessageHandler {

    public static Handler sendMessageHandler(Handler handler, String key, String messageBody) {
        Bundle bundle = new Bundle();
        bundle.putString(key, messageBody);

        Message message = handler.obtainMessage();
        message.setData(bundle);
        handler.sendMessage(message);

        return handler;
    }

    public static Thread executeHandler(Runnable runnable)
            throws InterruptedException {
        Thread isolatedThread = new Thread(runnable);
        isolatedThread.start();
        return isolatedThread;
    }
}
