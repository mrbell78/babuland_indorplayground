package com.babuland.babuland.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babuland.babuland.R;
import com.babuland.babuland.model.modelclass;

import java.util.List;

public class Adapterlistview extends RecyclerView.Adapter<Adapterlistview.customclass> {

    private Context  context;
    private List<modelclass>img;

    public Adapterlistview(Context context, List<modelclass> img) {
        this.context = context;
        this.img = img;
    }

    @NonNull
    @Override
    public customclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamicchild,parent,false);


        return new customclass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final customclass holder, final int position) {


        final modelclass current = img.get(position);


        String childname = holder.childname_full.getText().toString();
        String childob = holder.tv_dob_full.getText().toString();
        String childclass = holder.class_ful.getText().toString();

        holder.imageView.setImageResource(current.getImg());
/*
        if(TextUtils.isEmpty(holder.childname_full.getText().toString())){
            Intent intent = new Intent("custom_message");
            intent.putExtra("childname",holder.childname_full.getText().toString());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }*/
        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.imageView.setImageResource(current.getImg());
                img.add(position,current);
                notifyItemInserted(img.size());
                holder.tvchild.setText("child"+(position+1));

                Animation animation = AnimationUtils.loadAnimation(context,R.anim.up_from_bottom);
                holder.itemView.startAnimation(animation);
                Log.d("poisition", "onClick: ------position"+position);




            }
        });

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull customclass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return img.size();
    }

    public class customclass extends RecyclerView.ViewHolder {

        Button addbtn,removebtn;
        TextView tvchild;
        ImageView imageView;
        private TextView tv_dob_full;
        private Spinner spinner_full;
        private EditText childname_full,class_ful,school_full,parentname_full,spousename_full,number_full,email_full,address_full;
        public customclass(@NonNull View itemView) {
            super(itemView);

            tvchild=itemView.findViewById(R.id.chldnumber);
            addbtn=itemView.findViewById(R.id.addchild1);
            initializefullprofile(itemView);



        }

        private void initializefullprofile(View itemView) {

            tv_dob_full=itemView.findViewById(R.id.dateofbirth_full);
            spinner_full=itemView.findViewById(R.id.genderchild_full);
            childname_full=itemView.findViewById(R.id.childname_full);
            class_ful=itemView.findViewById(R.id.class_full);
            school_full=itemView.findViewById(R.id.schoolname_full);
            imageView=itemView.findViewById(R.id.imgiview);
            removebtn=itemView.findViewById(R.id.removechild);


        }
    }
}
