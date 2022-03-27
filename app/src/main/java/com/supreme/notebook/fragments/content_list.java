package com.supreme.notebook.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.supreme.notebook.R;
import com.supreme.notebook.customClasses.CustomAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class content_list extends Fragment {

    public content_list() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_list, container, false);
        File notesLocation = new File(Environment.getExternalStorageDirectory() + "/noteBook/notes");
        String[] files = notesLocation.list();
        List list = new ArrayList<>();
        if (files != null) {
            for (String i : files) {
                if (i.endsWith(".html")) {
                    list.add(i);
                }
            }
        }
        ListView item_list = view.findViewById(R.id.list_item);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), files);
        item_list.setAdapter(customAdapter);
        return view;
    }
}