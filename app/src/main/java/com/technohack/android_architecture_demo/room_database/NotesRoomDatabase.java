package com.technohack.android_architecture_demo.room_database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

//If we are trying to migrate the database we need to increase version under the hood
@Database(entities = {NotesEntity.class},version = 1)
public abstract class NotesRoomDatabase extends RoomDatabase {

    private static NotesRoomDatabase notesInstance;
     //Room's subclasses are abstract class
    //to access the notes
    public abstract  NotesDao notesDao();

    //We using synchronized keyword  because we want only single instance of the Room database
    public static synchronized NotesRoomDatabase getNotesInstance(Context context){

        if(notesInstance==null){
            notesInstance= Room.databaseBuilder(context.getApplicationContext(),NotesRoomDatabase.class,"notes_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)   //just to populate the pre added notes inside the table
                    .build();
             // fallbackToDestructiveMigration() will allow you to migrate the database at any point of time without this method if you
            // will try to migrate the database you will get the compile time error
        }
        return notesInstance;
    }


    //We want to populate the raw notes to display for that we will use the RoomDatabase callBack
    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //calling the asyncTask method
            new PopulateDbAsyncTask(notesInstance).execute();

        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{

        private NotesDao notesDao;

        PopulateDbAsyncTask(NotesRoomDatabase db){
            this.notesDao=db.notesDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            notesDao.addNotes(new NotesEntity("title1","Description1",1));
            notesDao.addNotes(new NotesEntity("title2","Description2",2));
            notesDao.addNotes(new NotesEntity("title3","Description3",3));

            return null;
        }
    }
}
