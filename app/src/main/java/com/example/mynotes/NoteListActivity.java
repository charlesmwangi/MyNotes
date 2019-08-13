package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
//        //add a list of notes
//        //ref the listView
//        final ListView listNotes = findViewById(R.id.list_notes);
//        //get the content to put to the list
//        List<NoteInfo> notes = DataManager.getInstance().getNotes();
//        //put the notes in the list view using an adapter
//        mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
//        //associate the adapter with the list view
//        listNotes.setAdapter(mAdapterNotes);
//
//        //set onclickListener
//        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //declare a variable of type intent
//                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
//                //ref the final ListView and get the position of the note
////                NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
//                //put extra
//                intent.putExtra(NoteActivity.NOTE_POSITION, position);
//                //start the intent
//                startActivity(intent);
//            }
//        });

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
