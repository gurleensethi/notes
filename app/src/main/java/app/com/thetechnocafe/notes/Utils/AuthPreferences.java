package app.com.thetechnocafe.notes.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gurleensethi on 11/04/17.
 */

public class AuthPreferences {
    private static final AuthPreferences sInstance = new AuthPreferences();
    private static final String SP_AUTH_FILE_NAME = "auth_preferences";
    private static final String SP_USERNAME = "username";
    private static final String SP_PASSWORD = "password";
    private static final String SP_IS_LOGGED_IN = "is_logged_in";

    //Instance method
    public static AuthPreferences getInstance() {
        return sInstance;
    }

    //Singleton class
    private AuthPreferences() {
    }

    //Get the SharedPreferences
    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SP_AUTH_FILE_NAME, Context.MODE_PRIVATE);
    }

    //Get the SharedPreferences Editor
    private SharedPreferences.Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }

    //Get the stored username
    public String getUsername(Context context) {
        return getPreferences(context).getString(SP_USERNAME, null);
    }

    //Update the Username
    public AuthPreferences setUsername(Context context, String username) {
        getPreferencesEditor(context).putString(SP_USERNAME, username).commit();
        return sInstance;
    }

    //Get the stored password
    public String getPassword(Context context) {
        return getPreferences(context).getString(SP_PASSWORD, null);
    }

    //Update the password
    public AuthPreferences setPassword(Context context, String password) {
        getPreferencesEditor(context).putString(SP_PASSWORD, password).commit();
        return sInstance;
    }

    //Get the logged in status
    public boolean isLoggedIn(Context context) {
        return getPreferences(context).getBoolean(SP_IS_LOGGED_IN, false);
    }

    //Set the login status
    public void setLoginStatus(Context context, boolean loginStatus) {
        getPreferencesEditor(context).putBoolean(SP_IS_LOGGED_IN, loginStatus).commit();
    }
}
