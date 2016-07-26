package com.duongkk.apkextract.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.StrictMode;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by phongpham on 6/24/16.
 */

public class SdkUtils {
    /**
     * Prevents outside initialization
     */
    private SdkUtils() {
        throw new AssertionError();
    }

    /**
     * Only enables translucent system bar, if we are on KitKat. On L, we should
     * define separate color later.
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean enableSystemTranslucent(Activity activity) {
        if (activity == null
                || Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
            return false;
        }
        int id = activity.getResources().getIdentifier(
                "config_enableTranslucentDecor", "bool", "android");
        if (id == 0 || !activity.getResources().getBoolean(id)) {
            return false;
        }
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return true;
    }

    /**
     * Checks if current system environment has translucent decor enabled.
     *
     * @param context
     * @return
     */
    public static boolean hasTranslucentSystemConfig(Context context) {
        if (context == null
                || Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
            return false;
        }
        int id = context.getResources().getIdentifier(
                "config_enableTranslucentDecor", "bool", "android");
        if (id == 0 || !context.getResources().getBoolean(id)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if device has DownloadManager feature.
     *
     * @param context
     * @return
     */
    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui",
                    "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager()
                    .queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enables strict mode for specific class.
     *
     * @param target
     */
    @TargetApi(11)
    public static void enableStrictMode(Class<?> target) {
        if (SdkUtils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyLog();

            if (SdkUtils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder.setClassInstanceLimit(target, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * Checks if device API is Froyo or above.
     *
     * @return
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Checks if device API is Gingerbread or above.
     *
     * @return
     */

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Checks if device API is Honeycomb or above.
     *
     * @return
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Checks if device API is Honeycomb MR1 or above.
     *
     * @return
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Checks if device API is Honeycomb MR2 or above.
     *
     * @return
     */
    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * Checks if device API is Ice Cream Sandwich or above.
     *
     * @return
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Checks if device API is Jellybean or above.
     *
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Checks if device API is Jellybean MR1 or above.
     *
     * @return
     */
    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * Checks if device API is Jellybean MR2 or above.
     *
     * @return
     */
    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * Checks if device API is KitKat or above.
     *
     * @return
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
