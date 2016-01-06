package me.dylanredfield.spfpe.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class AssignmentsFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private ParseObject mSelectClass;
    private ParseObject mCurrentStudent;
    private List<ParseObject> mAssignmentList;
    private AssignmentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        mView = inflater.inflate(R.layout.fragment_assignments, null, false);
        setDefaultValues();
        setListeners();
        queryParse();

        return mView;
    }

    public void setDefaultValues() {
        mAssignmentList = new ArrayList<>();
        mAdapter = new AssignmentAdapter(this);
        mListView = (ListView) mView.findViewById(R.id.list);
        mListView.setEmptyView(mView.findViewById(R.id.empty_list));
        mListView.setAdapter(mAdapter);
    }

    public void queryParse() {
        ParseQuery<ParseObject> studentQuery = Helpers.getStudentQuery(ParseUser.getCurrentUser());
        studentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    mSelectClass = parseObject.getParseObject(Keys.SELECTED_CLASS_POINT);
                    mCurrentStudent = parseObject;
                    queryForAssignments();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("Click", "Click!");
                String url = mAssignmentList.get(i).getString(Keys.LINK_STR);
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(url));
                startActivity(openUrlIntent);
            }
        });

    }

    private void queryForAssignments() {
        ParseQuery<ParseObject> assignmentQuery = ParseQuery.getQuery(Keys.ASSIGNMENT_KEY);
        assignmentQuery.whereEqualTo(Keys.CLASS_POINT, mSelectClass);
        assignmentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mAssignmentList = list;
                    mAdapter.refreshList();
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", Helpers.getReadableError(e));
                }
            }
        });
    }

    public List<ParseObject> getAssignmentList() {
        return mAssignmentList;
    }

    static class AssignmentAdapter extends BaseAdapter {
        private AssignmentsFragment mFragment;
        private List<ParseObject> mList;

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = mFragment.getActivity().getLayoutInflater().inflate(R.layout.row_fitness,
                        null, false);
            }

            TextView line1 = (TextView) view.findViewById(R.id.name);
            TextView line2 = (TextView) view.findViewById(R.id.info);
            line2.setVisibility(View.VISIBLE);

            line1.setText(mList.get(i).getString(Keys.ASSIGNMENT_NAME_STR));
            line2.setText(mList.get(i).getString(Keys.LINK_STR));

            return view;
        }

        public AssignmentAdapter(AssignmentsFragment fragment) {
            mFragment = fragment;
            mList = mFragment.getAssignmentList();
        }

        public void refreshList() {
            mList = mFragment.getAssignmentList();
            notifyDataSetChanged();
        }
    }
}
