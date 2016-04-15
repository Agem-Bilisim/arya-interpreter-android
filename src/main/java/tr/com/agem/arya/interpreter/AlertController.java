package tr.com.agem.arya.interpreter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import tr.com.agem.arya.MainActivity;


public class AlertController {
    public static void setAndShowPrimerAlert(Context context, String title, String message, String positiveButtonText) {
        final	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        MainActivity.setAlertDialog(builder.create());
        MainActivity.getAlertDialog().show();
    }
}
