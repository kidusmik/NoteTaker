package com.kidus.notetaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kidus.notetaker.R;
import com.kidus.notetaker.activities.NoteActivity;
import com.kidus.notetaker.database.NoteTakerDatabaseContract;
import com.kidus.notetaker.database.NoteTakerDatabaseContract.NoteInfoEntry;

import java.util.List;

public class NoteListRecyclerAdapter extends RecyclerView.Adapter<NoteListRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;
    private int mNoteTitileIndex;
    private int mCourseIdIndex;
    private int mIdIndex;

    public NoteListRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(mContext);
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if(mCursor == null)
            return;

        mNoteTitileIndex = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mCourseIdIndex = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mIdIndex = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_ID);
    }

    public void changeCursor(Cursor cursor){
        if(mCursor != null)
            mCursor.close();

        mCursor = cursor;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.list_item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String courseId = mCursor.getString(mCourseIdIndex);
        String noteTitle = mCursor.getString(mNoteTitileIndex);
        int id = mCursor.getInt(mIdIndex);

        holder.mTextCourse.setText(courseId);
        holder.mTextTitle.setText(noteTitle);
        holder.mId = id;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public final TextView mTextTitle;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = itemView.findViewById(R.id.tv_course_note_list);
            mTextTitle = itemView.findViewById(R.id.tv_title_note_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_INFO, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
