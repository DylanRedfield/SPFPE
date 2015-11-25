package me.dylanredfield.spfpe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.dylanredfield.spfpe.R;
import me.dylanredfield.spfpe.ui.LineNumberEditText;

/**
 * Created by dylan on 11/20/15.
 */
public class TestEditTextActivity extends AppCompatActivity {
    private TextView mLines;
    private LineNumberEditText mEditText;
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_test_edit_text);

        mLines = (TextView) findViewById(R.id.lines);
        mEditText = (LineNumberEditText) findViewById(R.id.edit_text);
        mEditText.setTextView(mLines);

    }
}
