package me.dylanredfield.spfpe.dialog;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class ModifyFitnessDialog extends NewFitnessDialog {
    private ParseObject mFitnessTest;

    @Override
    public void setListeners() {
        getEnterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> dataList = new ArrayList<>();
                if (getTopLine().getText().toString().trim().equals("")) {
                    dataList.add("0 " + getFieldNames().get(0));
                } else {
                    dataList.add(getTopLine().getText().toString().trim() + " " + getFieldNames().get(0));
                }

                // If there is ever more than 2 fields this will break...
                if (getEvent().getNumber(Keys.NUM_FIELDS_NUM) == 2) {
                    if (getBottomLine().getText().toString().trim().equals("")) {
                        dataList.add("0 " + getFieldNames().get(1));
                    } else {
                        dataList.add(getBottomLine().getText().toString().trim() + " "
                                + getFieldNames().get(1));
                    }
                }

                mFitnessTest.put(Keys.RESULTS_ARR, dataList);
                mFitnessTest.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            dismiss();
                        } else {
                            Helpers.showDialog(getActivity(), "Whoops", Helpers.getReadableError(e));
                        }
                    }
                });

            }
        });
    }

    public void setArguments(ParseObject event, ParseObject student, Fragment fragment, ParseObject test) {
        setArguments(event, student, fragment);
        mFitnessTest = test;
        Log.v("SetArguments", "super");

    }
}
