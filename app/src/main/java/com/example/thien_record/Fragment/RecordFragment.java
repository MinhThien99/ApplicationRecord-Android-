package com.example.thien_record.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thien_record.R;
import com.example.thien_record.RecordingService;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;


public class RecordFragment extends Fragment {


        Chronometer chronometer;
        TextView recordinhstatustxt;
        FloatingActionButton recordbtn;
        Button btnpause;
        private int mRecordPromptCount = 0;

        private boolean mStartRecording = true;
        private boolean mPauseRecording = true;
        long timeWhenPaused = 0;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View recordView = inflater.inflate(R.layout.fragment_record, container, false);

                chronometer = recordView.findViewById(R.id.chronometer);
                recordinhstatustxt = recordView.findViewById(R.id.recording_status_text);
                recordbtn = recordView.findViewById(R.id.btnRecord);
                btnpause = recordView.findViewById(R.id.btnPause);
                recordbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                onRecord(mStartRecording);
                                mStartRecording = !mStartRecording;
                        }
                });

                btnpause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                onPauseRecord(mPauseRecording);
                                mPauseRecording = !mPauseRecording;
                        }
                });

                return recordView;
        }

        private void onPauseRecord(boolean mPauseRecording) {

                if (mPauseRecording) {
                        //pause recording
                        btnpause.setCompoundDrawablesWithIntrinsicBounds
                                (R.drawable.ic_media_play ,0 ,0 ,0);
                        recordinhstatustxt.setText((String)getString(R.string.resume_recording_button).toUpperCase());
                        timeWhenPaused = chronometer.getBase() - SystemClock.elapsedRealtime();
                        chronometer.stop();
                } else {
                        //resume recording
                        btnpause.setCompoundDrawablesWithIntrinsicBounds
                                (R.drawable.ic_media_pause ,0 ,0 ,0);
                        recordinhstatustxt.setText((String)getString(R.string.pause_recording_button).toUpperCase());
                        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
                        chronometer.start();
                }

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                btnpause.setVisibility(View.GONE);
                recordbtn.setColorPressed(getResources().getColor(R.color.colorPrimary));


        }


        private void onRecord(boolean mStart) {

                Intent intent = new Intent(getActivity(), RecordingService.class);

                if (mStart) {
                        btnpause.setVisibility(View.VISIBLE);
                        // start recording
                        recordbtn.setImageResource(R.drawable.ic_media_stop);
                        //mPauseButton.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(),R.string.toast_recording_start,Toast.LENGTH_SHORT).show();
                        File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
                        if (!folder.exists()) {
                                //folder /SoundRecorder doesn't exist, create the folder
                                folder.mkdir();
                        }

                        //start Chronometer
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                                @Override
                                public void onChronometerTick(Chronometer chronometer) {
                                        if (mRecordPromptCount == 0) {
                                                recordinhstatustxt.setText(getString(R.string.record_in_progress) + ".");
                                        } else if (mRecordPromptCount == 1) {
                                                recordinhstatustxt.setText(getString(R.string.record_in_progress) + "..");
                                        } else if (mRecordPromptCount == 2) {
                                                recordinhstatustxt.setText(getString(R.string.record_in_progress) + "...");
                                                mRecordPromptCount = -1;
                                        }

                                        mRecordPromptCount++;
                                }
                        });

                        //start RecordingService
                        getActivity().startService(intent);
                        //keep screen on while recording
                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                        recordinhstatustxt.setText(getString(R.string.record_in_progress) + ".");
                        mRecordPromptCount++;

                } else {
                        btnpause.setVisibility(View.GONE);
                        //stop recording
                        recordbtn.setImageResource(R.drawable.ic_mic_white_36dp);
                        //mPauseButton.setVisibility(View.GONE);
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        timeWhenPaused = 0;
                        recordinhstatustxt.setText(getString(R.string.record_prompt));

                        getActivity().stopService(intent);
                        //allow the screen to turn off again once recording is finished
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
        }


}












