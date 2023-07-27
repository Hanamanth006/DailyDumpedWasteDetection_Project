package com.example.takepic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firestore extends AppCompatActivity {
    Button btnfetch,cycleview;
    FirebaseFirestore dbroot;
    TextView tv,tv1,tv2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);
        btnfetch=(Button) findViewById(R.id.btnfetch);
        tv=(TextView) findViewById(R.id.textViewfire);
        tv1=(TextView)findViewById(R.id.textViewfire2);
        tv2=(TextView)findViewById(R.id.textViewfire3);

        cycleview=(Button)findViewById(R.id.cycle);


        dbroot=FirebaseFirestore.getInstance();
        cycleview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Firestore.this,lastbutone.class);
                startActivity(i);
            }
        });

        btnfetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchdata();
            }
        });

    }

    public void fetchdata()
    {
        DocumentReference document=dbroot.collection("users").document("row1");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    tv.setText(documentSnapshot.getString("place"));
                    tv1.setText(documentSnapshot.getString("chiefEngineer"));
                    tv2.setText(documentSnapshot.getString("telephone"));
                    //tv2.setText(documentSnapshot.getString("wardNames"));


                }
                else
                    Toast.makeText(getApplicationContext()," row not found",Toast.LENGTH_SHORT).show();

            }
        });
        document.get().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to fetch data",Toast.LENGTH_LONG).show();
                    }
                });
    }


}