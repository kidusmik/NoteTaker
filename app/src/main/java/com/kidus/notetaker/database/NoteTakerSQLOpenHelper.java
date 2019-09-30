package com.kidus.notetaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

public class NoteTakerSQLOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NoteTaker.db";
    public static final int DATABASE_VERSION = 1;
    public NoteTakerSQLOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NoteTakerDatabaseContract.CourseInfoEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(NoteTakerDatabaseContract.NoteInfoEntry.SQL_CREATE_TABLE);

        NoteTakerDataWorker DataWorker = new NoteTakerDataWorker(sqLiteDatabase);
        DataWorker.insertCourses();
        DataWorker.insertSampleNotes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
