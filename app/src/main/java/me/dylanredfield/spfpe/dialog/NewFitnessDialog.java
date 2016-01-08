package me.dylanredfield.spfpe.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.fragment.FitnessMainFragment;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class NewFitnessDialog extends DialogFragment {

    private View mView;
    private ParseObject mEvent;
    private ProgressDialog mProgressDialog;
    private ParseObject mCurrentStudent;
    private FitnessMainFragment mFragment;
    private EditText mTopLine;
    private EditText mBottomLine;
    private Button mEnter;
    private AlertDialog.Builder mBuilder;
    private List<String> mFieldNames;


    public void setArguments(ParseObject event, ParseObject student, Fragment fragment) {
        mEvent = event;
        mCurrentStudent = student;
        mFragment = (FitnessMainFragment) fragment;

        Log.v("SetArguments", "sub");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_new_fitness_event, null);
        mBuilder.setView(mView);

        mBuilder.setTitle(mEvent.getString(Keys.NAME_STR));

        Log.v("onCreate", "sub");
        mFieldNames = mEvent.getList(Keys.FIELD_NAMES_ARR);
        defaultValues();
        setListeners();






        Dialog dialog = mBuilder.create();
        return dialog;

    }

    public AlertDialog.Builder getBuilder() {
        return mBuilder;
    }

    public void setListeners() {
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ParseObject fitnessTest = new ParseObject(Keys.FITNESS_TEST_KEY);
                fitnessTest.put(Keys.CLASS_POINT, mCurrentStudent
                        .get(Keys.SELECTED_CLASS_POINT));
                fitnessTest.put(Keys.EVENT_POINT, mEvent);
                fitnessTest.put(Keys.STUDENT_POINT, mCurrentStudent);

                // Data base holds an ArrayList of Strings
                ArrayList<String> dataList = new ArrayList<>();
                if (mTopLine.getText().toString().trim().equals("")) {
                    dataList.add("0 " + mFieldNames.get(0));
                } else {
                    dataList.add(mTopLine.getText().toString().trim() + " " + mFieldNames.get(0));
                }

                // If there is ever more than 2 fields this will break...
                if (mEvent.getNumber(Keys.NUM_FIELDS_NUM) == 2) {
                    if (mBottomLine.getText().toString().trim().equals("")) {
                        dataList.add("0 " + mFieldNames.get(1));
                    } else {
                        dataList.add(mBottomLine.getText().toString().trim() + " "
                                + mFieldNames.get(1));
                    }
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
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        // TODO this for erraything
                        attemptQuery.cancel();
                    }
                });
                attemptQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            // Since object is found, there is already one for that marking
                            // period, now need to make sure dont create more than 2

                            if (parseObject.getInt(Keys.TEST_NUMBER_NUM) >= 2) {
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
                                                    Helpers.getReadableError(e));
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
                                                Helpers.getReadableError(e));
                                    }
                                }
                            });
                        } else {
                            Helpers.showDialog(getActivity(), "Whoops", Helpers.getReadableError(e));
                        }
                    }
                });
            }
        });
    }

    public void defaultValues() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);


        mTopLine = (EditText) mView.findViewById(R.id.edit_text_1);
        mBottomLine = (EditText) mView.findViewById(R.id.edit_text_2);
        mEnter = (Button) mView.findViewById(R.id.enter);
        mEnter.setText("Enter");
        // fieldNames contains units
        // Will always be at least 1 field, so set the first
        mTopLine.setHint(mFieldNames.get(0));

        // If there is ever more than 2 fields this will break...
        if (mEvent.getNumber(Keys.NUM_FIELDS_NUM) == 1) {
            mBottomLine.setVisibility(View.GONE);
        } else {
            mBottomLine.setHint(mFieldNames.get(1));
        }


    }

    public Button getEnterButton() {
        return mEnter;
    }

    public EditText getTopLine() {
        return mTopLine;
    }

    public EditText getBottomLine() {
        return mBottomLine;
    }

    public List<String> getFieldNames() {
        return mFieldNames;
    }

    public ParseObject getEvent() {
        return mEvent;
    }

    private void sendResultObject(ParseObject fitnessTest) {
        getActivity().setResult(Keys.FITNESS_TEST_RESULT_CODE, new Intent()
                .putExtra(Keys.OBJECT_ID_EXTRA, fitnessTest.getObjectId()));

    }

}
