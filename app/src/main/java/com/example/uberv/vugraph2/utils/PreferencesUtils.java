package com.example.uberv.vugraph2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.uberv.vugraph2.VuGraphUser;
import com.google.gson.Gson;


public class PreferencesUtils {

    // What fields we are gonna get from Imgur API response
    // imgur returns access token
    public static final String ACCESS_TOKEN="access_token";
    // how many milliseconds our token expires in (when we will have to get it again)
    public static final String EXPIRES_IN="expires_in";
    // kind of token returned
    public static final String TOKEN_TYPE="token_type";
    public static final String REFRESH_TOKEN="refresh_token";
    public static final String ACCOUNT_USERNAME="account_username";
    public static final String CURRENT_USER="CURRENT_USER";

    private static SharedPreferences preferences;

    /** Initialize shared preferences */
    public static void initSharedPref(Context context) {
        preferences = context.getSharedPreferences("oauth", Context.MODE_PRIVATE);
    }

    private static SharedPreferences getPreferences() {
        return preferences;
    }

    private static SharedPreferences.Editor editSharedPrefs() {
        return getPreferences().edit();
    }

    @Nullable
    public static String get(String key) {
        return preferences.getString(key, null);
    }

    public static VuGraphUser getCurrentUser(){
        Gson gson = new Gson();
        VuGraphUser user = gson.fromJson(get(CURRENT_USER),VuGraphUser.class);
        return user;
    }

    public static void remove(String key){
        editSharedPrefs().remove(key).commit();
    }

    public static void setCurrentUser(VuGraphUser user){
        if(user==null){

        }else{
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            set(CURRENT_USER,userJson);
        }
    }

    public static void set(String key, String value) {
        editSharedPrefs().putString(key, value).commit();
    }

    /** Check if we have a valid token */
    public static boolean isAuthorized() {
        // TODO complete logic here
        return get(ACCESS_TOKEN)!=null && // token exists
                getLong(EXPIRES_IN)<System.currentTimeMillis(); // and it's not expired
    }

    public static Long getLong(String key) {
        return preferences.getLong(key, -1);
    }


}
