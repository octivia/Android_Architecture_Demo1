package com.technohack.android_architecture_demo.room_database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//Entity annotation is used to
//Here table name is notes
@Entity(tableName = "notes")
public class NotesEntity {

    //to show the any variable to primary key we need to use the annotation primary key
    @PrimaryKey
    private int id;

    private String title;

    private String description;

    private int priority;



    public NotesEntity() {
    }

    public NotesEntity(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
