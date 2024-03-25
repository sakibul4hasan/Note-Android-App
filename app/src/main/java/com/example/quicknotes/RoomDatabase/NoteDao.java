package com.example.quicknotes.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id ASC")
    List<NoteEntity> getNote();

    @Insert
    void insertNote(NoteEntity entities);

    @Update
    void updateNote(NoteEntity entity);

    @Delete
    void deleteNote(NoteEntity entity);

    /*//update 2nd way
    @Query("UPDATE notes SET title = :title, note = :note WHERE id = :id")
    void updateNote2(int id, String title, String note);*/

}
