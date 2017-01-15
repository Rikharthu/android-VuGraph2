package com.example.uberv.vugraph2.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * A class that helps parsing info for doing OAuth
 */
public class OAuthUtil {

    public static final String ACCESS_TOKEN="access_token";
    // how many milliseconds our token expires in (when we will have to get it again)
    public static final String EXPIRES_IN="expires_in";
    // kind of token returned
    public static final String TOKEN_TYPE="token_type";
    public static final String REFRESH_TOKEN="refresh_token";
    public static final String ACCOUNT_USERNAME="account_username";

    private static SharedPreferences sOAuthCredentials;

    /** Initialize shared preferences */
    public static void initSharedPref(Context context) {
        sOAuthCredentials = context.getSharedPreferences("oauth", Context.MODE_PRIVATE);
    }

    private static SharedPreferences getOAuthCredentials() {
        return sOAuthCredentials;
    }

    private static SharedPreferences.Editor editSharedPrefs() {
        return getOAuthCredentials().edit();
    }

    @Nullable
    public static String get(String key) {
        return sOAuthCredentials.getString(key, null);
    }

    public static Long getLong(String key) {
        return sOAuthCredentials.getLong(key, -1);
    }

    public static void set(String key, String value) {
        editSharedPrefs().putString(key, value).commit();
    }

    public static void set(String key, Long value) {
        editSharedPrefs().putLong(key, value);
    }

    /** Check if we have a valid token */
    public static boolean isAuthorized() {
        // TODO complete logic here
        return get(ACCESS_TOKEN)!=null && // token exists
                getLong(EXPIRES_IN)<System.currentTimeMillis(); // and it's not expired
    }


}
