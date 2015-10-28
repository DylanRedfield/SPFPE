package me.dylanredfield.spfpe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import me.dylanredfield.spfpe.R;

public class AssignmentsFragment extends Fragment {
    private View mView;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        mView = inflater.inflate(R.layout.fragment_assignments, null, false);
        setDefaultValues();

        return mView;
    }

    public void setDefaultValues() {
        mListView = (ListView) mView.findViewById(R.id.list);
        mListView.setEmptyView(mView.findViewById(R.id.empty_list));
    }
}
