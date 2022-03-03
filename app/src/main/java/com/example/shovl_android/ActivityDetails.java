package com.example.shovl_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class ActivityDetails extends AppCompatActivity {

    ImageView img_view;
    Button btn_pre,btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);

        btn_next=findViewById(R.id.btn_next);
        btn_pre=findViewById(R.id.btn_pre);
        img_view=findViewById(R.id.img_view);

    }

     public void img_view_setter(View v)
     {

     }


  /*  @Override
    public void onClick(View view) {

        int image_index = 0;
        private static final int MAX_IMAGE_COUNT = 3;

        private int[] mImageIds = {
                R.raw.image1,
                R.raw.image2,
                R.raw.image3
    }*/


}