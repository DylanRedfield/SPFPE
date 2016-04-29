package me.dylanredfield.spfpe.fragment.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.activity.teacher.AnalyzeClassActivity;
import me.dylanredfield.spfpe.activity.teacher.IndividualStudentPanelActivity;
import me.dylanredfield.spfpe.util.Helpers;
import me.dylanredfield.spfpe.util.Keys;

public class ClassFragment extends Fragment {
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
        setHasOptionsMenu(true);
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
                Intent intent = new Intent(getActivity(), IndividualStudentPanelActivity.class);
                intent.putExtra(Keys.STUDENT_OBJECT_ID_EXTRA, mStudentList.get(i).getObjectId());
                intent.putExtra(Keys.CLASS_OBJECT_ID_EXTRA, mClass.getObjectId());
                startActivity(intent);
            }
        });
    }

    public void queryForStudents() {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.whereEqualTo(Keys.CLASSES_REL, mClass);
        studentQuery.include(Keys.USER_POINT);
        studentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                // TODO handle error
                if (e == null) {
                    Log.v("ParseQuery", list.toString());
                    mStudentList = list;
                    mStudentAdapter.setList(mStudentList);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_class, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.analyze:
                Intent i = new Intent(getActivity(), AnalyzeClassActivity.class);
                i.putExtra(Keys.CLASS_OBJECT_ID_EXTRA, mClass.getObjectId());
                startActivity(i);
                return true;
        }
        return false;
    }
        public List<ParseObject> getStudents () {
            return mStudentList;
        }

        public static class StudentAdapter extends BaseAdapter {
            private ClassFragment mFragment;
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
                    view = LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.row_select_class, viewGroup, false);
                }

                TextView name = (TextView) view.findViewById(R.id.name);
                name.setText(Helpers.getTeacherName(mList.get(i).getParseUser(Keys.USER_POINT)));

                return view;
            }

            public StudentAdapter(ClassFragment fragment) {
                mFragment = fragment;
                mList = mFragment.getStudents();
            }

            public void setList(List<ParseObject> list) {
                mList = list;
                notifyDataSetChanged();
            }

        }
    }
