package com.example.thien_record.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.thien_record.R;
import com.example.thien_record.RecordingItems;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaybackFragment extends DialogFragment {


    @BindView(R.id.file_name_text_view)
    TextView filenametxtview;
    @BindView(R.id.file_length_text_view) TextView fileLenthtxtView;
    @BindView(R.id.current_progress_text_view) TextView filecurrent;
    @BindView(R.id.seekbar)
    SeekBar seekBar;
    @BindView(R.id.fab_play)
    FloatingActionButton floatingActionButton;

    private RecordingItems items;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private boolean isplaying = false;

    long minutes = 0;
    long second = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        minutes = TimeUnit.MILLISECONDS.toMinutes(items.getmLength());
        second = TimeUnit.MILLISECONDS.toSeconds(items.getmLength() - TimeUnit.MINUTES.toSeconds(minutes));

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_media_playback, null);
        ButterKnife.bind(this,view);

        setSeekBarValue();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onPlay(isplaying);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isplaying = !isplaying;
            }
        });

        filenametxtview.setText(items.getmName());
        fileLenthtxtView.setText(String.format("%02d:%02d" ,minutes,second ));

        builder.setView(view);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return  builder.create();
    }

    private void onPlay(boolean isplaying) throws IOException {

        if(isplaying){
            if(mediaPlayer == null){
                startPlaying();
            }
            else {
                pausePlaying();
            }
        }

    }

    private void pausePlaying() throws IOException {

        floatingActionButton.setImageResource(R.drawable.ic_media_play);
        handler.removeCallbacks(mRunable);
        mediaPlayer.pause();

    }

    private void startPlaying() throws IOException {


        floatingActionButton.setImageResource(R.drawable.ic_media_pause);
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(items.getmFilePath());
        mediaPlayer.prepare();
        seekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
            }
        });

        updateSeekBar();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void setSeekBarValue(){
        ColorFilter colorFilter = new LightingColorFilter(getResources().getColor(R.color.red_error), getResources().getColor(R.color.red_error));
        seekBar.getProgressDrawable().setColorFilter(colorFilter);
        seekBar.getThumb().setColorFilter(colorFilter);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i);
                    handler.removeCallbacks(mRunable);

                    long minute = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long second = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition() - TimeUnit.MINUTES.toSeconds(minute));

                    filecurrent.setText(String.format("%02d:%02d", minute, second));

                    updateSeekBar();
                }
                else if(mediaPlayer == null && b){
                    try {
                        prepareMediaPlayer(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateSeekBar();

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer != null){
                int mcurrentposition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mcurrentposition);

                long minute = TimeUnit.MILLISECONDS.toMinutes(mcurrentposition);
                long second = TimeUnit.MILLISECONDS.toSeconds(mcurrentposition - TimeUnit.MINUTES.toSeconds(minute));

                filecurrent.setText(String.format("%02d:%02d", minute, second));

                updateSeekBar();

            }
        }
    };

    private void prepareMediaPlayer(int progress) throws IOException {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(items.getmFilePath());
        mediaPlayer.prepare();
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.seekTo(progress);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
            }
        });
    }

    private void stopPlaying() {

        floatingActionButton.setImageResource(R.drawable.ic_media_play);
        handler.removeCallbacks(mRunable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();

        mediaPlayer = null;

        seekBar.setProgress(seekBar.getMax());
        isplaying = !isplaying;

        filecurrent.setText(fileLenthtxtView.getText());
        seekBar.setProgress(seekBar.getMax());

    }

    private void  updateSeekBar(){
        handler.postDelayed(mRunable , 1000);
    }

}
