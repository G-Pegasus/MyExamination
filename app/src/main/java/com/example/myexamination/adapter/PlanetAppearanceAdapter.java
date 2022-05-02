package com.example.myexamination.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexamination.R;

import java.util.List;

public class PlanetAppearanceAdapter extends RecyclerView.Adapter<PlanetAppearanceAdapter.PlanetViewHolder> {

    protected Context context;
    private List<Integer> mList;
    private OnItemClickListener onItemClickListener;

    public PlanetAppearanceAdapter(Context context, List<Integer> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public PlanetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanetViewHolder(LayoutInflater.from(context).inflate(R.layout.planet_img, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetViewHolder holder, int position) {
        holder.mImgPlanetIcon.setImageResource(mList.get(position));

        holder.mImgPlanetIcon.setOnClickListener(view -> {
            onItemClickListener.onItemClick(position, mList);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class PlanetViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImgPlanetIcon;

        public PlanetViewHolder(@NonNull View itemView) {
            super(itemView);
            mImgPlanetIcon = itemView.findViewById(R.id.planet_img);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<Integer> dataSource);
    }
}
