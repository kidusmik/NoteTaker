package com.kidus.notetaker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kidus.notetaker.R;
import com.kidus.notetaker.database.NoteTakerDatabaseContract;
import java.util.List;

public class CourseListRecyclerAdapter extends RecyclerView.Adapter<CourseListRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final Cursor mCursor;
    private int mCourseTitleIndex;
    private int mCourseIdIndex;

    public CourseListRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(mContext);
        populateCourseColumns();
    }

    private void populateCourseColumns() {
        if(mCursor ==null)
            return;

        mCourseTitleIndex = mCursor.getColumnIndex(NoteTakerDatabaseContract.CourseInfoEntry.COLUMN_COURSE_TITLE);
        mCourseIdIndex = mCursor.getColumnIndex(NoteTakerDatabaseContract.CourseInfoEntry.COLUMN_COURSE_ID);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.list_item_course_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String courseTitle = mCursor.getString(mCourseTitleIndex);
        holder.mTitleCourse.setText(courseTitle);
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTitleCourse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleCourse = itemView.findViewById(R.id.tv_course_course_list);
        }
    }
}
