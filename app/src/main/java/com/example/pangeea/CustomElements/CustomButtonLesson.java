package com.example.pangeea.CustomElements;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.pangeea.R;

public class CustomButtonLesson extends CardView {

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
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        addView(rootLayout);

        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable2.setColor(getResources().getColor(R.color.binaryblue));

        float cornerRadiusInPixels2 = 20f;
        gradientDrawable2.setCornerRadius(cornerRadiusInPixels2);

        setBackground(gradientDrawable2);

        Button button = new Button(getContext());
        button.setText("Title/Question");
        button.setBackgroundColor(getResources().getColor(R.color.white));

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(getResources().getColor(R.color.white));

        float cornerRadiusInPixels = 20f;
        gradientDrawable.setCornerRadius(cornerRadiusInPixels);

        button.setBackground(gradientDrawable);
        button.setElevation(10f);

        TextView textView1 = new TextView(getContext());
        textView1.setText("Lesson/Answer");
       // textView1.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"font/poppins_black.ttf"));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 16);

        rootLayout.addView(button, layoutParams);
        rootLayout.addView(textView1, layoutParams);
    }
}

