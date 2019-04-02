package com.technohack.android_architecture_demo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.technohack.android_architecture_demo.recyclerview_holder.NotesAdapter;
import com.technohack.android_architecture_demo.room_database.NotesEntity;
import com.technohack.android_architecture_demo.viewmodel.NotesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    NotesViewModel notesViewModel;

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        notesAdapter=new NotesAdapter();
        recyclerView.setAdapter(notesAdapter);

        notesViewModel=ViewModelProviders.of(this).get(NotesViewModel.class);

        notesViewModel.getAllNotes().observe(this, new Observer<List<NotesEntity>>() {
            @Override
            public void onChanged(@Nullable List<NotesEntity> notesEntities) {

                //it's a predefined method defined in ListAdapter Class
                notesAdapter.submitList(notesEntities);
            }
        });

        //For floating action button
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);

            }
        });

        //for making the list of the recyclerView as a swipe
      new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            notesViewModel.delete(notesAdapter.getNoteAt(viewHolder.getAdapterPosition()));
            Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
        }
    }).attachToRecyclerView(recyclerView);

        notesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NotesEntity note) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra(AddActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //fetching the data from add activity
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddActivity.EXTRA_PRIORITY, 1);

            NotesEntity note = new NotesEntity(title, description, priority);
            notesViewModel.insert(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){

            int id = data.getIntExtra(AddActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddActivity.EXTRA_PRIORITY, 1);

            NotesEntity note = new NotesEntity(title, description, priority);
            note.setId(id);
            notesViewModel.update(note);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    //For deleting the all notes from the room database
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notesId:
                notesViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
