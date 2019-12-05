package com.babuland.babuland1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.babuland.babuland1.R;

public class Viewpageradapter_homeslider extends PagerAdapter {

    LayoutInflater inflater;
    Context context;
    public int[]allimage={
            R.drawable.babu1,
            R.drawable.babu2,
            R.drawable.babu3,
            R.drawable.babu4,
            R.drawable.babu5,
            R.drawable.babu6,
            R.drawable.babu7
    };

    public Viewpageradapter_homeslider(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return allimage.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== (View) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.sliderlayout_homepage,container,false);
        ImageView imageView =view.findViewById(R.id.slider_image);
        imageView.setImageResource(allimage[position]);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
