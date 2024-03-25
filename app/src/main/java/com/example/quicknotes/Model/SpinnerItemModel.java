package com.example.quicknotes.Model;

public class SpinnerItemModel {

    private int color;
    private String category;

    public SpinnerItemModel(int color, String category) {
        this.color = color;
        this.category = category;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
