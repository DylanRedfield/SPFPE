package me.dylanredfield.spfpe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.software.shell.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.dialog.AddMakeupDialog;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class StudentMakeupsFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private List<ParseObject> mList;
    private ParseUser mCurrentUser;
    private ParseObject mCurrentStudent;
    private ParseObject mSelectedClass;
    private MakeupListAdapter mAdapter;
    private FloatingActionButton mButton;
    private Fragment mFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedState) {
        mView = inflater.inflate(R.layout.fragment_student_makeup, null, false);

        defaultValues();
        setListeners();
        studentQuery();

        return mView;
    }

    private void defaultValues() {
        if (mFragment == null) {
            mFragment = this;
        }
        mCurrentUser = ParseUser.getCurrentUser();
        mListView = (ListView) mView.findViewById(R.id.list);
        mAdapter = new MakeupListAdapter(this);

        mButton = (FloatingActionButton) mView.findViewById(R.id.button);
    }

    private void setListeners() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMakeupDialog dialog = AddMakeupDialog.newInstance();
                dialog.setTargetFragment(mFragment, 0);
                dialog.show(getFragmentManager(), null);
            }
        });
    }

    private void studentQuery() {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.include(Keys.USER_POINT);
        studentQuery.whereEqualTo(Keys.USER_POINT, mCurrentUser);
        studentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    mCurrentStudent = parseObject;
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
                mList = list;
            }
        });
    }

    public ParseObject getStudent() {
        return mCurrentStudent;
    }

    public ParseObject getSelectedClass() {
        return mSelectedClass;
    }

    static class MakeupListAdapter extends BaseAdapter {
        private StudentMakeupsFragment mFragment;
        private List<ParseObject> mList = new ArrayList<>();

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
                view = LayoutInflater.from(mFragment.getActivity())
                        .inflate(R.layout.row_two_line, null, false);
            }

            TextView minutesTextView = (TextView) view.findViewById(R.id.top_line);
            TextView dateTextView = (TextView) view.findViewById(R.id.bottom_line);

            minutesTextView.setText("" + mList.get(i).getInt(Keys.MINUTES_LOGGED_NUM) + " minutes logged");
            Date date = mList.get(i).getDate(Keys.DATE_DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy", Locale.US);
            String dateString = sdf.format(date);
            dateTextView.setText(dateString);

            return view;
        }

        public MakeupListAdapter(Fragment fragment) {
            mFragment = (StudentMakeupsFragment) fragment;
        }

        public void setList(List<ParseObject> list) {
            mList = list;
            notifyDataSetChanged();
        }
    }
}
