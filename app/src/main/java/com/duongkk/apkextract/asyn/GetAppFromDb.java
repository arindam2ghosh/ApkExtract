package com.duongkk.apkextract.asyn;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.duongkk.apkextract.Utils.LogX;
import com.duongkk.apkextract.adapter.AdapterApplication;
import com.duongkk.apkextract.databases.DatabaseHandler;
import com.duongkk.apkextract.models.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 7/26/2016.
 */
public class GetAppFromDb extends AsyncTask<Void,Void,List<Application>> {
    private ProgressBar progressBar;
    private DatabaseHandler db;
    private List<Application> applications;
    private AdapterApplication mAdapter;
    private Context context;
    private List<Application> sysApp;
    private List<Application> userApp;
    private OnLoadFail onLoadFail;
    public GetAppFromDb(Context context, ProgressBar progressBar, DatabaseHandler db, List<Application> sysapplications,
                        List<Application> Userlications , List<Application> applications, AdapterApplication mAdapter,OnLoadFail onLoadFail){
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        this.db =db;
        this.applications = applications;
        this.mAdapter = mAdapter;
        this.context = context;
        this.sysApp = sysapplications;
        this.userApp=Userlications;
        this.onLoadFail = onLoadFail;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Application> doInBackground(Void... voids) {
        List<Application> mListApps=new ArrayList<>();
        try {
            mListApps.addAll(db.getAllRows());
        } catch (Exception e) {
            e.printStackTrace();
            LogX.e(e.toString());
        }
        for (Application app:mListApps) {
            if(app.isSystemApp()){
                sysApp.add(app);
            }else{
                userApp.add(app);
            }
        }
        return mListApps;
    }

    @Override
    protected void onPostExecute(List<Application> applications) {
        super.onPostExecute(applications);
        progressBar.setVisibility(View.GONE);
       this.applications.clear();
       this.applications.addAll(applications);
        if(this.applications.size()==0) onLoadFail.onLoadFail();
        mAdapter.notifyDataSetChanged();
//

    }

    public interface OnLoadFail{
        public void onLoadFail();
    }

}
