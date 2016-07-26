package com.duongkk.apkextract.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by MyPC on 7/20/2016.
 */
public class Application {
    private String mName;
    private String mPackage;
    private Drawable mIcon;
    private boolean isChecked;
    private String mPath;
    private Bitmap mBitmapIcon;
    private boolean isSystemApp;

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public Bitmap getmBitmapIcon() {
        return mBitmapIcon;
    }

    public void setmBitmapIcon(Bitmap mBitmapIcon) {
        this.mBitmapIcon = mBitmapIcon;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Drawable getmIcon() {
        return mIcon;
    }

    public void setmIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPackage() {
        return mPackage;
    }

    public void setmPackage(String mPackage) {
        this.mPackage = mPackage;
    }
}
