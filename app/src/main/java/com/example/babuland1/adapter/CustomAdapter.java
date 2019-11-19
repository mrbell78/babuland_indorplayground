package com.example.babuland1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babuland1.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Customclass> {

    Context context;
    int img[];
    String []heading;
    String []content;
    Boolean imglike_status=false;
    int cakeLike=125;

    public CustomAdapter(Context context, int[] img, String[] heading, String []content) {
        this.context = context;
        this.img = img;
        this.heading = heading;
        this.content = content;
    }

    @NonNull
   @Override
   public Customclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemlayout,parent,false);
        Customclass holder = new Customclass(v);
       return holder;
   }

   @Override
   public void onBindViewHolder(@NonNull final Customclass holder, final int position) {

        holder.img.setImageResource(img[position]);
        holder.tvheading.setText(heading[position]);
        holder.content.setText(content[position]);
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imglike_status==false){
                    holder.imgLike.setImageResource(R.drawable.ic_favorite);
                    imglike_status=true;
                    cakeLike=cakeLike+1;
                    holder.like_number.setText(Integer.toString(cakeLike));
                }
                else if(imglike_status==true){
                    holder.imgLike.setImageResource(R.drawable.ic_favorite_unlike);
                    imglike_status=false;
                    cakeLike=cakeLike-1;
                    holder.like_number.setText(Integer.toString(cakeLike));
                }

            }
        });

   }

   @Override
   public int getItemCount() {
       return img.length;
   }

   public class Customclass extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tvheading,content,like_number;
        Switch swithchc;
        ImageView imgLike;

        View mView;
       public Customclass(@NonNull View itemView) {
           super(itemView);
           mView=itemView;
           img=mView.findViewById(R.id.image_show);
           tvheading=mView.findViewById(R.id.heading);
           content=mView.findViewById(R.id.content);
          /* response=mView.findViewById(R.id.response);
           swithchc=mView.findViewById(R.id.swithch);*/
          imgLike=mView.findViewById(R.id.like_unlike);
          like_number=mView.findViewById(R.id.num_like);
       }
   }
}
