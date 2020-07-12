package com.example.thien_record.listener;

import com.example.thien_record.RecordingItems;

public interface OnDatabaseChangedListener {

    void onNewDatabaseEntryAdd(RecordingItems recordingItems);

}
