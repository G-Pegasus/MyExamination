package com.example.myexamination.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexamination.R;
import com.example.myexamination.entity.Planets;

import java.util.List;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {

    protected Context context;
    private final List<Planets> mList;
    private OnItemClickListener onItemClickListener;

    public PlanetAdapter(Context context, List<Planets> mList) {
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
        return new PlanetViewHolder(LayoutInflater.from(context).inflate(R.layout.planet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetViewHolder holder, int position) {
        Planets planet = mList.get(position);
        holder.mImgPlanetIcon.setImageResource(planet.getImageResource());
        holder.mTxtPlanetName.setText(planet.getPlanetName());

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                if (position == 0) {
                    onItemClickListener.onItemClick(position,1, mList);
                } else {
                    onItemClickListener.onItemClick(position,2, mList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class PlanetViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImgPlanetIcon;
        public TextView mTxtPlanetName;

        public PlanetViewHolder(@NonNull View itemView) {
            super(itemView);
            mImgPlanetIcon = itemView.findViewById(R.id.img_planet_icon);
            mTxtPlanetName = itemView.findViewById(R.id.txt_planet_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position,int arg, List<Planets> dataSource);
    }
}