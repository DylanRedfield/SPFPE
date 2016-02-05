package me.dylanredfield.spfpe.ui;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.fragment.TeacherPanelFragment;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class ClassListAdapter extends BaseAdapter {
    private List<ParseObject> mClassList;
    private TeacherPanelFragment mFragment;

    @Override
    public int getCount() {
        return mClassList.size();
    }

    @Override
    public Object getItem(int i) {
        return mClassList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mFragment.getActivity().getLayoutInflater().inflate(R.layout.row_fitness, null, false);
        }

        TextView topLine = (TextView) view.findViewById(R.id.top_line);
        topLine.setText(Helpers.getTeacherName(mClassList.get(i).getParseObject(Keys.TEACHER_POINT)));

        TextView bottomLine = (TextView) view.findViewById(R.id.bottom_line);
        bottomLine.setText("MP" + mClassList.get(i).getInt(Keys.MARKING_PERIOD_NUM) + "," +
                " Period: " + mClassList.get(i).getParseObject(Keys.PERIOD_POINT).getString(Keys.PERIOD_NAME_STR));

        return view;
    }

    public ClassListAdapter(TeacherPanelFragment fragment) {
        mFragment = fragment;
    }

    public void setList(List<ParseObject> list) {
        mClassList = list;
    }

}
