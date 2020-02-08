package com.example.mynotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import java.util.List;

public class MainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView recyclerItems;
    private LinearLayoutManager notesLayoutManager;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    private GridLayoutManager coursesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create an intent for new note creation
                startActivity (new Intent(MainDrawerActivity.this, NoteActivity.class));

            }
        });

        PreferenceManager.setDefaultValues(this,R.xml.general_preferences,false);
        PreferenceManager.setDefaultValues(this,R.xml.messages_preferences,false);
        PreferenceManager.setDefaultValues(this,R.xml.sync_preferences,false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //call the method to display content
        initializeDisplayContent();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //let the array adapter know that the data has changed
        //mAdapterNotes.notifyDataSetChanged();
        noteRecyclerAdapter.notifyDataSetChanged();
        updateNavHeader();
    }

    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textUserName = headerView.findViewById(R.id.text_user_name);
        TextView textEmailAddress = headerView.findViewById(R.id.text_email_address);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = pref.getString("name_key","Charles");
        String emailAddress = pref.getString("email_key","charlimwangi@gmail.com");
        textUserName.setText(userName);
        textEmailAddress.setText(emailAddress);



    }

    private void initializeDisplayContent() {

        //get ref to the recycler view
        recyclerItems = findViewById(R.id.list_items);
        //create a layoutManager and pass the constructor
        notesLayoutManager = new LinearLayoutManager(this);
        //create the grid layout
        coursesLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.course_grid_span));

        //get the notes to display
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        //create an instance of the noteRecyclerAdapter
        noteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
        //associate the recyclerAdapter with the recycler view
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        courseRecyclerAdapter = new CourseRecyclerAdapter(this,courses);
        displayNotes();
    }

    private void displayNotes() {
        recyclerItems.setAdapter(noteRecyclerAdapter);
        //connect the recycler view to the layout manager
        recyclerItems.setLayoutManager(notesLayoutManager);
        selectNavigationMenuItem(R.id.nav_notes);

    }

    private void selectNavigationMenuItem(int id) {
        //show selection being displayed on the drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        //get menu in the nav view
        Menu menu = navigationView.getMenu();
        //ref the menu item to be selected
        menu.findItem(id).setChecked(true);
    }

    private void displayCourses(){
        //associate the layoutmanager with the recycler view
        recyclerItems.setAdapter(courseRecyclerAdapter);
        recyclerItems.setLayoutManager(coursesLayoutManager);
        selectNavigationMenuItem(R.id.nav_courses);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            // Handle selection and display notes
            displayNotes();
        } else if (id == R.id.nav_courses) {
            // Handle selection
            displayCourses();
        } else if (id == R.id.nav_share) {
            // Handle selection
            //handleSelection(R.string.now_share_message);
            handleShare();
        } else if (id == R.id.nav_send) {
            // Handle selection
            handleSelection(R.string.send_message);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleShare() {
        //display the message
        View view = findViewById(R.id.list_items);
        Snackbar.make(view, "Share to "+
                PreferenceManager.getDefaultSharedPreferences(this).getString("user_favorite_social","")
                , Snackbar.LENGTH_LONG).show();
    }

    private void handleSelection(int message_id) {
        //display the message
        View view = findViewById(R.id.list_items);
        Snackbar.make(view, message_id, Snackbar.LENGTH_LONG).show();
    }
}
