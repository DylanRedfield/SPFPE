package me.dylanredfield.spfpe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    private View mView;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mGrade;
    private Button mEnter;
    private TextView mAlreadyHave;
    private ParseUser mCurrentUser;

    public SignUpFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedState) {
        mView = inflater.inflate(R.layout.fragment_sign_up, null, false);

        setDefaultValues();
        return mView;
    }

    private void setDefaultValues() {
        mFirstName = (EditText) mView.findViewById(R.id.first_name);
        mLastName = (EditText) mView.findViewById(R.id.last_name);
        mPassword = (EditText) mView.findViewById(R.id.password);
        mConfirmPassword = (EditText) mView.findViewById(R.id.confirm_password);
        mGrade = (EditText) mView.findViewById(R.id.grade);

        mEnter = (Button) mView.findViewById(R.id.enter);

        mAlreadyHave = (TextView) mView.findViewById(R.id.already_have_account);

        setListeners();
    }

    private void setListeners() {
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputs()) {
                    signUp();
                }
            }
        });
    }

    private boolean checkInputs() {
        if (isOneWord(mFirstName.getText().toString().trim())) {
            if (isOneWord(mLastName.getText().toString().trim())) {
                if (mPassword.getText().toString().trim()
                        .equals(mConfirmPassword.getText().toString().trim())) {
                    return true;
                } else {
                    Helpers.createDialog(getActivity(), "Whoops", "Passwords Do Not Match");
                }
            } else {
                Helpers.createDialog(getActivity(), "Whoops", "Last name can only be one word");
            }
        } else {
            Helpers.createDialog(getActivity(), "Whoops", "First name can only be one word");
        }
        return false;
    }

    private boolean isOneWord(String s) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(s);
        return !matcher.find();
    }

    private void signUp() {
        String username = "";
        username += mFirstName.getText().toString().trim() + "."
                + mLastName.getText().toString().trim() + ".spfhs";
        mCurrentUser = new ParseUser();
        mCurrentUser.put(Keys.FIRST_NAME_STR, mFirstName.getText().toString().trim());
        mCurrentUser.put(Keys.LAST_NAME_STR, mLastName.getText().toString().trim());
        mCurrentUser.put(Keys.USERNAME_STR, username);
        mCurrentUser.put(Keys.PASSWORD_STR, mPassword.getText().toString());
        mCurrentUser.put(Keys.USER_TYPE_POINT, ParseObject.createWithoutData(Keys.USER_TYPE_KEY,
                Keys.STUDENT_OBJECT_ID));


        final ParseObject student = new ParseObject(Keys.STUDENT_KEY);
        //ParseRelation<ParseObject> classes = student.getRelation(Keys.CLASSES_REL);
        student.put(Keys.USER_POINT, mCurrentUser);
        student.put(Keys.GRADE_NUM, Integer.parseInt(mGrade.getText().toString().trim()));
        mCurrentUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("signup", "Student success");
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                } else {
                    Helpers.createDialog(getActivity(), "Whoops", e.getMessage());
                }

            }
        });
    }
}
