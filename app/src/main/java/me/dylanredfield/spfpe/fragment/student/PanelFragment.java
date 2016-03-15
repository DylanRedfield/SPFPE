package me.dylanredfield.spfpe.fragment.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import me.dylanredfield.spfpe.activity.student.AssignmentsActivity;
import me.dylanredfield.spfpe.activity.student.FitnessMainActivity;
import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.activity.LogInActivity;
import me.dylanredfield.spfpe.activity.student.NewClassActivity;
import me.dylanredfield.spfpe.activity.student.MakeUpListActivity;
import me.dylanredfield.spfpe.dialog.SelectClassDialog;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class PanelFragment extends Fragment {
    private View mView;
    private RelativeLayout mFitnessLayout;
    private RelativeLayout mMakeupLayout;
    private RelativeLayout mAssignmentsLayout;
    private ParseObject mCurrentStudent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedState) {
        mView = inflater.inflate(R.layout.fragment_student_panel, null, false);

        setDefaultValues();
        queryForStudent();

        return mView;
    }

    public void setDefaultValues() {
        mFitnessLayout = (RelativeLayout) mView.findViewById(R.id.fitness);
        mMakeupLayout = (RelativeLayout) mView.findViewById(R.id.makeups);
        mAssignmentsLayout = (RelativeLayout) mView.findViewById(R.id.assignments);

        setListeners();
        setHasOptionsMenu(true);
    }

    public void queryForStudent() {
        Helpers.getStudentQuery(ParseUser.getCurrentUser()).getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    mCurrentStudent = parseObject;
                    checkForClass();
                } else {
                    Toast.makeText(getActivity(), Helpers.getReadableError(e),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void checkForClass() {
        if (mCurrentStudent.get(Keys.SELECTED_CLASS_POINT) == null) {
            AlertDialog dialog = Helpers.showDialog(getActivity(), "Whoops", "You did not select a class\n " +
                    "You need to do that now!");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Intent i = new Intent(getActivity(), NewClassActivity.class);
                    i.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA, mCurrentStudent.getObjectId());
                    startActivity(i);
                    getActivity().finish();
                }
            });

        }
    }

    private void setListeners() {
        mFitnessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fitnessIntent();
            }
        });
        mMakeupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeupIntent();

            }
        });
        mAssignmentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AssignmentsActivity.class);
                startActivity(i);
            }
        });

    }

    public RelativeLayout getAssignmentsLayout() {
        return mAssignmentsLayout;
    }

    public void fitnessIntent() {
        Intent fitnessIntent = new Intent(getActivity(), FitnessMainActivity.class);
        startActivity(fitnessIntent);
    }

    public void makeupIntent() {
        Intent i = new Intent(getActivity(), MakeUpListActivity.class);
        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_student_panel, menu);

        super.onCreateOptionsMenu(menu, inflater);
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
                        Intent i = new Intent(getActivity(), LogInActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        Helpers.showDialog(getActivity(), "Whoops", "Log out did not work, try again");
                    }
                }
            });
            return true;
        } else if (id == R.id.new_class) {
            Intent i = new Intent(getActivity(), NewClassActivity.class);
            i.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA, mCurrentStudent.getObjectId());
            startActivity(i);
        } else if (id == R.id.select_class) {
            ParseRelation<ParseObject> classRelation =
                    mCurrentStudent.getRelation(Keys.CLASSES_REL);
            SelectClassDialog dialog = new SelectClassDialog();
            dialog.setArguments(classRelation, mCurrentStudent);
            dialog.show(getActivity().getSupportFragmentManager(), null);

        }

        return super.onOptionsItemSelected(item);
    }
}
