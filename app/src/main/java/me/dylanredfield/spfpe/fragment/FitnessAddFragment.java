package me.dylanredfield.spfpe.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;
import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.ui.SingleStringListAdapter;

public class FitnessAddFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private List<ParseObject> mEventList;
    private SingleStringListAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private ParseObject mCurrentStudent;
    private Fragment mMainFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fitness, null, false);

        setDefaultValues();
        queryParse();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        // Object Id from current student so don't have to query again
        mCurrentStudent = ParseObject.createWithoutData(Keys.STUDENT_KEY,
                getActivity().getIntent().getStringExtra(Keys.STUDENT_OBJECT_ID_EXTRA));
    }

    private void setDefaultValues() {
        mListView = (ListView) mView.findViewById(R.id.list);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);


        setListeners();
    }

    public void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewFitnessDialog dialog = new NewFitnessDialog();
                dialog.setArguments(mEventList.get(i), mCurrentStudent, mMainFragment);
                dialog.show(getFragmentManager(), null);
            }
        });
    }

    private void queryParse() {

        // An event is 20 yard dash, mile, ect
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Keys.EVENT_KEY);
        mProgressDialog.show();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    // Show the list because data is all here
                    mEventList = list;
                    mAdapter = new SingleStringListAdapter(getActivity(), mEventList);
                    mListView.setAdapter(mAdapter);
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static class NewFitnessDialog extends DialogFragment {

        private View mView;
        private ParseObject mEvent;
        private ProgressDialog mProgressDialog;
        private ParseObject mCurrentStudent;
        private FitnessMainFragment mFragment;


        public void setArguments(ParseObject event, ParseObject student, Fragment fragment) {
            mEvent = event;
            mCurrentStudent = student;
            mFragment = (FitnessMainFragment) fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            mView = inflater.inflate(R.layout.dialog_new_fitness_event, null);
            builder.setView(mView);

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setCancelable(false);

            builder.setTitle(mEvent.getString(Keys.NAME_STR));

            final List<String> fieldNames = (List<String>) mEvent.get(Keys.FIELD_NAMES_ARR);

            final EditText editText1 = (EditText) mView.findViewById(R.id.edit_text_1);
            final EditText editText2 = (EditText) mView.findViewById(R.id.edit_text_2);

            // fieldNames contains units
            // Will always be at least 1 field, so set the first
            editText1.setHint(fieldNames.get(0));

            // If there is ever more than 2 fields this will break...
            if (mEvent.getNumber(Keys.NUM_FIELDS_NUM) == 1) {
                editText2.setVisibility(View.GONE);
            } else {
                editText2.setHint(fieldNames.get(1));
            }


            Button enter = (Button) mView.findViewById(R.id.enter);
            enter.setText("Enter");

            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final ParseObject fitnessTest = new ParseObject(Keys.FITNESS_TEST_KEY);
                    fitnessTest.put(Keys.CLASS_POINT, mCurrentStudent
                            .get(Keys.SELECTED_CLASS_POINT));
                    fitnessTest.put(Keys.EVENT_POINT, mEvent);
                    fitnessTest.put(Keys.STUDENT_POINT, mCurrentStudent);

                    // Data base holds an ArrayList of Strings
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(editText1.getText().toString().trim() + " " + fieldNames.get(0));

                    // If there is ever more than 2 fields this will break...
                    if (mEvent.getNumber(Keys.NUM_FIELDS_NUM) == 2) {
                        dataList.add(editText2.getText().toString().trim() + " "
                                + fieldNames.get(1));
                    }

                    fitnessTest.put(Keys.RESULTS_ARR, dataList);


                    // Checks to see if it is the first test of the marking period
                    final ParseQuery<ParseObject> attemptQuery =
                            ParseQuery.getQuery(Keys.FITNESS_TEST_KEY);
                    attemptQuery.whereEqualTo(Keys.STUDENT_POINT, mCurrentStudent);
                    attemptQuery.whereEqualTo(Keys.EVENT_POINT, mEvent);
                    attemptQuery.whereEqualTo(Keys.CLASS_POINT,
                            mCurrentStudent.get(Keys.SELECTED_CLASS_POINT));
                    Log.d("FitnessAddQuery", mCurrentStudent.getObjectId());
                    mProgressDialog.show();
                    attemptQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                // TODO test this please
                                // Since object is found, there is already one for that marking
                                // period, now need to make sure dont create more than 2

                                if (parseObject.getInt(Keys.TEST_NUMBER_NUM) <= 2) {
                                    mProgressDialog.dismiss();
                                    Helpers.showDialog(getActivity(), "Whoops",
                                            "Already 2 attempts for the marking period");
                                    getActivity().finish();
                                } else {
                                    fitnessTest.put(Keys.TEST_NUMBER_NUM, 2);
                                    fitnessTest.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            mProgressDialog.dismiss();
                                            if (e == null) {
                                                sendResultObject(fitnessTest);
                                                getActivity().finish();
                                            } else {
                                                Helpers.showDialog(getActivity(), "Whoops",
                                                        e.getMessage());
                                            }
                                        }
                                    });
                                }
                            } else if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                Log.d("FitnessAddQuery", "ObjectNotFound");
                                fitnessTest.put(Keys.TEST_NUMBER_NUM, 1);
                                fitnessTest.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        mProgressDialog.dismiss();
                                        if (e == null) {
                                            sendResultObject(fitnessTest);
                                            getActivity().finish();
                                        } else {
                                            Helpers.showDialog(getActivity(), "Whoops",
                                                    e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                            }
                        }
                    });
                }
            });

            Dialog dialog = builder.create();
            return dialog;

        }

        private void sendResultObject(ParseObject fitnessTest) {
            getActivity().setResult(Keys.FITNESS_TEST_RESULT_CODE, new Intent()
                    .putExtra(Keys.OBJECT_ID_EXTRA, fitnessTest.getObjectId()));

        }

    }
}
