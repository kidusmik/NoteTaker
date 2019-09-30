package com.kidus.notetaker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kidus.notetaker.database.NoteTakerDatabaseContract.CourseInfoEntry;
import com.kidus.notetaker.database.NoteTakerDatabaseContract.NoteInfoEntry;

public class NoteTakerDataWorker {
    private SQLiteDatabase mDb;

    public NoteTakerDataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    public static void loadFromDatabase(NoteTakerSQLOpenHelper sqlOpenHelper){
        SQLiteDatabase database = sqlOpenHelper.getReadableDatabase();

        String[] courseColumns = {CourseInfoEntry.COLUMN_COURSE_ID, CourseInfoEntry.COLUMN_COURSE_TITLE};
        String[] noteColumns = {NoteInfoEntry.COLUMN_NOTE_TITLE, NoteInfoEntry.COLUMN_NOTE_TEXT, NoteInfoEntry.COLUMN_COURSE_ID};

        Cursor courseCursor = database.query(CourseInfoEntry.TABLE_NAME, courseColumns, null, null, null, null, null);
        loadCoursesFromDatabase(courseCursor);

        Cursor noteCursor = database.query(NoteInfoEntry.TABLE_NAME, noteColumns, null, null, null, null, null);
        loadNotesFromDatabase(noteCursor);
    }

    private static void loadCoursesFromDatabase(Cursor cursor) {
        int courseIdIndex = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
        int courseTitleIndex = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);

        DataManager dm = DataManager.getInstance();
        dm.mCourses.clear();
        while(cursor.moveToNext()){
            String courseId = cursor.getString(courseIdIndex);
            String courseTitle = cursor.getString(courseTitleIndex);

            CourseInfo courses = new CourseInfo(courseId, courseTitle, null);
            dm.mCourses.add(courses);
        }
        cursor.close();
    }

    private static void loadNotesFromDatabase(Cursor cursor) {
        int courseIdIndex = cursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        int noteTitleIndex = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        int noteTextIndex = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);

        DataManager dm = DataManager.getInstance();
        dm.mNotes.clear();
        while(cursor.moveToNext()){
            String courseId = cursor.getString(courseIdIndex);
            String courseTitle = cursor.getString(noteTitleIndex);
            String courseText = cursor.getString(noteTextIndex);

            NoteInfo notes = new NoteInfo(null, courseTitle, courseText);

            dm.mNotes.add(notes);
        }
        cursor.close();
    }


    public void insertCourses() {
        insertCourse("android_intents", "Android Programming with Intents");
        insertCourse("android_async", "Android Async Programming and Services");
        insertCourse("java_lang", "Java Fundamentals: The Java Language");
        insertCourse("java_core", "Java Fundamentals: The Core Platform");
    }

    public void insertSampleNotes() {
        insertNote("android_intents", "Dynamic intent resolution", "Wow, intents allow components to be resolved at runtime");
        insertNote("android_intents", "Delegating intents", "PendingIntents are powerful; they delegate much more than just a component invocation");

        insertNote("android_async", "Service default threads", "Did you know that by default an Android Service will tie up the UI thread?");
        insertNote("android_async", "Long running operations", "Foreground Services can be tied to a notification icon");

        insertNote("java_lang", "Parameters", "Leverage variable-length parameter lists?");
        insertNote("java_lang", "Anonymous classes", "Anonymous classes simplify implementing one-use types");

        insertNote("java_core", "Compiler options", "The -jar option isn't compatible with with the -cp option");
        insertNote("java_core", "Serialization", "Remember to include SerialVersionUID to assure version compatibility");
    }

    private void insertCourse(String courseId, String title) {
        ContentValues values = new ContentValues();
        values.put(CourseInfoEntry.COLUMN_COURSE_ID, courseId);
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, title);

        long newRowId = mDb.insert(CourseInfoEntry.TABLE_NAME, null, values);
    }

    private void insertNote(String courseId, String title, String text) {
        ContentValues values = new ContentValues();
        values.put(NoteInfoEntry.COLUMN_COURSE_ID, courseId);
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE, title);
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT, text);

        long newRowId = mDb.insert(NoteInfoEntry.TABLE_NAME, null, values);
    }

}