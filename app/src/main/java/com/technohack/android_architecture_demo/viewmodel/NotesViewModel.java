package com.technohack.android_architecture_demo.viewmodel;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.technohack.android_architecture_demo.repository_abstraction_layer.NotesRepository;
import com.technohack.android_architecture_demo.room_database.NotesEntity;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private NotesRepository notesRepository;
    private LiveData<List<NotesEntity>> allNotes;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        notesRepository=new NotesRepository(application);
        allNotes=notesRepository.getAllNotes();

    }

    public void insert(NotesEntity notes){
        notesRepository.addNoted(notes);
    }
    public void update(NotesEntity notes){
        notesRepository.updateNotes(notes);
    }
    public void delete(NotesEntity notes){
        notesRepository.deleteNotes(notes);
    }
    public void deleteAllNotes(){
        notesRepository.deleteAllNotes();
    }

    public LiveData<List<NotesEntity>>  getAllNotes(){
        return allNotes;
    }

}
