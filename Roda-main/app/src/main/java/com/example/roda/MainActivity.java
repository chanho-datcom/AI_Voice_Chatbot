package com.example.roda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button profile_button;
    private Button chat_button;
    private Button logout_button;
    private Button record_button;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String str = "Roda.";
        TextView tv = (TextView)findViewById(R.id.logo);
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),4,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb);

        chat_button = (Button)findViewById(R.id.mainActivity_chat);
        logout_button = (Button)findViewById(R.id.mainActivity_logout);
        record_button = (Button)findViewById(R.id.mainActivity_voiceRecode);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.mainActivity_bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), recordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), chatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), newMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_account :
                    Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case  R.id.action_chat:
                    Intent intent1 = new Intent(getApplicationContext(), chatActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                case R.id.action_home:
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent2);
                    finish();
            }
            return false;
        }
    }
}