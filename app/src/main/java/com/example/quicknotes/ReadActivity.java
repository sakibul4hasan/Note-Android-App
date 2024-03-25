package com.example.quicknotes;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quicknotes.CustomDesgin.MyDesgin;
import com.example.quicknotes.RoomDatabase.DatabaseHelper;
import com.example.quicknotes.RoomDatabase.NoteEntity;
import com.example.quicknotes.databinding.ActivityReadBinding;

public class ReadActivity extends AppCompatActivity {

    private ActivityReadBinding binding;
    private int id, colorRef;
    private String title, tempTitle, note, date, category;
    private CardView backButtonId, editButtonId, deleteButtonId;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityReadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //ID INIT
        backButtonId = findViewById(R.id.backButtonId);
        editButtonId = findViewById(R.id.editButtonId);
        deleteButtonId = findViewById(R.id.deleteButtonId);

        //DATABASE INIT
        databaseHelper = DatabaseHelper.getInstance(this);

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
        }

        readNotes();


        //CLICK LISTENER
        backButtonId.setOnClickListener(v -> {
            super.onBackPressed();
        });
        editButtonId.setOnClickListener(v -> {
            editeNote();
        });
        deleteButtonId.setOnClickListener(v -> deleteNote());


    }

    private void readNotes() {
        if (title.isEmpty()) {
            binding.noteTitleText.setText("No Title");
            binding.noteTitleText.setTextColor(getResources().getColor(R.color.empty_text));

        } else binding.noteTitleText.setText(title);

        if (note.isEmpty()) {
            binding.noteContentText.setText("Empty");
            binding.noteContentText.setTextColor(getResources().getColor(R.color.empty_text));

        } else binding.noteContentText.setText(note);

        binding.noteDate.setText(date);
    }

    private void deleteNote() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.anim.fade_in;
        CardView mainBackground = dialog.findViewById(R.id.mainBackground);
        TextView dialog_text = dialog.findViewById(R.id.dialog_text);
        TextView negative_button = dialog.findViewById(R.id.negative_button);
        TextView positive_button = dialog.findViewById(R.id.positive_button);

        negative_button.setOnClickListener(v -> dialog.dismiss());

        positive_button.setOnClickListener(v -> {
            databaseHelper.noteDao().deleteNote(new NoteEntity(id, title, tempTitle, note, date, category, colorRef));
            dialog.dismiss();
            super.onBackPressed();
            MyDesgin.CustomToast1(this, "Delete Successful", getResources().getColor(R.color.warning));
            finish();

            // Play sound effect
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.delete_sound2);
            mediaPlayer.start();

            // Release media player resources after sound finishes
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
            });

        });
        dialog.show();
    }


    private void editeNote() {
        Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("tempTitle", tempTitle);
        intent.putExtra("note", note);
        intent.putExtra("date", date);
        intent.putExtra("category", category);
        intent.putExtra("colorRef", colorRef);
        intent.putExtra("update", "Update");
        startActivity(intent);
    }
}