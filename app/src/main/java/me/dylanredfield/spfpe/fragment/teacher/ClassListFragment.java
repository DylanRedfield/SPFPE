package me.dylanredfield.spfpe.fragment.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import me.dylanredfield.spfpe.activity.teacher.ClassActivity;
import me.dylanredfield.spfpe.ui.ClassListAdapter;
import me.dylanredfield.spfpe.util.Keys;

public class ClassListFragment extends Fragment {
    private View mView;
    private ParseObject mClass;
    private ParseUser mCurrentUser;
    private List<ParseObject> mClassList;
    private ClassListAdapter mAdapter;
    private ListView mClassListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_teacher_panel, viewGroup, false);

        defaultValues();
        defaultListeners();
        classQuery();

        return mView;
    }

    public void defaultValues() {
        mCurrentUser = ParseUser.getCurrentUser();
        mAdapter = new ClassListAdapter(this);
        mClassListView = (ListView) mView.findViewById(R.id.list);
        mClassListView.setAdapter(mAdapter);
    }

    public void defaultListeners() {
        mClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ClassActivity.class);
                intent.putExtra(Keys.CLASS_OBJECT_ID_EXTRA, mClassList.get(i).getObjectId());
                startActivity(intent);
            }
        });
    }

    public void classQuery() {
        ParseQuery<ParseObject> schoolYearQuery = ParseQuery.getQuery(Keys.CURRENT_SCHOOL_YEAR_KEY);
        schoolYearQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> classQuery = ParseQuery.getQuery(Keys.CLASS_KEY);
                    classQuery.include(Keys.PERIOD_KEY);
                    classQuery.include(Keys.SCHOOL_YEAR_KEY);
                    classQuery.whereEqualTo(Keys.TEACHER_POINT, mCurrentUser);
                    // classQuery.whereEqualTo(Keys.SCHOOL_YEAR_POINT, parseObject.getParseObject(Keys.SCHOOL_YEAR_POINT));

                    classQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                Log.v("ParseQuery", list.toString());
                                Log.v("ParseQuery", mCurrentUser.getUsername() + parseObject.getObjectId());
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
