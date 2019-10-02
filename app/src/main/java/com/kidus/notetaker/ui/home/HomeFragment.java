package com.kidus.notetaker.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kidus.notetaker.R;
import com.kidus.notetaker.activities.MainActivity;
import com.kidus.notetaker.adapters.NoteListRecyclerAdapter;
import com.kidus.notetaker.database.NoteTakerDataWorker;
import com.kidus.notetaker.database.NoteTakerDatabaseContract;
import com.kidus.notetaker.database.NoteTakerDatabaseContract.NoteInfoEntry;
import com.kidus.notetaker.database.NoteTakerSQLOpenHelper;

import java.util.List;

public class HomeFragment extends Fragment{

    public static final int LOADER_NOTES = 0;
    private NoteTakerSQLOpenHelper mSqlOpenHelper;

    private HomeViewModel homeViewModel;
    private View mRoot;
    private NoteListRecyclerAdapter mNoteListRecyclerAdapter;
    private Cursor mNoteCursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);

        mSqlOpenHelper = MainActivity.mSQLOpenHelper;

        final RecyclerView recyclerNotes = mRoot.findViewById(R.id.rv_note_list);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(mRoot.getContext());
        recyclerNotes.setLayoutManager(notesLayoutManager);
        mNoteListRecyclerAdapter = new NoteListRecyclerAdapter(mRoot.getContext(), null);
        recyclerNotes.setAdapter(mNoteListRecyclerAdapter);

        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        String noteOrderBy = NoteInfoEntry.COLUMN_COURSE_ID + "," + NoteInfoEntry.COLUMN_NOTE_TITLE;
        String[] noteColumns = {NoteInfoEntry.COLUMN_NOTE_TITLE, NoteInfoEntry.COLUMN_COURSE_ID, NoteInfoEntry.COLUMN_ID};
        Cursor noteCursor = MainActivity.mDatabase.query(NoteInfoEntry.TABLE_NAME, noteColumns, null, null, null, null, noteOrderBy);
        mNoteListRecyclerAdapter.changeCursor(noteCursor);
    }
}