package com.example.myfoodcoach.Activity;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodcoach.R;
import com.example.myfoodcoach.logic.ConvertBase64;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private ArrayList<MyData> mDataset;

    public CardViewAdapter(ArrayList<MyData> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutri_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).text);
        holder.mImageView.setImageResource(mDataset.get(position).score);
        holder.base64ImageView.setImageBitmap(ConvertBase64.decodeBase64(mDataset.get(position).img));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public ImageView base64ImageView;

        public ViewHolder(View view) {

            super(view);
            mImageView = (ImageView) view.findViewById(R.id.nutri_card_image);
            base64ImageView = (ImageView) view.findViewById(R.id.base64Image);
            mTextView = (TextView) view.findViewById(R.id.nutri_card_text_view);

            view.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    System.out.println(mTextView.getText());
                    Intent intent = new Intent(v.getContext(), MapActivity.class);
                    intent.putExtra("id", "urn:epc:id:sgtin:0000003.000001.1");
                    v.getContext().startActivity(intent);
                }
            });
        }

    }
}

class MyData {
    public String text;
    public int score;
    public String img;

    public MyData(String text, int score, String img) {

        this.text = text;
        this.score = score;
        this.img = img;
    }
}