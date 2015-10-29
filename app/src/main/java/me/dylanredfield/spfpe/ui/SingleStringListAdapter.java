package me.dylanredfield.spfpe.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Keys;

public class SingleStringListAdapter extends BaseAdapter {
    private List<ParseObject> mList;
    private Context mContext;

    public SingleStringListAdapter(Context context, List<ParseObject> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.row_fitness, null, false);
        }

        // TODO fix this for classes and other types
        TextView name = (TextView) view.findViewById(R.id.name);
        if (mList.get(i).getClassName().equals(Keys.USER_KEY)) {
            name.setText(mList.get(i).getString(Keys.USERNAME_STR));
        } else if (mList.get(i).getClassName().equals(Keys.PERIOD_KEY)) {
            name.setText(mList.get(i).getString(Keys.PERIOD_NAME_STR));
        } else if (mList.get(i).getClassName().equals(Keys.SCHOOL_YEAR_KEY)) {
            name.setText(mList.get(i).getString(Keys.YEAR_STR));
        } else {
            name.setText(mList.get(i).getString(Keys.NAME_STR));
        }
        Log.d("queryLog:", i + " : " + mList.get(i).getString(Keys.NAME_STR));
        return view;
    }
}