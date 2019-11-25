package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.babuland1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.itangqi.waveloadingview.WaveLoadingView;

public class Profiling_moreActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private String preferedBranch;
    private EditText edt_childname;
    private EditText edt_class;
    private EditText edt_schoolname;
    private Button btn_savechange;

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private String userId;

    private int progress;

    WaveLoadingView mWaveLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_more);

        mWaveLoadingView=(WaveLoadingView) findViewById(R.id.waveLoadingView);

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progress =dataSnapshot.child("progressvalue").getValue(Integer.class);

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
          }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        preferedBranch=spinner.getSelectedItem().toString();
        Log.d("preferedbranch", "onCreate:------------------------------------------------------spiner item _ branch "+ preferedBranch);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
