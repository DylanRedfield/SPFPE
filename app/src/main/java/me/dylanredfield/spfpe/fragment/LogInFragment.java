package me.dylanredfield.spfpe.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import me.dylanredfield.spfpe.activity.SignUpActivity;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.activity.MainActivity;
import me.dylanredfield.spfpe.R;

public class LogInFragment extends Fragment {
    private View mView;
    private EditText mUsername;
    private EditText mPassword;
    private Button mEnter;
    private TextView mNoAccount;
    private ParseUser mCurrentUser;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_log_in, container, false);

        setDefaultValues();
        setListeners();

        return mView;
    }

    private void setDefaultValues() {
        mUsername = (EditText) mView.findViewById(R.id.username);
        mPassword = (EditText) mView.findViewById(R.id.password);
        mEnter = (Button) mView.findViewById(R.id.enter);
        mNoAccount = (TextView) mView.findViewById(R.id.no_account);

        mUsername.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        mCurrentUser = ParseUser.getCurrentUser();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
    }

    private void setListeners() {
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValid()) {
                    logIn();
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", "Fill in the required fields");
                }
            }
        });

        mNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SignUpActivity.class);
                startActivity(i);

                getActivity().finish();
            }
        });
    }

    // Ensure has correct fields
    private boolean checkValid() {
        return mUsername.getText().toString().trim().length() > 0
                && mUsername.getText().toString().trim().length() > 0;
    }

    private void logIn() {
        mProgressDialog.show();
        ParseUser.logInInBackground(
                mUsername.getText().toString().trim()
                , mPassword.getText().toString().trim()
                , new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        mProgressDialog.dismiss();
                        if (e == null) {
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        } else {
                            Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                        }

                    }
                });
    }
}
