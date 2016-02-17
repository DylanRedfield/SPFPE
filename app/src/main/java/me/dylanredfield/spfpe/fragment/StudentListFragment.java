package me.dylanredfield.spfpe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.util.Keys;

public class StudentListFragment extends Fragment {
    private View mView;
    private ListView mStudentListView;
    private ParseObject mClass;
    private List<ParseObject> mStudentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_student_list, null, false);

        defaultValues();

        return mView;
    }

    public void defaultValues() {
        mStudentListView = (ListView) mView.findViewById(R.id.list);
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

    public void queryForStudents() {
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery(Keys.STUDENT_KEY);
        studentQuery.whereEqualTo(Keys.CLASS_POINT, mClass);
        studentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                // TODO handle error
                if (e == null) {
                    mStudentList = list;
                }
            }
        });
    }
}
