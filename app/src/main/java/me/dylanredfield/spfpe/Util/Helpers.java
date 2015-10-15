package me.dylanredfield.spfpe.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import bolts.Task;

public class Helpers {
    public static AlertDialog createDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;

    }
    public static Task<ParseObject> getStudentFromUser(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Keys.STUDENT_KEY);
        query.whereEqualTo(Keys.USER_POINT, user);
        return query.getFirstInBackground();
    }

}
