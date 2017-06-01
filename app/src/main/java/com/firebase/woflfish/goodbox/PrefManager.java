package com.firebase.woflfish.goodbox;
/**
 * Created by Win 8.1 VS8 X64 on 1/5/2017.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Welcome";

    private static final String IS_SIGNED_IN = "IsSignedIn";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void SetSignInStatus(boolean isSignedIn) {
        editor.putBoolean(IS_SIGNED_IN, isSignedIn);
        editor.commit();
    }

    public boolean isSignedIn() {
        return pref.getBoolean(IS_SIGNED_IN, false);
    }
}
