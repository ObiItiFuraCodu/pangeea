package com.example.pangeea.CustomElements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
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
        RelativeLayout rootLayout = new RelativeLayout(getContext());
        addView(rootLayout);

        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable2.setColor(getResources().getColor(R.color.binaryblue));

        float cornerRadiusInPixels2 = 20f;
        gradientDrawable2.setCornerRadius(cornerRadiusInPixels2);

        setBackground(gradientDrawable2);

        TextView text = new TextView(getContext());
        text.setText("NAME");
        text.setTextColor(getResources().getColor(R.color.white));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(getResources().getColor(R.color.white));

        float cornerRadiusInPixels = 20f;
        gradientDrawable.setCornerRadius(cornerRadiusInPixels);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(16, 16, 16, 0);

        ImageView view = new ImageView(getContext());
        view.setImageResource(R.drawable.rankhigh);
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        rootLayout.addView(text, textParams);
        rootLayout.addView(view, viewParams);
    }
}