package com.babuland.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.babuland.babuland1.model.Model_childlist;
import com.babuland.babuland1.utils.Dbhelper_childprofile;
import com.babuland.babuland1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import me.itangqi.waveloadingview.WaveLoadingView;

public class Profiling_moreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatePickerDialog.OnDateSetListener mDatesetListener;
    private Spinner spinner,spiner_gender;
    private FirebaseRecyclerOptions<Model_childlist> options;
    private FirebaseRecyclerAdapter<Model_childlist, Profiling_moreActivity.UserViewholder> fadapter;
    private EditText edt_childname;
    private EditText edt_class;
    private EditText edt_schoolname;
    private Button btn_savechange;
    private Button btn_addchild;
    private byte[] thumbdata;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss. aa");
    String formattedDate = dateFormat.format(new Date()).toString();
    private StorageReference mStorage;

    private DatabaseReference adapterquerydb;


    LinearLayout rcvview;
    CardView edtview,addchildimg,saveimg,selectimg;

    RecyclerView recyclerView;

    TextView tv_dateofBirdth,tv_gender;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private String userId;
    private int progress;

    private Uri imgeuri;
    private ProgressDialog mdialog;
    private CircleImageView imgProfile;
    private Dbhelper_childprofile db;
    private String name,dateOfbirdthglb,chil_gender,school_class,preferedBranch,Schoolname;

    WaveLoadingView mWaveLoadingView;

    String phone;

    DatabaseReference chilDatabase;

    int progressvalue;
    Toolbar mToolbar;


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
        mdialog=new ProgressDialog(this);
        recyclerView=findViewById(R.id.childrcv);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rcvview=findViewById(R.id.childrcvview);
        edtview=findViewById(R.id.chil_edtview);
        db=new Dbhelper_childprofile(this);
        btn_addchild=findViewById(R.id.btn_addchild);
        saveimg=findViewById(R.id.save);
        selectimg=findViewById(R.id.select);

        imgProfile=findViewById(R.id.child_image);
        addchildimg=findViewById(R.id.addnewimg);
        mStorage= FirebaseStorage.getInstance().getReference();

        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        //db=new DbHelper_freeTicket(this);
        getSupportActionBar().setTitle("My Children list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        progressvalue=intent.getIntExtra("progress",0);



        addchildimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(Profiling_moreActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(Profiling_moreActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    }else{
                        BringImagePicker();

                    }
                }else{
                    BringImagePicker();


                }
            }
        });







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
            chilDatabase=FirebaseDatabase.getInstance().getReference().child("Childdb").child(userId);

      /*      mDatabase.addValueEventListener(new ValueEventListener() {
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
            });*/




        }else{
            Log.d("firebase data pulling", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }

        if(progressvalue!=0){

            mWaveLoadingView.setProgressValue(progressvalue);
            mWaveLoadingView.setCenterTitle(progressvalue+"%");

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
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_list_item_1);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_gender.setAdapter(adapter_gender);
        spiner_gender.setOnItemSelectedListener(this);



        btn_savechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                final String random_timephone=phone+currentTime;

                mUser= FirebaseAuth.getInstance().getCurrentUser();
                String mUserid=mUser.getUid();
                mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(mUserid);


                phone=getphone(mDatabase);



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


                if(phone!=null && !phone.equals("default")){


                  if(imgeuri!=null){

                      mdialog.setTitle("Updating profile");
                      mdialog.setMessage("Please wait...................");
                      mdialog.setCanceledOnTouchOutside(false);
                      mdialog.show();
                      mdialog.show();

                      mUser=FirebaseAuth.getInstance().getCurrentUser();
                      String useridlocal=mUser.getUid();

                      chilDatabase=FirebaseDatabase.getInstance().getReference().child("Childdb").child(useridlocal);
                      useridlocal=random_timephone;

                      final StorageReference filepath = mStorage.child("Childimages").child(useridlocal+".jpg");

                      filepath.putFile(imgeuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {
                                      Uri chilimg =uri;
                                      imgeuri=chilimg;


                                      Map childata = new HashMap();

                                      childata.put("child_name",name);
                                      childata.put("dob",dateOfbirdthglb);
                                      childata.put("child_gender",chil_gender);
                                      childata.put("class",school_class);
                                      childata.put("school",Schoolname);
                                      childata.put("pre_branch",preferedBranch);
                                      childata.put("child_image",imgeuri.toString());

                                      db.insertdata_childpro(random_timephone);
                                      chilDatabase.child(random_timephone).updateChildren(childata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              saveimg.setVisibility(View.INVISIBLE);
                                              addchildimg.setVisibility(View.VISIBLE);
                                              selectimg.setVisibility(View.INVISIBLE);
                                              rcvview.setVisibility(View.VISIBLE);
                                              edtview.setVisibility(View.INVISIBLE);
                                              imgeuri=null;
                                              Toast.makeText(Profiling_moreActivity.this, "upload successful", Toast.LENGTH_SHORT).show();
                                              mdialog.dismiss();
                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Log.d("uploaderror", "onFailure: ---------------error "+e.getMessage());
                                          }
                                      });


                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(Profiling_moreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                  }

                              });



                          }
                      });





                   /*   Map childata = new HashMap();

                      childata.put("child_name",name);
                      childata.put("dob",dateOfbirdthglb);
                      childata.put("child_gender",chil_gender);
                      childata.put("class",school_class);
                      childata.put("school",Schoolname);
                      childata.put("pre_branch",preferedBranch);
                      childata.put("child_image",imgeuri);

                      String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                      String random_timephone=phone+currentTime;
                      db.insertdata_childpro(random_timephone);

                      chilDatabase.child(random_timephone).updateChildren(childata).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                              edtview.setVisibility(View.INVISIBLE);
                              rcvview.setVisibility(View.VISIBLE);
                              addchildimg.setVisibility(View.VISIBLE);
                              selectimg.setVisibility(View.INVISIBLE);
                              saveimg.setVisibility(View.INVISIBLE);
                          }
                      });
                      */






                  }else {
                      Toast.makeText(Profiling_moreActivity.this, "Please select your baby pic first", Toast.LENGTH_SHORT).show();
                  }

                }else {

                    Toast.makeText(Profiling_moreActivity.this, "Phone number is required", Toast.LENGTH_SHORT).show();
                    dialogbox_phonenumber();

                     }


            }
        });

        btn_addchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rcvview.setVisibility(View.INVISIBLE);
                edtview.setVisibility(View.VISIBLE);
            }
        });

        saveimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mdialog.setTitle("Updating profile");
                mdialog.setMessage("Please wait...................");
                mdialog.setCanceledOnTouchOutside(false);
                mdialog.show();
                mdialog.show();

               // Map imgdata = new HashMap();

                if(imgeuri!=null){

                    mUser=FirebaseAuth.getInstance().getCurrentUser();
                    final String useridlocal=mUser.getUid();

                    chilDatabase=FirebaseDatabase.getInstance().getReference().child("Childdb").child(useridlocal).child(phone);


                    final StorageReference filepath = mStorage.child("Childimages").child(useridlocal+".jpg");

                    filepath.putFile(imgeuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri chilimg =uri;
                                    imgeuri=chilimg;


                                    Map childimguri = new HashMap();
                                    childimguri.put("image_child",imgeuri.toString());
                                    chilDatabase.updateChildren(childimguri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            saveimg.setVisibility(View.INVISIBLE);
                                            addchildimg.setVisibility(View.VISIBLE);
                                            edtview.setVisibility(View.INVISIBLE);
                                            rcvview.setVisibility(View.VISIBLE);
                                            Toast.makeText(Profiling_moreActivity.this, "upload successful", Toast.LENGTH_SHORT).show();
                                            mdialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("uploaderror", "onFailure: ---------------error "+e.getMessage());
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Profiling_moreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });



                        }
                    });


                }else {
                    Toast.makeText(Profiling_moreActivity.this, "image link is empty", Toast.LENGTH_SHORT).show();

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

    public void dialogbox_phonenumber(){
        AlertDialog.Builder albuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.phonenumber_dialog,null);
        albuilder.setView(view);
        final AlertDialog dialog = albuilder.create();
        if(!isFinishing()){
            dialog.show();
        }

       Button btn_continiue = view.findViewById(R.id.btn_continiue);
        final EditText edtphone=view.findViewById(R.id.idPhone);

        btn_continiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=edtphone.getText().toString();

                if(phone.length()<10){
                    Toast.makeText(Profiling_moreActivity.this, "Please Enter valid phonenumber", Toast.LENGTH_SHORT).show();
                }else if(phone!=null && phone.length()>10){

                    mUser= FirebaseAuth.getInstance().getCurrentUser();
                    String mUserid=mUser.getUid();
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(mUserid);
                    mDatabase.child("phone").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mDatabase.child("number_up").setValue(20);
                                    dialog.dismiss();
                                Toast.makeText(Profiling_moreActivity.this, "Thank you", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });
    }


    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512,512)
                .setAspectRatio(1, 1)
                .start(Profiling_moreActivity.this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgeuri  = result.getUri();
                imgProfile.setImageURI(imgeuri);
                saveimg.setVisibility(View.INVISIBLE);
                addchildimg.setVisibility(View.INVISIBLE);
                selectimg.setVisibility(View.VISIBLE);
                //Log.d(TAG, "onActivityResult: ---------------------------------------------------------------fetche image uri----------------------------------"+imgeuri);

                File orginalImagefile =  new File(imgeuri.getPath());

                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(orginalImagefile);
                    ByteArrayOutputStream bos  = new ByteArrayOutputStream();
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


    @Override
    protected void onStart() {
        super.onStart();

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        userId=mUser.getUid();
        adapterquerydb=FirebaseDatabase.getInstance().getReference().child("Childdb").child(userId);

        options= new FirebaseRecyclerOptions.Builder<Model_childlist>().setQuery(adapterquerydb,Model_childlist.class).build();
        fadapter=new FirebaseRecyclerAdapter<Model_childlist, UserViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewholder userViewholder, int i, @NonNull Model_childlist model_childlist) {

                Picasso.with(getApplicationContext()).load(model_childlist.getChild_image()).into(userViewholder.imgchilpic);

                userViewholder.tv_name.setText(model_childlist.getChild_name());
                userViewholder.tv_dob.setText(model_childlist.getDob());
                userViewholder.tv_gender.setText(model_childlist.getChild_gender());

                final String userid_child=getRef(i).getKey();

                userViewholder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Profiling_moreActivity.this,ChildprofileeditActivity.class).putExtra("userid",userid_child));
                    }
                });

                userViewholder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapterquerydb.child(userid_child).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Profiling_moreActivity.this, "child deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }

            @NonNull
            @Override
            public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.childrenviewadapter,parent,false);

                return new UserViewholder(view);
            }
        };

        fadapter.startListening();
        recyclerView.setAdapter(fadapter);
    }

    public class UserViewholder extends RecyclerView.ViewHolder {

        CircleImageView imgchilpic;
        TextView tv_name,tv_dob,tv_gender;
        Button btn_delete;
        View mView;
        public UserViewholder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;

            imgchilpic=mView.findViewById(R.id.childpicimg);
            tv_name=mView.findViewById(R.id.name_child);
            tv_dob=mView.findViewById(R.id.dob_child);
            tv_gender=mView.findViewById(R.id.gender_child);
            btn_delete=mView.findViewById(R.id.btn_delete_child);
        }
    }


    private  String getphone(DatabaseReference mDatabase){





        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("phone").exists()){


                    phone=dataSnapshot.child("phone").getValue().toString();
                    //Log.d(TAG, "onDataChange: -------------------------phone "+phone);


                }else {
                    Toast.makeText(Profiling_moreActivity.this, "db problem", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return phone;
    }
}
