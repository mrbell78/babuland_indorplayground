package com.babuland.babuland.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babuland.babuland.R;
import com.babuland.babuland.adapter.Adapterlistview;
import com.babuland.babuland.model.modelclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccountSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private DatePickerDialog.OnDateSetListener mDatesetListener;
    private Toolbar mToolbar;
    private TextView dateofBirdth;
    private EditText edt_name,email,address,edt_enternumber;
    private String[] Gender;
    private Spinner spinner;
    private CircleImageView cr_profileimage;
    private static final String TAG = "AccountSettingsActivity";
    private Uri imgeuri;
    private String name,emailvlue,addressvalue,dateOfbirdthglb,gendervalue,numbervalue;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,chilDatabase;
    private FirebaseUser mUser;
    private Button btn_saveChange,foreditbtn;
    private ProgressDialog mdialog;
    private LinearLayout editlayout,viewlayout,pin_layout;
    private StorageReference mstorage;
    private byte[] thumbdata;
    private TextView tv_name,tv_gender,tv_dataofbirdth,tv_address,tv_email,tv_mobile,edit_dateofBirdth,tv_profilestatus;
    private String fbVarificationId;
    private String number_fromdb,cdoesent;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private Button btn_enterpin;
    private EditText edt_pin;
    private String userId;
    private String valueeventlistener;
    private int profilestatus=5;
    private ProgressBar progressBar;

    Map <String,Object> prifilemap;


    CardView cardViewaddimage;
    CardView saveimage;

    int numberprogress=0,addressprogress=0,emailprogress=0,genderprogress=0,dateofbirdthprogress=0,profilepicprogress=0;

    Button btn_more;

    String childname_adp,dob_adp,gender_adp,class_adp,school_adp;

    private TextView tv_dob_full;
    private Spinner spinner_full;
    private EditText childname_full,class_ful,school_full,parentname_full,spousename_full,number_full,email_full,address_full;
    private Button btn_save_full;
    Map fullmap = new HashMap();
    private Button btn_addchild12,btn_addchild3;
    private LinearLayout ll2,ll3;

    int indexlist[]=new int[1];
    RecyclerView recyclerView;
    Adapterlistview adapeter;
    Button btnadd,btnbinus;
    LinearLayoutManager manager;

    RadioGroup gender_radiogroup;
    RadioButton radioButton;
    RadioButton radioButton_male,radioButton_female;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);

        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.editprofile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializefullprofile();
        btn_save_full=findViewById(R.id.save_full);


        edt_name=findViewById(R.id.name_id_accountSettings);
        email=findViewById(R.id.email_accountsettings);
        //gender=findViewById(R.id.gender_accountSettings);
        address=findViewById(R.id.address_accountsettings);
        cr_profileimage=findViewById(R.id.cr_account_imagesettings);
        btn_saveChange=findViewById(R.id.btn_saveChange);
        foreditbtn=findViewById(R.id.forEditbtn);
        mstorage= FirebaseStorage.getInstance().getReference();
        edt_enternumber=findViewById(R.id.number_id_accountSettings);

        saveimage=findViewById(R.id.saveimage);
        edit_dateofBirdth=findViewById(R.id.tv_dateofBirdth);

        progressBar=findViewById(R.id.myprogressbar_accountsettings);
        cardViewaddimage=findViewById(R.id.addimagee);
        Intent intent = getIntent();
        fbVarificationId=intent.getStringExtra("r_home");
       // Log.d(TAG, "onCreate: ----------------------------------------------------------------------------------------fb varificationcode-----------------------------"+fbVarificationId);


        btn_more=findViewById(R.id.btn_more);

        //mAuth= FirebaseAuth.getInstance();
      //  mUser=mAuth.getCurrentUser();

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        }else{
            //Log.d(TAG, "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }


        mdialog=new ProgressDialog(this);
        editlayout=findViewById(R.id.editlayout);
        viewlayout=findViewById(R.id.viewlayout);
        prifilemap=new HashMap<>();



        //for view layout
        tv_name=findViewById(R.id.name_id_account);
        tv_gender=findViewById(R.id.gender_account);
        tv_dataofbirdth=findViewById(R.id.tv_birdthdday);
        tv_email=findViewById(R.id.email_account);
        tv_address=findViewById(R.id.address_accounts);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_profilestatus=findViewById(R.id.tv_profilepercentage);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String namel=dataSnapshot.child("name").getValue().toString();
                    name=namel;
                    tv_name.setText(name);
                    edt_name.setText(name);

                    String gender=dataSnapshot.child("gender").getValue().toString();
                    gendervalue=gender;
                    Log.d(TAG, "onDataChange: -------------------------------------------------gendervalue "+gendervalue);
                    tv_gender.setText(gendervalue);


                    String dateofbirdth = dataSnapshot.child("dateofbirdth").getValue().toString();

                    dateOfbirdthglb=dateofbirdth;
                    Log.d(TAG, "onDataChange: -------------------------------------------dateof birdth "+dateOfbirdthglb);
                    tv_dataofbirdth.setText(dateOfbirdthglb);
                    edit_dateofBirdth.setText(dateOfbirdthglb);


                    String emaill = dataSnapshot.child("email").getValue().toString();
                    emailvlue=emaill;
                    //Log.d(TAG, "onDataChange: -----------------------------------------emailvalue "+emailvlue);
                    tv_email.setText(emailvlue);
                    email.setText(emailvlue);


                  Uri imlink= Uri.parse(dataSnapshot.child("image").getValue().toString());
                  imgeuri= imlink;
                   // Log.d(TAG, "onDataChange: --------------------------------------------------------------image uri= "+imgeuri);

                    File orginalImagefile =  new File(imgeuri.getPath());

                    try {
                        Bitmap thumb_bitmap = new Compressor(AccountSettingsActivity.this)
                                .setMaxWidth(200)
                                .setMaxHeight(200)
                                .setQuality(75)
                                .compressToBitmap(orginalImagefile);
                        ByteArrayOutputStream  bos  = new ByteArrayOutputStream();
                        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                        thumbdata=bos.toByteArray();
                       // Log.d(TAG, "onDataChange: ---------------------------------------------------------------thumbdata "+ thumbdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Picasso.with(getApplicationContext()).load(imgeuri).placeholder(R.drawable.profiledefaultpic).error(R.drawable.profiledefaultpic).into(cr_profileimage);

                    int  pic_up = dataSnapshot.child("pic_up").getValue(Integer.class);
                    int  name_up = dataSnapshot.child("name_up").getValue(Integer.class);
                    int  number_up = dataSnapshot.child("number_up").getValue(Integer.class);
                    int dob_up = dataSnapshot.child("dob_up").getValue(Integer.class);
                    int mail_up = dataSnapshot.child("mail_up").getValue(Integer.class);
                    int address_up = dataSnapshot.child("address_up").getValue(Integer.class);
                    int totalProgress= pic_up+name_up+number_up+dob_up+mail_up+address_up;
                    //int databaspoint = dataSnapshot.child("BabulandPoints").getValue(Integer.class);

                    if(totalProgress!=0){
                        profilestatus=totalProgress;
                        progressBar.setProgress(profilestatus);
                        tv_profilestatus.setText(Integer.toString(profilestatus)+"%");
                    }/*else {

                        progressBar.setProgress(profilestatus);
                        tv_profilestatus.setText(progressvalueformdb+"%");
                    }*/


                    String addressl=dataSnapshot.child("address").getValue().toString();
                    addressvalue=addressl;
                    Log.d(TAG, "onDataChange: ------------------------------------------addressvalue "+addressvalue);
                    tv_address.setText(addressvalue);
                    address.setText(addressvalue);

                    String image = dataSnapshot.child("image").getValue().toString();
                    if(image!=null){
                        Picasso.with(AccountSettingsActivity.this).load(image).into(cr_profileimage);
                    }

                    number_fromdb=dataSnapshot.child("phone").getValue().toString();
                    numbervalue=number_fromdb;
                    Log.d(TAG, "onDataChange: --------------------------------------------------number "+numbervalue);
                    tv_mobile.setText(numbervalue);
                    edt_enternumber.setText(numbervalue);


                    gendervalue=dataSnapshot.child("gender").getValue().toString();
                    tv_gender.setText(gendervalue);

                    dateOfbirdthglb=dataSnapshot.child("dateofbirdth").getValue().toString();
                    tv_dataofbirdth.setText(dateofbirdth);


                    emailvlue=dataSnapshot.child("email").getValue().toString();
                    tv_email.setText(emailvlue);


                    addressvalue=dataSnapshot.child("address").getValue().toString();
                    tv_address.setText(addressvalue);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // til now block


        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                cdoesent = s;
                Toast.makeText(AccountSettingsActivity.this, "code sent", Toast.LENGTH_SHORT).show();

            }
        };



      /*  cr_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                 if(ContextCompat.checkSelfPermission(AccountSettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                     ActivityCompat.requestPermissions(AccountSettingsActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                 }else{
                     BringImagePicker();

                 }
             }else{
                 BringImagePicker();
                 }
            }
        });*/

        cardViewaddimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(AccountSettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(AccountSettingsActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    }else{
                        BringImagePicker();

                    }
                }else{
                    BringImagePicker();


                }
            }

        });


        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mdialog.setTitle("Updating Image");
                mdialog.setMessage("Please wait...................");
                mdialog.setCanceledOnTouchOutside(false);
                mdialog.show();

                mdialog.show();
                saveImage();
            }
        });

        dateofBirdth=findViewById(R.id.tv_dateofBirdth);

        spinner=findViewById(R.id.account_spiner);



        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
         spinner.setOnItemSelectedListener(this);

         gendervalue = spinner.getSelectedItem().toString();

        dateofBirdth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day  = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog  = new DatePickerDialog(AccountSettingsActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth,mDatesetListener,year,month,day);
                dialog.getWindow();
                dialog.show();
            }
        });

        mDatesetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
                dateOfbirdthglb=i2+"/"+i1+"/"+i;
                dateofBirdth.setText(dateOfbirdthglb);

            }
        };

        foreditbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editlayout.setVisibility(View.VISIBLE);
                viewlayout.setVisibility(View.INVISIBLE);
            }
        });






        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSettingsActivity.this,Profiling_moreActivity.class).putExtra("progress",profilestatus));
            }
        });

        btn_saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=edt_name.getText().toString();
                emailvlue=email.getText().toString();
                addressvalue=address.getText().toString();
                numbervalue=edt_enternumber.getText().toString();

                if( name!=null && numbervalue!=null && dateOfbirdthglb!=null && emailvlue!=null && gendervalue!=null || address!=null){


                    Log.d(TAG, "onClick: ----------------------------------------------------number----------------------................--------"+numbervalue);
                    Log.d(TAG, "onClick: ----------------------------------------------------fbvarefication----------------------................--------"+dateOfbirdthglb);


                        //pin_layout.setVisibility(View.VISIBLE);
                        editlayout.setVisibility(View.INVISIBLE);
                        //viewlayout.setVisibility(View.VISIBLE);
                       // sendVarificationcode();
                    mdialog.setTitle("Updating profile");
                    mdialog.setMessage("Please wait...................");
                    mdialog.setCanceledOnTouchOutside(false);
                    mdialog.show();

                    mdialog.show();
                    saveProfile();

                    /*//Toast.makeText(AccountSettingsActivity.this, (CharSequence) imgeuri, Toast.LENGTH_SHORT).show();
                    if(imgeuri==null){
                        Toast.makeText(AccountSettingsActivity.this, "image is empty", Toast.LENGTH_SHORT).show();
                    }else if(imgeuri!=null){
                       *//* mdialog.setTitle("Updating profile");
                        mdialog.setMessage("Please wait...................");
                        mdialog.setCanceledOnTouchOutside(false);
                        mdialog.show();
                        saveProfile();*//*
                    }*/
                }else {
                    Toast.makeText(AccountSettingsActivity.this, "You must have to fill up the following field", Toast.LENGTH_SHORT).show();
                    edt_name.setError("your name pleaes");
                    email.setError("your email ");
                   // gender.setError("Enter gender please");
                    dateofBirdth.setError("your date of birth");

                }


                //editlayout.setVisibility(View.INVISIBLE);
                ;
            }
        });
       /* btn_enterpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                varyfycode();
            }
        });*/


        LocalBroadcastManager.getInstance(AccountSettingsActivity.this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom_message"));

       btn_save_full.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               String gendertext=null;
               if(gender_radiogroup.getCheckedRadioButtonId()==-1){
                   Toast.makeText(AccountSettingsActivity.this, "select gender", Toast.LENGTH_SHORT).show();
                   gender_radiogroup.findFocus();
               }else {
                   int id  = gender_radiogroup.getCheckedRadioButtonId();
                   radioButton= findViewById(id);
                   gendertext = radioButton.getText().toString();

               }



               if(!adapter.isEmpty() && gendertext!=null){
                       mUser=FirebaseAuth.getInstance().getCurrentUser();
                       String useridlocal=mUser.getUid();
                       chilDatabase=FirebaseDatabase.getInstance().getReference().child("Childdb").child(useridlocal);


               /*String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
               String random_timephone=number_full.getText().toString()+currentTime;
*/
                       //childname_full,class_ful,school_full,parentname_full,spousename_full,number_full,email_full,address_full;


                       mUser=FirebaseAuth.getInstance().getCurrentUser();
                       String useridlc=mUser.getUid();
                       mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(useridlc);
                       Map parent = new HashMap();
                       parent.put("name",parentname_full.getText().toString());
                       parent.put("spousename",spousename_full.getText().toString());
                       parent.put("phone",number_full.getText().toString());
                       parent.put("email",email_full.getText().toString());
                       parent.put("address",address_full.getText().toString());
                       parent.put("gender",gendertext);

                       mDatabase.updateChildren(parent).addOnCompleteListener(new OnCompleteListener() {
                           @Override
                           public void onComplete(@NonNull Task task) {

                               if(task.isSuccessful()){
                                   FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
                                   String useridlocal=mUser.getUid();
                                   DatabaseReference chilDatabase= FirebaseDatabase.getInstance().getReference().child("Childdb").child(useridlocal);

                                   String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                   String random_timephone=childname_adp+currentTime;

                                   fullmap.put("child_name",childname_adp);
                                   fullmap.put("class",class_adp);
                                   fullmap.put("school",school_adp);
                                   fullmap.put("child_image","notset");
                                   fullmap.put("dob",dob_adp);
                                   fullmap.put("pre_branch","notset");
                                   fullmap.put("child_gender",gender_adp);

                                   if(childname_adp!=null && !childname_adp.isEmpty()){

                                       chilDatabase.child(childname_adp).updateChildren(fullmap).addOnCompleteListener(new OnCompleteListener() {
                                           @Override
                                           public void onComplete(@NonNull Task task) {
                                               Toast.makeText(AccountSettingsActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                               btn_save_full.setText("Edit Profile");
                                           }
                                       });
                                   }

                               }
                           }
                       });
                       Toast.makeText(AccountSettingsActivity.this, "Upload successful.No child added", Toast.LENGTH_SHORT).show();
                       btn_save_full.setText("Edit Profile");


                   }






           }
       });

        recyclerView=findViewById(R.id.recyclerview);
        manager=new LinearLayoutManager(this);
       // recyclerView.getLayoutManager().scrollToPosition(modelclass.getobject().size()-1);
        recyclerView.setLayoutManager(manager);

        adapeter = new Adapterlistview(AccountSettingsActivity.this, modelclass.getobject());

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapeter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0){
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);

                }
            }
        });


        showdatgadefault();




      /*  btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexlist=new int[indexlist.length+1];
                adapeter = new Adapterlistview(AccountSettingsActivity.this,modelclass.getobject());
                recyclerView.smoothScrollToPosition(indexlist.length);
                recyclerView.setAdapter(adapeter);
                if(indexlist.length>1){
                    btnbinus.setVisibility(View.VISIBLE);
                }else {
                    btnbinus.setVisibility(View.INVISIBLE);
                }

            }
        });

        btnbinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(indexlist.length>1){
                    indexlist=new int[indexlist.length-1];
                    adapeter = new Adapterlistview(AccountSettingsActivity.this,Mod);
                    recyclerView.smoothScrollToPosition(indexlist.length);
                    recyclerView.setAdapter(adapeter);
                }else {
                    btnbinus.setVisibility(View.INVISIBLE);
                }

            }
        });*/




    }

    private void showdatgadefault() {

        DatabaseReference showdatgabse;
        FirebaseUser mshowuser;
        String showuserid;

        mshowuser=FirebaseAuth.getInstance().getCurrentUser();
        showuserid=mshowuser.getUid();
        showdatgabse = FirebaseDatabase.getInstance().getReference().child("User").child(showuserid);

        showdatgabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    parentname_full.setText(dataSnapshot.child("name").getValue().toString());
                    spousename_full.setText(dataSnapshot.child("spousename").getValue().toString());
                    number_full.setText(dataSnapshot.child("phone").getValue().toString());
                    email_full.setText(dataSnapshot.child("email").getValue().toString());
                    address_full.setText(dataSnapshot.child("address").getValue().toString());
                    String gender = dataSnapshot.child("gender").getValue().toString();

                    if(gender.equals("Mr.")){
                        radioButton_male.setChecked(true);
                        radioButton_female.setChecked(false);
                    }else{
                        radioButton_male.setChecked(true);
                        radioButton_female.setChecked(false);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void saveProfile(){

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        String useridlc=mUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(useridlc);




                    if(!numbervalue.equals("Number") && numberprogress==0){

                        prifilemap.put("phone",numbervalue);
                        numberprogress=20;
                        prifilemap.put("number_up",numberprogress);
                        //profilestatus=profilestatus+numberprogress;
                        tv_mobile.setText(numbervalue);
                        Log.d(TAG, "onDataChange: ---------------------------------------number inserted");
                    }
                     if(!dateOfbirdthglb.equals("Date of Birth") && dateofbirdthprogress==0){
                        dateofbirdthprogress=15;
                        prifilemap.put("dateofbirdth",dateOfbirdthglb);
                        prifilemap.put("dob_up",dateofbirdthprogress);
                       // profilestatus+=dateofbirdthprogress;
                        Log.d(TAG, "onDataChange: ----------------------------------------------dateofbirdthd inserted");
                        tv_dataofbirdth.setText(dateOfbirdthglb);
                    }
                    if(!emailvlue.equals("Email") && emailprogress==0){
                        emailprogress=10;
                      prifilemap.put("email",emailvlue);
                      prifilemap.put("mail_up",emailprogress);

                        //profilestatus+=emailprogress;
                        Log.d(TAG, "onDataChange: ---------------------------------------------------email inserted");
                        tv_email.setText(emailvlue);
                    }


                    Log.d(TAG, "importatn: -------gendervalue "+gendervalue);
                     if(!gendervalue.equals("Gender") ){
                        prifilemap.put("gender",gendervalue);
                        genderprogress=5;
                        prifilemap.put("gender_up",genderprogress);

                       // profilestatus+=12;
                        //Log.d(TAG, "onDataChange: ----------------------------------------------------------gender inserted");

                        tv_gender.setText(gendervalue);
                    }
                     if(!addressvalue.equals("Your address") && addressprogress==0){
                        addressprogress=10;
                        prifilemap.put("address",addressvalue);
                        prifilemap.put("address_up",addressprogress);
                        //profilestatus+=addressprogress;
                        Log.d(TAG, "onDataChange: -----------------------------------------------------------address inserted");
                        tv_address.setText(addressvalue);
                    }




                if(prifilemap!=null){
                    mDatabase.updateChildren(prifilemap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            //mDatabase.child("name").setValue(name);

                            Log.d(TAG, "onComplete: ----------------------map "+ prifilemap);


                            mdialog.dismiss();
                            Toast.makeText(AccountSettingsActivity.this, " upload successful", Toast.LENGTH_SHORT).show();
                            editlayout.setVisibility(View.INVISIBLE);
                            viewlayout.setVisibility(View.VISIBLE);
                            // pin_layout.setVisibility(View.INVISIBLE);
                        }
                    });
                }










    }
    private void saveImage() {

         if( imgeuri!=null){
             mUser=FirebaseAuth.getInstance().getCurrentUser();
             String userid=mUser.getUid();
             mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);

             final StorageReference filepath=mstorage.child("allprofileimage").child(userid+".jpg");
             final StorageReference thumb_nailimg=mstorage.child("allthumnailimg").child(userid+".jpg");
             filepath.putFile(imgeuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     if(taskSnapshot!=null){
                         filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {
                                 Uri downloaduri = uri;
                                 imgeuri=downloaduri;

                                 UploadTask uploadTask =   thumb_nailimg.putBytes(thumbdata);

                                 uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                                         if(task.isSuccessful()){
                                             thumb_nailimg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                 @Override
                                                 public void onSuccess(Uri uri) {
                                                     Uri thumuri= uri;
                                                     if(task.isSuccessful()){
                                                         Map imagedatawiththum=  new HashMap();
                                                         imagedatawiththum.put("image",imgeuri.toString());
                                                         imagedatawiththum.put("thumb_nail",thumuri.toString());
                                                         mDatabase.updateChildren(imagedatawiththum).addOnCompleteListener(new OnCompleteListener() {
                                                             @Override
                                                             public void onComplete(@NonNull Task task) {
                                                                 if(task.isSuccessful()){
                                                                     if(profilepicprogress==0){
                                                                         profilepicprogress=30;
                                                                         mDatabase.child("pic_up").setValue(profilepicprogress);
                                                                     }
                                                                     Toast.makeText(AccountSettingsActivity.this, "save successful", Toast.LENGTH_SHORT).show();
                                                                     saveimage.setVisibility(View.INVISIBLE);
                                                                     cardViewaddimage.setVisibility(View.VISIBLE);
                                                                     mdialog.dismiss();
                                                                 }
                                                             }
                                                         });


                                                     }

                                                 }
                                             });
                                         }
                                     }
                                 });

                             }
                         });
                     }
                 }
             });
         }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gendervalue  = adapterView.getItemAtPosition(i).toString();
        //gender.setText(gendervalue);
        Log.d(TAG, "onItemSelected: -------------------------------------------------------------------------gendervalue-----------------------------------------"+gendervalue);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512,512)
                .setAspectRatio(1, 1)
                .start(AccountSettingsActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgeuri  = result.getUri();
                cr_profileimage.setImageURI(imgeuri);
                saveimage.setVisibility(View.VISIBLE);
                cardViewaddimage.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onActivityResult: ---------------------------------------------------------------fetche image uri----------------------------------"+imgeuri);

                File orginalImagefile =  new File(imgeuri.getPath());

                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(orginalImagefile);
                    ByteArrayOutputStream  bos  = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                    thumbdata=bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }


   /* private void sendVarificationcode() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(numbervalue, 60, TimeUnit.SECONDS, this, mCallback);
    }*/

   /* private void varyfycode() {

        String code =edt_pin.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(cdoesent, code);
        signInWithPhoneAuthCredential(credential);


    }*/


  /*  private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = mCurrentUser.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
                            mDatabase.orderByChild("phone").equalTo(numbervalue).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()!=null)
                                    {
                                        Toast.makeText(AccountSettingsActivity.this, "User exist", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        mdialog.dismiss();
                                        finish();
                                    }else{
                                        mDatabase.child("name").setValue(name);
                                        mDatabase.child("dateofbirdth").setValue(dateOfbirdthglb);
                                        mDatabase.child("email").setValue(emailvlue);
                                        mDatabase.child("gender").setValue(gendervalue);
                                        mDatabase.child("address").setValue(addressvalue);
                                        mDatabase.child("phone").setValue(numbervalue);

                                        if(imgeuri!=null){
                                            mdialog.setTitle("Updating profile");
                                            mdialog.setMessage("Please wait...................");
                                            mdialog.setCanceledOnTouchOutside(false);
                                            mdialog.show();
                                            saveProfile();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    mdialog.dismiss();

                                }
                            });
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(AccountSettingsActivity.this, "incorrenct code", Toast.LENGTH_SHORT).show();
                                mdialog.dismiss();
                            }

                        }
               }
                });
    }*/

    private void initializefullprofile() {

        tv_dob_full=findViewById(R.id.dateofbirth_full);
        spinner_full=findViewById(R.id.genderchild_full);
        childname_full=findViewById(R.id.childname_full);
        class_ful=findViewById(R.id.class_full);
        school_full=findViewById(R.id.schoolname_full);
        parentname_full=findViewById(R.id.name_parent_full);
        spousename_full=findViewById(R.id.spousename_full);
        number_full=findViewById(R.id.number_full);
        email_full=findViewById(R.id.email_full);
        address_full=findViewById(R.id.address_full);
        gender_radiogroup=findViewById(R.id.radiogroup_gender);
        radioButton_male=findViewById(R.id.radio_male);
        radioButton_female=findViewById(R.id.radio_female);



        //ll2,ll3;

        /*ll2=findViewById(R.id.liniarchild2);
        ll3=findViewById(R.id.liniarchild3);*/

    }


    BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            childname_adp = intent.getStringExtra("childname");
            dob_adp=intent.getStringExtra("dob");
            class_adp=intent.getStringExtra("class");
            gender_adp=intent.getStringExtra("gender");
            school_adp=intent.getStringExtra("school");
            Toast.makeText(AccountSettingsActivity.this,school_adp,Toast.LENGTH_SHORT).show();
        }
    };



}
