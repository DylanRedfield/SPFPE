package me.dylanredfield.spfpe.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.ui.SingleStringListAdapter;

public abstract class AbstractSingleLineListDialog extends DialogFragment {
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

    public abstract void setListeners();

    public Adapter getAdapter() {
        return mAdapter;
    }

    public ListView getListView() {
        return mListView;
    }
    public List<ParseObject> getList() {
        return mList;
    }

    public void hideProgressDialog() {
        mProgressDialog.dismiss();
    }

}
