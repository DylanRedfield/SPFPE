package me.dylanredfield.spfpe.fragment.teacher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Keys;

public class MakeUpListFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private List<ParseObject> mMakeUpList = new ArrayList<>();
    private ParseObject mStudent;
    private ParseObject mClass;
    private MakeUpListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_makeup_list, viewGroup, false);

        defaultValues();
        queryForMakeUps();
        return mView;
    }

    public void defaultValues() {
        mStudent = ParseObject.createWithoutData(Keys.STUDENT_KEY,
                getActivity().getIntent().getStringExtra(Keys.STUDENT_OBJECT_ID_EXTRA));

        mClass = ParseObject.createWithoutData(Keys.CLASS_KEY,
                getActivity().getIntent().getStringExtra(Keys.CLASS_OBJECT_ID_EXTRA));

        mAdapter = new MakeUpListAdapter(this);
        mListView = (ListView) mView.findViewById(R.id.list);
        mListView.setEmptyView(mView.findViewById(R.id.empty_view));
        mListView.setAdapter(mAdapter);

    }

    public void queryForMakeUps() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Keys.GYM_MAKEUP_KEY);
        query.whereEqualTo(Keys.STUDENT_POINT, mStudent);
        query.whereEqualTo(Keys.CLASS_POINT, mClass);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mMakeUpList = list;
                    mAdapter.setList(list);
                }
            }
        });
    }

    public List<ParseObject> getMakeUpList() {
        return this.mMakeUpList;
    }

    static class MakeUpListAdapter extends BaseAdapter {
        private MakeUpListFragment mFragment;
        private List<ParseObject> mMakeUpList;

        @Override
        public int getCount() {
            return mMakeUpList.size();
        }

        @Override
        public Object getItem(int i) {
            return mMakeUpList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return mMakeUpList.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = mFragment.getActivity().getLayoutInflater().inflate(R.layout.row_two_line, viewGroup, false);
            }

            TextView minutesTextView = (TextView) view.findViewById(R.id.top_line);
            TextView bottomLine = (TextView) view.findViewById(R.id.bottom_line);

            minutesTextView.setText("" + mMakeUpList.get(i).getInt(Keys.MINUTES_LOGGED_NUM) + " minutes logged");
            Date date = mMakeUpList.get(i).getDate(Keys.DATE_DATE);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            String dateString = sdf.format(date);
            bottomLine.setText(dateString);

            return view;
        }

        public MakeUpListAdapter(MakeUpListFragment fragment) {
            mFragment = fragment;
            mMakeUpList = mFragment.getMakeUpList();
        }
        public void setList(List<ParseObject> list) {
            mMakeUpList = list;
        }
    }
}
