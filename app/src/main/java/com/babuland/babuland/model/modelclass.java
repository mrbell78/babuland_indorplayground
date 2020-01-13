package com.babuland.babuland.model;

import com.babuland.babuland.R;

import java.util.ArrayList;
import java.util.List;

public class modelclass {

    int img;



    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public static List<modelclass> getobject(){
        List<modelclass> datalist = new ArrayList<>();
        int []imges= getImage();

        for(int i=0;i<imges.length;i++){
            modelclass model = new modelclass();
            model.setImg(imges[i]);
            datalist.add(model);
        }
        return datalist;
    }


    private static int[] getImage(){
        int [] image={R.drawable.default_baby};
        return image;
    }
}
