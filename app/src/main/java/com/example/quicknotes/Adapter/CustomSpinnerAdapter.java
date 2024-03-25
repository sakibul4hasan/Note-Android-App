package com.example.quicknotes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.quicknotes.Model.SpinnerItemModel;
import com.example.quicknotes.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<SpinnerItemModel> data;

    public CustomSpinnerAdapter(Context context, ArrayList<SpinnerItemModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_spinner_dropdown_item, null);
        }
        CardView color = convertView.findViewById(R.id.colorId);
        TextView text = convertView.findViewById(R.id.text);

        color.setCardBackgroundColor(data.get(position).getColor());
        text.setText(data.get(position).getCategory());

        return convertView;
    }
}
