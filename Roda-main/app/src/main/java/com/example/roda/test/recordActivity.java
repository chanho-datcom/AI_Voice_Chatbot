//package com.example.roda.test;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.style.ForegroundColorSpan;
//import android.view.MenuItem;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.example.roda.MainActivity;
//import com.example.roda.R;
//import com.example.roda.profileActivity;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class recordActivity extends AppCompatActivity {
//    private BottomNavigationView bottomNavigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_record);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        String str = "Roda.";
//        TextView tv = (TextView)findViewById(R.id.recordActivity_logo);
//        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
//        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),4,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv.setText(ssb);
//
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.recordAtivity_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.getBackground().setAlpha(0);
//
//        bottomNavigationView = (BottomNavigationView)findViewById(R.id.recordAtivity_bottomNavigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new  ItemSelectedListener());
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case android.R.id.home:
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()){
//                case R.id.action_account :
//                    Intent intent = new Intent(getApplicationContext(), profileActivity.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                case  R.id.action_record_list:
//                    Intent intent3 = new Intent(getApplicationContext(), recordActivity.class);
//                    startActivity(intent3);
//                    break;
//                case R.id.action_home:
//                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent2);
//                    finish();
//            }
//            return false;
//        }
//    }
//}