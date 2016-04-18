package me.dylanredfield.spfpe.activity.teacher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.fragment.teacher.MakeUpListFragment;

public class MakeUpListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_make_up_list);

        addFragment(new MakeUpListFragment());
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.frame, fragment)
                .commit();
    }
}
