package me.dylanredfield.spfpe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;


// TODO make parseexceptions readable
public class MainActivity extends AppCompatActivity {
    private ParseUser mCurrentUser;
    private ParseObject mCurrentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCurrentUser = ParseUser.getCurrentUser();

        Helpers.getStudentQuery(mCurrentUser).getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    // TODO only show ActionBarButton now
                    mCurrentStudent = parseObject;
                } else {
                    Helpers.showDialog(getApplicationContext(), "Whoops", e.getMessage());
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                        startActivity(i);
                        finish();
                    } else {

                    }
                }
            });
            return true;
        } else if (id == R.id.new_class) {
            Intent i = new Intent(getApplicationContext(), NewClassActivity.class);
            i.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA, mCurrentStudent.getObjectId());
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
