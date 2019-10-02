package com.kidus.notetaker.ui.gallery;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kidus.notetaker.R;
import com.kidus.notetaker.activities.MainActivity;
import com.kidus.notetaker.adapters.CourseListRecyclerAdapter;
import com.kidus.notetaker.adapters.NoteListRecyclerAdapter;
import com.kidus.notetaker.database.NoteTakerDatabaseContract;
import com.kidus.notetaker.database.NoteTakerDatabaseContract.CourseInfoEntry;

import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        final RecyclerView recyclerCourses = root.findViewById(R.id.rv_course_list);
        final GridLayoutManager coursesLayoutManager = new GridLayoutManager(root.getContext(), 2);
        recyclerCourses.setLayoutManager(coursesLayoutManager);

        String[] courseColumns = {CourseInfoEntry.COLUMN_COURSE_TITLE, CourseInfoEntry.COLUMN_COURSE_ID};
        Cursor courseCursor = MainActivity.mDatabase.query(CourseInfoEntry.TABLE_NAME, courseColumns, null, null, null, null, CourseInfoEntry.COLUMN_COURSE_TITLE);

        final CourseListRecyclerAdapter courseListRecyclerAdapter = new CourseListRecyclerAdapter(root.getContext(), courseCursor);
        recyclerCourses.setAdapter(courseListRecyclerAdapter);

        return root;
    }
}