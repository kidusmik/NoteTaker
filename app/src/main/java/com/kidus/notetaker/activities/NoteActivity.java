package com.kidus.notetaker.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.CursorLoader;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.kidus.notetaker.R;
import com.kidus.notetaker.database.NoteTakerContentProviderContract;
import com.kidus.notetaker.database.NoteTakerContentProviderContract.Courses;
import com.kidus.notetaker.database.NoteTakerDatabaseContract.CourseInfoEntry;
import com.kidus.notetaker.database.NoteTakerDatabaseContract.NoteInfoEntry;
import com.kidus.notetaker.database.NoteTakerSQLOpenHelper;
import com.kidus.notetaker.ui.NoteTakerNotification;

public class NoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int ID_NOT_SET = -1;
    public static final int LOADER_COURSES = 0;
    public static final int LOADER_NOTES = 1;
    private SimpleCursorAdapter mAdapterCourses;
    private Spinner mSpinnerCourses;
    private NoteTakerSQLOpenHelper mSqlOpenHelper;
    private String[] courseColumns;
    public static String NOTE_INFO = "com.kidus.notetaker.activities.NOTE_INFO";
    private int mNoteId;
    private int mCourseIdIndex;
    private int mNoteTitleIndex;
    private int mNoteTextIndex;
    private Cursor mNotesCursor;
    private EditText mEditTextNoteTitle;
    private EditText mEditTextNoteText;
    private boolean mCoursesQueryFinished;
    private boolean mNotesQueryFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSqlOpenHelper = new NoteTakerSQLOpenHelper(this);

        mSpinnerCourses = findViewById(R.id.spinner_note_courses);

        mAdapterCourses = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null,
                new String[]{CourseInfoEntry.COLUMN_COURSE_TITLE}, new int[]{android.R.id.text1}, 0);

        mAdapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(mAdapterCourses);

        getLoaderManager().initLoader(LOADER_COURSES, null, this);

//        loadCoursesData();

        mEditTextNoteTitle = findViewById(R.id.et_note_title);
        mEditTextNoteText = findViewById(R.id.et_note_text);

//        loadNoteData();
        getLoaderManager().initLoader(LOADER_NOTES, null, this);

        Intent intent = getIntent();
        mNoteId = intent.getIntExtra(NOTE_INFO, ID_NOT_SET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        if(menuId == R.id.action_reminder){
            showReminderNotification();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showReminderNotification() {
        NoteTakerNotification.notify(this, "This is test text", 0);
    }

    private void loadCoursesData() {
        String[] courseColumns = {CourseInfoEntry.COLUMN_COURSE_TITLE, CourseInfoEntry.COLUMN_COURSE_ID, CourseInfoEntry.COLUMN_ID};
        Cursor courseCursor = MainActivity.mDatabase.query(CourseInfoEntry.TABLE_NAME, courseColumns, null, null, null, null, CourseInfoEntry.COLUMN_COURSE_TITLE);
        mAdapterCourses.changeCursor(courseCursor);
    }

    private void loadNoteData() {
        Intent intent = getIntent();
        mNoteId = intent.getIntExtra(NOTE_INFO, ID_NOT_SET);
//        SQLiteDatabase database = mSqlOpenHelper.getReadableDatabase();

        String[] columnNotes = {NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_NOTE_TEXT};

        String selection = NoteInfoEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {Integer.toString(mNoteId)};

        mNotesCursor = MainActivity.mDatabase.query(NoteInfoEntry.TABLE_NAME, columnNotes, selection, selectionArgs, null, null, null);

        mCourseIdIndex = mNotesCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitleIndex = mNotesCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextIndex = mNotesCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mNotesCursor.moveToNext();

        displayNote();
    }

    private void displayNote() {
        String courseId = mNotesCursor.getString(mCourseIdIndex);
        String noteTitle = mNotesCursor.getString(mNoteTitleIndex);
        String noteText = mNotesCursor.getString(mNoteTextIndex);

        int courseIdIndex = getCourseIdIndex(courseId);
        mSpinnerCourses.setSelection(courseIdIndex);

        mEditTextNoteTitle.setText(noteTitle);
        mEditTextNoteText.setText(noteText);
    }

    private int getCourseIdIndex(String courseId) {
        Cursor cursor = mAdapterCourses.getCursor();
        int courseIdIndex = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
        int courseRowIndex = 0;

        boolean more = cursor.moveToFirst();
        while(more){
            String cursorCourseId = cursor.getString(courseIdIndex);
            if(cursorCourseId.equals(courseId))
                break;
            courseRowIndex++;
            more = cursor.moveToNext();
        }
        return courseRowIndex;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = null;

        if(i == LOADER_COURSES)
            loader = createLoaderCourses();
        else if(i == LOADER_NOTES)
            loader = createLoaderNotes();

        return loader;
    }

    private CursorLoader createLoaderNotes() {
        mNotesQueryFinished = false;
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                String[] columnNotes = {NoteInfoEntry.COLUMN_COURSE_ID,
                        NoteInfoEntry.COLUMN_NOTE_TITLE,
                        NoteInfoEntry.COLUMN_NOTE_TEXT};

                String selection = NoteInfoEntry.COLUMN_ID + " = ?";
                String[] selectionArgs = {Integer.toString(mNoteId)};

                return MainActivity.mDatabase.query(NoteInfoEntry.TABLE_NAME, columnNotes, selection, selectionArgs, null, null, null);
            }
        };
    }

    private CursorLoader createLoaderCourses() {
        mCoursesQueryFinished = false;
        Uri uri = Courses.CONTENT_URI;
        String[] courseColumns = {Courses.COLUMN_COURSE_TITLE, Courses.COLUMN_COURSE_ID, Courses._ID};

        return new CursorLoader(this, uri, courseColumns, null, null, Courses.COLUMN_COURSE_TITLE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(loader.getId() == LOADER_COURSES){
            mAdapterCourses.changeCursor(cursor);
            mCoursesQueryFinished = true;
            displayNotesWhenQueriesFinished();
        }
        else if(loader.getId() == LOADER_NOTES)
            loadFinishedNotes(cursor);
    }

    private void loadFinishedNotes(Cursor cursor) {
        mNotesCursor = cursor;
        mCourseIdIndex = mNotesCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitleIndex = mNotesCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextIndex = mNotesCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mNotesCursor.moveToNext();
        mNotesQueryFinished = true;
        displayNotesWhenQueriesFinished();
    }

    private void displayNotesWhenQueriesFinished() {
        if(mNotesQueryFinished && mCoursesQueryFinished)
            displayNote();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == LOADER_COURSES)
            mAdapterCourses.changeCursor(null);
        else if(loader.getId() == LOADER_NOTES){
            if(mNotesCursor != null)
                mNotesCursor.close();
        }
    }
}