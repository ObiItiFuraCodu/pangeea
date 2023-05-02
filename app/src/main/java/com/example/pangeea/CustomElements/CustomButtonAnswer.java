package com.example.pangeea.CustomElements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;

public class CustomButtonAnswer extends LinearLayout {
    public CustomButtonAnswer(Context context) {
        super(context);/*comentariu lung */
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

        TextView textView2 = new TextView(getContext());
        textView2.setText("Question was:");

        TextView textView25 = new TextView(getContext());
        textView25.setText("Answer was");

        TextView textView3 = new TextView(getContext());
        textView3.setText("Why?:");


        addView(button);
        addView(textView2);
        addView(textView25);
        addView(textView3);

    }
}
