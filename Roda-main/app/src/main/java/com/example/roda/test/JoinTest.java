//package com.example.roda;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.roda.model.userModel;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.UploadTask;
//
//public class JoinTest extends AppCompatActivity {
//
//    private static final int PICK_FORM_ALBUM = 10;
//    private TextInputLayout email;
//    private TextInputLayout password;
//    private Button join;
//    private ImageView profileImage;
//    private Uri imageUri;
//
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), newMainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_join);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//
//        email = (TextInputLayout)findViewById(R.id.joinAtivity_id);
//        password = (TextInputLayout)findViewById(R.id.joinAtivity_password);
//        join = (Button)findViewById(R.id.joinAtivity_next);
//        profileImage = (ImageView)findViewById(R.id.joinAtivity_Profile_image);
//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                startActivityForResult(intent,PICK_FORM_ALBUM);
//            }
//        });
//
//
//        join.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String id = email.getEditText().getText().toString();
//                String pw = password.getEditText().getText().toString();
//                String userName = name.getEditText().getText().toString();
//
//                if (id == null || pw == null || userName == null){
//                    return;
//                }else{
//                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(id,pw)
//                            .addOnCompleteListener(JoinTest.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//
//                                    String uid = task.getResult().getUser().getUid();
//
//                                    FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                            String imageUrl = task.getResult().getUploadSessionUri().toString();
//                                            userModel usermodel = new userModel();
//                                            usermodel.User = userName;
//                                            usermodel.profileImageUrl = imageUrl;
//                                            FirebaseDatabase.getInstance().getReference().child("user").child(uid).setValue(usermodel);
//                                        }
//                                    });
//
//                                }
//                            });
//                }
//            }
//        });
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_FORM_ALBUM && resultCode == RESULT_OK) {
//            profileImage.setImageURI(data.getData());
//            imageUri = data.getData();
//        }
//    }
//
//}