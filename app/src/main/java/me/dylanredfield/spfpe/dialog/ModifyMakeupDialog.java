package me.dylanredfield.spfpe.dialog;

import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;


import java.util.HashMap;

import me.dylanredfield.spfpe.util.Keys;
import me.dylanredfield.spfpe.wrapper.Makeup;

public class ModifyMakeupDialog extends AddMakeupDialog {
    private Makeup mMakeup;
    private Firebase mRef = new Firebase(Keys.REFERENCE);
    private Firebase mMakeupRef;

    public static ModifyMakeupDialog newInstance() {
        ModifyMakeupDialog dialog = new ModifyMakeupDialog();
        return dialog;
    }

    @Override
    public void defaultValues() {
        super.defaultValues();
        getDateText().setVisibility(View.GONE);
        getBuilder().setTitle("Modify Makeup");

        String classKey = getArguments().getString(Keys.CLASS_KEY);
        String studentKey = getArguments().getString(Keys.STUDENT_KEY);
        String makeupKey = getArguments().getString(Keys.MAKEUP_KEY);
        mMakeupRef = mRef.child(Keys.CLASS_KEY).child(classKey).child(studentKey).child(Keys.MAKEUP_KEY)
                .child(makeupKey);
    }

    @Override
    public void setOnEnterClickListener() {
        getEnter().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> updates = new HashMap<>();
                updates.put(Keys.TIME_NUM, Integer.parseInt(getTime().getText().toString().trim()));

                getDateText().setVisibility(View.GONE);

                mRef.updateChildren(updates);
            }
        });
    }

    public void setMakeup(Makeup makeup) {
        mMakeup = makeup;
    }
}
