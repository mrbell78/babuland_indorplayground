package com.babuland.babuland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.babuland.babuland.R;

public class QuizresultActivity extends AppCompatActivity {

    TextView tv_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizresult);

        tv_score=findViewById(R.id.scortv);

        Intent intent = getIntent();

        int correct = intent.getIntExtra("correctans",0);
        int total = intent.getIntExtra("total",0);
        total=total-1;

        tv_score.setText(Integer.toString(correct)+"/"+Integer.toString(total));
    }
}
