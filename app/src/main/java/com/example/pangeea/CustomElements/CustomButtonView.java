package com.example.pangeea.CustomElements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.pangeea.R;

public class CustomButtonView extends CardView {

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
        int cardMargin = 16;
        setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ));
        setCardElevation(8);
        setRadius(20);
        RelativeLayout.LayoutParams cardParams = (RelativeLayout.LayoutParams) getLayoutParams();
        cardParams.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);

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
        button.setText("Button");
        button.setBackgroundColor(getResources().getColor(R.color.white));

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(getResources().getColor(R.color.white));

        float cornerRadiusInPixels = 20f;
        gradientDrawable.setCornerRadius(cornerRadiusInPixels);

        button.setBackground(gradientDrawable);
        button.setElevation(10f);

        TextView textView1 = new TextView(getContext());
        textView1.setText("TextView1");
        TextView textView2 = new TextView(getContext());
        textView2.setText("TextView2");
        TextView textView3 = new TextView(getContext());
        textView3.setText("TextView3");

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.setMargins(16, 16, 16, 0);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textViewLayoutParams.setMargins(16, 16, 16, 16);

        rootLayout.addView(button, buttonLayoutParams);
        rootLayout.addView(textView1, textViewLayoutParams);
        rootLayout.addView(textView2, textViewLayoutParams);
        rootLayout.addView(textView3, textViewLayoutParams);
    }
}

