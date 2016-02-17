package me.dylanredfield.spfpe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.ui.ClassListAdapter;
import me.dylanredfield.spfpe.util.Keys;

public class TeacherPanelFragment extends Fragment {
    private View mView;
    private ParseObject mClass;
    private ParseUser mCurrentUser;
    private List<ParseObject> mClassList;
    private ClassListAdapter mAdapter;
    private ListView mClassListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_teacher_panel, null, false);

        defaultValues();
        defaultListeners();
        classQuery();

        return mView;
    }

    public void defaultValues() {
        mCurrentUser = ParseUser.getCurrentUser();
        mAdapter = new ClassListAdapter(this);
        mClassListView = (ListView) mView.findViewById(R.id.list);
    }

    public void defaultListeners() {
        mClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void classQuery() {
        ParseQuery<ParseObject> schoolYearQuery = ParseQuery.getQuery(Keys.CURRENT_SCHOOL_YEAR_KEY);
        schoolYearQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> classQuery = ParseQuery.getQuery(Keys.CLASS_KEY);
                    classQuery.include(Keys.PERIOD_KEY);
                    classQuery.include(Keys.SCHOOL_YEAR_KEY);
                    classQuery.whereEqualTo(Keys.TEACHER_KEY, mCurrentUser);
                    classQuery.whereEqualTo(Keys.SCHOOL_YEAR_KEY, parseObject);

                    classQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                mClassList = list;
                                mAdapter.setList(mClassList);
                            } else {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }


}
