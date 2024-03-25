package com.example.quicknotes.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quicknotes.CustomDesgin.MyDesgin;
import com.example.quicknotes.MainActivity;
import com.example.quicknotes.R;
import com.example.quicknotes.ReadActivity;
import com.example.quicknotes.RoomDatabase.DatabaseHelper;
import com.example.quicknotes.RoomDatabase.NoteEntity;

import java.util.ArrayList;

public class RecyclerNoteAdapter extends RecyclerView.Adapter<RecyclerNoteAdapter.MyViewHolder> {

    private final Context context;
    private ArrayList<NoteEntity> entities;
    private ArrayList<NoteEntity> filteredEntities; // New list to hold filtered items
    private final DatabaseHelper databaseHelper;

    public RecyclerNoteAdapter(Context context, ArrayList<NoteEntity> entities, DatabaseHelper databaseHelper) {
        this.context = context;
        this.entities = entities;
        this.filteredEntities = new ArrayList<>(entities); // Initialize with all items
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoteEntity item = filteredEntities.get(position); // Get item from filtered list

        holder.itemMainLayout.setCardBackgroundColor(item.getColorRef());
        holder.dateText.setText(item.getDate());
        if (!item.getNote().isEmpty()) {
            holder.noteContentText.setText(item.getNote());
        } else {
            holder.noteContentText.setText("No text");
            //holder.noteContentText.setTextColor(context.getResources().getColor(R.color.empty_text));
        }
        if (item.getTitle().isEmpty()) {
            holder.noteTitleText.setText(item.getTempTitle());
        } else {
            holder.noteTitleText.setText(item.getTitle());
        }
        holder.itemView.setOnClickListener(v -> itemClick(position));
        holder.itemView.setOnLongClickListener(v -> {
            itemLongClick(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return filteredEntities.size(); // Return size of filtered list
    }

    public void filter(String query) {
        filteredEntities.clear();
        if (query.isEmpty()) {
            filteredEntities.addAll(entities); // If query is empty, show all items
        } else {
            query = query.toLowerCase();
            for (NoteEntity entity : entities) {
                // Check if any part of the note or title contains the query
                if (entity.getNote().toLowerCase().contains(query) || entity.getTitle().toLowerCase().contains(query)) {
                    filteredEntities.add(entity);
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter of changes
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView itemMainLayout;
        private TextView noteTitleText, noteContentText, dateText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemMainLayout = itemView.findViewById(R.id.itemMainLayout);
            noteTitleText = itemView.findViewById(R.id.noteTitleText);
            noteContentText = itemView.findViewById(R.id.noteContentText);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }


    //READ DATA ->
    private void itemClick(int position) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra("id", entities.get(position).getId());
        intent.putExtra("title", entities.get(position).getTitle());
        intent.putExtra("tempTitle", entities.get(position).getTempTitle());
        intent.putExtra("note", entities.get(position).getNote());
        intent.putExtra("date", entities.get(position).getDate());
        intent.putExtra("category", entities.get(position).getCategory());
        intent.putExtra("colorRef", entities.get(position).getColorRef());
        context.startActivity(intent);
    }

    //DELETE DATA ->
    private void itemLongClick(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.anim.fade_in;
        CardView mainBackground = dialog.findViewById(R.id.mainBackground);
        TextView dialog_text = dialog.findViewById(R.id.dialog_text);
        TextView negative_button = dialog.findViewById(R.id.negative_button);
        TextView positive_button = dialog.findViewById(R.id.positive_button);

        negative_button.setOnClickListener(v -> dialog.dismiss());

        positive_button.setOnClickListener(v -> {
            NoteEntity entity = entities.get(position);
            databaseHelper.noteDao().deleteNote(new NoteEntity(entity.getId(), entity.getTitle(), entity.getTempTitle(),
                    entity.getNote(), entity.getDate(), entity.getCategory(), entity.getColorRef()));

            ((MainActivity) context).showNotes();
            dialog.dismiss();
            MyDesgin.CustomToast1(context, "Delete Successful!", context.getResources().getColor(R.color.warning));


            // Play sound effect
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.delete_sound2);
            mediaPlayer.start();

            // Release media player resources after sound finishes
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
            });

        });
        dialog.show();

    }

}
