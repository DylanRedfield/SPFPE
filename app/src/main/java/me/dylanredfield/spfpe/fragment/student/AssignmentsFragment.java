package me.dylanredfield.spfpe.fragment.student;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.parse.ParseObject;
import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Keys;
import me.dylanredfield.spfpe.wrapper.Assignment;

public class AssignmentsFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private AssignmentAdapter mAdapter;
    private Firebase mRef = new Firebase(Keys.REFERENCE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        mView = inflater.inflate(R.layout.fragment_assignments, null, false);
        setDefaultValues();
        setListeners();
        assignmentQuery();
        //queryParse();

        return mView;
    }

    public void setDefaultValues() {
        mAdapter = new AssignmentAdapter(getContext());
        mListView = (ListView) mView.findViewById(R.id.list);
        mListView.setEmptyView(mView.findViewById(R.id.empty_list));
        mListView.setAdapter(mAdapter);
    }


    public void assignmentQuery() {
        String classKey = getActivity().getIntent().getStringExtra(Keys.CLASS_KEY);
        Query assignmentQuery = mRef.child(Keys.CLASS_KEY).child(classKey).child(Keys.ASSIGNMENT_KEY);

        assignmentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Assignment assignment = childSnapshot.getValue(Assignment.class);
                    assignment.setKey(childSnapshot.getKey());

                    mAdapter.addAssignment(assignment);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /*
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
    } */

    public void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("Click", "Click!");
                String url = ((Assignment) mAdapter.getItem(i)).getUrl();

                if (!(url.startsWith("https://") || url.startsWith("http://"))) {
                    url = "http://" + url;
                }
                Log.d("openUrlIntent", url);
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW);
                openUrlIntent.setData(Uri.parse(url));
                startActivity(openUrlIntent);
            }
        });

    }

    /*
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
    } */


    static class AssignmentAdapter extends BaseAdapter {
        private Context mContext;
        private List<Assignment> mList;

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
            return getItem(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.row_two_line, viewGroup, false);
            }

            TextView line1 = (TextView) view.findViewById(R.id.top_line);
            TextView line2 = (TextView) view.findViewById(R.id.bottom_line);
            line2.setVisibility(View.VISIBLE);

            line1.setText(mList.get(i).getName());
            line2.setText(mList.get(i).getUrl());

            return view;
        }

        public AssignmentAdapter(Context context) {
            mContext = context;
        }

        public void addAssignment(Assignment assignment) {
            mList.add(assignment);
            notifyDataSetChanged();
        }
    }
}
