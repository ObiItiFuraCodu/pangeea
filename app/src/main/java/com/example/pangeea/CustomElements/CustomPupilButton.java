package com.example.pangeea.CustomElements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;

public class CustomPupilButton extends LinearLayout {
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

        setOrientation(LinearLayout.HORIZONTAL);

        setBackgroundColor(getResources().getColor(R.color.gray));

        Button button = new Button(getContext());
        button.setText("Name");
        ImageView view = new ImageView(getContext());
        view.setImageResource(R.drawable.ic_launcher_foreground);
        addView(button);
        addView(view);


    }
}
