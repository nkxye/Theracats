package com.bilonvaldez.theracats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {
    private final Context fContext;
    private final List<Cat> cats;

    public CatAdapter(Context fContext, List<Cat> cats) {
        this.fContext = fContext;
        this.cats = cats;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.layout_saved, null);
        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        Cat catRecord = cats.get(position);
        holder.textViewName.setText(catRecord.getImageId());
        holder.createdAt.setText(catRecord.getCreatedAt());
        Picasso.get().load(catRecord.getImageUrl()).into(holder.imageView);
        holder.btnOpenPhoto.setTag(catRecord.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    static class CatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, createdAt;
        ImageView imageView;
        Button btnOpenPhoto;

        public CatViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            createdAt = itemView.findViewById(R.id.createdAt);
            imageView = itemView.findViewById(R.id.imageView);
            btnOpenPhoto = itemView.findViewById(R.id.btnOpenPhoto);
        }
    }
}
