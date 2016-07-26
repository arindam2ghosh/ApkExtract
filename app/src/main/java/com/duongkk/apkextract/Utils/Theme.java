package com.duongkk.apkextract.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phongpham on 6/24/16.
 */

public enum Theme {
    INSTANCE;
    private static String TAG = Theme.class.getName();

    private Context mContext;
    private Map<String, Typeface> mTypefaces;

    public static void init(Context context) {
        INSTANCE.mContext = context;
    }

    public Typeface getTypeface(String name) {
        if (mTypefaces == null) {
            mTypefaces = new HashMap<String, Typeface>();
        }
        Typeface typeface = mTypefaces.get(name);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(mContext.getAssets(), name);
        }
        if (typeface == null) {
            throw new IllegalArgumentException("No typeface with provided font name");
        }
        mTypefaces.put(name, typeface);
        return typeface;
    }
}
