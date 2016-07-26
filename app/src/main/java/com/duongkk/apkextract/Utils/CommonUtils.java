package com.duongkk.apkextract.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.duongkk.apkextract.R;

/**
 * Created by MyPC on 6/28/2016.
 */
public class CommonUtils {

    public static AlertDialog showDialog(Context context, String name, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(name);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
    public static Boolean checkPermissions(Activity activity) {
        Boolean res = false;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            res = true;
        }

        return res;
    }

    public static int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] {R.color.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public static Bitmap drawableToBitmap (Drawable drawable,Context context) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Bitmap bmResult = Bitmap.createScaledBitmap(bitmap,context.getResources().getDimensionPixelSize(R.dimen.icon_app_size),context.getResources().getDimensionPixelSize(R.dimen.icon_app_size),false);
        Canvas canvas = new Canvas(bmResult);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmResult;
    }
//    public static boolean verifyEditext(Context context, @Nullable EditText mEdtPass, @Nullable EditText mEdtFirstName,@Nullable EditText mEdtLastName, @Nullable EditText mEdtPass2, @Nullable EditText mEdtAddress, @Nullable EditText mEdtPhone) {
//        if (mEdtPhone != null && (mEdtPhone.getText().length() < 9 || mEdtPhone.getText().length() > 11)) {
//            CommonUtils.showDialog(context, "Thông báo", "Số điện thọai không hợp lệ. Vui lòng kiểm tra lại!");
//            return false;
//        }
//        if (mEdtAddress != null && mEdtAddress.getText().length() == 0) {
//            CommonUtils.showDialog(context, "Thông báo", "Địa chỉ không hợp lệ. Vui lòng kiểm tra lại!");
//            return false;
//        }
//        if (mEdtPass != null && mEdtPass.getText().length() < 6) {
//            CommonUtils.showDialog(context, "Thông báo", "Mật khẩu phải trên 6 ký tự. Vui lòng kiểm tra lại!");
//            return false;
//        }
//        if (mEdtFirstName != null && mEdtFirstName.getText().length() <=0) {
//            CommonUtils.showDialog(context, "Thông báo", "Bạn chưa nhập Họ. Vui lòng kiểm tra lại!");
//            return false;
//        }
//        if (mEdtLastName != null && mEdtLastName.getText().length() <=0) {
//            CommonUtils.showDialog(context, "Thông báo", "Bạn chưa nhập Tên. Vui lòng kiểm tra lại!");
//            return false;
//        }
//        if (mEdtPass2 != null && mEdtPass != null && !mEdtPass2.getText().toString().equals(mEdtPass.getText().toString())) {
//            CommonUtils.showDialog(context, "Thông báo", "Mật khẩu không trùng khớp. Vui lòng kiểm tra lại!");
//            return false;
//        }
//        return true;
//
//    }
//
//    public static void startToDirectionTo(Context context, LatLng latLng) {
//
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?daddr=" + latLng.latitude + "," + latLng.longitude  ));
//            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//            context.startActivity(intent);
//        }catch (Exception e){
//            RLog.e(e.toString());
//            Intent intent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?daddr=" + latLng.latitude + "," + latLng.longitude  ));
//            context.startActivity(intent);
//        }
//
//    }
//
//    public static String getDate(long milliSeconds, String dateFormat) {
//        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);
//        return formatter.format(calendar.getTime());
//    }
//
//    public static long convertToSec2(String givenDateString) {
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
//        try {
//            Date mDate = sdf.parse(givenDateString);
//            long timeInMilliseconds = mDate.getTime();
//            System.out.println("Date in milli :: " + timeInMilliseconds);
//            return timeInMilliseconds;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    public static String getCurrentTime() {
//        long date = System.currentTimeMillis();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        return sdf.format(date);
//    }
////    public static void sendViaFacebook(final Activity context, final String msg, final SimpleFacebook mSimpleFacebook) {
////        // final AlertDialog dialog = AlertDialogCustom.getInstance(context).showDialogGenerateLink(context);
////        final OnPublishListener onPublishListener = new OnPublishListener() {
////            @Override
////            public void onComplete(String response) {
////                super.onComplete(response);
////                Toast.makeText(context, "Share success!", Toast.LENGTH_SHORT).show();
////                // dialog.dismiss();
////                //     context.setResult(Activity.RESULT_OK);
////                try {
////                    ((ShareLocationActivity) context).sendBack();
////                    ((ShareLocationActivity) context).hideDialog();
////                }catch (Exception e){
////                    LogX.e(e.toString());
////                    ((BaseRecyclerViewActivitiy) context).sendBack();
////                    ((BaseRecyclerViewActivitiy) context).hideDialog();
////                }
////                context.finish();
////            }
////        };
////        OnLoginListener onLoginListener = new OnLoginListener() {
////            @Override
////            public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
////                Feed feed = new Feed.Builder()
////                        .setMessage(msg)
////                        .build();
////                mSimpleFacebook.publish(feed,onPublishListener);
////            }
////
////            @Override
////            public void onCancel() {
////                LogX.e("Cancel");
////            }
////
////            @Override
////            public void onException(Throwable throwable) {
////                LogX.e("Exception");
////            }
////
////            @Override
////            public void onFail(String reason) {
////                LogX.e("fail " + reason);
////            }
////        };
////        mSimpleFacebook.login(onLoginListener);
////
////
////    }
//
//    //    public static void shareMessage(String msg, List<Contact> listPickers, AlertDialog dialog,SimpleFacebook mSimpleFacebook,Activity context) {
////        if (listPickers != null && listPickers.size() == 1) {
////            switch (listPickers.get(0).getCode()) {
////                case Constants.SOCIAL_FB: {
////                    dialog.dismiss();
////                    CommonUtils.sendViaFacebook(context, msg + " " + listPickers.get(0).getMessage(), mSimpleFacebook);
////                    break;
////                }
////                case Constants.SOCIAL_TW: {
////                    dialog.dismiss();
////                    CommonUtils.sendViaTwitter(context, msg + " " + listPickers.get(0).getMessage());
////                    break;
////                }
////                case Constants.CONTACT: {
////                    CommonUtils.sendSMS(context, listPickers, msg);
////                    break;
////                }
////            }
////        } else {
////            // dialog= AlertDialogCustom.getInstance(this).showDialogGenerateLink(this);
////            CommonUtils.sendSMS(context, listPickers, msg);
////        }
////    }
//    public static void sendViaTwitter(Activity context, String msg) {
//        // AlertDialog dialog = AlertDialogCustom.getInstance(context).showDialogGenerateLink(context);
//        String tweetUrl = "https://twitter.com/intent/tweet?text=" + msg + " &url=";
//        Uri uri = Uri.parse(tweetUrl);
//        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
//        // context.setResult(Activity.RESULT_OK);
//        //context.finish();
//    }
//
//    public static void focusCurrentLocation(LatLng latLng, float sizeZoom, GoogleMap mMap) {
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(sizeZoom).build();
//        CameraUpdate zoom = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        mMap.animateCamera(zoom);
//    }
//    public static void focusLocations(LatLngBounds bounds,GoogleMap mMap) {
//        CameraUpdate zoom = CameraUpdateFactory.newLatLngBounds(bounds, 300, 300, 5);
//        mMap.animateCamera(zoom);
//    }
//    public static void focusAllMarkers(List<Place> places, GoogleMap mMap, Context context) {
//        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
//        for (Place place : places) {
//            latLngBounds.include(place.getLatLng());
//        }
//        LatLngBounds bounds = latLngBounds.build();
//        int width = context.getResources().getDisplayMetrics().widthPixels;
//        int height = context.getResources().getDisplayMetrics().heightPixels;
//        CameraUpdate zoom = CameraUpdateFactory.newLatLngBounds(bounds, 300, 300, 5);
//        mMap.animateCamera(zoom);
//
//    }
//
//    public static void focusAllMarkers(List<LatLng> places, GoogleMap mMap) {
//        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
//        for (LatLng place : places) {
//            latLngBounds.include(place);
//        }
//        LatLngBounds bounds = latLngBounds.build();
//        CameraUpdate zoom = CameraUpdateFactory.newLatLngBounds(bounds, 300, 300, 5);
//        mMap.animateCamera(zoom);
//// Calls moveCamera passing screen size as parameters
//        //map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, 10));
//
//    }
//
//    public static void focuseLocation(LatLng latLng, float sizeZoom, GoogleMap mMap) {
//        LatLng latLng1 = new LatLng(latLng.latitude - 0.005, latLng.longitude);
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng1).zoom(sizeZoom).build();
//        CameraUpdate zoom = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        mMap.animateCamera(zoom);
//    }
//
////    public static void setTypeMap(GoogleMap mMap) {
////        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
////            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
////            DataStore.getInstance().saveInt(Constants.TYPE_MAP,GoogleMap.MAP_TYPE_SATELLITE).commit();
////        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
////            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
////            DataStore.getInstance().saveInt(Constants.TYPE_MAP,GoogleMap.MAP_TYPE_HYBRID).commit();
////        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
////            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
////            DataStore.getInstance().saveInt(Constants.TYPE_MAP,GoogleMap.MAP_TYPE_NORMAL).commit();
////        } else {
////            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
////            DataStore.getInstance().saveInt(Constants.TYPE_MAP,GoogleMap.MAP_TYPE_NORMAL).commit();
////        }
////    }
//
//    public static Marker addMarker(int resource, GoogleMap mMap, LatLng latLng) {
//        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(resource)));
//        marker.showInfoWindow();
//        return marker;
//    }
//
//    public static Marker addMarker(int resource, GoogleMap mMap, LatLng latLng, String title) {
//        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(resource)).title(title));
//        marker.showInfoWindow();
//        return marker;
//    }
//
//
////    public static Marker addTextMarker(GoogleMap mMap, LatLng latLng, String text, Context context) {
////        Bitmap bitmap = writeTextOnDrawable(text,context);
////
////        return mMap.addMarker(new MarkerOptions().position(latLng)
////                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
////    }
//
//    public static Bitmap drawableToBitmap(Drawable drawable) {
//
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//
//        return bitmap;
//    }
//
//    public static void shareSimpleText(String msg, Context context) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//
//        intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
//        intent.putExtra(Intent.EXTRA_TEXT, msg);
//        context.startActivity(Intent.createChooser(intent, "Send via"));
//    }
////    public static Bitmap writeTextOnDrawable(String text, Context context) {
////        String content = text;
////        String content2 = null;
////        if (content.length() > 10) {
////            content = text.substring(0, 10);
////            content2 = text.substring(10);
////        }
////
////        Bitmap bm = null;
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
////            bm = drawableToBitmap(context.getDrawable(R.drawable.bg_edt_guide_map));
////        }
////        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
////        Paint paint = new Paint();
////        paint.setStyle(Paint.Style.FILL);
////        paint.setColor(Color.RED);
////        paint.setTypeface(tf);
////        paint.setTextAlign(Paint.Align.CENTER);
////        paint.setTextSize(convertToPixels(context, 12));
////        Rect textRect = new Rect();
////        paint.getTextBounds(text, 0, text.length(), textRect);
////        Canvas canvas = new Canvas(bm);
////        //If the text is bigger than the canvas , reduce the font size
////        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
////            paint.setTextSize(convertToPixels(context, 11));        //Scaling needs to be used for different dpi's
////        //Calculate the positions
////        int xPos = (canvas.getWidth() / 2);     //-2 is for regulating the x position offset
////        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
////        int yPos = (int) ((canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2));
////        canvas.drawText(content, xPos, yPos, paint);
////        if (content2 != null) {
////            canvas.drawText(content2, xPos, yPos * 2, paint);
////        }
////        return bm;
////    }
//
//    public static int convertToPixels(Context context, int nDP) {
//        final float conversionScale = context.getResources().getDisplayMetrics().density;
//
//        return (int) ((nDP * conversionScale) + 0.5f);
//
//    }
//
//    public static void transToTab(FragmentManager fragmentManager, Fragment fragment) {
//        fragmentManager.beginTransaction().replace(R.id.contentview, fragment).addToBackStack(null).commit();
//    }
//
//    //    public static void sendSMS(final Context context, List<Contact> contacts, String msg) {
////        try {
////            String SENT = "sent";
////            String DELIVERED = "delivered";
////            Intent sentIntent = new Intent(SENT);
////            PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////            Intent deliveryIntent = new Intent(DELIVERED);
////            PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, deliveryIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////            SmsManager smsManager = SmsManager.getDefault();
////            for (Contact c : contacts) {
////                //  LogX.e("send to "+ c.getmNumber() +" with text  : "+c.getMessage());
////                ArrayList<String > s=new ArrayList<>();
////                ArrayList<PendingIntent > spen=new ArrayList<>();
////                spen.add(deliverPI);
////                ArrayList<PendingIntent > ssent=new ArrayList<>();
////                spen.add(sentPI);
////                if(msg==null) msg ="";
////                s.add(msg+" ");
////                if(c.getMessage()!=null && c.getMessage().length()>60){
////                    s.addAll(splitByNumber(60,c.getMessage()));
////                }else {
////                    s.add(c.getMessage());
////                }
////                smsManager.sendMultipartTextMessage(c.getmNumber(), null,s, ssent, spen);
////
////            }
////        } catch (Exception ex) {
////            LogX.e( ex.getMessage().toString());
////            Toast.makeText(context,
////                    R.string.k_gui_tin_nhan, Toast.LENGTH_LONG)
////                    .show();
////            ex.printStackTrace();
////        }
////    }
//    private static List<String> splitByNumber(int size, String text) {
//        List<String> strings = new ArrayList<String>();
//        int index = 0;
//        while (index < text.length()) {
//            strings.add(text.substring(index, Math.min(index + size, text.length())));
//            index += size;
//        }
//        return strings;
//    }
//
//    //    public static String fixNumberPhone(String number){
////        if (number!=null && !number.equals("null") && number.contains(Constants.DAU_SO)) {
////            number = "0" + number.substring(3);
////            return number;
////        }else{
////            return number;
////        }
////
////    }
//    public static double[] getLatlngBox(LatLng latLng) {
//        double latlngBox[] = new double[4];
//        double lat = latLng.latitude;
//        double lng = latLng.longitude;
//        lat = round(lat, 6);
//        lng = round(lng, 6);
//
//        latlngBox[0] = lat;   //north
//        latlngBox[1] = lat - 0.001;   //south
//        latlngBox[2] = lng;   //east
//        latlngBox[3] = lng - 0.001;   //west
//        return latlngBox;
//    }
//
//    public static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//        long factor = (long) Math.pow(10, places);
//        value = value * factor;
//        long tmp = Math.round(value);
//        return (double) tmp / factor;
//    }
//
//    public static String getPhoneNumber(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String phone = telephonyManager.getLine1Number();
//        phone = phone.trim();
//        phone = "0" + phone.substring(3);
//        // LogX.e("Phone number = ",phone);
//        return phone;
//    }
//
//




}
