package me.dylanredfield.spfpe.fragment.teacher;

import android.content.Context;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;
import me.dylanredfield.spfpe.wrapper.FitnessAnalysis;

public class AnalyzeClassFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private ParseObject mEvent;
    private List<FitnessAnalysis> mAnalysisList = new ArrayList<>();
    private ParseObject mClass;
    private AnalysisListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_analyize_class, viewGroup, false);

        defaultViews();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new AnalysisListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);
        mEvent = ParseObject.createWithoutData(Keys.EVENT_KEY, "wE9lVquTtI");
        mClass = ParseObject.createWithoutData(Keys.CLASS_KEY,
                getActivity().getIntent().getStringExtra(Keys.CLASS_OBJECT_ID_EXTRA));
        mEvent.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                mEvent = parseObject;
                mClass.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        mClass = parseObject;

                        queryForStudents();
                    }
                });
            }
        });
    }

    public void queryForStudents() {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.whereEqualTo(Keys.CLASSES_REL, mClass);
        studentQuery.include(Keys.USER_POINT);
        studentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject student : list) {
                    mAnalysisList.add(new FitnessAnalysis(student));
                }
                queryForTests();
            }
        });
    }

    public void queryForTests() {
        ParseQuery<ParseObject> testQuery = ParseQuery.getQuery(Keys.FITNESS_TEST_KEY);
        testQuery.whereEqualTo(Keys.EVENT_POINT, mEvent);
        testQuery.whereEqualTo(Keys.CLASS_POINT, mClass);
        testQuery.include(Keys.STUDENT_POINT);
        testQuery.include(Keys.EVENT_POINT);

        Log.d("queries", mEvent.getObjectId());
        Log.d("queries", mClass.getObjectId());
        testQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                Log.d("queries", list.toString());
                Log.d("queries", "anal " + mAnalysisList.toString());
                for (ParseObject fitnessTest : list) {
                    for (FitnessAnalysis anal : mAnalysisList) {

                        if (anal.getStudent().getObjectId().equals(fitnessTest.getParseObject(Keys.STUDENT_POINT)
                                .getObjectId())) {
                            if (fitnessTest.getInt(Keys.TEST_NUMBER_NUM) == 1) {
                                anal.setTestOne(fitnessTest);
                            } else if (fitnessTest.getInt(Keys.TEST_NUMBER_NUM) == 2) {
                                anal.setTestTwo(fitnessTest);
                            }
                        }
                    }
                }
                makeAnalysisList();

            }
        });
    }

    public void makeAnalysisList() {
        for (FitnessAnalysis anal : mAnalysisList) {
            if (anal.getTestOne() != null && anal.getTestTwo() != null) {
                if (anal.getTestOne().getList(Keys.RESULTS_ARR) != null
                        && anal.getTestTwo().getList(Keys.RESULTS_ARR) != null) {


                    // FitnessTest
                    String testOneMinutes = (String) anal.getTestOne().getList(Keys.RESULTS_ARR).get(0);
                    testOneMinutes = testOneMinutes.trim();

                    for (int i = 0; i < testOneMinutes.length(); i++) {
                        if (!Character.isDigit(testOneMinutes.charAt(i))) {
                            testOneMinutes = testOneMinutes.substring(0, i);
                        }
                    }
                    String testOneSeconds = "0";
                    if (anal.getTestOne().getList(Keys.RESULTS_ARR).size() > 1) {
                        testOneSeconds = (String) anal.getTestOne().getList(Keys.RESULTS_ARR).get(1);
                        testOneSeconds = testOneSeconds.trim();
                    }

                    for (int i = 0; i < testOneSeconds.length(); i++) {
                        if (!Character.isDigit(testOneSeconds.charAt(i))) {
                            testOneSeconds = testOneSeconds.substring(0, i);
                        }
                    }

                    int testOneTime = Integer.parseInt(testOneMinutes) * 60 + Integer.parseInt(testOneSeconds);


                    String testTwoMinutes = (String) anal.getTestTwo().getList(Keys.RESULTS_ARR).get(0);
                    testTwoMinutes = testTwoMinutes.trim();

                    for (int i = 0; i < testTwoMinutes.length(); i++) {
                        if (!Character.isDigit(testTwoMinutes.charAt(i))) {
                            testTwoMinutes = testTwoMinutes.substring(0, i);
                        }
                    }
                    String testTwoSeconds = (String) anal.getTestTwo().getList(Keys.RESULTS_ARR).get(1);
                    testTwoSeconds = testTwoSeconds.trim();

                    for (int i = 0; i < testTwoSeconds.length(); i++) {
                        if (!Character.isDigit(testTwoSeconds.charAt(i))) {
                            testTwoSeconds = testTwoSeconds.substring(0, i);
                        }
                    }

                    int testTwoTime = Integer.parseInt(testTwoMinutes) * 60 + Integer.parseInt(testTwoSeconds);

                    int difference = testOneTime - testTwoTime;

                    int differenceMinutes = difference / 60;
                    int differenceSeconds = difference % 60;

                    String diffString = "";

                    if (differenceSeconds < 0 && differenceMinutes == 0) {
                        differenceSeconds *= -1;
                        diffString = "-" + differenceMinutes + ":" + differenceSeconds;
                    } else if (differenceSeconds < 0 && differenceMinutes > 0) {

                        differenceSeconds *= -1;
                        differenceMinutes *= -1;
                        diffString = "" + differenceMinutes + ":" + differenceSeconds;
                    } else {
                        diffString = "" + differenceMinutes + ":" + differenceSeconds;
                    }

                    anal.setDifference(diffString);

                    Log.v("Difference", diffString);
                }
            }
        }
        mListAdapter.setList(mAnalysisList);
    }

    public void defaultViews() {
        mListView = (ListView) mView.findViewById(R.id.list);
    }

    static class AnalysisListAdapter extends BaseAdapter {
        private Context mContext;
        private List<FitnessAnalysis> mList = new ArrayList<>();

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
                view = LayoutInflater.from(mContext).inflate(R.layout.row_analysis, viewGroup, false);
            }

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView difference = (TextView) view.findViewById(R.id.difference);

            FitnessAnalysis analysis = (FitnessAnalysis) getItem(i);

            name.setText(Helpers.getTeacherName(analysis.getStudent().getParseUser(Keys.USER_POINT)));
            difference.setText(analysis.getDifference());

            if (analysis.getDifference() != null && analysis.getDifference().length() > 0) {
                difference.setVisibility(View.VISIBLE);
                if (analysis.getDifference().charAt(0) == '-') {
                    difference.setTextColor(Color.RED);
                } else {
                    difference.setTextColor(Color.BLUE);
                }
            } else {
                difference.setVisibility(View.GONE);
            }

            return view;
        }

        public AnalysisListAdapter(Context context) {
            mContext = context;
        }

        public void setList(List<FitnessAnalysis> list) {
            mList = list;
            notifyDataSetChanged();
        }
    }
}
