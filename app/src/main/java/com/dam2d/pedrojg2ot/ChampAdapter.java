package com.dam2d.pedrojg2ot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ChampAdapter extends RecyclerView.Adapter<ChampAdapter.MyViewHolder> {

    String data1[];
    String champIDs[];
    Context context;

    public ChampAdapter(Context ct, String s1[], String ids[]) {
        context = ct;
        data1 = s1;
        champIDs = ids;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView champName;
        ImageView myImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            champName = itemView.findViewById(R.id.champName);
            myImage = itemView.findViewById(R.id.champImage);
        }
    }
    @NonNull
    @Override
    public ChampAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.champ_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChampAdapter.MyViewHolder holder, int position) {
        String url = "http://ddragon.leagueoflegends.com/cdn/11.3.1/img/champion/" + champIDs[position];
        holder.champName.setText(data1[position]);

        Glide.with(holder.myImage.getContext()).load(url).into(holder.myImage);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }
}
