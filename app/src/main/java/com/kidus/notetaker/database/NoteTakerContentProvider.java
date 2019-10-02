package com.kidus.notetaker.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NoteTakerContentProvider extends ContentProvider {

    private NoteTakerSQLOpenHelper mSqlOpenHelper;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int COURSES = 0;

    public static final int NOTES = 1;

    public static final int NOTES_EXPANDED = 2;

    static{
        sUriMatcher.addURI(NoteTakerContentProviderContract.AUTHORITY, NoteTakerContentProviderContract.Courses.PATH, COURSES);
        sUriMatcher.addURI(NoteTakerContentProviderContract.AUTHORITY, NoteTakerContentProviderContract.Notes.PATH, NOTES);
        sUriMatcher.addURI(NoteTakerContentProviderContract.AUTHORITY, NoteTakerContentProviderContract.Notes.PATH_EXPANDED, NOTES_EXPANDED);
    }

    public NoteTakerContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        mSqlOpenHelper = new NoteTakerSQLOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        SQLiteDatabase database = mSqlOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);

        switch (uriMatch){
            case COURSES:
                cursor = database.query(NoteTakerDatabaseContract.CourseInfoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTES:
                cursor = database.query(NoteTakerDatabaseContract.NoteInfoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTES_EXPANDED:

        }

        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
