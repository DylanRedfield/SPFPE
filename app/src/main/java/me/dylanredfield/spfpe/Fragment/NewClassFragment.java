package me.dylanredfield.spfpe.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import me.dylanredfield.spfpe.R;

public class NewClassFragment extends Fragment {
    private View mView;
    private EditText mTeacher;
    private EditText mMarkingPeriod;
    private EditText mPeriod;
    private Button mEnter;

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
    }

    private void queryParse() {

    }
}
