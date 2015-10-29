package me.dylanredfield.spfpe.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.ui.SingleStringListAdapter;
import me.dylanredfield.spfpe.util.NewClassQueryCallback;

public class SelectClassDialog extends SelectTextDialog {
    private SingleStringListAdapter mAdapter;
    private ListView mListView;
    private Dialog mDialog;

    @Override
    public void setListeners() {

    }

    @Override
    public Dialog onCreateDialog(Bundle s) {
        mDialog = super.onCreateDialog(s);
        mListView = getListView();

        // If the adapter was not set before because onCreateDialog had not yet been called
        if (mListView.getAdapter() != null) {
            mListView.setAdapter(mAdapter);
        }
        return mDialog;

    }

    public void setArguments(ParseRelation<ParseObject> relation) {
        ParseQuery<ParseObject> classQuery = relation.getQuery();
        classQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                mAdapter = new SingleStringListAdapter(getActivity(), list);

                // onCreateDialog may not have been called yet
                if (mListView != null) {
                    mListView.setAdapter(mAdapter);
                }
            }
        });
    }
}
