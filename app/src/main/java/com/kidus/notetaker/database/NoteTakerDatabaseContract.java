package com.kidus.notetaker.database;

import android.provider.BaseColumns;

public final class NoteTakerDatabaseContract {
    private NoteTakerDatabaseContract(){}

    public static final class CourseInfoEntry implements BaseColumns {
        //CREATE TABLE course_info(_id INTEGER PRIMARY KEY, course_id UNIQUE TEXT NOT NULL, course_title TEXT NOT NULL)
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_COURSE_TITLE = "course_title";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_COURSE_ID + " TEXT UNIQUE NOT NULL, " +
                        COLUMN_COURSE_TITLE + " TEXT NOT NULL)";
    }

    public static final class NoteInfoEntry implements BaseColumns {
        //CREATE TABLE note_info(_id INTEGER PRIMARY KEY, note_title TEXT NOT NULL, note_text TEXT, course_id TEXT NOT NULL)
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String TABLE_NAME  = "note_info";
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";
        public static final String COLUMN_COURSE_ID = "course_id";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
                        COLUMN_NOTE_TEXT + " TEXT, " +
                        COLUMN_COURSE_ID + " TEXT NOT NULL)";
    }
}