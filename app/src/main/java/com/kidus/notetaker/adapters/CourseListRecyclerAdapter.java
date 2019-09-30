package com.kidus.notetaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kidus.notetaker.R;
import com.kidus.notetaker.database.CourseInfo;
import com.kidus.notetaker.database.NoteInfo;

import java.util.List;

public class CourseListRecyclerAdapter extends RecyclerView.Adapter<CourseListRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CourseInfo> mCourses;
    private final LayoutInflater mLayoutInflater;

    public CourseListRecyclerAdapter(Context context, List<CourseInfo> courses) {
        mContext = context;
        mCourses = courses;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.list_item_course_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseInfo course = mCourses.get(position);
        holder.mTitleCourse.setText(course.getTitle());
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTitleCourse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleCourse = itemView.findViewById(R.id.tv_course_course_list);
        }
    }
}
