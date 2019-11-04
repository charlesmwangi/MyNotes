package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{
    //create a field of the context
    private final Context mContext;
    private final LayoutInflater layoutInflater;
    private final List<CourseInfo> mCourses;

    //context constructor
    public CourseRecyclerAdapter(Context mContext, List<CourseInfo> mCourses) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.mCourses = mCourses;
    }

    @NonNull
    @Override
    //creates instances of the view holder and create the views themselves
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a local variable of type view
        View itemView = layoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
     //get the note
        CourseInfo course = mCourses.get(position);
        //get the course title
        viewHolder.textCourse.setText(course.getTitle());
        //get the note position
        viewHolder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        //return the size of the list
        return mCourses.size();
    }

    //create a new class viewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView textCourse;
        public int mCurrentPosition;

        //viewholder constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //ref the textviews to be used
            textCourse = itemView.findViewById(R.id.text_course);
            //associate a click event handler with the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create a snackbar
                    Snackbar.make(v, mCourses.get(mCurrentPosition).getTitle(),
                    Snackbar.LENGTH_LONG).show();
//                    //create an intent to show the NoteActivity
//                    Intent intent = new Intent(mContext,NoteActivity.class);
//                    //pass the note position
//                    intent.putExtra(NoteActivity.NOTE_POSITION, mCurrentPosition);
//                    //start the intent
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}
