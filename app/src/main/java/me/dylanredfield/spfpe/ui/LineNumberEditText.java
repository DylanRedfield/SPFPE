package me.dylanredfield.spfpe.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;
import android.widget.TextView;

public class LineNumberEditText extends EditText implements TextWatcher , View.OnKeyListener {
    private int mPreviousLineNum = 1;
    private int mTabCount = 0;
    private TextView mLines;
    private String mPreviousText;

    public LineNumberEditText(Context context) {
        super(context);
        Log.d("Constructor", "word");
        setMaxLines(Integer.MAX_VALUE);
        setOnKeyListener(this);
    }

    public LineNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("Constructor", "word");
        setOnKeyListener(this);
    }

    public LineNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("Constructor", "word");
        setOnKeyListener(this);
    }

    String prev = "";
    public boolean onKey(View v, int i, KeyEvent keyEvent) {
        Log.d("Tab", "Key Up");
        if (i == KeyEvent.KEYCODE_DEL && prev.length() > 0) {
            int pos = getSelectionStart();
            char c = prev.charAt(pos);

            Log.d("Tab", "" + c);
            if (c == '{') {
                mTabCount--;
                Log.d("Tab", "Remove tab: " + c);
            } else if (c == '}') {
                mTabCount++;
                Log.d("Tab", "Add tab" + c);
            }
        }
        prev = getText().toString();
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        Log.d("Tab2", "Before: " + charSequence.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPreviousText = getText().toString();
        int lines = getLineCount();
        if (s.length() > 0) {
            if (s.charAt(s.length() - 1) == '{') {
                mTabCount++;
                Log.d("Tab", "Add tab");
            } else if (s.charAt(s.length() - 1) == '}') {
                mTabCount--;
                Log.d("Tab", "Removend he drank  tab");
            } else {
                Log.d("Tab", s.toString());
            }
        }

        if (mPreviousLineNum < lines) {
            mPreviousLineNum = lines;
            mLines.append("\n" + lines);

            for (int i = 0; i < mTabCount; i++) {
                append("\t");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("Tab", "After: " + s.toString());
    }

    public void setTextView(TextView tv) {
        mLines = tv;
        mLines.setText("1");
    }

    public void onTextDelete() {
        Log.d("Tab", "K");

    }
    /*
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new CodeEditTextInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class CodeEditTextInputConnection extends InputConnectionWrapper {

        public CodeEditTextInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                LineNumberEditText.this.onTextDelete();
                // Un-comment if you wish to cancel the backspace:
                // return false;
            }
            return super.sendKeyEvent(event);
        }

    }
    */
}
