package me.dylanredfield.spfpe.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.Util.Helpers;
import me.dylanredfield.spfpe.Util.Keys;
import me.dylanredfield.spfpe.Util.NewClassQueryCallback;

public class NewClassFragment extends Fragment {
    private View mView;
    private EditText mTeacher;
    private EditText mMarkingPeriod;
    private EditText mPeriod;
    private Button mEnter;

    private List<ParseObject> mPeriodList;
    private List<ParseObject> mTeacherList;

    private ParseObject mCurrentYear;
    private ParseObject mPeriodObject;
    private ParseObject mTeacherObject;
    private ParseObject mNewClass;

    private ParseObject mCurrentStudent;
    private Fragment mFragment;
    private SelectTextDialog mTeacherDialog;
    private SelectTextDialog mPeriodDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_class, null, false);
        setDefaultValues();

        if (mFragment == null) {
            mFragment = this;
        }

        return mView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCurrentStudent = ParseObject.createWithoutData(Keys.STUDENT_KEY,
                getActivity().getIntent().getStringExtra(Keys.STUDENT_OBJECT_ID_EXTRA));
        queryParse();
        setListeners();
    }

    private void setDefaultValues() {
        mTeacher = (EditText) mView.findViewById(R.id.teacher);
        mMarkingPeriod = (EditText) mView.findViewById(R.id.marking_period);
        mPeriod = (EditText) mView.findViewById(R.id.period);

        mEnter = (Button) mView.findViewById(R.id.enter);

        mTeacherDialog = new SelectTextDialog();
        mPeriodDialog = new SelectTextDialog();


        mTeacherDialog.setTargetFragment(mFragment, Keys.TEACHER_RESULT_CODE);
        mPeriodDialog.setTargetFragment(mFragment, Keys.PERIOD_RESULT_CODE);
    }

    private void queryParse() {
        ParseQuery.getQuery(Keys.CURRENT_SCHOOL_YEAR_KEY)
                .getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            mCurrentYear = parseObject;
                        } else {
                            Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                        }

                    }
                });
        //TODO mess with clickable of edittext

        ParseQuery<ParseObject> periodQuery = ParseQuery.getQuery(Keys.PERIOD_KEY);
        periodQuery.findInBackground(new NewClassQueryCallback(mPeriodDialog));

        ParseObject teacherUserType = ParseObject.createWithoutData(Keys.USER_TYPE_KEY,
                Keys.TEACHER_OBJECT_ID);
        ParseQuery<ParseObject> teacher = ParseQuery.getQuery(Keys.USER_KEY);
        teacher.whereEqualTo(Keys.USER_TYPE_KEY, teacherUserType);
        teacher.findInBackground(new NewClassQueryCallback(mTeacherDialog));
    }

    private void setListeners() {
        mTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTeacherDialog.show(getFragmentManager(), null);
            }
        });
        mPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPeriodDialog.show(getFragmentManager(), null);
            }
        });
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPeriodObject != null && mCurrentYear != null && mTeacherObject != null
                        && mMarkingPeriod.getText().toString().trim().length() > 0) {
                    checkForExistingClass().getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                mNewClass = parseObject;
                                saveClassInStudent();
                            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                mNewClass = ParseObject.create(Keys.CLASS_KEY);
                                mNewClass.put(Keys.PERIOD_POINT, mPeriodObject);
                                mNewClass.put(Keys.SCHOOL_YEAR_POINT, mCurrentYear);
                                mNewClass.put(Keys.TEACHER_POINT, mTeacherObject);
                                mNewClass.put(Keys.MARKING_PERIOD_NUM,
                                        Integer.parseInt(mMarkingPeriod.getText()
                                                .toString().trim()));
                                mNewClass.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        saveClassInStudent();

                                    }
                                });
                            } else {
                                Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                            }
                        }
                    });
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", "Enter all Fields");
                    Log.d("EnterAllField", mPeriodObject.toString());
                }
            }
        });
    }

    public ParseQuery<ParseObject> checkForExistingClass() {
        ParseQuery<ParseObject> classQuery = ParseQuery.getQuery(Keys.CLASS_KEY);
        classQuery.whereEqualTo(Keys.PERIOD_POINT, mPeriodObject);
        classQuery.whereEqualTo(Keys.SCHOOL_YEAR_POINT, mCurrentYear);
        classQuery.whereEqualTo(Keys.TEACHER_POINT, mTeacherObject);
        classQuery.whereEqualTo(Keys.MARKING_PERIOD_NUM,
                Integer.parseInt(mMarkingPeriod.getText().toString().trim()));

        return classQuery;
    }

    public void saveClassInStudent() {
        mCurrentStudent.put(Keys.SELECTED_CLASS_POINT, mNewClass);
        mCurrentStudent.getRelation(Keys.CLASSES_REL).add(mNewClass);
        mCurrentStudent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // TODO stuff
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Keys.TEACHER_RESULT_CODE && resultCode == Keys.TEACHER_RESULT_CODE) {
            mTeacherObject = ParseObject.createWithoutData(Keys.USER_KEY,
                    data.getStringExtra(Keys.OBJECT_ID_EXTRA));

            //Fuck to be teacher first and last name
            mTeacher.setText(mTeacherObject.getString(Keys.USERNAME_STR));
        } else if (requestCode == Keys.TEACHER_RESULT_CODE && resultCode ==
                Keys.TEACHER_RESULT_CODE) {
            mPeriodObject = ParseObject.createWithoutData(Keys.USER_KEY,
                    data.getStringExtra(Keys.OBJECT_ID_EXTRA));
            mPeriod.setText(mPeriodObject.getString(Keys.PERIOD_NAME_STR));
        }
    }
}