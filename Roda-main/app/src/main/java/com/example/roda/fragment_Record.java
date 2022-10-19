package com.example.roda;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class fragment_Record extends Fragment {
    private NavController navController;
    private ImageButton list_btn;
    private ImageButton record_btn;
    private Chronometer chronometer;
    private TextView textView;

    private MediaRecorder mediaRecorder;

    private boolean is_recoding = false;
    private int PERMISSON_CODE = 21;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private String recordFile;


    public fragment_Record() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        TextView tv = (TextView)v.findViewById(R.id.fragment_record_logo);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)v.findViewById(R.id.fragment_record_bottomNavigation);

        String str = "Roda.";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),4,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb);

        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        list_btn = view.findViewById(R.id.fragment_record_list_btn);
        record_btn = view.findViewById(R.id.fragment_record_btn);
        chronometer = view.findViewById(R.id.fragment_record_timer);
        textView = view.findViewById(R.id.fragment_record_text);

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_recoding){
                    stop_Recording();
                    record_btn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped));
                    is_recoding = false;
                }else {

                    if(is_Permission_Check()){
                        start_Recording();
                        record_btn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording));
                        is_recoding = true;
                    }
                }
            }
        });

        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_recoding){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            navController.navigate(R.id.action_fragment_Record_to_fragment_audio);
                            is_recoding = false;
                        }
                    });
                    alertDialog.setNegativeButton("Cancle", null);
                    alertDialog.setTitle("Audio Still record");
                    alertDialog.setMessage("Are you sure, you want to stop the recording?");
                    alertDialog.create().show();

                }else {
                    navController.navigate(R.id.action_fragment_Record_to_fragment_audio);
                }
            }
        });

    }

    private void stop_Recording() {
        chronometer.stop();

        textView.setText("Press the mic button\n to start recording");

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void start_Recording() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();

        recordFile="음성파일_" +simpleDateFormat.format(now)+".mp3";
        textView.setText("Recording start, File name : " + recordFile);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private boolean is_Permission_Check() {

        if(ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSON_CODE);
            return false;
        }

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_account :
                    Intent intent = new Intent(getActivity(), profileActivity.class);
                    startActivity(intent);
                    break;
                case  R.id.action_chat:
                    break;
                case R.id.action_home:
                    Intent intent2 = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent2);
            }
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(is_recoding){
            stop_Recording();
        }
    }
}