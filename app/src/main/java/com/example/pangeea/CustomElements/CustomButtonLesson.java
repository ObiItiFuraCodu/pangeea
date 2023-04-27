package com.example.pangeea.CustomElements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;

public class CustomButtonLesson extends LinearLayout {

    public CustomButtonLesson(Context context) {
        super(context);
        init();
    }

    public CustomButtonLesson(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonLesson(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        setOrientation(LinearLayout.VERTICAL);

        setBackgroundColor(getResources().getColor(R.color.gray));

        Button button = new Button(getContext());
        button.setText("Title/Question");

        TextView textView1 = new TextView(getContext());
        textView1.setText("Lesson/Answer");




        addView(button);
        addView(textView1);


    }
}

