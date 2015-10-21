package me.dylanredfield.spfpe.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.UI.SingleStringListAdapter;
import me.dylanredfield.spfpe.Util.Keys;

public class SelectTextDialog extends DialogFragment {

    private View mView;
    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private SingleStringListAdapter mAdapter;
    private List<ParseObject> mList;
    private ParseObject mClass;
    private EditText mEditText;


    public void setArguments(List<ParseObject> list, ParseObject object, EditText editText) {
        mList = list;
        mClass = object;
        mEditText = editText;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_new_class, null);
        builder.setView(mView);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);

        mListView = (ListView) mView.findViewById(R.id.list);
        mAdapter = new SingleStringListAdapter(getActivity(), mList);
        Log.d("ListTest", mList.toString());
        mListView.setAdapter(mAdapter);
        setListeners();

        Dialog dialog = builder.create();
        return dialog;

    }

    private void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mList.get(i).getClassName().equals(Keys.USER_KEY)) {
                    mClass = mList.get(i);
                    //TODO fix this string
                    mEditText.setText(mList.get(i).getString(Keys.USERNAME_STR));
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Keys.);
                } else if (mList.get(i).getClassName().equals(Keys.PERIOD_KEY)) {
                    mClass = mList.get(i);
                    mEditText.setText(mList.get(i).getString(Keys.PERIOD_NAME_STR));
                } else if (mList.get(i).getClassName().equals(Keys.SCHOOL_YEAR_KEY)) {
                    mClass = mList.get(i);
                    mEditText.setText(mList.get(i).getString(Keys.YEAR_STR));
                }
                dismiss();
            }
        });
    }

}
