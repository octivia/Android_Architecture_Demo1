package com.technohack.android_architecture_demo.room_database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotesDao {


    @Insert
    void addNotes(NotesEntity notes);

    @Update
    void updateNotes(NotesEntity notes);

    @Delete
    void deleteNotes(NotesEntity notes);

    //if we want to something out of box we will use Query annotation and inside this we will use simple sql query
    //to delete the all notes
    @Query("delete from notes")
    void deleteAllNotes();

    //order by function will order the rows by checking the priority
    // and desc shows the decrement order
    @Query("select *from notes order by priority desc")
    //LiveData will observe the any changes in data and it will automatically reflect the in the UIs
    LiveData<List<NotesEntity>> getAllNotes();


}
