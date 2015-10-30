package me.dylanredfield.spfpe.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.List;

import me.dylanredfield.spfpe.activity.MainActivity;
import me.dylanredfield.spfpe.ui.SingleStringListAdapter;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class SelectClassDialog extends AbstractSingleLineListDialog {
    private ParseObject mCurrentStudent;
    private Activity mActivity;


    public void setArguments(ParseRelation<ParseObject> relation, ParseObject currentStudent) {
        mCurrentStudent = currentStudent;
        ParseQuery<ParseObject> classQuery = relation.getQuery();
        classQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    setArguments(list);
                } else {
                    Helpers.showDialog(getActivity(), "Whoops", e.getMessage());
                }
            }
        });
    }

    @Override
    public void setListeners() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();
                ((MainActivity) getActivity()).onReturnValue(getList().get(i).getObjectId());
            }
        });


    }

}
