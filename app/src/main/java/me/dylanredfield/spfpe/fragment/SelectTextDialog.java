package me.dylanredfield.spfpe.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.ui.SingleStringListAdapter;
import me.dylanredfield.spfpe.util.Keys;

public class SelectTextDialog extends DialogFragment {

    private View mView;
    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private SingleStringListAdapter mAdapter;
    private List<ParseObject> mList;


    public void setArguments(List<ParseObject> list) {
        mList = list;


        if (getActivity() != null) {
            mAdapter = new SingleStringListAdapter(getActivity(), mList);
        }
        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_new_class, null);
        builder.setView(mView);
        setRetainInstance(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        //mProgressDialog.show();

        mListView = (ListView) mView.findViewById(R.id.list);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        } else {
            if (mList != null) {
                mAdapter = new SingleStringListAdapter(getActivity(), mList);
                mListView.setAdapter(mAdapter);
            }
        }


        setListeners();

        Dialog dialog = builder.create();
        return dialog;

    }

    private void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mList.get(i).getClassName().equals(Keys.USER_KEY)) {
                    //TODO fix this string so instead of username it is the teachers actual name
                    getTargetFragment().onActivityResult(getTargetRequestCode()
                            , Keys.TEACHER_RESULT_CODE, new Intent()
                            .putExtra(Keys.OBJECT_ID_EXTRA, mList.get(i).getObjectId()));
                } else if (mList.get(i).getClassName().equals(Keys.PERIOD_KEY)) {
                    getTargetFragment().onActivityResult(getTargetRequestCode()
                            , Keys.PERIOD_RESULT_CODE
                            , new Intent()
                            .putExtra(Keys.OBJECT_ID_EXTRA, mList.get(i).getObjectId()));
                } else if (mList.get(i).getClassName().equals(Keys.SCHOOL_YEAR_KEY)) {
                    getTargetFragment().onActivityResult(getTargetRequestCode()
                            , Keys.TEACHER_RESULT_CODE
                            , new Intent()
                            .putExtra(Keys.OBJECT_ID_EXTRA, mList.get(i).getObjectId()));
                }
                dismiss();
            }
        });
    }

    public void hideProgressDialog() {
        mProgressDialog.dismiss();
    }

}
