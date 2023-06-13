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

public class CustomCardElement extends CardView {
    public CustomCardElement(Context context) {
        super(context);
        init();
    }

    public CustomCardElement(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCardElement(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(rootLayout);

        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);

        float cornerRadiusInPixels2 = 20f;
        gradientDrawable2.setCornerRadius(cornerRadiusInPixels2);
        gradientDrawable2.setColor(getResources().getColor(R.color.white));

        setBackground(gradientDrawable2);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(getResources().getColor(R.color.white));

        float cornerRadiusInPixels = 20f;
        gradientDrawable.setCornerRadius(cornerRadiusInPixels);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textViewLayoutParams.setMargins(16, 16, 16, 16);

        TextView textView1 = new TextView(getContext());
        textView1.setText("TextView1");

        TextView textView2 = new TextView(getContext());
        textView2.setText("TextView2");

        rootLayout.addView(textView1, textViewLayoutParams);
        rootLayout.addView(textView2, textViewLayoutParams);


    }
}
