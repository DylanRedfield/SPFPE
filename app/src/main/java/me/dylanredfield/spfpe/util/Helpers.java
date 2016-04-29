package me.dylanredfield.spfpe.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class Helpers {
    public static AlertDialog showDialog(Context context, String title, String message) {
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

    public static ParseQuery<ParseObject> getStudentQuery(ParseUser user) {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.include(Keys.USER_POINT);
        studentQuery.whereEqualTo(Keys.USER_POINT, user);
        return studentQuery;

    }

    public static String getTeacherName(ParseObject teacher) {
        String username = teacher.getString(Keys.USERNAME_STR);
        String name = "";
        name = username.substring(0, 1).toUpperCase() + username.substring(1, username.indexOf("."));
        username = username.substring(username.indexOf(".") + 1, username.length());
        Log.d("getTeacherName", username);
        name += " " + username.substring(0, 1).toUpperCase() + username.substring(1, username.indexOf("."));

        return name;
    }

    public static String getReadableError(ParseException e) {
        if (e.getCode() == 100) {
            return "Connection Failed, try again";
        } else {

            return e.getMessage();
        }
    }
}
