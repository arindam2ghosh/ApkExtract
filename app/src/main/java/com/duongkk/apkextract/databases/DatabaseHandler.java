package com.duongkk.apkextract.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.duongkk.apkextract.models.Application;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DB_NAME = "Application.db";
    public static final String TABLE_NAME = "t_application";
    public static final String TABLE_NAME_SYS = "t_sys_application";
    public static final int VERSION = 1;
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ICON = "icon";
    public static final String KEY_PATH = "path";
    public static final String KEY_SYSAPP = "is_system";
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_DATE = "date";
    public static final String KEY_SIZE = "size";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    public List<Application> getAllRows() throws Exception {
        List<Application> listApps = new ArrayList<>();
        String query = "Select * from " + TABLE_NAME + " ORDER BY " + KEY_NAME +" ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while ( cursor!=null && cursor.moveToNext()) {
            Application app = new Application();
            app.setmName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            app.setSystemApp(cursor.getInt(cursor.getColumnIndex(KEY_SYSAPP)) == 1 ? true : false);
            app.setmPath(cursor.getString(cursor.getColumnIndex(KEY_PATH)));
            app.setmPackage(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE)));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(KEY_ICON));
            app.setmBitmapIcon(DbBitmapUtility.getImage(image));
            listApps.add(app);

        }
        String querysys = "Select * from " + TABLE_NAME_SYS + " ORDER BY " + KEY_NAME +" ASC";

        Cursor cursorsys = db.rawQuery(querysys, null);
        while (cursorsys!=null && cursorsys.moveToNext()) {
            Application app = new Application();
            app.setmName(cursorsys.getString(cursorsys.getColumnIndex(KEY_NAME)));
            app.setSystemApp(cursorsys.getInt(cursorsys.getColumnIndex(KEY_SYSAPP)) == 1 ? true : false);
            app.setmPath(cursorsys.getString(cursorsys.getColumnIndex(KEY_PATH)));
            app.setmPackage(cursorsys.getString(cursorsys.getColumnIndex(KEY_PACKAGE)));
            byte[] image = cursorsys.getBlob(cursorsys.getColumnIndex(KEY_ICON));
            app.setmBitmapIcon(DbBitmapUtility.getImage(image));

            listApps.add(app);

        }
        return listApps;
    }

    public void removeRow(String packageNam) {
        SQLiteDatabase database = this.getWritableDatabase();
        int remove1 = database.delete(TABLE_NAME, KEY_PACKAGE + "=?", new String[]{packageNam});
        int remove2 = database.delete(TABLE_NAME_SYS, KEY_PACKAGE + "=?", new String[]{packageNam});

    }

    public long insertRow(Application app) {
        SQLiteDatabase database = this.getWritableDatabase();
        String querysys = "Select * from " + TABLE_NAME + " where " + KEY_PACKAGE +  " = ' " + app.getmPackage() + " ' ";
        Cursor cursor = database.rawQuery(querysys, null);

        if (cursor == null || !cursor.moveToFirst()) {

            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, app.getmName());
            cv.put(KEY_ICON, DbBitmapUtility.getBytes(app.getmBitmapIcon()));
            cv.put(KEY_PACKAGE, app.getmPackage());
            cv.put(KEY_PATH, app.getmPath());
            cv.put(KEY_SYSAPP, app.isSystemApp() == true ? 1 : 0);
            if (app.isSystemApp()) {
                return database.insert(TABLE_NAME_SYS, null, cv);
            } else {
                return database.insert(TABLE_NAME, null, cv);
            }
        }
        return 0;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE table " + TABLE_NAME + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                KEY_ICON + " BLOB , " +
                KEY_NAME + " text , " +
                KEY_PACKAGE + " text , " +
                KEY_SYSAPP + " integer, " +
                KEY_PATH + " text , " +
                KEY_DATE + " text , " +
                KEY_SIZE + " float ) ";
        String query2 = "CREATE table " + TABLE_NAME_SYS + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                KEY_ICON + " BLOB , " +
                KEY_NAME + " text , " +
                KEY_PACKAGE + " text , " +
                KEY_SYSAPP + " integer, " +
                KEY_PATH + " text , " +
                KEY_DATE + " text , " +
                KEY_SIZE + " float ) ";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
