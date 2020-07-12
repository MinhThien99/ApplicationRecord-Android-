package com.example.thien_record.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.thien_record.Database;
import com.example.thien_record.Fragment.PlaybackFragment;
import com.example.thien_record.R;
import com.example.thien_record.RecordingItems;
import com.example.thien_record.listener.OnDatabaseChangedListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FileViewerAdapter extends RecyclerView.Adapter<FileViewerAdapter.RecordingsViewHolder> implements OnDatabaseChangedListener {

    private static final String LOG_TAG = "FileViewerAdapter";

    public static Context context;
    LinearLayoutManager llm;
    public static ArrayList<RecordingItems> arrayList;

    private Database database;

    public FileViewerAdapter(Context context, ArrayList<RecordingItems> arrayList , LinearLayoutManager llm){
        this.context = context;
        this.llm = llm;
        this.arrayList = arrayList;

        database.setOnDatabaseChangedListener(this);

    }

    @NonNull
    @Override
    public RecordingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view, parent, false);

        return new RecordingsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordingsViewHolder holder, int position) {
        RecordingItems recordingItem = arrayList.get(position);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(recordingItem.getmLength());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(recordingItem.getmLength() - TimeUnit.MINUTES.toSeconds(minutes));

        holder.vName.setText(recordingItem.getmName());
        holder.vLength.setText(String.format("%02d:%02d" , minutes, seconds));
        holder.vDate.setText(DateUtils.formatDateTime(context , recordingItem.getmTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onNewDatabaseEntryAdd(RecordingItems recordingItems) {
        arrayList.add(recordingItems);
        notifyItemInserted(arrayList.size() - 1);
    }


    public static class RecordingsViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vLength;
        TextView vDate;
        View careView;

        public RecordingsViewHolder(@NonNull View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.file_name_text);
            vLength = (TextView) v.findViewById(R.id.file_length_text);
            vDate = (TextView) v.findViewById(R.id.file_date_added_text);
            careView = v.findViewById(R.id.card_view);

            careView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlaybackFragment playbackFragment = new PlaybackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("items" , arrayList.get(getAdapterPosition()));
                    playbackFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();

                    playbackFragment.show(fragmentTransaction, "Dialog Playback");
                }
            });

        }
    }


}
