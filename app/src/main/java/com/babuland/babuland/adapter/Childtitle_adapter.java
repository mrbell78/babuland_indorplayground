package com.babuland.babuland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babuland.babuland.R;



public class Childtitle_adapter extends RecyclerView.Adapter<Childtitle_adapter.Customclass> {


    Context context;


    @NonNull
    @Override
    public Customclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.childtitle_recylerciew,parent,false);
        return  new Customclass(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Customclass holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Customclass extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public Customclass(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.childlist);
        }
    }
}
