package com.example.pangeea.CustomElements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        int cardMargin = 16;
        setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ));
        setCardElevation(8);
        setRadius(20);
        RelativeLayout.LayoutParams cardParams = (RelativeLayout.LayoutParams) getLayoutParams();
        cardParams.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);
        RelativeLayout.LayoutParams rootParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rootParams.setMargins(16, 16, 16, 16);
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(rootLayout, rootParams);

        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable2.setColor(getResources().getColor(R.color.binaryblue));

        float cornerRadiusInPixels2 = 20f;
        gradientDrawable2.setCornerRadius(cornerRadiusInPixels2);

        setBackground(gradientDrawable2);

        TextView text = new TextView(getContext());
        text.setText("NAME");
        text.setTextColor(getResources().getColor(R.color.white));

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(16, 16, 16, 0);

        ImageView view = new ImageView(getContext());
        view.setImageResource(R.drawable.ic__rank_1);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        viewParams.gravity = Gravity.END; // Set the layout_gravity to end (right)
        rootLayout.addView(view, viewParams);
        rootLayout.addView(text, textParams);

    }
}