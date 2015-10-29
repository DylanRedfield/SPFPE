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

    public SelectClassDialog() {
    }

    @Override
    public void setListeners() {

    }

    @Override
    public Dialog onCreateDialog(Bundle s) {
        mDialog = super.onCreateDialog(s);

        mListView = getListView();
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
                Log.d("relationQuery",
                        ((TextView)mAdapter.getView(0, null, null).findViewById(R.id.name)).getText().toString() + "shit");
                if (mListView != null) {
                    Log.d("relationQuery", "Success");
                    mListView.setAdapter(mAdapter);
                }
            }
        });
    }
}
