package me.dylanredfield.spfpe.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.fragment.StudentMakeupsFragment;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class AddMakeupDialog extends DialogFragment {
    private View mView;
    private EditText mTime;
    private EditText mDateText;

    public StudentMakeupsFragment getFragment() {
        return mFragment;
    }

    private Button mEnter;
    private ParseObject mCurrentStudent;
    private ParseObject mMakeup;
    private ParseObject mSelectedClass;
    private StudentMakeupsFragment mFragment;
    private Calendar mDate;

    public EditText getTime() {
        return mTime;
    }

    public EditText getDateText() {
        return mDateText;
    }

    public Button getEnter() {
        return mEnter;
    }

    public ParseObject getCurrentStudent() {
        return mCurrentStudent;
    }

    public ParseObject getSelectedClass() {
        return mSelectedClass;
    }

    public Calendar getDate() {
        return mDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_new_makeup, null);
        builder.setView(mView);
        builder.setTitle("New Makeup");

        // setRetainInstance(true);

        defaultValues();
        setListeners();


        Dialog dialog = builder.create();
        return dialog;

    }

    private void defaultValues() {

        // TODO add DatePickerDialog
        mTime = (EditText) mView.findViewById(R.id.time);
        mDateText = (EditText) mView.findViewById(R.id.date);
        mEnter = (Button) mView.findViewById(R.id.enter);

        mFragment = (StudentMakeupsFragment) getTargetFragment();
        mSelectedClass = mFragment.getSelectedClass();
        mCurrentStudent = mFragment.getStudent();

        mMakeup = ParseObject.create(Keys.GYM_MAKEUP_KEY);
    }

    private void setListeners() {
        setOnEnterClickListener();
        mDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDate = Calendar.getInstance();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mDateText.getWindowToken(), 0);
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mDate.set(year, monthOfYear, dayOfMonth);

                                String format = "MM/dd/yy";
                                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                                mDateText.setText(sdf.format(mDate.getTime()));
                            }
                        }, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
                        mDate.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mDateText.setText("");
                    }
                });
            }
        });
    }

    public void setOnEnterClickListener() {
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    mMakeup.put(Keys.CLASS_POINT, mSelectedClass);
                    mMakeup.put(Keys.STUDENT_POINT, mCurrentStudent);
                    mMakeup.put(Keys.MINUTES_LOGGED_NUM,
                            Integer.parseInt(mTime.getText().toString().trim()));
                    mMakeup.put(Keys.DATE_DATE, mDate.getTime());
                    mMakeup.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // TODO error handle
                            if (e == null) {
                                mFragment.addMakeup(mMakeup);
                                dismiss();
                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
                                        .show();

                            }
                        }
                    });
                }
            }
        });
    }

    public boolean isValid() {
        if (mTime.getText().length() > 0) {
            if (Integer.parseInt(mTime.getText().toString()) > 0) {
                return true;
            } else {
                Helpers.showDialog(getActivity(), "Whoops", "Invalid time");
            }
        } else {
            Helpers.showDialog(getActivity(), "Whoops", "Enter something!");
        }
        return false;
    }

    public static AddMakeupDialog newInstance() {
        AddMakeupDialog dialog = new AddMakeupDialog();
        return dialog;
    }
}
