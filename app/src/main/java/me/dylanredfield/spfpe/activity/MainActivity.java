package me.dylanredfield.spfpe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.fragment.SelectClassDialog;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;


// TODO make parseexceptions readable
public class MainActivity extends AppCompatActivity {
    private ParseUser mCurrentUser;
    private ParseObject mCurrentStudent;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private MenuItem mSelectClass;
    private MenuItem mNewClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mActivity == null) {
            mActivity = this;
        }
        mCurrentUser = ParseUser.getCurrentUser();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);

        Helpers.getStudentQuery(mCurrentUser).getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    mCurrentStudent = parseObject;
                    mNewClass.setVisible(true);
                    mSelectClass.setVisible(true);
                } else {
                    Helpers.showDialog(getApplicationContext(), "Whoops", Helpers.getReadableError(e));
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mNewClass = menu.findItem(R.id.new_class);
        mSelectClass = menu.findItem(R.id.select_class);
        mNewClass.setVisible(false);
        mSelectClass.setVisible(false);
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
        } else if (id == R.id.select_class) {
            ParseRelation<ParseObject> classRelation =
                    mCurrentStudent.getRelation(Keys.CLASSES_REL);
            SelectClassDialog dialog = new SelectClassDialog();
            dialog.setArguments(classRelation, mCurrentStudent);
            dialog.show(getSupportFragmentManager(), null);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("SelectClass", "Method Called");
        if (requestCode == Keys.CLASS_RESULT_CODE && resultCode == Keys.CLASS_RESULT_CODE) {
            Log.d("SelectClass", "Condition True");

        }
    }

    public void onReturnValue(String value) {
        final ParseObject replaceClass = ParseObject.createWithoutData(Keys.CLASS_KEY, value);
        mCurrentStudent.put(Keys.SELECTED_CLASS_POINT, replaceClass);
        mCurrentStudent.getRelation(Keys.CLASSES_REL).add(replaceClass);
        mCurrentStudent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    Log.d("SelectClass", replaceClass.toString());
                } else {
                    Helpers.showDialog(mActivity, "Whoops", Helpers.getReadableError(e));
                }
            }
        });
    }

}
