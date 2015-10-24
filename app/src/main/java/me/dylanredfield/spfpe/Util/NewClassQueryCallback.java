package me.dylanredfield.spfpe.Util;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.Fragment.SelectTextDialog;

public class NewClassQueryCallback implements FindCallback<ParseObject> {
    private SelectTextDialog mDialog;

    public NewClassQueryCallback(SelectTextDialog dialog) {
        mDialog = dialog;
    }

    @Override
    public void done(List<ParseObject> list, ParseException e) {
        mDialog.setArguments(list);
        Log.d("ListDialogDebug", list.toString());
    }
}
