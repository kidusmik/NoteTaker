package com.kidus.notetaker.ui.home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kidus.notetaker.R;
import com.kidus.notetaker.activities.MainActivity;
import com.kidus.notetaker.adapters.NoteListRecyclerAdapter;
import com.kidus.notetaker.database.DataManager;
import com.kidus.notetaker.database.NoteInfo;
import com.kidus.notetaker.database.NoteTakerDataWorker;
import com.kidus.notetaker.database.NoteTakerSQLOpenHelper;

import java.util.List;

public class HomeFragment extends Fragment {

    private NoteTakerSQLOpenHelper mSqlOpenHelper;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mSqlOpenHelper = MainActivity.mSQLOpenHelper;

        displayNotes();

        final RecyclerView recyclerNotes = root.findViewById(R.id.rv_note_list);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerNotes.setLayoutManager(notesLayoutManager);

        loadNotesFromDatabase();


        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        final NoteListRecyclerAdapter noteListRecyclerAdapter = new NoteListRecyclerAdapter(root.getContext(), notes);
        recyclerNotes.setAdapter(noteListRecyclerAdapter);

        return root;
    }

    private void loadNotesFromDatabase() {

    }

    private void displayNotes() {
        NoteTakerDataWorker.loadFromDatabase(mSqlOpenHelper);
    }
}