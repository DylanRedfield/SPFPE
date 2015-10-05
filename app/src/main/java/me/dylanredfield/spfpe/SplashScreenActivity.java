package me.dylanredfield.spfpe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

public class SplashScreenActivity extends Activity {

    private ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setDefaultValues();
        splashLogic();
    }

    private void setDefaultValues() {
        mCurrentUser = ParseUser.getCurrentUser();
    }

    private void splashLogic () {

        // Checks to see if user is logged in
        if (mCurrentUser == null) {
            // TODO intent to SignInActivity
            Log.d("Cached User: ", "false");

            Intent i = new Intent(getApplicationContext(), LogInActivity.class);
        } else {
            // TODO intent to MainActivity
            Log.d("Cached User: ", "true");
        }
    }
}
