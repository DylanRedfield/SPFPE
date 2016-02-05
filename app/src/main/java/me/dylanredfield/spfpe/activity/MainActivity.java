package me.dylanredfield.spfpe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.dialog.SelectClassDialog;
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

        if (mCurrentUser.getParseObject(Keys.USER_TYPE_KEY).getObjectId().equals(Keys.TEACHER_OBJECT_ID)) {
            // TODO teacher shit
        } else {
            queryForStudent();
        }
    }

    public void queryForStudent() {
        Helpers.getStudentQuery(mCurrentUser).getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    mCurrentStudent = parseObject;
                    mNewClass.setVisible(true);
                    mSelectClass.setVisible(true);
                    checkForClass();
                } else {
                    Toast.makeText(getApplicationContext(), Helpers.getReadableError(e),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void checkForClass() {
        if (mCurrentStudent.get(Keys.SELECTED_CLASS_POINT) == null) {
            AlertDialog dialog = Helpers.showDialog(this, "Whoops", "You did not select a class\n " +
                    "You need to do that now!");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Intent i = new Intent(getApplicationContext(), NewClassActivity.class);
                    i.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA, mCurrentStudent.getObjectId());
                    startActivity(i);
                    finish();
                }
            });

        }
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
                    Toast.makeText(getApplicationContext(), e.getMessage() + "\n Try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
