package me.dylanredfield.spfpe.dialog;

import android.view.View;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


import me.dylanredfield.spfpe.util.Keys;

public class ModifyMakeupDialog extends AddMakeupDialog {
    private ParseObject mMakeup;

    public static ModifyMakeupDialog newInstance() {
        ModifyMakeupDialog dialog = new ModifyMakeupDialog();
        return dialog;
    }

    @Override
    public void setOnEnterClickListener() {
        mMakeup.put(Keys.MINUTES_LOGGED_NUM,
                Integer.parseInt(getTime().getText().toString().trim()));
        getDateText().setVisibility(View.GONE);

        mMakeup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // TODO update list
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
                            .show();

                }
            }
        });
    }

    public void setMakeup(ParseObject makeup) {
        mMakeup = makeup;
    }
}
