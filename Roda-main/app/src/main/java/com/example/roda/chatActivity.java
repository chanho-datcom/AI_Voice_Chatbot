package com.example.roda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.roda.fragment.peopleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class chatActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.chatActvity_bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        getSupportFragmentManager().beginTransaction().replace(R.id.chatActvity_view, new peopleFragment()).commit();
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