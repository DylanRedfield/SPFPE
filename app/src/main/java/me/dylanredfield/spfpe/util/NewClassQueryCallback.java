package me.dylanredfield.spfpe.util;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.fragment.CreateClassDialog;

public class NewClassQueryCallback implements FindCallback<ParseObject> {
    private CreateClassDialog mDialog;

    public NewClassQueryCallback(CreateClassDialog dialog) {
        mDialog = dialog;
    }

    @Override
    public void done(List<ParseObject> list, ParseException e) {
        mDialog.setArguments(list);
        Log.d("ListDialogDebug", list.toString());
    }
}
