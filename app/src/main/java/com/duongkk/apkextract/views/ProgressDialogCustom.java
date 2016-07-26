package com.duongkk.apkextract.views;

import android.app.ProgressDialog;
import android.content.Context;

import com.duongkk.apkextract.R;


/**
 * Created by DuongKK on 4/29/2016.
 */
public class ProgressDialogCustom extends ProgressDialog {

    Context context;
    private ProgressDialog mProgressDialog;
    public ProgressDialogCustom(Context context, String msg, boolean isCancelable) {
        super(context);
        this.context=context;
        mProgressDialog= new ProgressDialog(context);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(isCancelable);
    }
    public ProgressDialogCustom(Context context){
        super(context);

        this.context=context;
        mProgressDialog= new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.dang_xu_ly));
        mProgressDialog.setCancelable(false);
    }
   public void showDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    public void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }





}
