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
    private static final String TAG_CAME_WITH_CAR = "came_with_car";
    private static final String TAG_IN_OFFICE = "in_office";

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

    public boolean saveCameWithCar(boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_CAME_WITH_CAR, value);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public boolean getCameWithCar(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(TAG_CAME_WITH_CAR, true);
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
}