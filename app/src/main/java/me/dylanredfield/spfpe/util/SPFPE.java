package me.dylanredfield.spfpe.util;

import android.app.Application;

import com.parse.Parse;

public class SPFPE extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(getApplicationContext(), Keys.APP_ID, Keys.CLIENT_KEY);
    }
}
