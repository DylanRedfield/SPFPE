package me.dylanredfield.spfpe.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.Activity.FitnessAddActivity;
import me.dylanredfield.spfpe.Util.Helpers;
import me.dylanredfield.spfpe.Util.Keys;
import me.dylanredfield.spfpe.R;

public class FitnessMainFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private List<ParseObject> mEventList;
    private FitnessTestAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private FloatingActionButton mActionButton;
    private ParseUser mCurrentUser;
    private ParseObject mCurrentStudent;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fitness_main, null, false);

        setDefaultValues();
        queryParse();

        return mView;
    }

    private void setDefaultValues() {
        mListView = (ListView) mView.findViewById(R.id.list);
        mListView.setEmptyView(mView.findViewById(R.id.empty_list));
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);

        mActionButton = (FloatingActionButton) mView.findViewById(R.id.button);

        mCurrentUser = ParseUser.getCurrentUser();

        setListeners();
    }

    public void setListeners() {
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FitnessAddActivity.class);
                startActivity(i);
            }
        });
    }

    private void queryParse() {
        studentQuery();

    }

    private void studentQuery() {
        ParseQuery<ParseUser> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.include(Keys.USER_POINT);
        studentQuery.whereEqualTo(Keys.USER_POINT, mCurrentUser);
        mProgressDialog.show();
        studentQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseObject, ParseException e) {
                if (e == null) {
                    mCurrentStudent = parseObject;
                    eventQuery();
                } else {
                    Helpers.createDialog(getActivity(), "Whoops", e.getMessage());
                    mProgressDialog.dismiss();
                    Log.d("noResults", mCurrentUser.getUsername());
                }

            }
        });
    }

    private void eventQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Keys.FITNESS_TEST_KEY);
        query.whereEqualTo(Keys.STUDENT_KEY, mCurrentStudent);
        query.whereEqualTo(Keys.SELECTED_CLASS_POINT,
                mCurrentStudent.get(Keys.SELECTED_CLASS_POINT));
        query.include(Keys.EVENT_KEY);
        query.include(Keys.CLASS_KEY);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    mEventList = list;
                    mAdapter = new FitnessTestAdapter(getActivity(), mEventList);
                    mListView.setAdapter(mAdapter);
                } else if (e.getMessage().equals("no results found for query")) {
                    mEventList = list;
                    mAdapter = new FitnessTestAdapter(getActivity(), mEventList);
                    mListView.setAdapter(mAdapter);
                } else {
                    Helpers.createDialog(getActivity(), "Whoops", e.getMessage());
                }
            }
        });
    }


    public static class FitnessTestAdapter extends BaseAdapter {

        private List<ParseObject> mList;
        private Activity mContext;

        public FitnessTestAdapter(Activity context, List<ParseObject> list) {
            mList = list;
            mContext = context;
        }

        public void setList(ArrayList<ParseObject> list) {
            mList = list;
        }

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
                view = mContext.getLayoutInflater()
                        .inflate(R.layout.row_fitness_test, null, false);
            }


            TextView topLine = (TextView) view.findViewById(R.id.top_line);
            TextView bottomLine = (TextView) view.findViewById(R.id.bottom_line);

            String topLineString = "";
            topLineString += ((ParseObject) mList.get(i).get(Keys.EVENT_KEY))
                    .getString(Keys.NAME_STR) + ": Attempt "
                    + mList.get(i).getNumber(Keys.TEST_NUMBER_NUM);
            topLine.setText(topLineString);

            bottomLine.setText(mList.get(i).get(Keys.RESULTS_ARR).toString());
            return view;
        }
    }
}
