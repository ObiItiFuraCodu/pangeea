package com.example.pangeea.CustomElements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.pangeea.R;

public class CustomPupilButton extends CardView {
    public CustomPupilButton(Context context) {
        super(context);
        init();
    }

    public CustomPupilButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPupilButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable2.setColor(getResources().getColor(R.color.binaryblue));

        float cornerRadiusInPixels2 = 20f;
        gradientDrawable2.setCornerRadius(cornerRadiusInPixels2);

        setBackground(gradientDrawable2);


        Button button = new Button(getContext());
        button.setText("Name");
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(getResources().getColor(R.color.white));

        float cornerRadiusInPixels = 20f;
        gradientDrawable.setCornerRadius(cornerRadiusInPixels);
        button.setBackground(gradientDrawable);
       // button.setElevation(10f);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.setMargins(16, 16, 16, 0);
        ImageView view = new ImageView(getContext());
        view.setImageResource(R.drawable.ic_launcher_foreground);
        addView(button,buttonLayoutParams);
        addView(view);


    }
}
