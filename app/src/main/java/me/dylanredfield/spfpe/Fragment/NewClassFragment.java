package me.dylanredfield.spfpe.Fragment;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_class, null, false);
        setDefaultValues();

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
        periodQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mPeriodList = list;
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                }

            }
        });

        ParseObject teacherUserType = ParseObject.createWithoutData(Keys.USER_TYPE_KEY,
                Keys.TEACHER_OBJECT_ID);
        ParseQuery<ParseObject> teacher = ParseQuery.getQuery(Keys.USER_KEY);
        teacher.whereEqualTo(Keys.USER_TYPE_KEY, teacherUserType);
        teacher.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mTeacherList = list;
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                }

            }
        });
    }

    private void setListeners() {
        mTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTextDialog teacherDialog = new SelectTextDialog();
                teacherDialog.setArguments(mTeacherList, mTeacherObject, mTeacher);
                Log.d("ListTest", mTeacherList.toString());
                teacherDialog.show(getFragmentManager(), "Test");
            }
        });
        mPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTextDialog periodDialog = new SelectTextDialog();
                periodDialog.setArguments(mPeriodList, mPeriodObject, mPeriod);
                periodDialog.show(getFragmentManager(), null);
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
}
