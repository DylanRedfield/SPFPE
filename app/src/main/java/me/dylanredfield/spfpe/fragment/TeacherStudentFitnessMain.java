package me.dylanredfield.spfpe.fragment;

import android.view.View;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class TeacherStudentFitnessMain extends FitnessMainFragment {
    @Override
    public void studentQuery() {
        ParseObject student = ParseObject.createWithoutData(Keys.STUDENT_KEY
                , getActivity().getIntent().getStringExtra(Keys.STUDENT_OBJECT_ID_EXTRA));
        student.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    setStudent(parseObject);
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                }
            }
        });
    }

    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        getActionButton().setVisibility(View.GONE);
    }
}
