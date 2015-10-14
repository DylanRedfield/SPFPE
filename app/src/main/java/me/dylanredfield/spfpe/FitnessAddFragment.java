package me.dylanredfield.spfpe;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.List;

public class FitnessAddFragment extends Fragment {
    private View mView;
    private ListView mListView;
    private List<ParseObject> mEventList;
    private SingleStringListAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fitness, null, false);

        setDefaultValues();
        queryParse();

        return mView;
    }

    private void setDefaultValues() {
        mListView = (ListView) mView.findViewById(R.id.list);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading...");
    }

    public void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void queryParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Keys.EVENT_KEY);
        mProgressDialog.show();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    mEventList = list;
                    mAdapter = new SingleStringListAdapter(getActivity(), mEventList);
                    mListView.setAdapter(mAdapter);
                } else {
                    Helpers.createDialog(getActivity(), "Whoops", e.getMessage());
                }
            }
        });
    }

    public static class NewFitnessDialog extends DialogFragment {
        private View mView;
        private ParseObject mEvent;
        public NewFitnessDialog() {

        }

        public void setArguments(ParseObject event) {
            mEvent = event;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            mView = inflater.inflate(R.layout.dialog_new_fitness_event, null);

            EditText editText = (EditText) mView.findViewById(R.id.edit_text);
            Button enter = (Button) mView.findViewById(R.id.enter);

            editText.setHint("");

            builder.setTitle("New Fitness Test");


            Dialog dialog = builder.create();
            return dialog;

        }
    }
}
