package me.dylanredfield.spfpe.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.Util.Helpers;
import me.dylanredfield.spfpe.Util.Keys;

public class NewClassFragment extends Fragment {
    private View mView;
    private EditText mTeacher;
    private EditText mMarkingPeriod;
    private EditText mPeriod;
    private Button mEnter;

    private List<ParseObject> mPeriodList;
    private List<ParseObject> mTeacherList;
    private ParseObject mCurrentYear;

    private ParseObject mNewClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_class, null, false);
        setDefaultValues();

        return mView;
    }

    private void setDefaultValues() {
        mTeacher = (EditText) mView.findViewById(R.id.teacher);
        mMarkingPeriod = (EditText) mView.findViewById(R.id.marking_period);
        mPeriod = (EditText) mView.findViewById(R.id.period);

        mEnter = (Button) mView.findViewById(R.id.enter);

        mNewClass = ParseObject.create(Keys.CLASS_KEY);

        queryParse();
    }

    private void queryParse() {

        ParseQuery<ParseObject> periodQuery = ParseQuery.getQuery(Keys.PERIOD_KEY);
        periodQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mPeriodList = list;
                    mPeriod.setClickable(true);
                } else {
                    Helpers.createDialog(getActivity(), "Whoops", e.getMessage());
                }

            }
        });

        ParseQuery<ParseObject> teacher = ParseQuery.getQuery(Keys.TEACHER_KEY);
        periodQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mTeacherList = list;
                    mTeacher.setClickable(true);
                } else {
                    Helpers.createDialog(getActivity(), "Whoops", e.getMessage());
                }

            }
        });
    }

    private void setListeners() {
        mTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTextDialog teacherDialog = new SelectTextDialog();
                teacherDialog.setArguments(mTeacherList, mNewClass, mTeacher);
            }
        });
        mPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTextDialog periodDialog = new SelectTextDialog();
                periodDialog.setArguments(mPeriodList, mNewClass, mPeriod);
            }
        });
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
