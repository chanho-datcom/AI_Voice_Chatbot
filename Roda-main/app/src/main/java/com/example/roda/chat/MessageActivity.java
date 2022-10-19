package com.example.roda.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roda.R;
import com.example.roda.model.botModels;
import com.example.roda.model.chatModels;
import com.example.roda.model.userModels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MessageActivity extends AppCompatActivity {

    private String destinationUid;
    private Button button;
    private FloatingActionButton micButton;
    private EditText editText;



    private String uid;
    private String chatRoomUid;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message4);
        uid = FirebaseAuth.getInstance().getUid();
        destinationUid = getIntent().getStringExtra("destination");
        button = (Button)findViewById(R.id.messageActivity_Button);
        editText = (EditText)findViewById(R.id.messageActivity_editText);

        recyclerView = (RecyclerView)findViewById(R.id.messageActivity_RecyclerView);


        micButton =(FloatingActionButton)findViewById(R.id.messageActivity_micButton);
        micButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                VoiceTask voiceTask = new VoiceTask();
                voiceTask.execute();
            }
        }));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // send버튼 클릭시
                chatModels chatModel = new chatModels();

                chatModel.users.put(uid,true);
                chatModel.users.put(destinationUid,true);

                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(MessageActivity.this,"메세지를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(chatRoomUid == null){
                    button.setEnabled(false);// 버튼 활성화 비활성화로 예방
                    FirebaseDatabase.getInstance().getReference().child("chatList").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {// callback 만약 빠른 요청시 딜레이로 인해 체크코드 넘어갈수있음
                            checkChatRoom();
                        }
                    });
                }else{
                    //실질적인 Message를 firebaseDB에 보내줌
                    getResponse(editText.getText().toString());
                    chatModels.Comment comment = new chatModels.Comment();
                    comment.uid = uid; // 보낸사람
                    comment.message = editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatList").child(chatRoomUid).child("comments").push().setValue(comment);// 파이어 베이스에 push가 됨

                }
            }


        });
    }



    public void getResponse(String message) {


        String url = "http://api.brainshop.ai/get?bid=159991&key=TRuBDJabF7mmf56i&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<botModels> call = retrofitAPI.getMessage(url);

        System.out.println(url);
        System.out.println(BASE_URL);
        System.out.println(retrofitAPI.getMessage(url).toString());

        call.enqueue(new Callback<botModels>() {
            @Override
            public void onResponse(Call<botModels> call, Response<botModels> response) {
                if(response.isSuccessful()){
                    chatModels.Comment comment = new chatModels.Comment();


                    comment.uid = destinationUid;
                    comment.message = response.body().getBotChat();



                    System.out.println("보낸 메세지5: "  + response.body().getClass().getName());
                    System.out.println("보낸 메세지4 : "  +response.headers());
                    System.out.println("보낸 메세지3 : "  +response.message());
                    System.out.println("보낸 메세지2 : "  + response.code());
                    System.out.println("보낸 메세지1 : "  + response.body().toString());
                    System.out.println("보낸 메세지0 : "  + message);
                    FirebaseDatabase.getInstance().getReference().child("chatList").child(chatRoomUid).child("comments").push().setValue(comment);



                }
            }

            @Override
            public void onFailure(Call<botModels> call, Throwable t) {
                chatModels.Comment comment = new chatModels.Comment();
                comment.uid = destinationUid;
                comment.message = "다시 질문해 주세요";
            }
        });
    }




    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatList").orderByChild("users/"+destinationUid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()){
                    chatModels chatModel = item.getValue(chatModels.class);
                    if(chatModel.users.containsKey(destinationUid)){
                        chatRoomUid = item.getKey();
                        button.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                        System.out.println(chatRoomUid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{ // 화면에 채팅을 보여주는 부분
        List<chatModels.Comment> comments;
        userModels userModel;
        public RecyclerViewAdapter(){
            comments = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot snapshot) {
                    userModel = snapshot.getValue(userModels.class);
                    System.out.println("확인하세요@@@@@@@@@" + FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid));
                    getMessageList();
                }

                @Override
                public void onCancelled( DatabaseError error) {

                }
            });
        }

        void getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("chatList").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    comments.clear();
                    for (DataSnapshot item : snapshot.getChildren()){
                        comments.add(item.getValue(chatModels.Comment.class));
                    }
                    notifyDataSetChanged();
                    recyclerView.scrollToPosition(comments.size()-1);
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);
            if(comments.get(position).uid.equals(FirebaseAuth.getInstance().getUid())){
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.linearLayout.setVisibility(View.VISIBLE);
                messageViewHolder.textView_name.setText("Me");

            }else{

                StorageReference Ref = FirebaseStorage.getInstance().getReference();
                StorageReference img = Ref.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")) +  "/" + destinationUid);
                img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .apply(new RequestOptions().circleCrop())
                                .into((messageViewHolder).imageView_profile);
                    }
                });
                messageViewHolder.textView_name.setText(destinationUid);
                messageViewHolder.linearLayout.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(20);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }


    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_message;
        public TextView textView_name;
        public ImageView imageView_profile;
        public LinearLayout linearLayout;

        public MessageViewHolder(View view) {
            super(view);
            textView_message = (TextView) view.findViewById(R.id.item_Message_textView);
            textView_name = (TextView)view.findViewById(R.id.item_Message_textView_name);
            linearLayout = (LinearLayout)view.findViewById(R.id.item_Message_LinearLayout_destination);
            imageView_profile = (ImageView)view.findViewById(R.id.item_Message_imageView_profile);

        }
    }

    public class VoiceTask extends AsyncTask<String, Integer, String> {
        String str = null;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                getVoice();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

            } catch (Exception e) {
                Log.d("onActivityResult", "getImageURL exception");
            }
        }
    }

    private void getVoice() {

        Intent intent = new Intent();
        intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        String language = "ko-KR";

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        startActivityForResult(intent, 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String str = results.get(0);
            Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();

            TextView tv = findViewById(R.id.messageActivity_editText);
            tv.setText(str);
        }
    }

}