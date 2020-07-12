package com.example.thien_record.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thien_record.Adapter.FileViewerAdapter;
import com.example.thien_record.Database;
import com.example.thien_record.R;
import com.example.thien_record.RecordingItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FileViewerFragment extends Fragment {

    RecyclerView recyclerView;
    Database database;

    ArrayList<RecordingItems> arrayListitem;

    private FileViewerAdapter fileViewerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_viewer, container, false);
        recyclerView = view.findViewById(R.id.rcvview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = new Database(getContext());

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        arrayListitem = database.getAllAudio();
        if(arrayListitem == null){
            Toast.makeText(getContext(), "No audio files " , Toast.LENGTH_SHORT).show();
        }
        else {
            fileViewerAdapter = new FileViewerAdapter(getActivity(), arrayListitem, llm);
            recyclerView.setAdapter(fileViewerAdapter);

        }

    }
}










