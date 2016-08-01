package com.duongkk.apkextract.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duongkk.apkextract.R;
import com.duongkk.apkextract.Utils.CommonUtils;
import com.duongkk.apkextract.Utils.FileUtils;
import com.duongkk.apkextract.Utils.LogX;
import com.duongkk.apkextract.activities.MainActivity;
import com.duongkk.apkextract.asyn.ExtractFileInBackground;
import com.duongkk.apkextract.models.Application;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.turingtechnologies.materialscrollbar.ICustomAdapter;
import com.turingtechnologies.materialscrollbar.INameableAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MyPC on 7/20/2016.
 */
public class AdapterApplication extends RecyclerView.Adapter<AdapterApplication.ViewHolder> implements Filterable,INameableAdapter, ICustomAdapter {
    List<Application> listApplications;
    SparseBooleanArray sparseBooleanArray;
    Activity context;
    OnCheckResponse onCheckResponse;
    boolean isLong = false;
    Vibrator vibe ;
    public AdapterApplication(Activity context, List<Application> listApplications,OnCheckResponse onCheckResponse){
        this.listApplications= listApplications;
        this.context = context;
        this.onCheckResponse = onCheckResponse;
        sparseBooleanArray= new SparseBooleanArray();
       // vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        for (int i = 0; i < listApplications.size(); i++) {
            sparseBooleanArray.put(i,listApplications.get(i).isChecked());
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.application_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(holder!=null){
            final Application app = listApplications.get(position);
          holder.mIcon.setImageBitmap(app.getmBitmapIcon());
        //    Glide.with(context).load(app.getmBitmapIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mIcon);
            holder.mTvPackage.setText(app.getmPackage());
            holder.mTvName.setText(app.getmName());
            holder.mCheckbox.setOnCheckedChangeListener(null);
            holder.mCheckbox.setChecked(sparseBooleanArray.get(position));
            if(sparseBooleanArray.get(position)) {
                addCheck(holder);
            }else{
                removeCheck(holder);
            }
            holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    setCheckedApp(isChecked, position, app);
                    onCheckResponse.onChecked(getListAppChecked().size());
                }
            });
            holder.mRoot.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if(isLong) {
                        if (app.isChecked()) {
                            removeCheck(holder);
                           // setCheckedApp(false, position, app);
                            holder.mCheckbox.setChecked(false);
                        } else {

                            addCheck(holder);
                         //   setCheckedApp(true, position, app);
                            holder.mCheckbox.setChecked(true);
                        }
                    }

                }
            });
            holder.mRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    isLong =true;
                    if(app.isChecked()){
                        removeCheck(holder);
                      //  setCheckedApp(false, position, app);
                        holder.mCheckbox.setChecked(false);
                    }else{
                        addCheck(holder);
                      //  setCheckedApp(true, position, app);
                        holder.mCheckbox.setChecked(true);
                    }

                    return true;
                }

            });
            final Drawable[] circleSubButtonDrawables = new Drawable[3];
            int[] drawablesResource = new int[]{
                    R.drawable.open,
                    R.drawable.share,
                    R.drawable.package_down_24
            };

            for (int i = 0; i < 3; i++)
                circleSubButtonDrawables[i]
                        = ContextCompat.getDrawable(context, drawablesResource[i]);
            final int[][] subButtonColors = new int[3][2];
            for (int i = 0; i < 3; i++) {
                subButtonColors[i][1] = GetRandomColor();
                subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
            }

            final String[] circleSubButtonTexts = new String[]{
                    context.getString(R.string.open_app),
                    context.getString(R.string.share),
                    context.getString(R.string.extract)};
            holder.mMenu.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Now with Builder, you can init BMB more convenient
                    new BoomMenuButton.Builder()
                            .subButtons(circleSubButtonDrawables, subButtonColors, circleSubButtonTexts)
                            .button(ButtonType.HAM)
                            .boom(BoomType.PARABOLA)
                            .place(PlaceType.HAM_3_1)
                            .frames(80)
                            .duration(500)
                            .delay(10)
                            .subButtonsShadow(Util.getInstance().dp2px(1), Util.getInstance().dp2px(1))
                            .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                                @Override
                                public void onClick(int buttonIndex) {
                                    switch (buttonIndex){
                                        case 0:{
                                            try {
                                                Intent intent = context.getPackageManager().getLaunchIntentForPackage(app.getmPackage());
                                                context.startActivity(intent);
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                                LogX.e(e.toString());
                                            }
                                            break;
                                        }
                                        case 1:{
                                            File sourceFile = new File(app.getmPath());
                                            File destFile = new File(FileUtils.getFolder(context)+File.separator+app.getmName()+".apk");
                                            try {
                                                org.apache.commons.io.FileUtils.copyFile(sourceFile,destFile);
                                                Intent shareIntent = CommonUtils.getShareIntent(destFile);
                                                context.startActivity(Intent.createChooser(shareIntent, String.format(context.getResources().getString(R.string.send_to), app.getmName())));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            break;
                                        }
                                        case 2:{
                                            ExtractInBackground(app);
                                            break;
                                        }
                                    }
                                }
                            })
                            .init(holder.mMenu);
                }
            }, 0);
        }
    }

    private void removeCheck(ViewHolder holder) {
        holder.mLayoutCheckbox.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         //   holder.mParentLayout.setElevation(0f);
        }
    }

    private void addCheck(ViewHolder holder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         //   holder.mParentLayout.setElevation(context.getResources().getDimension(R.dimen.elevation));
        }
        holder.mLayoutCheckbox.setVisibility(View.VISIBLE);
    }

    private void ExtractInBackground(Application app) {
        List<Application> apps =new ArrayList<Application>();
        apps.add(app);
        ExtractFileInBackground extractFileInBackground =new ExtractFileInBackground(context, new ProgressDialog(context), apps,((MainActivity) context).snackbar1);
        extractFileInBackground.execute();
    }

    public void resetView(boolean isChecked){
        if(!isChecked) sparseBooleanArray.clear();
        for (int i = 0; i < listApplications.size(); i++) {
            listApplications.get(i).setChecked(false);
        }
        notifyDataSetChanged();
    }
    private void setCheckedApp(boolean isChecked, int position, Application app) {

        sparseBooleanArray.put(position,isChecked);
        app.setChecked(isChecked);
        listApplications.set(position,app);
    }

    public List<Application> getListAppChecked(){
       List<Application> listAppChecked;
       listAppChecked =new ArrayList<>();
        for (Application app:listApplications) {
            if(app.isChecked()){
                listAppChecked.add(app);
            }
        }
       return listAppChecked;
    }
    @Override
    public int getItemCount() {

        try{
            return listApplications.size();
        } catch (NullPointerException e){

        }
        return 0;
    }


    private static String[] Colors = {
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};

    public static int GetRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }
    @Override
    public String getCustomStringForElement(int element) {
        return listApplications.get(element).getmName().substring(0,1);
    }

    List<Application> appListSearch;
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Application> results = new ArrayList<>();
                if (appListSearch == null) {
                    appListSearch = listApplications;
                }
                if (charSequence != null) {
                    if (appListSearch != null && appListSearch.size() > 0) {
                        for (final Application appInfo : appListSearch) {
                            if (appInfo.getmName().toLowerCase().contains(charSequence.toString())) {
                                results.add(appInfo);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listApplications = (ArrayList<Application>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public Character getCharacterForElement(int element) {
        if(listApplications.size()>0){
           return listApplications.get(element).getmName().charAt(0);
        }else{
            return 'A';
        }
    }


    class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView mTvName;
        private TextView mTvPackage;
        private CheckBox mCheckbox;
        private ImageView mIcon;
        private LinearLayout mRoot;
        private BoomMenuButton mMenu;
        private RelativeLayout mLayoutCheckbox;
        private FrameLayout mParentLayout;
        CircleOutlineProvider mOutLineProvider;
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView)itemView.findViewById(R.id.tv_name);
            mTvPackage = (TextView)itemView.findViewById(R.id.tv_package);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mCheckbox = (CheckBox)itemView.findViewById(R.id.cb);
            mMenu =(BoomMenuButton)itemView.findViewById(R.id.boom_circle);
            mLayoutCheckbox = (RelativeLayout) itemView.findViewById(R.id.checkBoxLayout);
            mParentLayout =(FrameLayout)itemView.findViewById(R.id.parentLayout);
            mRoot = (LinearLayout)itemView.findViewById(R.id.card_application);
            mOutLineProvider = new CircleOutlineProvider();
            mParentLayout.setOutlineProvider(mOutLineProvider);
            mParentLayout.setClipToOutline(true);

        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CircleOutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            int margin = Math.min(view.getWidth(), view.getHeight())/10;
            outline.setRoundRect(0, 0, view.getWidth() , view.getHeight() , 0);
        }
    }
    public interface OnCheckResponse{
        public void onChecked(int count);
    }
}
