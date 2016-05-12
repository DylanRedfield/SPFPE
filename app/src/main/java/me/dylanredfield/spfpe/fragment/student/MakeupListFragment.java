package me.dylanredfield.spfpe.fragment.student;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.software.shell.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.dialog.AddMakeupDialog;
import me.dylanredfield.spfpe.dialog.ModifyMakeupDialog;
import me.dylanredfield.spfpe.util.Keys;
import me.dylanredfield.spfpe.wrapper.Makeup;

public class MakeupListFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private MakeupListAdapter mAdapter;
    private FloatingActionButton mButton;
    private Fragment mFragment;
    private Firebase ref = new Firebase(Keys.REFERENCE);
    private String mClassKey;
    private String mStudentKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedState) {
        mView = inflater.inflate(R.layout.fragment_student_makeup, null, false);

        defaultValues();
        setListeners();
        makeupQuery();

        return mView;
    }

    private void defaultValues() {
        if (mFragment == null) {
            mFragment = this;
        }
        mListView = (ListView) mView.findViewById(R.id.list);
        mAdapter = new MakeupListAdapter(getContext());
        mListView.setAdapter(mAdapter);

        mButton = (FloatingActionButton) mView.findViewById(R.id.button);

        // TODO make actual values
        mClassKey = getActivity().getIntent().getStringExtra(Keys.CLASS_KEY);
        mStudentKey = "";
    }

    private void setListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putString(Keys.CLASS_KEY, mClassKey);
                arguments.putString(Keys.STUDENT_KEY, mStudentKey);
                AddMakeupDialog dialog = AddMakeupDialog.newInstance();
                dialog.setArguments(arguments);
                dialog.setTargetFragment(mFragment, 0);
                dialog.show(getFragmentManager(), null);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                modifyMakeup(i);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                modifyMakeup(i);
                return false;
            }
        });
    }

    public void modifyMakeup(int i) {
        Bundle args = new Bundle();
        args.putString(Keys.CLASS_KEY, mClassKey);
        args.putString(Keys.STUDENT_KEY, mStudentKey);
        args.putString(Keys.MAKEUP_KEY, ((Makeup) mAdapter.getItem(i)).getKey());
        ModifyMakeupDialog dialog = ModifyMakeupDialog.newInstance();
        dialog.setArguments(args);
        dialog.setTargetFragment(mFragment, 0);
        dialog.show(getFragmentManager(), null);
    }

    public String getClassKey() {
        return mClassKey;
    }

    public String getStudentKey() {
        return mStudentKey;
    }

    public void makeupQuery() {

        Query makeupQuery = ref.child(Keys.CLASS_KEY).child(mClassKey).child(Keys.STUDENTS_KEY)
                .child(mStudentKey).child(Keys.MAKEUP_KEY);
        makeupQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Makeup makeup = childSnapshot.getValue(Makeup.class);
                    makeup.setKey(childSnapshot.getKey());
                    mAdapter.addMakeup(makeup);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /*private void studentQuery() {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.include(Keys.USER_POINT);
        studentQuery.whereEqualTo(Keys.USER_POINT, mCurrentUser);
        studentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    mCurrentStudent = parseObject;
                    mSelectedClass = (ParseObject) mCurrentStudent.get(Keys.SELECTED_CLASS_POINT);
                    queryParseForMakeups();
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", Helpers.getReadableError(e));
                }

            }
        });
    }

    public void queryParseForMakeups() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Keys.GYM_MAKEUP_KEY);
        query.whereEqualTo(Keys.CLASS_POINT, mCurrentStudent.get(Keys.SELECTED_CLASS_POINT));
        query.whereEqualTo(Keys.STUDENT_POINT, mCurrentStudent);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mList = list;
                    mAdapter.setList(mList);
                    Log.d("Makeups", list.toString());
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    } */

    public void addMakeup(Makeup makeup) {
        mAdapter.addMakeup(makeup);
    }

    static class MakeupListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Makeup> mList = new ArrayList<>();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);

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
                view = LayoutInflater.from(mContext)
                        .inflate(R.layout.row_two_line, viewGroup, false);
            }

            TextView minutesTextView = (TextView) view.findViewById(R.id.top_line);
            TextView dateTextView = (TextView) view.findViewById(R.id.bottom_line);

            minutesTextView.setText(mList.get(i).getTime() / 60 + " minutes logged");

            dateTextView.setText(mDateFormat.format(new Date(mList.get(i).getDateInMili())));

            return view;
        }

        public MakeupListAdapter(Context context) {
            mContext = context;
        }

        public void setList(List<Makeup> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public void addMakeup(Makeup makeup) {
            mList.add(makeup);
            notifyDataSetChanged();
        }
    }
}
