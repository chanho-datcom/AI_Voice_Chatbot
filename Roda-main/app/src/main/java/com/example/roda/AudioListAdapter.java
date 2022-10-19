package com.example.roda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private File[] fileList;
    private timeCheck timeCheck;
    private onItemListClick onItemListClick;

    public  AudioListAdapter(File[] fileList, onItemListClick onItemListClick){
        this.fileList = fileList;
        this.onItemListClick = onItemListClick;
    }
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        timeCheck = new timeCheck();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioListAdapter.AudioViewHolder holder, int position) {
        holder.list_filename.setText(fileList[position].getName());
        holder.list_date.setText(timeCheck.getTimeCheck(fileList[position].lastModified()));
    }

    @Override
    public int getItemCount() {
        return fileList.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder{

        private ImageView list_image;
        private TextView list_filename;
        private TextView list_date;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.list_item_view);
            list_date  = itemView.findViewById(R.id.list_item_date);
            list_filename  = itemView.findViewById(R.id.list_item_filename);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemListClick.onClickListener(fileList[getAdapterPosition()],getAdapterPosition());
                }
            });
        }
    }

    public interface  onItemListClick{
        void onClickListener(File file, int position);
    }

}
