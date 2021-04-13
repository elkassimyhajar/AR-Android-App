package com.example.match_it;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class Adapter extends PagerAdapter {

    private List<Etablissement> etablissements;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<Etablissement> etablissements, Context context) {
        this.etablissements = etablissements;
        this.context = context;
    }

    @Override
    public int getCount() {
        return etablissements.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        this.layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.topic, container, false);

        ImageView imageView = view.findViewById(R.id.topicImage);
        //TextView textView = view.findViewById(R.id.topicName);

        imageView.setImageResource(etablissements.get(position).getImage());
        //textView.setText(etablissements.get(position).getName());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
