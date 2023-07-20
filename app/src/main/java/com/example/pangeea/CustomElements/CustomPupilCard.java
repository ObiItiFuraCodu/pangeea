package com.example.pangeea.CustomElements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.pangeea.R;

public class CustomPupilCard extends CardView {
    public CustomPupilCard(Context context) {
        super(context);
        init();
    }

    public CustomPupilCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPupilCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        int cardMargin = 16;
        setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
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
        float cornerRadiusInPixels2 = 20f;
        gradientDrawable2.setCornerRadius(cornerRadiusInPixels2);
        gradientDrawable2.setColor(getResources().getColor(R.color.light_red));

        setBackground(gradientDrawable2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 16, 16, 16);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(16, 16, 16, 16);
        TextView name = new TextView(getContext());
        name.setTextColor(getResources().getColor(R.color.black));
        name.setText("DETAILS");
        name.setElevation(10.0f);
        name.setLayoutParams(textParams);

        CustomCardElement element1 = new CustomCardElement(getContext());
        element1.setElevation(10.0f);
        element1.setLayoutParams(layoutParams);

        CustomCardElement element2 = new CustomCardElement(getContext());
        element2.setElevation(10.0f);
        element2.setLayoutParams(layoutParams);

        rootLayout.addView(name);
        rootLayout.addView(element1);
        rootLayout.addView(element2);

    }
}
