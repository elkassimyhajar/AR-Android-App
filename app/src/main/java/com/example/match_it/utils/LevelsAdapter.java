package com.example.match_it.utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroomproject.R;
import com.example.match_it.activities.GameActivity;

import java.util.ArrayList;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelsViewHolder>{

    private final ArrayList<LevelEstablishment> levelEstablishments;
    private final String selectedTopic;
    private final int lastUnlockedLevel;

    public LevelsAdapter(ArrayList<LevelEstablishment> levelEstablishments, String selectedTopic, int lastUnlockedLevel) {
        this.levelEstablishments = levelEstablishments;
        this.selectedTopic = selectedTopic;
        this.lastUnlockedLevel = lastUnlockedLevel;
    }

    @NonNull
    @Override
    public LevelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.level, parent, false);
        return new LevelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LevelsViewHolder holder, int position) {
        LevelEstablishment e = levelEstablishments.get(position);
        holder.label.setText(e.getLabel());
        holder.img.setImageResource(e.getImg());
    }

    @Override
    public int getItemCount() {
        return levelEstablishments.size();
    }

    public class LevelsViewHolder extends RecyclerView.ViewHolder {
        private final TextView label;
        private final ImageView img;
        private LevelEstablishment currentEtab;
        public LevelsViewHolder(final View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.level_label);
            img= itemView.findViewById(R.id.level_img);
            // Add a click listener on itemView
            itemView.setOnClickListener(view -> {
                currentEtab = levelEstablishments.get(getLayoutPosition());
                if(currentEtab.getNum()<=lastUnlockedLevel) {
                    Intent intent = new Intent(currentEtab.getContext(), GameActivity.class);
                    intent.putExtra("topic", selectedTopic);
                    intent.putExtra("level", currentEtab.getNum());
                    currentEtab.getContext().startActivity(intent);
                }
            });
        }
    }
}
