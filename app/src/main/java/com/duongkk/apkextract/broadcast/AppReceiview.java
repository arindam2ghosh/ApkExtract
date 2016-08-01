package com.duongkk.apkextract.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.duongkk.apkextract.Utils.CommonUtils;
import com.duongkk.apkextract.Utils.LogX;
import com.duongkk.apkextract.databases.DatabaseHandler;
import com.duongkk.apkextract.models.Application;

/**
 * Created by MyPC on 7/27/2016.
 */
public class AppReceiview extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHandler db =new DatabaseHandler(context);
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            LogX.e("Installed:", packageName + "package name of the program");
            packageName=packageName.substring(8);
            try {
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
                Application app = new Application();
                String title = context.getPackageManager().getApplicationLabel(appInfo).toString();
                Drawable icon = context.getPackageManager().getApplicationIcon(appInfo);
                String path = appInfo.sourceDir;
                app.setmPath(path);
                app.setmIcon(icon);
                app.setmName(title);
                app.setmPackage(packageName);
                Bitmap bmIcon = CommonUtils.drawableToBitmap(icon,context);
                app.setmBitmapIcon(bmIcon);
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    app.setSystemApp(false);
                } else {
                    app.setSystemApp(true);
                }


                db.insertRow(app);
            } catch (PackageManager.NameNotFoundException e) {
                Toast toast = Toast.makeText(context, "error in getting icon", Toast.LENGTH_SHORT);
                toast.show();
                e.printStackTrace();
            }
        }
        // Receive uninstall broadcast
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {

            String packageName = intent.getDataString();
            packageName=packageName.substring(8);
            LogX.e("Uninstalled:", packageName + "package name of the program");
            db.removeRow(packageName);
        }
    }
}
