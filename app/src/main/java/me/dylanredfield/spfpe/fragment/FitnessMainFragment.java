package me.dylanredfield.spfpe.fragment;

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

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.software.shell.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.activity.FitnessAddActivity;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;
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
    private Fragment mFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fitness_main, null, false);

        setDefaultValues();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        queryParse();

        if (mFragment == null) {
            mFragment = this;
        }
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
                Gson gson = new Gson();
                Intent i = new Intent(getActivity(), FitnessAddActivity.class);
                // Don't want to have to query for current student every time
                i.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA, mCurrentStudent.getObjectId());
                startActivityForResult(i, Keys.FITNESS_TEST_RESULT_CODE);
            }
        });
    }

    public List<ParseObject> getList() {
        return mEventList;
    }

    private void queryParse() {
        studentQuery();

    }

    // Sets the current student
    private void studentQuery() {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.include(Keys.USER_POINT);
        studentQuery.whereEqualTo(Keys.USER_POINT, mCurrentUser);
        mProgressDialog.show();
        studentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    mCurrentStudent = parseObject;
                    eventQuery();
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                    mProgressDialog.dismiss();
                }

            }
        });
    }

    public void addToList(ParseObject object) {
        mEventList.add(object);
        mAdapter.setList();
    }

    // Sets the FitnessTests relevant to the current student
    private void eventQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Keys.FITNESS_TEST_KEY);
        query.whereEqualTo(Keys.STUDENT_POINT, mCurrentStudent);
        query.whereEqualTo(Keys.CLASS_POINT,
                mCurrentStudent.get(Keys.SELECTED_CLASS_POINT));
        query.include(Keys.EVENT_KEY);
        query.include(Keys.CLASS_KEY);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    mEventList = list;
                    mAdapter = new FitnessTestAdapter(mFragment);
                    mListView.setAdapter(mAdapter);
                    Log.d("EventQuery", mEventList.toString());
                } else if (e.getMessage().equals("no results found for query")) {
                    mEventList = list;
                    mAdapter = new FitnessTestAdapter(mFragment);
                    mListView.setAdapter(mAdapter);
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mAdapter != null && requestCode == Keys.FITNESS_TEST_RESULT_CODE
                && resultCode == Keys.FITNESS_TEST_RESULT_CODE) {
            mEventList.add(ParseObject.createWithoutData(Keys.FITNESS_TEST_KEY,
                    data.getStringExtra(Keys.OBJECT_ID_EXTRA)));
            mAdapter.notifyDataSetChanged();
            Log.d("AdapterNotify", "Success");
        } else {
            Log.d("AdapterNotify", "Failure");
        }
    }


    public static class FitnessTestAdapter extends BaseAdapter {

        private List<ParseObject> mList;
        private FitnessMainFragment mFragment;

        public FitnessTestAdapter(Fragment fragment) {
            mFragment = (FitnessMainFragment) fragment;
            mList = mFragment.getList();
        }

        public void setList() {
            mList = mFragment.getList();
            notifyDataSetChanged();
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
                view = mFragment.getActivity().getLayoutInflater()
                        .inflate(R.layout.row_fitness_test, null, false);
            }


            TextView topLine = (TextView) view.findViewById(R.id.top_line);
            TextView bottomLine = (TextView) view.findViewById(R.id.bottom_line);

            String topLineString = "";
            topLineString += ((ParseObject) mList.get(i).get(Keys.EVENT_KEY))
                    .getString(Keys.NAME_STR) + ": Attempt "
                    + mList.get(i).getNumber(Keys.TEST_NUMBER_NUM);
            topLine.setText(topLineString);

            int length = mList.get(i).get(Keys.RESULTS_ARR).toString().length();
            bottomLine.setText(mList.get(i).get(Keys.RESULTS_ARR)
                    .toString().substring(1, length - 1));

            return view;
        }
    }
}
