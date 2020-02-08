package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter noteRecyclerAdapter;

    //private ArrayAdapter<NoteInfo> adapterNotes;
    //private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create an intent for new note creation
                startActivity (new Intent(NoteListActivity.this, NoteActivity.class));

            }
        });
        //call the method to display content
        initializeDisplayContent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //let the array adapter know that the data has changed
        //mAdapterNotes.notifyDataSetChanged();
        noteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {

        //get ref to the recycler view
        final RecyclerView recyclerNotes = findViewById(R.id.list_notes);
        //create a layoutManager and pass the constructor
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        //connect the recycler view to the layout manager
        recyclerNotes.setLayoutManager(notesLayoutManager);

        //get the notes to display
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        //create an instance of the noteRecyclerAdapter
        noteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
        //associate the recyclerAdapter with the recycler view
        recyclerNotes.setAdapter(noteRecyclerAdapter);
    }

}
