package com.example.roda;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class JoinActivity extends AppCompatActivity {

    private static final int PICK_FORM_ALBUM = 10;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button join;
    private ImageView profileImage;
    private Toolbar toolbar;

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), newMainActivity.class);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        email = (TextInputLayout)findViewById(R.id.joinAtivity_id);
        password = (TextInputLayout)findViewById(R.id.joinAtivity_password);
        join = (Button)findViewById(R.id.joinAtivity_Sign_in);

        toolbar = (Toolbar) findViewById(R.id.joinAtivitye_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getBackground().setAlpha(0);






        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = email.getEditText().getText().toString();
                String pw = password.getEditText().getText().toString();

                if (id == null || pw == null ){
                    return;
                }else{
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(id,pw)
                            .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(JoinActivity.this, "계정 생성에 성공하였습니다. 로그인하세요", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), newMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), newMainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


}