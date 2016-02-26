package me.dylanredfield.spfpe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Keys;

public class StudentListFragment extends Fragment {
    private View mView;
    private ListView mStudentListView;
    private StudentAdapter mStudentAdapter;
    private ParseObject mClass;
    private List<ParseObject> mStudentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_student_list, null, false);

        defaultValues();
        defaultListeners();

        return mView;
    }

    public void defaultValues() {
        mStudentList = new ArrayList<>();
        mStudentListView = (ListView) mView.findViewById(R.id.list);

        mStudentAdapter = new StudentAdapter(this);
        mStudentListView.setAdapter(mStudentAdapter);

        mClass = ParseObject.createWithoutData(Keys.CLASS_KEY,
                getActivity().getIntent().getStringExtra(Keys.CLASS_OBJECT_ID_EXTRA));

        mClass.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                // TODO test if need to fetch students

                mClass = parseObject;
                queryForStudents();
            }
        });
    }

    public void defaultListeners() {
        mStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void queryForStudents() {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.whereEqualTo(Keys.CLASS_POINT, mClass);
        studentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                // TODO handle error
                if (e == null) {
                    mStudentList = list;
                    mStudentAdapter.setList(mStudentList);
                }
            }
        });
    }

    public List<ParseObject> getStudents() {
        return mStudentList;
    }

    public static class StudentAdapter extends BaseAdapter {
        private StudentListFragment mFragment;
        private List<ParseObject> mList;

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
                view = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.row_select_class, viewGroup);
            }

            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(mList.get(i).getString(Keys.NAME_STR));

            return view;
        }

        public StudentAdapter(StudentListFragment fragment) {
            mFragment = fragment;
            mList = mFragment.getStudents();
        }

        public void setList(List<ParseObject> list) {
            mList = list;
            notifyDataSetChanged();
        }

    }
}
