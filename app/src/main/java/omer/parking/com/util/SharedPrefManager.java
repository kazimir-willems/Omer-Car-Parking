package omer.parking.com.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "CarParkingPreference";
    private static final String TAG_CURRENT_OFFICE_ID = "current_office_id";
    private static final String TAG_CURRENT_OFFICE = "current_office";
    private static final String TAG_OFFICE_ADDRESS = "current_address";
    private static final String TAG_LATITUDE = "current_latitude";
    private static final String TAG_LONGITUDE = "current_longitude";
    private static final String TAG_NO_LOT_TUNE = "no_lot_tune";
    private static final String TAG_DEFAULT_TUNE = "default_tune";
    private static final String TAG_FIRST_RUN = "first_run";
    private static final String TAG_DEFAULT_TUNE_NAME = "default_tune_name";
    private static final String TAG_NO_LOT_TUNE_NAME = "no_tune_name";
    private static final String TAG_CAME_WITH_CAR = "came_with_car";        //0:No Slot, 1:Parked, 2:No Car, 3:Not parked
    private static final String TAG_IN_OFFICE = "in_office";
    private static final String TAG_LEAVE_FLAG = "leave_flag";  //true: leaved, false: not leaved.
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_MOBILE_PHONE = "phone";
    private static final String TAG_CAR_PLATE_NUM = "car_plate_num";
    private static final String TAG_ACTION = "action";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean saveUserID(int value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TAG_USER_ID, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public int getUserID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(TAG_USER_ID, 0);
    }

    public boolean saveCurrentOfficeID(int value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TAG_CURRENT_OFFICE_ID, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public int getCurrentOfficeID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(TAG_CURRENT_OFFICE_ID, 0);
    }

    public boolean saveDefaultTuneName(String name){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_DEFAULT_TUNE_NAME, name);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDefaultTuneName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_DEFAULT_TUNE_NAME, "Default");
    }

    public boolean saveNoTuneName(String name){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_NO_LOT_TUNE_NAME, name);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getNoTuneName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_NO_LOT_TUNE_NAME, "Default");
    }

    public boolean saveOffice(String office){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_CURRENT_OFFICE, office);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getOffice(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_CURRENT_OFFICE, "");
    }

    public boolean saveOfficeAddress(String address){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_OFFICE_ADDRESS, address);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getOfficeAddress(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_OFFICE_ADDRESS, "");
    }

    public boolean saveLatitude(String latitude){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_LATITUDE, latitude);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getLatitude(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_LATITUDE, "");
    }

    public boolean saveLongitude(String longitude){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_LONGITUDE, longitude);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getLongitude(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_LONGITUDE, "");
    }

    //this method will save the device token to shared preferences
    public boolean saveNoLotTune(String uri){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_NO_LOT_TUNE, uri);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getNoLotTune(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(mCtx, RingtoneManager.TYPE_RINGTONE);
        return  sharedPreferences.getString(TAG_NO_LOT_TUNE, defaultRintoneUri.toString());
    }

    public boolean saveDefaultTune(String uri){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_DEFAULT_TUNE, uri);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDefaultTune(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(mCtx, RingtoneManager.TYPE_RINGTONE);
        return  sharedPreferences.getString(TAG_DEFAULT_TUNE, defaultRintoneUri.toString());
    }

    public boolean saveFirstRun(boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_FIRST_RUN, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public boolean getFirstRun(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_FIRST_RUN, true);
    }

    public boolean saveCameWithCar(int value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TAG_CAME_WITH_CAR, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public int getCameWithCar(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(TAG_CAME_WITH_CAR, 3);
    }

    public boolean saveInOffice(boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_IN_OFFICE, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public boolean getInOffice(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_IN_OFFICE, false);
    }

    public boolean saveLeaving(boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_LEAVE_FLAG, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public boolean getLeaving(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_LEAVE_FLAG, true);
    }

    public boolean saveCarPlateNum(String num){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_CAR_PLATE_NUM, num);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getCarPlateNum(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_CAR_PLATE_NUM, "");
    }

    public boolean saveMobilePhone(String num){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_MOBILE_PHONE, num);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getMobilePhone(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_MOBILE_PHONE, "");
    }

    public boolean saveAction(boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_ACTION, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public boolean getAction(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_ACTION, false);
    }
}