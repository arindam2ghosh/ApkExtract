package com.duongkk.apkextract.activities;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.duongkk.apkextract.R;
import com.duongkk.apkextract.Utils.CommonUtils;
import com.duongkk.apkextract.Utils.Constants;
import com.duongkk.apkextract.Utils.FileUtils;
import com.duongkk.apkextract.Utils.SharedPref;
import com.duongkk.apkextract.adapter.AdapterApplication;
import com.duongkk.apkextract.asyn.ExtractFileInBackground;
import com.duongkk.apkextract.asyn.GetAppFromDb;
import com.duongkk.apkextract.asyn.InsertAppToDb;
import com.duongkk.apkextract.databases.DatabaseHandler;
import com.duongkk.apkextract.models.Application;
import com.duongkk.apkextract.views.ProgressDialogCustom;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.TouchScrollBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterApplication.OnCheckResponse, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private List<Application> mListUserApps;
    private AdapterApplication mAdapter;
    private ProgressDialogCustom mProgress;
    private ProgressBar mProgressbar;
    private android.support.design.widget.FloatingActionButton mBtnExtract;
    ProgressDialog dialog;
    private CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    private DatabaseHandler mDb;
    private boolean isFirstTime;
    private List<Application> mListSystemApps;
    private AdapterApplication mAdapterSystemApp;

    private List<Application> mListAllApps;
    private AdapterApplication mAdapterAllApp;
    private int code=-1;
    private List<Application> listCurrentApps = new ArrayList<>();
    private AdapterApplication currentAdapterApp = new AdapterApplication(this,listCurrentApps,this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isFirstTime = SharedPref.getInstance(this).getBoolean(Constants.FIRST_TIME, true);
        mDb = new DatabaseHandler(this);
        mBtnExtract = (FloatingActionButton) findViewById(R.id.btn_extract);
        mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_application);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListUserApps = new ArrayList<>();
        mAdapter = new AdapterApplication(getApplicationContext(), mListUserApps, MainActivity.this);
        mListSystemApps =new ArrayList<>();
        mAdapterSystemApp = new AdapterApplication(this, mListSystemApps, MainActivity.this);
        mListAllApps = new ArrayList<>();
        mAdapterAllApp = new AdapterApplication(getApplicationContext(), mListAllApps, MainActivity.this);
        mRecyclerView.setAdapter(mAdapterAllApp);
        TouchScrollBar touchScrollBar =new TouchScrollBar(this,mRecyclerView,true);
        touchScrollBar.addIndicator(new AlphabetIndicator(this),true);
        touchScrollBar.setHandleColour(getResources().getColor(R.color.colorAccent));
        //
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (isFirstTime) {
            SharedPref.getInstance(this).putBoolean(Constants.FIRST_TIME, false);
            ApplicationTask myTask = new ApplicationTask();
            myTask.execute();
        } else {
            new GetAppFromDb(this, mProgressbar, mDb, mListSystemApps,mListUserApps, mListAllApps, mAdapterAllApp).execute();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_filter_all:{
                code = 0;
                listCurrentApps.clear();
                listCurrentApps.addAll(mListAllApps);
                mRecyclerView.setAdapter(currentAdapterApp);
                break;
            }
            case R.id.action_filter_system:{
                listCurrentApps.clear();
                listCurrentApps.addAll(mListSystemApps);
                code = 1;
                mRecyclerView.setAdapter(currentAdapterApp);
                break;
            }
            case R.id.action_filter_user:{
                listCurrentApps.clear();
                listCurrentApps.addAll(mListUserApps);
                code = 2;
                mRecyclerView.setAdapter(currentAdapterApp);
                break;
            }

           case R.id.action_sort_alpha: {
               // Comparator by Name (default)
               if(code == -1){
                   Collections.sort(mListAllApps, new Comparator<Application>() {
                       @Override
                       public int compare(Application application, Application t1) {
                           return application.getmName().compareTo(t1.getmName());
                       }
                   });
                   mAdapterAllApp.notifyDataSetChanged();
               }else{
                   Collections.sort(listCurrentApps, new Comparator<Application>() {
                       @Override
                       public int compare(Application application, Application t1) {
                           return application.getmName().compareTo(t1.getmName());
                       }
                   });
               }

               break;
           }
            case  R.id.action_sort_size: {
                // Comparator by Size
                if(code == -1){
                    Collections.sort(mListAllApps, new Comparator<Application>() {
                        @Override
                        public int compare(Application p1, Application p2) {
                            Long size1 = new File(p1.getmPath()).length();
                            Long size2 = new File(p2.getmPath()).length();
                            return size2.compareTo(size1);
                        }
                    });
                    mAdapterAllApp.notifyDataSetChanged();
                }else{
                    Collections.sort(listCurrentApps, new Comparator<Application>() {
                        @Override
                        public int compare(Application p1, Application p2) {
                            Long size1 = new File(p1.getmPath()).length();
                            Long size2 = new File(p2.getmPath()).length();
                            return size2.compareTo(size1);
                        }
                    });

                }
                break;
            }

        }

        currentAdapterApp.notifyDataSetChanged();
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_extract: {
                Snackbar snackbar1 = Snackbar.make(view, String.format(getString(R.string.msg_dialog_extract), getString(R.string.app_name)), Snackbar.LENGTH_LONG)
                        .setAction("Mở thư mục", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FileUtils.openFolder(MainActivity.this);
                            }
                        });
                snackbar1.setActionTextColor(Color.YELLOW);
                        if(code==-1){
                            new ExtractFileInBackground(this, dialog, mAdapterAllApp.getListAppChecked(), snackbar1).execute();

                        }else{
                            new ExtractFileInBackground(this, dialog, currentAdapterApp.getListAppChecked(), snackbar1).execute();

                        }

                break;

            }
        }
    }

    @Override
    public void onChecked(int count) {
        if (count > 0) {
            mBtnExtract.setVisibility(View.VISIBLE);
        } else {
            mBtnExtract.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        searchView = (SearchView)menu.findItem(R.id.action_settings).getActionView();
//        searchView.setIconified(false);
//        searchView.setIconifiedByDefault(false);
//
//        searchView.requestFocus();

        return true;
    }

    class ApplicationTask extends AsyncTask<Void, Void, List<Application>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressbar.setVisibility(View.VISIBLE);
            mProgressbar.setProgress(0);

        }

        @Override
        protected List<Application> doInBackground(Void... params) {
            return getInstalledAppList();
        }

        private List<Application> getInstalledAppList() {
            List<Application> listApp = new ArrayList<>();
            final PackageManager packageManager = getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

            for (PackageInfo packageInfo : packages) {
                if (!(packageManager.getApplicationLabel(packageInfo.applicationInfo).equals("") || packageInfo.packageName.equals(""))) {
                    Application app = new Application();
                    String title = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
                    Drawable icon = packageManager.getApplicationIcon(packageInfo.applicationInfo);
                    String strPackageName = packageInfo.packageName;
                    String path = packageInfo.applicationInfo.sourceDir;

                    app.setmPath(path);
                    app.setmIcon(icon);
                    app.setmName(title);
                    app.setmPackage(strPackageName);
                    Bitmap bmIcon = CommonUtils.drawableToBitmap(icon);
                    app.setmBitmapIcon(bmIcon);
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        app.setSystemApp(false);
                        mListUserApps.add(app);
                    } else {
                        app.setSystemApp(true);
                        mListSystemApps.add(app);
                    }

                    listApp.add(app);
                }
            }
            ResolveInfoComparator resolveInfoComparator = new ResolveInfoComparator();
            Collections.sort(listApp, resolveInfoComparator);
            Collections.sort(mListUserApps, resolveInfoComparator);
            Collections.sort(mListSystemApps, resolveInfoComparator);
            return listApp;
        }

        @Override
        protected void onPostExecute(List<Application> applications) {
            super.onPostExecute(applications);
            new InsertAppToDb(applications, mDb).execute();
            mListAllApps.addAll(applications);
            listCurrentApps.addAll(applications);
            mAdapterAllApp.notifyDataSetChanged();
            mAdapterSystemApp.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
            TouchScrollBar touchScrollBar = new TouchScrollBar(getApplicationContext(), mRecyclerView, true);
            touchScrollBar.addIndicator(new AlphabetIndicator(getApplicationContext()), true);
            mProgressbar.setVisibility(View.GONE);
        }
    }

    private class ResolveInfoComparator implements Comparator<Application> {
        public ResolveInfoComparator() {
        }

        @Override
        public int compare(Application application, Application t1) {
            return application.getmName().compareTo(t1.getmName());
        }
    }


}
