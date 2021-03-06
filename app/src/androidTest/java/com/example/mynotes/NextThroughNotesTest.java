package com.example.mynotes;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

public class NextThroughNotesTest {
 @Rule
    public ActivityTestRule<MainDrawerActivity> mainDrawerActivityTestRule =
         new ActivityTestRule(MainDrawerActivity.class);
 @Test
    public void NextThroughNote() {
  onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
  onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes));

  onView(withId(R.id.list_items)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

  //verify we have the data required, get ref to notelist from Datamanager
  List<NoteInfo> notes = DataManager.getInstance().getNotes();
  for (int index = 0; index < notes.size(); index++) {

   NoteInfo note = notes.get(index);

   onView(withId(R.id.spinner_courses)).check(
           matches(withSpinnerText(note.getCourse().getTitle()))
   );
   onView(withId(R.id.text_note_title)).check(matches(withText(note.getTitle())));
   onView(withId(R.id.text_note_text)).check(matches(withText(note.getText())));
//click next note

   if (index < notes.size()) {
//      onView(allOf(withId(R.id.action_next).isEnabled()))
    //onView(allOf(withId(R.id.action_next).isEnabled())).perform(click());

   }
   // onView(withId(R.id.action_next)).check(matches(not(isEnabled())));
   pressBack();

  }
 }
}