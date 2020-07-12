package com.example.thien_record;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class RecordingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final String LOG_TAG = "RecordingService";

    private String mFileName = null;
    private String mFilePath = null;
    private File file;
    private MediaRecorder mRecorder = null;

    private Database database;

    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        database = new Database(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if(mRecorder == null){
            stopRecording();
        }

        super.onDestroy();
    }

    private void stopRecording() {

        mRecorder.stop();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
        Toast.makeText(getApplicationContext(), "Recording Save " + mFileName, Toast.LENGTH_SHORT).show();

        // add database

        RecordingItems recordingItems = new RecordingItems(mFileName, file.getAbsolutePath(), mElapsedMillis, System.currentTimeMillis());
        database.addRecording(recordingItems);

    }



    private void startRecording() {

        setFileNameAndPath();

       mRecorder = new MediaRecorder();
       mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
       mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
       mRecorder.setOutputFile(mFilePath);
       mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
       mRecorder.setAudioChannels(1);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Prepare() fault");
        }

    }

    public void setFileNameAndPath(){
        int count = 0;
        File f;

        do{
            count++;

            mFileName = getString(R.string.default_file_name)
                    + "_" + (database.getCount() + count) + ".mp3";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/SoundRecorder/" + mFileName;

            f = new File(mFilePath);
        }while (f.exists() && !f.isDirectory());
    }


}
