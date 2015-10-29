package me.dylanredfield.spfpe.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.ui.SingleStringListAdapter;
import me.dylanredfield.spfpe.util.Keys;

public class CreateClassDialog extends AbstractSingleLineListDialog {

    public void setListeners() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (getList().get(i).getClassName().equals(Keys.USER_KEY)) {
                    //TODO fix this string so instead of username it is the teachers actual name
                    getTargetFragment().onActivityResult(getTargetRequestCode()
                            , Keys.TEACHER_RESULT_CODE, new Intent()
                            .putExtra(Keys.OBJECT_ID_EXTRA, getList().get(i).getObjectId()));
                } else if (getList().get(i).getClassName().equals(Keys.PERIOD_KEY)) {
                    getTargetFragment().onActivityResult(getTargetRequestCode()
                            , Keys.PERIOD_RESULT_CODE
                            , new Intent()
                            .putExtra(Keys.OBJECT_ID_EXTRA, getList().get(i).getObjectId()));
                } else if (getList().get(i).getClassName().equals(Keys.SCHOOL_YEAR_KEY)) {
                    getTargetFragment().onActivityResult(getTargetRequestCode()
                            , Keys.TEACHER_RESULT_CODE
                            , new Intent()
                            .putExtra(Keys.OBJECT_ID_EXTRA, getList().get(i).getObjectId()));
                }
                dismiss();
            }
        });
    }
}
