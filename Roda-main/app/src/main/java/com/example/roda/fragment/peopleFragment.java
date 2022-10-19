package com.example.roda.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roda.R;
import com.example.roda.chat.MessageActivity;
import com.example.roda.model.userModels;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class peopleFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_people,container,false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragmentPeopleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new fragementPeopleRecyclerViewAdapter());
        return view;
    }

    class fragementPeopleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<userModels> userModelList;
        public fragementPeopleRecyclerViewAdapter(){
            userModelList = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child((FirebaseAuth.getInstance().getCurrentUser().getEmail()).substring(0,(FirebaseAuth.getInstance().getCurrentUser().getEmail()).indexOf("@"))).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot snapshot) {
                    userModelList.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        userModelList.add(dataSnapshot.getValue(userModels.class));
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled( DatabaseError error) {

                }
            });
        }

        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragement_people_list,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            StorageReference Ref = FirebaseStorage.getInstance().getReference();
            StorageReference img = Ref.child(userModelList.get(position).User_Id.substring(0,userModelList.get(position).User_Id.indexOf("@")) +  "/" + userModelList.get(position).User);
            //StorageReference img = Ref.child(userModelList.get(position).User_Id.substring(0,userModelList.get(position).User_Id.indexOf("@")) +  "/" + userModelList.get(position).User + ".png");
            System.out.println("path ->" + img);
            img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext())
                    .load(uri)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "사진 로드 실패",Toast.LENGTH_SHORT).show();
                }
            });


            ((CustomViewHolder)holder).textView.setText(userModelList.get(position).User);

            holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    intent.putExtra("destination", userModelList.get(position).User);
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fade_out, R.anim.fade_in);
                    startActivity(intent, activityOptions.toBundle());
                }
            });


        }

        @Override
        public int getItemCount() {
            return userModelList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView)view.findViewById(R.id.fragment_people_list_imageview);
                textView = (TextView)view.findViewById(R.id.fragment_people_list_userId);
            }
        }
    }
}
