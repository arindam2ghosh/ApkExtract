package com.duongkk.apkextract.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.duongkk.apkextract.broadcast.AppReceiview;
import com.duongkk.apkextract.databases.DatabaseHandler;
import com.duongkk.apkextract.models.Application;
import com.duongkk.apkextract.views.ProgressDialogCustom;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kobakei.ratethisapp.RateThisApp;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.TouchScrollBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterApplication.OnCheckResponse, View.OnClickListener,SearchView.OnQueryTextListener {
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
    public Snackbar snackbar1;
    private List<Application> mListAllApps;
    private AdapterApplication mAdapterAllApp;
    private int code=-1;
    private List<Application> listCurrentApps = new ArrayList<>();
    private AdapterApplication currentAdapterApp = new AdapterApplication(MainActivity.this,listCurrentApps,this);
     Animation animationFadeIn ;
     Animation animationFadeout;
    private AdView mAdView;
    private AppReceiview receiview;
    private String TAG = MainActivity.class.getSimpleName();
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET},100);
            }
        }

        animationFadeIn= AnimationUtils.loadAnimation(this, R.anim.fadein);
        animationFadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        isFirstTime = SharedPref.getInstance(this).getBoolean(Constants.FIRST_TIME, true);
        mDb = new DatabaseHandler(this);
        mBtnExtract = (FloatingActionButton) findViewById(R.id.btn_extract);
        snackbar1 = Snackbar.make(mBtnExtract, String.format(getString(R.string.msg_dialog_extract), getString(R.string.app_name)), Snackbar.LENGTH_LONG)
                .setAction(R.string.open, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FileUtils.openFolder(MainActivity.this);
                    }
                });
        snackbar1.setActionTextColor(Color.YELLOW);
        mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_application);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListUserApps = new ArrayList<>();
        mAdapter = new AdapterApplication(MainActivity.this, mListUserApps, MainActivity.this);
        mListSystemApps =new ArrayList<>();
        mAdapterSystemApp = new AdapterApplication(MainActivity.this, mListSystemApps, MainActivity.this);
        mListAllApps = new ArrayList<>();
        mAdapterAllApp = new AdapterApplication(MainActivity.this, mListAllApps, MainActivity.this);
        mRecyclerView.setAdapter(mAdapterAllApp);
        TouchScrollBar touchScrollBar =new TouchScrollBar(this,mRecyclerView,true);
        touchScrollBar.addIndicator(new AlphabetIndicator(this),true);
        touchScrollBar.setHandleColour(getResources().getColor(R.color.colorAccent));
        dialog = new ProgressDialog(this);


        if (isFirstTime) {
            SharedPref.getInstance(this).putBoolean(Constants.FIRST_TIME, false);
            ApplicationTask myTask = new ApplicationTask();
            myTask.execute();
        } else {
            new GetAppFromDb(this, mProgressbar, mDb, mListSystemApps, mListUserApps, mListAllApps, mAdapterAllApp, new GetAppFromDb.OnLoadFail() {
                @Override
                public void onLoadFail() {
                    ApplicationTask myTask = new ApplicationTask();
                    myTask.execute();
                }
            }).execute();
        }


        receiview =new AppReceiview();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addDataScheme("package");
        registerReceiver(receiview, intentFilter);
        mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder()
                .build()
                ;

        mAdView.loadAd(adRequest);
        RateThisApp.init(new RateThisApp.Config(5, 10));
    }

    @Override
    protected void onStart() {
        super.onStart();
        RateThisApp.onStart(this);
        // Show a dialog if criteria is satisfied
        RateThisApp.showRateDialogIfNeeded(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAdView!=null) mAdView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdView!=null) mAdView.resume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharedPref.getInstance(this).putBoolean(Constants.ISGRANTED,true);
//                    if(code==-1){
//                        new ExtractFileInBackground(MainActivity.this, dialog, mAdapterAllApp.getListAppChecked(), snackbar1).execute();
//
//                    }else{
//                        new ExtractFileInBackground(MainActivity.this, dialog, currentAdapterApp.getListAppChecked(), snackbar1).execute();
//
//                    }
                } else {
                    SharedPref.getInstance(this).putBoolean(Constants.ISGRANTED,false);

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{
                ((AdapterApplication) mRecyclerView.getAdapter()).resetView(false);
               onChecked(0);
                break;
            }
            case R.id.action_filter_all:{
                code = 0;
                listCurrentApps.clear();
                listCurrentApps.addAll(mListAllApps);
                mRecyclerView.setAdapter(currentAdapterApp);
                currentAdapterApp.resetView(false);
                currentAdapterApp.notifyDataSetChanged();
                mBtnExtract.setVisibility(View.GONE);
                break;
            }
            case R.id.action_filter_system:{
                listCurrentApps.clear();
                listCurrentApps.addAll(mListSystemApps);
                code = 1;
                mRecyclerView.setAdapter(currentAdapterApp);
                currentAdapterApp.resetView(false);
                currentAdapterApp.notifyDataSetChanged();
                mBtnExtract.setVisibility(View.GONE);
                break;
            }
            case R.id.action_filter_user:{
                listCurrentApps.clear();
                listCurrentApps.addAll(mListUserApps);
                code = 2;
                mRecyclerView.setAdapter(currentAdapterApp);
                currentAdapterApp.resetView(false);
                currentAdapterApp.notifyDataSetChanged();
                mBtnExtract.setVisibility(View.GONE);
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
               currentAdapterApp.resetView(false);
               currentAdapterApp.notifyDataSetChanged();
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
                currentAdapterApp.resetView(false);
                currentAdapterApp.notifyDataSetChanged();
                break;
            }

        }


        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiview);
        if(mAdView!=null) mAdView.destroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_extract: {
                        if(code==-1){
                            new ExtractFileInBackground(MainActivity.this, dialog, mAdapterAllApp.getListAppChecked(), snackbar1).execute();

                        }else{
                            new ExtractFileInBackground(MainActivity.this, dialog, currentAdapterApp.getListAppChecked(), snackbar1).execute();

                        }

                break;

            }
        }
    }

    @Override
    public void onChecked(int count) {
        if (count > 0) {
            getSupportActionBar().setTitle(getString(R.string.apps)+"("+count+")");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           mBtnExtract.setVisibility(View.VISIBLE);
            //mBtnExtract.setAnimation(animationFadeout);
        } else {
            getSupportActionBar().setTitle(getString(R.string.app_name));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
          ///  mBtnExtract.setAnimation(animationFadeIn);
            mBtnExtract.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
//        searchView = (SearchView)menu.findItem(R.id.action_settings).getActionView();
//        searchView.setIconified(false);
//        searchView.setIconifiedByDefault(false);
//
//        searchView.requestFocus();

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mBtnExtract.setVisibility(View.GONE);
        ((AdapterApplication) mRecyclerView.getAdapter()).resetView(false);
          if (newText.isEmpty()) {
              ((AdapterApplication) mRecyclerView.getAdapter()).getFilter().filter("");
          } else {
              ((AdapterApplication) mRecyclerView.getAdapter()).getFilter().filter(newText.toLowerCase());
          }




        return true;
    }
    private List<Application> filter(List<Application> models, String query) {
        query = query.toLowerCase();

        final List<Application> filteredModelList = new ArrayList<>();
        for (Application model : models) {
            final String text = model.getmName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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
                    Bitmap bmIcon = CommonUtils.drawableToBitmap(icon,getBaseContext());
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
