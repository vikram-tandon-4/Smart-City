package helpout.vikramtandonapps.com.helpout.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vikra on 10/1/2017.
 */

public class AppPreferences {

    private static String USER_PHONE_NUMBER = "user_phone_number";
    private static String LOGIN = "login";

                /*
    *
    * Saving and Retrieving user's phone number
    * */

    public static String getUserPhoneNumber(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_PHONE_NUMBER, "");
    }

    public static void setUserPhoneNumber(Context context, String userPhoneNumber) {
        SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(USER_PHONE_NUMBER, userPhoneNumber);
        editor.commit();
    }

    public static boolean alreadyLoggedIn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LOGIN, false);
    }

    public static void setLogin(Context context, boolean log) {
        SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putBoolean(LOGIN, log);
        editor.commit();
    }
}
