package com.duongkk.apkextract.asyn;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.duongkk.apkextract.R;
import com.duongkk.apkextract.Utils.CommonUtils;
import com.duongkk.apkextract.Utils.FileUtils;
import com.duongkk.apkextract.Utils.LogX;
import com.duongkk.apkextract.models.Application;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by MyPC on 7/25/2016.
 */
public class ExtractFileInBackground extends AsyncTask<Void, ExtractFileInBackground.ProgressUpdate, Boolean> {
    private Context context;
    private Activity activity;
    private ProgressDialog dialog;
    private List<Application> appInfos;
    private Snackbar snackbar;
    private File resultFile;

    public ExtractFileInBackground(Activity context, ProgressDialog dialog, List<Application> appInfos, Snackbar snackbar) {
        this.activity = (Activity) context;
        this.context = context;
        this.dialog = dialog;
        this.appInfos = appInfos;
        this.snackbar = snackbar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgress(0);
        dialog.setTitle(String.format(context.getString(R.string.title_dialog_extract), appInfos.get(0).getmName()));
        // dialog.setMessage(String.format(context.getResources().getString(R.string.msg_dialog_extract),context.getString(R.string.app_name)));
        dialog.setCancelable(false);
        dialog.setMax(100);

        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean status = false;
        int size = appInfos.size();
        float total = 0;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (CommonUtils.checkPermissions(activity)) {
                for (Application appInfo : appInfos) {
                    total += 1;
                    int progress = (int) total * 100 / size;
                    String appname = appInfo.getmName();
                    publishProgress(new ProgressUpdate(appname, progress));
                    File sourceFile = new File(appInfo.getmPath());
                    if (sourceFile.exists()) {
                        try {

                            File destFile = new File(FileUtils.getFolder(context) + File.separator + appInfo.getmName() + ".apk");
                            org.apache.commons.io.FileUtils.copyFile(sourceFile, destFile);
                            status = true;
                            resultFile = destFile;
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogX.e(e.toString());
                        }
                    }
                }

            }
        }

        return status;
    }

    @Override
    protected void onProgressUpdate(ProgressUpdate... values) {
        super.onProgressUpdate(values);
        ProgressUpdate value = values[0];
        dialog.setTitle(String.format(context.getString(R.string.title_dialog_extract), value.detail));
        dialog.setProgress(value.value);
        if (value.value == 100) dialog.dismiss();
        LogX.e(value.value + "/" + "");
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        if (dialog.isShowing()) dialog.dismiss();
        if (status) {
            snackbar.show();
        } else {
            snackbar.setText(R.string.opps);
            snackbar.setAction(R.string.setting, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });
            snackbar.show();
        }
    }

    public class ProgressUpdate {
        public final String detail;
        public final int value;

        public ProgressUpdate(String detail, int value) {
            this.detail = detail;
            this.value = value;
        }
    }
}