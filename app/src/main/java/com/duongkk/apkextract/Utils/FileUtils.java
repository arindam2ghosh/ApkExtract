package com.duongkk.apkextract.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.duongkk.apkextract.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * Created by DuongKK on 4/18/2016.
 */
public class FileUtils {
    public static String getFolder(Context context) {
        String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator
                + context.getString(R.string.app_name) + File.separator + "file" + File.separator;// +fileName;
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return filePath;
    }


    public static void openFolder(Context context)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + getFolder(context));
        intent.setDataAndType(uri, "resource/folder");
        context.startActivity(Intent.createChooser(intent, "Open folder"));
    }
    public static File SaveImage(Context context,Bitmap finalBitmap) {

        String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator
                + context.getPackageName() + File.separator + "image" + File.separator;// +fileName;
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (filePath, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            LogX.e("Create file image success");
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static File writeFileSD(Context context, String nameFile, String content) {       // folder getFolder
        File mFile = new File(FileUtils.getFolder(context) + nameFile);
        try {
            mFile.createNewFile();
            if (mFile.exists()) {
                FileOutputStream fileOutputStream = new FileOutputStream(mFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                writer.write(content);
                writer.flush();
                writer.close();
                fileOutputStream.close();
                LogX.e("create file "+mFile.getPath()+" successful !");
                return mFile;
            }
        } catch (FileNotFoundException e) {
            LogX.e(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            LogX.e(e.toString());
            e.printStackTrace();
        }
        return null;
    }
    public static String readFile(Context context,String nameFile,String namePath){
        String result ="";
        return result;
    }
}
