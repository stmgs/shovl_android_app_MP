package com.example.shovl_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeamInformationPage extends AppCompatActivity {


    Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_information_page);

         backbtn=(Button) findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),Splashscreen.class);
                startActivity(intent);


            }
        });

    }

}