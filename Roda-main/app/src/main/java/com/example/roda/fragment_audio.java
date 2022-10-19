package com.example.roda;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;

public class fragment_audio extends Fragment implements AudioListAdapter.onItemListClick {

    private ConstraintLayout playerView;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView audioList;
    private File[] files;
    private AudioListAdapter audioListAdapter;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    private File filePlay;

    private ImageButton playbtn;
    private TextView playerFileName;
    private TextView PlayerHeader;
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;



    public fragment_audio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerView = view.findViewById(R.id.player_view);
        bottomSheetBehavior = BottomSheetBehavior.from(playerView);
        audioList = view.findViewById(R.id.fragment_audio_view);
        playbtn = view.findViewById(R.id.playerView_play);
        playerFileName = view.findViewById(R.id.playerView_filename);
        PlayerHeader = view.findViewById(R.id.playerView_playerTitle);
        playerSeekbar = view.findViewById(R.id.player_view_seekbar);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File document = new File(path);
        files = document.listFiles();

        audioListAdapter = new AudioListAdapter(files, this);
        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    pauseAudio();
                }else{
                    if(filePlay != null){
                        resumeAudio();
                    }
                }
            }
        });

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });


        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == bottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    public void onClickListener(File file, int position) {
        filePlay = file;
        if(isPlaying){
             stopAudio();
             playAudio(filePlay);
        }else{
            playAudio(filePlay);
        }
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.outline_play_circle_outline_black_24dp));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void resumeAudio(){
        mediaPlayer.start();
        playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.outline_pause_circle_outline_black_24dp));
        isPlaying = true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void stopAudio() {
        isPlaying = false;
        PlayerHeader.setText("Stop");
        playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.outline_play_circle_outline_black_24dp));
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void playAudio(File filePlay) {
        mediaPlayer = new MediaPlayer();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            mediaPlayer.setDataSource(filePlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (IOException e){
            e.printStackTrace();
        }

        playbtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.outline_pause_circle_outline_black_24dp));
        playerFileName.setText(filePlay.getName());
        PlayerHeader.setText("Playing");

        isPlaying = true;

        mediaPlayer.setOnCompletionListener((mp) -> {
                stopAudio();
                PlayerHeader.setText("Finish");
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());

        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAudio();
    }
}