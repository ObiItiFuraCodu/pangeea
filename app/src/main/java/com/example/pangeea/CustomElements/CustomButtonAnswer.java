package com.example.pangeea.CustomElements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;

public class CustomButtonAnswer extends LinearLayout {
    public CustomButtonAnswer(Context context) {
        super(context);
        init();
    }

    public CustomButtonAnswer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonAnswer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        setOrientation(LinearLayout.VERTICAL);

        setBackgroundColor(getResources().getColor(R.color.gray));

        Button button = new Button(getContext());
        button.setText("Your answer:");



        TextView textView3 = new TextView(getContext());
        textView3.setText("Actual answer:");


        addView(button);
        addView(textView3);

    }
}
