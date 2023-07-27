package com.example.takepic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class second extends AppCompatActivity {
    Button detectgarbage, classifyimage,next;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        detectgarbage = findViewById(R.id.button2);
        classifyimage= (Button) findViewById(R.id.button4);
        next=(Button) findViewById(R.id.unique);
        classifyimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(second.this,imageclassification.class);
                startActivity(intent);
            }
        });


        detectgarbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://teachablemachine.withgoogle.com/models/8gMm0JZTj/");

            }

            private void gotoUrl(String s) {
                Uri uri = Uri.parse(s);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }


        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(second.this,Firestore.class);
                startActivity(intent);
            }
        });

    }

}
