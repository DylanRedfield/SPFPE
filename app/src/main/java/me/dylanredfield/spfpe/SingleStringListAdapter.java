package me.dylanredfield.spfpe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

public class SingleStringListAdapter extends BaseAdapter {
    private ArrayList<ParseObject> mList;
    private Context mContext;

    public SingleStringListAdapter(Context context, ArrayList<ParseObject> list) {
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

        TextView name = (TextView) view.findViewById(R.id.text);
        name.setText(mList.get(i).getString(Keys.NAME_STR));
        return view;
    }
}
