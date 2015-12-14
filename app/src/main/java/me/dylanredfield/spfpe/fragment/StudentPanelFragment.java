package me.dylanredfield.spfpe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import me.dylanredfield.spfpe.activity.AssignmentsActivity;
import me.dylanredfield.spfpe.activity.FitnessMainActivity;
import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.activity.StudentMakeupsActivity;
import me.dylanredfield.spfpe.activity.TestEditTextActivity;

public class StudentPanelFragment extends Fragment {
    private View mView;
    private RelativeLayout mFitnessLayout;
    private RelativeLayout mMakeupLayout;
    private RelativeLayout mAssignmentsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedState) {
        mView = inflater.inflate(R.layout.fragment_student_panel, null, false);

        setDefaultValues();

        return mView;
    }

    private void setDefaultValues() {
        mFitnessLayout = (RelativeLayout) mView.findViewById(R.id.fitness);
        mMakeupLayout = (RelativeLayout) mView.findViewById(R.id.makeups);
        mAssignmentsLayout = (RelativeLayout) mView.findViewById(R.id.assignments);

        setListeners();
    }

    private void setListeners() {
        mFitnessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fitnessIntent = new Intent(getActivity(), FitnessMainActivity.class);
                startActivity(fitnessIntent);
            }
        });
        mMakeupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), StudentMakeupsActivity.class);
                startActivity(i);

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
}
