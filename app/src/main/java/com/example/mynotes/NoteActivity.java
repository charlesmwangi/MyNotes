package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
//Declare variable
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.mynotes.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.mynotes.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.mynotes.ORIGINAL_NOTE_TEXT";
    public static final String NOTE_POSITION = "com.example.mynotes.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private TextView noteTitle;
    private TextView noteText;
    private int notePosition;
    private boolean isCancelling;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ref the views in the layout
        spinnerCourses = findViewById(R.id.spinner_courses);
        noteTitle = findViewById(R.id.text_note_title);
        noteText = findViewById(R.id.text_note_text);

        //get a list of courses
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        //generate an array adapter with sample course items
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        //associate the list to use for the dropdown list of courses
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //associate the adapter with the spinner
        spinnerCourses.setAdapter(adapterCourses);

        //read contents of the intent
        readDisplayStateValues();
        //check if savedInstance is null
        if (savedInstanceState == null){
            //save the original state of the note(prevent saving when one clicks send as mail)
            saveOriginalNoteValues();
    }else{
            //restore note values
            restoreOriginalNoteValues(savedInstanceState);
        }
        //check if the note is a new note
        if(!isNewNote)
           // display note on the views
           displayNotes(spinnerCourses, noteTitle, noteText);

    }

    private void restoreOriginalNoteValues(Bundle savedInstance) {
        //get the values
        originalNoteCourseId = savedInstance.getString(ORIGINAL_NOTE_COURSE_ID);
        originalNoteTitle = savedInstance.getString(ORIGINAL_NOTE_TITLE);
        originalNoteText = savedInstance.getString(ORIGINAL_NOTE_TEXT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //the values to save
        outState.putString(ORIGINAL_NOTE_COURSE_ID,originalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE,originalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT,originalNoteText);
    }

    private void saveOriginalNoteValues() {
        //check if the note is new
        if(isNewNote)
            return;
        //save the course id
        originalNoteCourseId = mNote.getCourse().getCourseId();
        originalNoteTitle = mNote.getTitle();
        originalNoteText = mNote.getText();


    }

    private void  displayNotes(Spinner spinnerCourses, TextView noteTitle, TextView noteText) {
        //get list of courses from the dataManager
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        //get index of note course
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        //set value to all the views
        noteTitle.setText(mNote.getTitle());
        noteText.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        //declare a local variable of type intent
        Intent intent = getIntent();
        //get the extra contained in the intent
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        //check if we are creating a new note
        isNewNote = position == POSITION_NOT_SET;
        //check if the note is empty
        if(isNewNote){
            //create new note
            createNewNote();
        } else {
            //open the note
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        //ref the data manager
        DataManager dm = DataManager.getInstance();
        //call the create new note method of the data manager
        notePosition = dm.createNewNote();
        //get the note at the given position
        mNote = dm.getNotes().get(notePosition);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            //invoke a method to send the mail
            sendEmail();
            return true;
        }else if(id==R.id.item_cancel){
            //create a local boolean variable
            isCancelling = true;
            //exit the activity
            finish();
        }else if(id==R.id.action_next){
            moveNext();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //ref the menu item
        MenuItem item = menu.findItem(R.id.action_next);
        //check if we are at the end of the note list
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;
        item.setEnabled(notePosition < lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        //save the note before moving to the next note
        saveNote();

        //increment the note position
        ++notePosition;
        //get the note at current position from the data manager
        mNote = DataManager.getInstance().getNotes().get(notePosition);
        //save note original values
        saveOriginalNoteValues();
        //display the notes
        displayNotes(spinnerCourses, noteTitle,noteText);
        //when at the last note
        invalidateOptionsMenu();
    }

    //save the note when the user hits the back button
    @Override
    protected void onPause() {
        super.onPause();
        //check if we are cancelling
        if (isCancelling){
            //check if the note is new
            if(isNewNote) {
                //get data manager instance and dont save note
                DataManager.getInstance().removeNote(notePosition);
            }else{
                storePreviousNoteValues();
            }

        }else {
            //save the changes
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        //get the course id and save the note values
        CourseInfo course = DataManager.getInstance().getCourse(originalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(originalNoteTitle);
        mNote.setText(originalNoteText);
    }

    private void saveNote() {
        //set the note details with regards to the data selected
        mNote.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        mNote.setTitle(noteTitle.getText().toString());
        mNote.setText(noteText.getText().toString());
    }

    private void sendEmail() {
       //use an implicit intent to send the mail
       //get the selected item
        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();
        //get variable note title and set it as the email subject
        String subject = noteTitle.getText().toString();
        //get variable note text and set it as the email body
        String text = "Check out what I learnt in the pluralsight course \"" +
                course.getTitle() + "\"\n" + noteText.getText().toString();
        //create an new intent to send the mail
        Intent intent = new Intent(Intent.ACTION_SEND);
        //associate the type of this intent ,message/rfc2822 deals with mail intent
        intent.setType("message/rfc2822");
        //provide a subject for the mail
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //provide a body for the mail
        intent.putExtra(Intent.EXTRA_TEXT,text);
        //parse the intent
        try {

            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("No mail Activity found", e.toString());
        }
    }
}
