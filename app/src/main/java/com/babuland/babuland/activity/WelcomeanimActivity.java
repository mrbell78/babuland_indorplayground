package com.babuland.babuland.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.babuland.babuland.MainActivity;
import com.babuland.babuland.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class WelcomeanimActivity extends AppCompatActivity {

    private ImageView imglogo;
    private TextView tv_splash;
    String TAG="splash";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescan);

        imglogo=findViewById(R.id.imgbabuland);
        tv_splash=findViewById(R.id.tv_titlebabuland);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splashanim);
        imglogo.startAnimation(myanim);
        tv_splash.startAnimation(myanim);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            Log.e(TAG, " my referlink "+deepLink.toString());
                            //   "http://www.blueappsoftware.com/myrefer.php?custid=cust123-prod456"
                            String referlink = deepLink.toString();
                            try {

                                referlink = referlink.substring(referlink.lastIndexOf("=")+1);
                                Log.e(TAG, " substring "+referlink); //cust123-prod456

                                String custid = referlink.substring(0, referlink.indexOf("-"));
                                String prodid = referlink.substring(referlink.indexOf("-")+1);

                                Log.e(TAG, " custid "+custid +"----prpdiid "+ prodid);

                                // shareprefernce (prodid, custid);
                                //sharepreference  (refercustid, custid)



                            }catch (Exception e){
                                Log.e(TAG, " error "+e.toString());
                            }


                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDynamicLink:onFailure", e);
                    }
                });

        Thread timer = new Thread(){

            public void run(){

                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        };
        timer.start();
    }
}
