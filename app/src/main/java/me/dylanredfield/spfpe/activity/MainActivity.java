package me.dylanredfield.spfpe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.fragment.student.PanelFragment;
import me.dylanredfield.spfpe.fragment.teacher.ClassListFragment;
import me.dylanredfield.spfpe.util.Keys;


// TODO make parseexceptions readable
public class MainActivity extends AppCompatActivity {
    private ParseUser mCurrentUser;
    private ParseObject mCurrentStudent;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);

        if (mActivity == null) {
            mActivity = this;
        }
        mCurrentUser = ParseUser.getCurrentUser();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);

        if (mCurrentUser.getParseObject(Keys.USER_TYPE_KEY).getObjectId().equals(Keys.TEACHER_OBJECT_ID)) {
            // TODO teacher shit
            addFragment(new ClassListFragment());
        } else {
            addFragment(new PanelFragment());
        }
    }
    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();
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
