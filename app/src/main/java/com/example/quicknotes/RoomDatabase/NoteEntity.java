package com.example.quicknotes.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "tempTitle")
    public String tempTitle;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "colorRef")
    public int colorRef;

    public NoteEntity(int id, String title, String tempTitle, String note, String date, String category, int colorRef) {
        this.id = id;
        this.title = title;
        this.tempTitle = tempTitle;
        this.note = note;
        this.date = date;
        this.category = category;
        this.colorRef = colorRef;
    }

    @Ignore
    public NoteEntity(String title, String tempTitle, String note, String date, String category, int colorRef) {
        this.title = title;
        this.tempTitle = tempTitle;
        this.note = note;
        this.date = date;
        this.category = category;
        this.colorRef = colorRef;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTempTitle() {
        return tempTitle;
    }

    public void setTempTitle(String tempTitle) {
        this.tempTitle = tempTitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getColorRef() {
        return colorRef;
    }

    public void setColorRef(int colorRef) {
        this.colorRef = colorRef;
    }
}
