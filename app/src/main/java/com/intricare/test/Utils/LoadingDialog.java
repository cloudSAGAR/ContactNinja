package com.intricare.test.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;

import com.intricare.test.R;


public class LoadingDialog {
    private Activity activity = null;
    private String infoText = "";
    private AlertDialog dialog;
    private AlertDialog.Builder alertDialogBuilder;
    private static ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    LoadingDialog(){

    }
    public LoadingDialog(Activity activity){
        this.activity = activity;
        alertDialogBuilder = new AlertDialog.Builder(activity);
        dialog = alertDialogBuilder.create();

    }
    public void showLoadingDialog()
    {


        openCustomProgressDialog();
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

    }

    public void cancelLoading( )
    {
        dialog.dismiss();

    }
    @SuppressLint("InflateParams")
    public void openCustomProgressDialog() {
        try {

            dialog = alertDialogBuilder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setView(activity.getLayoutInflater().inflate(R.layout.custom_dialog_progess, null));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
