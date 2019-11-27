package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.itangqi.waveloadingview.WaveLoadingView;

public class Profiling_moreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatePickerDialog.OnDateSetListener mDatesetListener;
    private Spinner spinner,spiner_gender;

    private EditText edt_childname;
    private EditText edt_class;
    private EditText edt_schoolname;
    private Button btn_savechange;


    LinearLayout rcvview,edtview;

    TextView tv_dateofBirdth,tv_gender;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private String userId;
    private int progress;

    private String name,dateOfbirdthglb,chil_gender,school_class,preferedBranch,Schoolname;

    WaveLoadingView mWaveLoadingView;

    String phone;

    DatabaseReference chilDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_more);

        mWaveLoadingView=(WaveLoadingView) findViewById(R.id.waveLoadingView);

        mUser= FirebaseAuth.getInstance().getCurrentUser();

        tv_dateofBirdth=findViewById(R.id.tv_dateofBirdth_child);
        edt_childname=findViewById(R.id.chil_name);
        edt_class=findViewById(R.id.class_child);
        edt_schoolname=findViewById(R.id.schollname_child);
        btn_savechange=findViewById(R.id.btn_saveChange);

        rcvview=findViewById(R.id.childrcvview);
        edtview=findViewById(R.id.chil_edtview);


        chilDatabase=FirebaseDatabase.getInstance().getReference().child("Childdb").child(userId);
        tv_dateofBirdth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day  = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog  = new DatePickerDialog(Profiling_moreActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth,mDatesetListener,year,month,day);
                dialog.getWindow();
                dialog.show();
            }
        });

        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progress =dataSnapshot.child("progressvalue").getValue(Integer.class);
                    phone=dataSnapshot.child("phone").getValue().toString();

                    mWaveLoadingView.setProgressValue(progress);
                    mWaveLoadingView.setCenterTitle(progress+"%");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Log.d("firebase data pulling", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }

        spinner=findViewById(R.id.profilng_spiner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.branch_prefered,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        mDatesetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
                dateOfbirdthglb=i2+"/"+i1+"/"+i;
                tv_dateofBirdth.setText(dateOfbirdthglb);

            }
        };



        spiner_gender=findViewById(R.id.spiner_child);

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_gender.setAdapter(adapter_gender);
        spiner_gender.setOnItemSelectedListener(this);

        name=edt_childname.getText().toString();
        school_class=edt_class.getText().toString();
        Schoolname=edt_schoolname.getText().toString();



        if(TextUtils.isEmpty(name)){
            name="not set";
        }else if(TextUtils.isEmpty(dateOfbirdthglb)){
            dateOfbirdthglb="not set";
        }

        else if(TextUtils.isEmpty(chil_gender)){
            chil_gender="not set";
        }
        else if(TextUtils.isEmpty(chil_gender)){
            chil_gender="not set";
        }
        else if(TextUtils.isEmpty(school_class)){
            school_class="not set";
        }
        else if(TextUtils.isEmpty(Schoolname)){
            Schoolname="not set";
        }

        else if(TextUtils.isEmpty(preferedBranch)){
            preferedBranch="not set";
        }



        btn_savechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phone!=null){
                    Map childata = new HashMap();

                    childata.put("child_name",name);
                    childata.put("dob",dateOfbirdthglb);
                    childata.put("child_gender",chil_gender);
                    childata.put("class",school_class);
                    childata.put("school",Schoolname);
                    childata.put("pre_branch",preferedBranch);
                    chilDatabase.child(phone).setValue(childata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });


                }else {

                    Toast.makeText(Profiling_moreActivity.this, "Phone number is required", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        preferedBranch=spinner.getSelectedItem().toString();

        chil_gender=spiner_gender.getSelectedItem().toString();
        Log.d("preferedbranch", "onCreate:------------------------------------------------------spiner item _ branch "+ chil_gender);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
