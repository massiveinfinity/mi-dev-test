package com.mi.showcase.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mi.showcase.dialog.ConnectionDialog;
import com.mi.showcase.dialog.InformationDialog;
import com.mi.showcase.view.dto.MobileDataListModel;
import com.mi.showcase.view.dto.Model;

/**
 * @author Sachith Dickwella
 */
public abstract class ShowcaseUtils {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public static class ShowcaseUtilDialogs {

        private final Activity context;
        private final DialogTypes dialogTypes;
        private InterceptionBeforeExecutor interceptionBeforeExecutor;
        private InterceptionAfterExecutor interceptionAfterExecutor;

        public ShowcaseUtilDialogs(Activity context, DialogTypes dialogTypes,
                                   InterceptionBeforeExecutor interceptionBeforeExecutor,
                                   InterceptionAfterExecutor interceptionAfterExecutor) {
            this.context = context;
            this.dialogTypes = dialogTypes;
            this.interceptionBeforeExecutor = interceptionBeforeExecutor;
            this.interceptionAfterExecutor = interceptionAfterExecutor;
        }

        public void showDialog(Model model) {
            if (interceptionBeforeExecutor != null) {
                interceptionBeforeExecutor.run();
            }
            switch (dialogTypes) {
                case CONNECTION_DIALOG:
                    ConnectionDialog connectionDialog = new ConnectionDialog();
                    connectionDialog.show(context.getFragmentManager(), null);
                    break;
                case INFORMATION_DIALOG:
                    InformationDialog informationDialog = new InformationDialog();
                    informationDialog.setMobileDataListModel((MobileDataListModel)model);
                    informationDialog.show(context.getFragmentManager(), null);
                    break;
                default:
                    break;
            }
            if (interceptionAfterExecutor != null) {
                interceptionAfterExecutor.run();
            }
        }

        public void setInterceptionBeforeExecutor(InterceptionBeforeExecutor interceptionBeforeExecutor) {
            this.interceptionBeforeExecutor = interceptionBeforeExecutor;
        }

        public void setInterceptionAfterExecutor(InterceptionAfterExecutor interceptionAfterExecutor) {
            this.interceptionAfterExecutor = interceptionAfterExecutor;
        }

        public interface InterceptionBeforeExecutor {
            void run();
        }

        public interface InterceptionAfterExecutor {
            void run();
        }
    }
}
