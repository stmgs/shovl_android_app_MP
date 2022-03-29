package com.example.shovl_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import java.util.Calendar;


public class ActivityDetails extends AppCompatActivity {

    ImageSwitcher img_scroller;
    Button btn_pre,btn_next;
    EditText dp_e,dp_s;  // aa dateTXT che   ...   video ma
    ImageButton toChoose,fromChoose;  // aa "  cal "  che  ...video ma

   int imageList[ ] = {R.drawable.snow_image_1, R.drawable.snow_image_2,R.drawable.snow_image_3,R.drawable.snow_image_4}; // DEMO IMAGES
   int count = imageList.length;
   int currentIndex = 0;

 //  calander mate che aa...
    private int mDate, mMonth, mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);

        btn_next=findViewById(R.id.btn_next);
        btn_pre=findViewById(R.id.btn_pre);
        img_scroller=findViewById(R.id.img_scroller);
        dp_e=findViewById(R.id.dp_e);
        dp_s=findViewById(R.id.dp_s);
        toChoose=findViewById(R.id.toChoose);
        fromChoose=findViewById(R.id.fromChoose);

        img_scroller.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView  = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

        img_scroller.setImageResource(imageList[0]);

        btn_pre.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        img_scroller.setInAnimation(ActivityDetails.this,
                R.anim.from_right);
        img_scroller.setOutAnimation(ActivityDetails.this,
                R.anim.to_left);
        --currentIndex;
        if (currentIndex < 0)
            currentIndex = imageList.length-1;
        img_scroller.setImageResource(imageList[currentIndex]);
        }
    });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_scroller.setInAnimation(ActivityDetails.this,
                        R.anim.from_left);
                img_scroller.setOutAnimation(ActivityDetails.this,
                        R.anim.to_right);

                currentIndex++;
                if (currentIndex == count)
                    currentIndex = 0;
                img_scroller.setImageResource(imageList[currentIndex]);
            }
        });
        

 // Date choose karva mate calander event call kari chee...
        toChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);
                mYear = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityDetails.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        dp_s.setText(date+"-"+month+"-"+year);
                    }
                }, mYear, mMonth, mDate);
  // past nij date select karva devi hoy to  ->  // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        fromChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);
                mYear = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(ActivityDetails.this,
                                android.R.style.Theme_DeviceDefault_Dialog,
                                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        dp_e.setText(date+"-"+month+"-"+year);
                    }
                }, mYear, mMonth, mDate);
                // past nij date select karva devi hoy to  ->  // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();

            }
        });





    }
}