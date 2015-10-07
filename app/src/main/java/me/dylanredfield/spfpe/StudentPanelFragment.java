package me.dylanredfield.spfpe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class StudentPanelFragment extends Fragment {
    private View mView;
    private RelativeLayout mFitnessLayout;
    private RelativeLayout mMakeupLayout;
    private RelativeLayout mAssignmentsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedState) {
        mView = inflater.inflate(R.layout.fragment_student_panel, null, false);

        mFitnessLayout = (RelativeLayout) mView.findViewById(R.id.fitness);
        mMakeupLayout = (RelativeLayout) mView.findViewById(R.id.makeups);
        mAssignmentsLayout = (RelativeLayout) mView.findViewById(R.id.assignments);
        return mView;
    }
}
