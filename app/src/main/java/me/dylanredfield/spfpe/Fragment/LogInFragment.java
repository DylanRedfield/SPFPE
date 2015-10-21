package me.dylanredfield.spfpe.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import me.dylanredfield.spfpe.Activity.SignUpActivity;
import me.dylanredfield.spfpe.Util.Helpers;
import me.dylanredfield.spfpe.Activity.MainActivity;
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

        mCurrentUser = ParseUser.getCurrentUser();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
    }

    private void setListeners() {
        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        mNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SignUpActivity.class);
                startActivity(i);
                // TODO use arrays to not finish this activity until account is created
                getActivity().finish();
            }
        });
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
