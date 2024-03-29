package me.dylanredfield.spfpe.fragment.teacher;

import android.content.Intent;
import android.view.View;

import me.dylanredfield.spfpe.activity.teacher.IndividualStudentFitnessMainActivity;
import me.dylanredfield.spfpe.activity.teacher.MakeUpListActivity;
import me.dylanredfield.spfpe.fragment.student.PanelFragment;
import me.dylanredfield.spfpe.util.Keys;

public class IndividualStudentPanelFragment extends PanelFragment {

    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        getAssignmentsLayout().setVisibility(View.GONE);
    }

    @Override
    public void fitnessIntent() {
        Intent i = new Intent(getActivity(), IndividualStudentFitnessMainActivity.class);
        i.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA,
                getActivity().getIntent().getStringExtra(Keys.STUDENT_OBJECT_ID_EXTRA));
        i.putExtra(Keys.CLASS_OBJECT_ID_EXTRA, getActivity().getIntent().getStringExtra(Keys.CLASS_OBJECT_ID_EXTRA));
        startActivity(i);
    }

    @Override
    public void makeupIntent() {
        Intent i = new Intent(getActivity(), MakeUpListActivity.class);
        i.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA,
                getActivity().getIntent().getStringExtra(Keys.STUDENT_OBJECT_ID_EXTRA));
        i.putExtra(Keys.CLASS_OBJECT_ID_EXTRA, getActivity().getIntent().getStringExtra(Keys.CLASS_OBJECT_ID_EXTRA));
        startActivity(i);
    }

    @Override
    public void queryForStudent() {
        // intentionally blank
    }
}
