package com.babuland.babuland.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babuland.babuland.R;
import com.babuland.babuland.model.modelclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Adapterlistview extends RecyclerView.Adapter<Adapterlistview.customclass> {

    private Context  context;
    private List<modelclass>img;
    private DatePickerDialog.OnDateSetListener mDatesetListener;
    String dateOfbirdthglb,removeid[]=new String[15];
    int index_remove=0;

    public Adapterlistview(Context context, List<modelclass> img) {
        this.context = context;
        this.img = img;
    }

    @NonNull
    @Override
    public customclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dynamicchild,parent,false);
        return  new customclass(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final customclass holder, final int position) {


        final modelclass current = img.get(position);
        holder.tvchild.setText("child"+(position+1));

        final String childname = holder.childname_full.getText().toString();
        final String childob = holder.tv_dob_full.getText().toString();
        final String childclass = holder.class_ful.getText().toString();
        final String childschool = holder.school_full.getText().toString();

         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.gender_child,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);


        holder.dob_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day  = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog  = new DatePickerDialog(context,android.R.style.Theme_Holo_Dialog_MinWidth,mDatesetListener,year,month,day);
                dialog.getWindow();
                dialog.show();
            }
        });

        mDatesetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
                dateOfbirdthglb=i2+"/"+i1+"/"+i;
                holder.tv_dob_full.setText(dateOfbirdthglb);

            }
        };

        holder.imageView.setImageResource(current.getImg());
        if( position!=0){
            holder.removebtn.setVisibility(View.VISIBLE);
        }else {
            holder.removebtn.setVisibility(View.INVISIBLE);
        }

/*
        if(TextUtils.isEmpty(holder.childname_full.getText().toString())){
            Intent intent = new Intent("custom_message");
            intent.putExtra("childname",holder.childname_full.getText().toString());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }*/

        holder.addbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String name = holder.childname_full.getText().toString();
                String class_child=holder.class_ful.getText().toString();
                String school=holder.school_full.getText().toString();
                String dob = holder.tv_dob_full.getText().toString();
                String gender = holder.spinner.getSelectedItem().toString();
                Log.d("gender", "onClick: -------------gender "+gender);


                if(TextUtils.isEmpty(name)){
                    holder.childname_full.findFocus();
                    holder.childname_full.setError("Enter name please");
                    Toast.makeText(context, "Enter name filed please", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(class_child)){

                    holder.class_ful.findFocus();
                    holder.class_ful.setError("Enter class please");


                }else if(TextUtils.isEmpty(school)){

                    holder.school_full.findFocus();
                    holder.school_full.setError("Enter School name");


                }else if(TextUtils.isEmpty(dob)){

                    holder.tv_dob_full.findFocus();
                    holder.tv_dob_full.setError("Enter date of birth");

                }
                else {



                    holder.imageView.setImageResource(current.getImg());
                    img.add(position,current);
                    notifyItemInserted(position);
                    notifyItemRangeChanged(position,img.size());
                    savechildren(holder.childname_full,holder.class_ful,holder.school_full,holder.tv_dob_full,gender);

                }



               /* Animation animation = AnimationUtils.loadAnimation(context,R.anim.up_from_bottom);
                holder.itemView.startAnimation(animation);
                Log.d("poisition", "onClick: ------position"+position);*/

            }
        });

        holder.removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position!=0){
                    img.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,img.size());
                }
               //
            }
        });




    }

    /*private void removechild() {

        FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
        String useridlocal=mUser.getUid();
        DatabaseReference chilDatabase= FirebaseDatabase.getInstance().getReference().child("Childdb").child(useridlocal);
        chilDatabase.child(removeid[index_remove--]).removeValue();


          }*/

    private void savechildren(final EditText edtchilname, final EditText childclass, final EditText childschool, final TextView tv_dob, String gender) {
        Map fullmap = new HashMap();

        FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
        String useridlocal=mUser.getUid();
        final DatabaseReference chilDatabase= FirebaseDatabase.getInstance().getReference().child("Childdb").child(useridlocal);

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        final String random_timephone=edtchilname.getText().toString()+currentTime;

        //childname_full,class_ful,school_full,parentname_full,spousename_full,number_full,email_full,address_full;

        if(edtchilname.getText().toString()!=null && !TextUtils.isEmpty(random_timephone)){

            //removeid[index_remove++]=random_timephone;

            fullmap.put("child_name",edtchilname.getText().toString());
            fullmap.put("class",childclass.getText().toString());
            fullmap.put("school",childschool.getText().toString());
            fullmap.put("child_image","notset");
            fullmap.put("dob",tv_dob.getText().toString());
            fullmap.put("pre_branch","notset");
            fullmap.put("child_gender",gender);
            chilDatabase.child(random_timephone).updateChildren(fullmap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        /*mUser=FirebaseAuth.getInstance().getCurrentUser();
                        String useridlc=mUser.getUid();
                        mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(useridlc);
                        Map parent = new HashMap();
                        parent.put("name",parentname_full.getText().toString());
                        parent.put("spousename",spousename_full.getText().toString());
                        parent.put("phone",number_full.getText().toString());
                        parent.put("email",email_full.getText().toString());
                        parent.put("address",address_full.getText().toString());
                        mDatabase.updateChildren(parent);*/

                        chilDatabase.child(random_timephone).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                edtchilname.setText(dataSnapshot.child("child_name").getValue().toString());
                                childclass.setText(dataSnapshot.child("class").getValue().toString());
                                tv_dob.setText(dataSnapshot.child("dob").getValue().toString());
                                childschool.setText(dataSnapshot.child("school").getValue().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
            });
        }
    }

  /*  @Override
    public void onViewDetachedFromWindow(@NonNull customclass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }*/

    @Override
    public int getItemCount() {
        return img.size();
    }



    public class customclass extends RecyclerView.ViewHolder {

        View mview;
        Spinner spinner;
        Button addbtn,removebtn;
        TextView tvchild;
        ImageView imageView;
        private TextView tv_dob_full,dob_txt;
        private Spinner spinner_full;
        private EditText childname_full,class_ful,school_full,parentname_full,spousename_full,number_full,email_full,address_full;
        public customclass(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
            tvchild=itemView.findViewById(R.id.chldnumber);
            addbtn=itemView.findViewById(R.id.addchild1);
            initializefullprofile(itemView);
        }

        private void initializefullprofile(View itemView) {

            tv_dob_full=itemView.findViewById(R.id.dateofbirth_full);
            dob_txt=itemView.findViewById(R.id.dob_txt);
            spinner_full=itemView.findViewById(R.id.genderchild_full);
            childname_full=itemView.findViewById(R.id.childname_full);
            class_ful=itemView.findViewById(R.id.class_full);
            school_full=itemView.findViewById(R.id.schoolname_full);
            imageView=itemView.findViewById(R.id.imgiview);
            removebtn=itemView.findViewById(R.id.removechild);
            spinner=itemView.findViewById(R.id.genderchild_full);


        }
    }



}
