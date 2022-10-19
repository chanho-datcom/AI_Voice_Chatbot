//package com.example.roda;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.ShapeDrawable;
//import android.graphics.drawable.shapes.OvalShape;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.io.InputStream;
//
//public class profileActivity_test extends AppCompatActivity {
//
//    private static final int REQUEST_CODE = 0;
//    private static final int PICK_FROM_ALBUM = 10;
//    private ImageView imageView;
//    private Uri imageUri;
//    private FloatingActionButton imageLoad;
//    private BottomNavigationView bottomNavigationView;
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.Profile_toolbar);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.getBackground().setAlpha(0);
//
//        imageView = (ImageView)findViewById(R.id.Profile_image);
//        imageView.setBackground(new ShapeDrawable(new OvalShape()));
//        imageView.setClipToOutline(true);
//
//        bottomNavigationView = (BottomNavigationView)findViewById(R.id.Profile_bottomNavigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
//
////        imageView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(Intent.ACTION_PICK);
////                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
////                startActivityForResult(intent, PICK_FROM_ALBUM);
////            }
////        });
//
//        imageLoad = findViewById(R.id.Profile_image_load);
//        imageLoad.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, REQUEST_CODE);
//            }
//        });
//
//
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
//    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if(requestCode == PICK_FROM_ALBUM && REQUEST_CODE == RESULT_OK){
////            imageView.setImageURI(data.getData());
////            imageUri = data.getData();
////        }else if (resultCode == RESULT_CANCELED) {
////            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
////    }
//
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                try {
//
////                    imageView.setImageURI(data.getData());
////                    imageUri = data.getData();
//
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//
//                    imageView.setImageBitmap(img);
//                } catch (Exception e) {
//
//                }
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()){
//                case R.id.action_account :
//                    Intent intent = new Intent(getApplicationContext(), profileActivity_test.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                case  R.id.action_chat:
//                    break;
//                case R.id.action_home:
//                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent2);
//                    finish();
//            }
//            return false;
//        }
//    }
//
//
//}