package com.example.pangeea.CustomElements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;

public class CustomButtonView extends LinearLayout {

    public CustomButtonView(Context context) {
        super(context);
        init();
    }

    public CustomButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        setOrientation(LinearLayout.VERTICAL);

        setBackgroundColor(getResources().getColor(R.color.gray));

        Button button = new Button(getContext());
        button.setText("Button");

        TextView textView1 = new TextView(getContext());
        textView1.setText("TextView 1");

        TextView textView2 = new TextView(getContext());
        textView2.setText("TextView 2");

        TextView textView3 = new TextView(getContext());
        textView3.setText("TextView 3");


        addView(button);
        addView(textView1);
        addView(textView2);
        addView(textView3);

    }
}

