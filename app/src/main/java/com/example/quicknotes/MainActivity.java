package com.example.quicknotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.quicknotes.Adapter.RecyclerNoteAdapter;
import com.example.quicknotes.RoomDatabase.DatabaseHelper;
import com.example.quicknotes.RoomDatabase.NoteEntity;
import com.example.quicknotes.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CardView filterButtonId;
    private SearchView searchView;
    DatabaseHelper databaseHelper;
    private RecyclerNoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //ID INIT
        filterButtonId = findViewById(R.id.filterButtonId);
        searchView = findViewById(R.id.searchView);

        //DATABASE INIT
        databaseHelper = DatabaseHelper.getInstance(this);
        showNotes();


        //CLICK LISTENER

        //SEARCH QUERY
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });


        filterButtonId.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Filter", Toast.LENGTH_SHORT).show();
        });

        binding.notesAddFBtnId.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), WriteActivity.class));
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        showNotes();
    }

    public void showNotes() {
        ArrayList<NoteEntity> entities = (ArrayList<NoteEntity>) databaseHelper.noteDao().getNote();

        if (!entities.isEmpty()) {
            binding.emptyText.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);

            adapter = new RecyclerNoteAdapter(this, entities, databaseHelper);
            // binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            binding.recyclerView.setAdapter(adapter);

        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyText.setVisibility(View.VISIBLE);
        }

    }

}