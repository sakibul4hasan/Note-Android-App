package com.example.quicknotes.Model;


import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerUtils {

    // Define the date format
    private static final String DATE_FORMAT = "MMM dd, yyyy";

    // Method to show date picker dialog
    public static void showDatePickerDialog(Context context, final OnDateSelectedListener listener) {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, month, dayOfMonth) -> {
                    // Update the calendar instance with the selected date
                    calendar.set(year, month, dayOfMonth);

                    // Format the selected date
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());

                    // Pass the selected date to the listener
                    if (listener != null) {
                        listener.onDateSelected(formattedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Show the date picker dialog
        datePickerDialog.show();
    }

    // Method to parse a date string to Date object
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to format a Date object to a date string
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }

    // Interface for handling date selection
    public interface OnDateSelectedListener {
        void onDateSelected(String formattedDate);
    }
}
