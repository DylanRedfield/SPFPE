package me.dylanredfield.spfpe.fragment;

import android.view.View;

public class TeacherStudentFragment extends StudentPanelFragment {

    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        getAssignmentsLayout().setVisibility(View.GONE);
    }

    @Override
    public void fitnessIntent() {

    }

    @Override
    public void makeupIntent() {

    }
}
