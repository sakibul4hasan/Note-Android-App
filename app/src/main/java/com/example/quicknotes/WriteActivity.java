package com.example.quicknotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.quicknotes.Adapter.CustomSpinnerAdapter;
import com.example.quicknotes.CustomDesgin.MyDesgin;
import com.example.quicknotes.Model.DatePickerUtils;
import com.example.quicknotes.Model.SpinnerItemModel;
import com.example.quicknotes.RoomDatabase.DatabaseHelper;
import com.example.quicknotes.RoomDatabase.NoteDao;
import com.example.quicknotes.RoomDatabase.NoteEntity;
import com.example.quicknotes.databinding.ActivityWriteBinding;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityWriteBinding binding;
    private CardView backButtonId;
    private MaterialButton saveButtonId, updateBtnId;
    ArrayList<SpinnerItemModel> catList;
    private String noteTitle, noteTempTitle, notesText, selectedDate, categoryName;
    private int categoryColor;
    DatabaseHelper databaseHelper;

    private int id, colorRef;
    private String title, tempTitle, note, date, category, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //ID INIT
        backButtonId = findViewById(R.id.backId);
        saveButtonId = findViewById(R.id.saveBtnId);
        updateBtnId = findViewById(R.id.updateBtnId);

        //DATABASE INIT
        databaseHelper = DatabaseHelper.getInstance(this);

        //DATE SET
        Calendar calendar = Calendar.getInstance();
        // Format the current date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());
        binding.dateSpinnerId.setText(formattedDate);
        selectedDate = formattedDate;


        //SPINNER SETUP...
        catList = new ArrayList<>();
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.yellow_light), "Personal"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.blue_light), "Work"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.green_light), "Study"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.pink_light), "Travel"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.yellow_light), "Recipes"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.blue_light), "Finance"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.green_light), "Health"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.pink_light), "Ideas"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.yellow_light), "Entertainment"));
        catList.add(new SpinnerItemModel(getResources().getColor(R.color.green_light), "Miscellaneous"));

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, catList);
        binding.catSpinnerId.setAdapter(adapter);


        //CLICK LISTENER
        backButtonId.setOnClickListener(this);
        saveButtonId.setOnClickListener(this);
        binding.dateSpinnerId.setOnClickListener(this);
        binding.catSpinnerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItemModel selectedItem = (SpinnerItemModel) parent.getItemAtPosition(position);
                categoryName = selectedItem.getCategory();
                categoryColor = selectedItem.getColor();
                binding.titleBgId.setCardBackgroundColor(categoryColor);
                binding.catBgId.setCardBackgroundColor(categoryColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //NOTES EDIT
        noteEdit();

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.backId) {
            super.onBackPressed();
        }
        if (v.getId() == R.id.dateSpinnerId) {
            //CHAT-GPT CREATION
            DatePickerUtils.showDatePickerDialog(this, formattedDate -> {
                // Do something with the selected date
                binding.dateSpinnerId.setText(formattedDate);
                selectedDate = formattedDate;
            });

            //MY CREATION
            /*Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        // Update the calendar instance with the selected date
                        calendar.set(year, month, dayOfMonth);

                        // Format the selected date
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(calendar.getTime());
                        binding.dateSpinnerId.setText(formattedDate);

                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();*/
        }

        //SAVE NOTES
        if (v.getId() == R.id.saveBtnId) {
            noteTitle = binding.noteTitleEd.getText().toString();
            notesText = binding.noteContentEd.getText().toString();

            if (noteTitle.isEmpty()) {

                if (!notesText.isEmpty()) {
                    StringBuilder builder = new StringBuilder(notesText);
                    builder.setLength(30);
                    noteTempTitle = String.valueOf(builder);
                    saveNotes();

                } else
                    MyDesgin.CustomToast1(getApplicationContext(), "No text", getResources().getColor(R.color.warning));

            } else saveNotes();
        }
    }

    private void saveNotes() {
        databaseHelper.noteDao().insertNote(new NoteEntity(noteTitle, noteTempTitle, notesText, selectedDate, categoryName, categoryColor));
        MyDesgin.CustomToast1(getApplicationContext(), "Save Successful!", getResources().getColor(R.color.success));
        super.onBackPressed();
        finish();
    }


    //NOTE UPDATE AND EDIT
    private void noteEdit() {
        //INTENT RECEIVED
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            title = bundle.getString("title");
            tempTitle = bundle.getString("tempTitle");
            note = bundle.getString("note");
            date = bundle.getString("date");
            category = bundle.getString("category");
            colorRef = bundle.getInt("colorRef");
            update = bundle.getString("update");

            updateBtnId.setVisibility(View.VISIBLE);
            saveButtonId.setVisibility(View.GONE);

            //set value
            binding.titleBgId.setCardBackgroundColor(colorRef);
            binding.noteTitleEd.setText(title);
            binding.catBgId.setCardBackgroundColor(colorRef);
            binding.dateSpinnerId.setText(date);
            binding.noteContentEd.setText(note);
            if (category.contains("Personal")) binding.catSpinnerId.setSelection(0);
            if (category.contains("Work")) binding.catSpinnerId.setSelection(1);
            if (category.contains("Study")) binding.catSpinnerId.setSelection(2);
            if (category.contains("Travel")) binding.catSpinnerId.setSelection(3);
            if (category.contains("Recipes")) binding.catSpinnerId.setSelection(4);
            if (category.contains("Finance")) binding.catSpinnerId.setSelection(5);
            if (category.contains("Health")) binding.catSpinnerId.setSelection(6);
            if (category.contains("Ideas")) binding.catSpinnerId.setSelection(7);
            if (category.contains("Entertainment")) binding.catSpinnerId.setSelection(8);
            if (category.contains("Miscellaneous")) binding.catSpinnerId.setSelection(9);

            updateBtnId.setOnClickListener(v -> {
                //
                noteTitle = binding.noteTitleEd.getText().toString();
                notesText = binding.noteContentEd.getText().toString();

                if (noteTitle.isEmpty()) {

                    if (!notesText.isEmpty()) {
                        StringBuilder builder = new StringBuilder(notesText);
                        builder.setLength(30);
                        noteTempTitle = String.valueOf(builder);
                        updateNote();

                    } else
                        MyDesgin.CustomToast1(getApplicationContext(), "No text", getResources().getColor(R.color.error));

                } else updateNote();

            });
        }
    }

    private void updateNote() {

        databaseHelper.noteDao().updateNote(new NoteEntity(id, noteTitle, noteTempTitle, notesText, selectedDate, categoryName, categoryColor));
        MyDesgin.CustomToast1(getApplicationContext(), "Update Successful!", getResources().getColor(R.color.success));

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}