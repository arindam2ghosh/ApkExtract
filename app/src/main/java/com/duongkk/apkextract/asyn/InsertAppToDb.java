package com.duongkk.apkextract.asyn;

import android.os.AsyncTask;

import com.duongkk.apkextract.Utils.LogX;
import com.duongkk.apkextract.databases.DatabaseHandler;
import com.duongkk.apkextract.models.Application;

import java.util.List;

/**
 * Created by MyPC on 7/26/2016.
 */
public class InsertAppToDb extends AsyncTask<Void,Void,Boolean> {
    private List<Application> applications;
    private DatabaseHandler db;
    public InsertAppToDb(List<Application> applications,DatabaseHandler db){
        this.applications =applications;
        this.db =db;
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean status = true;
        for (Application app:applications) {
           if (db.insertRow(app) <0 ) status =false;
        }
        return status;
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);
        boolean result  = aVoid;
        LogX.e("insert db " + (result == true ? "thanh cong" : "Khong thanh cong"));
    }
}
