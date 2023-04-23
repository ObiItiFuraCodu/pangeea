package com.example.pangeea.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pangeea.R;

import java.util.ArrayList;

public class T_Q_Adapter extends ArrayAdapter<Test_Question> {
    private Context context;
    private int resource;
    public T_Q_Adapter(@NonNull Context context, int resource, @NonNull ArrayList<Test_Question> objects) {
        super(context, resource, objects);
        this.context =  context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView prompt = convertView.findViewById(R.id.element_prompt);
        TextView a = convertView.findViewById(R.id.element_a);
        TextView b = convertView.findViewById(R.id.element_b);
        TextView c = convertView.findViewById(R.id.element_c);
        prompt.setText(getItem(position).getPrompt());
        a.setText(getItem(position).getA());
        b.setText(getItem(position).getB());
        c.setText(getItem(position).getC());


        return convertView;
    }
}
