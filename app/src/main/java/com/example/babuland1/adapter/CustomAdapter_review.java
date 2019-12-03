package com.example.babuland1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babuland1.R;

public class CustomAdapter_review extends RecyclerView.Adapter<CustomAdapter_review.Customclass> {
    private Context context;
    private int[]img;
    private String[]name;
    private float []ratingvalue;

    float toyolet,foodquality,pgaservice,hyginic,avaragereview;


    public CustomAdapter_review(Context context, int[] img, String[] name) {
        this.context = context;
        this.img = img;
        this.name = name;
    }

    @NonNull
    @Override
    public Customclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review,parent,false);
        ratingvalue=new float[name.length];
        return new Customclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Customclass holder, final int position) {
        holder.imageView.setImageResource(img[position]);
        holder.textView.setText(name[position]);
        holder.ratingBar.getRating();
        Toast.makeText(context, "score"+ holder.ratingBar.getRating(), Toast.LENGTH_SHORT).show();
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                ratingvalue[position]= v;

                if(position==0){
                    toyolet=v;
                }else if(position==1){
                    foodquality=v;
                }else if(position==2){
                    pgaservice=v;
                }else if(position==3){
                    hyginic=v;
                }

                Log.d("review", "onRatingChanged: -------------------------------------------------------toylet "+toyolet);
                Log.d("review", "onRatingChanged: -------------------------------------------------------foodquality "+foodquality);
                Log.d("review", "onRatingChanged: -------------------------------------------------------pgaservice "+pgaservice);
                Log.d("review", "onRatingChanged: -------------------------------------------------------hyginic "+hyginic);

                avaragereview=(toyolet+foodquality+pgaservice+hyginic)/4;

                Log.d("avaragereview", "onRatingChanged: ---------------------------------------------------avaragereview "+avaragereview);
            }
        });
    }

    @Override
    public int getItemCount() {
        return img.length;
    }
    public class Customclass extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;
        RatingBar ratingBar;
        View mview;
        public Customclass(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
            imageView=mview.findViewById(R.id.image);
            textView=mview.findViewById(R.id.title);
            checkBox=mview.findViewById(R.id.checkbox);
            ratingBar=mview.findViewById(R.id.rating);
        }
    }
}
