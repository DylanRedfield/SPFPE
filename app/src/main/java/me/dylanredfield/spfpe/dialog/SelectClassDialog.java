package me.dylanredfield.spfpe.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

import me.dylanredfield.spfpe.activity.MainActivity;
import me.dylanredfield.spfpe.util.Helpers;

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
                    Helpers.showDialog(getActivity(), "Whoops", Helpers.getReadableError(e));
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
