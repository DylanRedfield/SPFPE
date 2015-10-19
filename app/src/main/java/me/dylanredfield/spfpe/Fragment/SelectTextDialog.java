package me.dylanredfield.spfpe.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.UI.SingleStringListAdapter;
import me.dylanredfield.spfpe.Util.Keys;

public class SelectTextDialog extends DialogFragment {
    @SuppressWarnings("unchecked")
    public static class NewFitnessDialog extends DialogFragment {

        private View mView;
        private ProgressDialog mProgressDialog;
        private ListView mListView;
        private SingleStringListAdapter mAdapter;
        private List<ParseObject> mList;
        private ParseObject mClass;

        public NewFitnessDialog() {

        }

        public void setArguments(List<ParseObject> list, ParseObject object) {
            mList = list;
            mClass = object;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            mView = inflater.inflate(R.layout.dialog_new_fitness_event, null);
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setCancelable(false);

            mListView = (ListView) mView.findViewById(R.id.list);
            mAdapter  = new SingleStringListAdapter(getActivity(), mList);
            setListeners();

            Dialog dialog = builder.create();
            return dialog;

        }

        private void setListeners() {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mList.get(i).getClassName().equals(Keys.USER_KEY)) {
                        mClass.put(Keys.TEACHER_POINT, mList.get(i));
                    } else if (mList.get(i).getClassName().equals(Keys.PERIOD_KEY)) {
                        mClass.put(Keys.PERIOD_POINT, mList.get(i));
                    } else if (mList.get(i).getClassName().equals(Keys.SCHOOL_YEAR_KEY)) {
                        mClass.put(Keys.SCHOOL_YEAR_POINT, mList.get(i));
                    }
                }
            });
        }

    }
}
