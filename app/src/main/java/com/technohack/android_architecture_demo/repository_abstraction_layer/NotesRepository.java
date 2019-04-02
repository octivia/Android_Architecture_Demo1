package com.technohack.android_architecture_demo.repository_abstraction_layer;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.technohack.android_architecture_demo.room_database.NotesDao;
import com.technohack.android_architecture_demo.room_database.NotesEntity;
import com.technohack.android_architecture_demo.room_database.NotesRoomDatabase;

import java.util.List;

public class NotesRepository {

    private NotesDao notesDao;

    private LiveData<List<NotesEntity>>  allNotes;

    //Application is subclass of Context class
    public NotesRepository(Application application){

        NotesRoomDatabase database=NotesRoomDatabase.getNotesInstance(application);
        notesDao=database.notesDao();
        allNotes=notesDao.getAllNotes();

    }

    //for access the database we have to use background thread to access the data from notes tables
    //For that we will use asyncTask
    //We have to call the asyncTaskClass to do operation on database
    public void addNoted(NotesEntity notes){

        new InsertNotesAsyncTask(notesDao).execute(notes);

    }

    public void updateNotes(NotesEntity notes){

        new UpdateNotesAsyncTask(notesDao).equals(notes);
    }

    public void deleteNotes(NotesEntity notes){

        new UpdateNotesAsyncTask(notesDao).execute(notes);

    }

    public void deleteAllNotes(){

        new DeleteAllNotesAsyncTask(notesDao).execute();

    }

    //LiveData is already running in background so we don'e need to use asynTask Class
    public LiveData<List<NotesEntity>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNotesAsyncTask extends AsyncTask<NotesEntity,Void ,Void>{

        private NotesDao notesDao;

        public InsertNotesAsyncTask(NotesDao notesDao){
            this.notesDao=notesDao;
        }

        @Override
        protected Void doInBackground(NotesEntity... notesEntities) {

            //inserting the data into the table in background thread
            notesDao.addNotes(notesEntities[0]);

            return null;
        }

    }

    private static class UpdateNotesAsyncTask extends AsyncTask<NotesEntity,Void ,Void>{

        private NotesDao notesDao;

        public  UpdateNotesAsyncTask(NotesDao notesDao){
            this.notesDao=notesDao;
        }

        @Override
        protected Void doInBackground(NotesEntity... notesEntities) {

            //fetching the data from the notes table
            notesDao.updateNotes(notesEntities[0]);

            return null;
        }

    }

    private static class DeleteNotesAsyncTask extends AsyncTask<NotesEntity,Void ,Void>{

        private NotesDao notesDao;

        public DeleteNotesAsyncTask(NotesDao notesDao){
            this.notesDao=notesDao;
        }

        @Override
        protected Void doInBackground(NotesEntity... notesEntities) {

            //fetching the data from the notes table
            notesDao.deleteNotes(notesEntities[0]);

            return null;
        }

    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void,Void ,Void>{

        private NotesDao notesDao;

        public DeleteAllNotesAsyncTask(NotesDao notesDao){
            this.notesDao=notesDao;
        }

        @Override
        protected Void doInBackground(Void ... voids) {

            //fetching the data from the notes table
            notesDao.deleteAllNotes();

            return null;
        }

    }



}
