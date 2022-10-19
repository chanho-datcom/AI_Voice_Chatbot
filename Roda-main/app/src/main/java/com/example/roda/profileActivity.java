package com.example.roda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.roda.model.userModels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class profileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private static final int PICK_FROM_ALBUM = 10;
    private ImageView imageView;
    private Uri imageUri;
    private Button button_complete;
    private TextInputLayout name;
    private TextInputLayout message;
    private BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.Profile_toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getBackground().setAlpha(0);
        name = (TextInputLayout)findViewById(R.id.Profile_NameField);
        message = (TextInputLayout)findViewById(R.id.Profile_stateField);
        button_complete = (Button)findViewById(R.id.Profile_complete);

        imageView = (ImageView)findViewById(R.id.Profile_image);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.Profile_bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModels current_userModel = new userModels();
                current_userModel.User_Id  = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                current_userModel.User = name.getEditText().getText().toString();
                current_userModel.User_Message = message.getEditText().getText().toString();






                if (FirebaseAuth.getInstance().getCurrentUser()!= null) {

                } else {
                    Toast.makeText(profileActivity.this, "로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                }

                System.out.println("사용자 아이디"+FirebaseAuth.getInstance().getCurrentUser().getEmail());

                FirebaseStorage.getInstance().getReference().child((FirebaseAuth.getInstance().getCurrentUser().getEmail()).substring(0,(FirebaseAuth.getInstance().getCurrentUser().getEmail()).indexOf("@"))).child(current_userModel.User).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        StorageReference storageReference =  FirebaseStorage.getInstance().getReferenceFromUrl("gs://roda-2c89e.appspot.com/" + (current_userModel.User_Id).substring(0,current_userModel.User_Id.indexOf("@")));

                        String imageUrl =storageReference.child(current_userModel.User).toString();

                        System.out.println(imageUrl);
                        current_userModel.profileImageUrl = imageUrl;
                        FirebaseDatabase.getInstance().getReference().child(((current_userModel.User_Id).substring(0,(FirebaseAuth.getInstance().getCurrentUser().getEmail()).indexOf("@")))).child(current_userModel.User).setValue(current_userModel);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();


                        Toast.makeText(profileActivity.this, "프로필 등록 성공", Toast.LENGTH_SHORT).show();;
                    }
                });


            }
        });

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == RESULT_OK) {
                try {
                    imageView.setImageURI(data.getData());// image view바꿈
                    imageUri = data.getData();//이미지 원본 경로

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
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