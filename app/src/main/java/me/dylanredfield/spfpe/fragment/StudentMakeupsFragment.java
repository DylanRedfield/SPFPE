package me.dylanredfield.spfpe.fragment;

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
import android.widget.Toast;

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
import me.dylanredfield.spfpe.dialog.ModifyFitnessDialog;
import me.dylanredfield.spfpe.dialog.ModifyMakeupDialog;
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
        mListView.setAdapter(mAdapter);

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
        ModifyMakeupDialog dialog = ModifyMakeupDialog.newInstance();
        dialog.setMakeup((ParseObject) mAdapter.getItem(i));
        dialog.show(getFragmentManager(), null);
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
    }

    public ParseObject getStudent() {
        return mCurrentStudent;
    }

    public ParseObject getSelectedClass() {
        return mSelectedClass;
    }
    public void addMakeup(ParseObject makeup) {
        mList.add(makeup);
        mAdapter.setList(mList);
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
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
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
