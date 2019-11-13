package com.example.babuland1.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.babuland1.R;

public class Viewpageradapter extends PagerAdapter {




    Context context;
    LayoutInflater layoutInflater;

    public Viewpageradapter(Context context) {
        this.context = context;
    }

    public int[]allimage={
            R.drawable.fragment2,
            R.drawable.fragment3,
            R.drawable.fragment4,
            R.drawable.frgment1,
            R.drawable.fragment6
    };
    public String[] headline={
        "Have a ride guys","we are having fun","game is fun","ha ha we are gang","we are flying"
    };

    @Override
    public int getCount() {
        return allimage.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slidelayout,container,false);
        ImageView imageView=view.findViewById(R.id.slid_image);
        TextView tvHeadline = view.findViewById(R.id.sliderheading);


        imageView.setImageResource(allimage[position]);
        tvHeadline.setText(headline[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
