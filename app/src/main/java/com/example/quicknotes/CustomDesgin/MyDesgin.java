package com.example.quicknotes.CustomDesgin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.quicknotes.R;

public class MyDesgin {

    public static void CustomToast1(Context context, String text, int color) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        toast.setView(view);
        CardView toastColor = view.findViewById(R.id.toastColor);
        TextView toastText = view.findViewById(R.id.toastText);
        toastColor.setCardBackgroundColor(color);
        toastText.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

}
