package me.dylanredfield.spfpe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

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

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(mList.get(i).getString(Keys.NAME_STR));
        Log.d("queryLog:", i + " : " + mList.get(i).getString(Keys.NAME_STR));
        return view;
    }
}
