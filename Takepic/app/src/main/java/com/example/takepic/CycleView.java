package com.example.takepic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class CycleView extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<user> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    //ProgressDialog progressDialog;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_view);



        //progressDialog=new ProgressDialog(this);
        //progressDialog.setCancelable(false);
        //progressDialog.setMessage("fetching data...");
        //progressDialog.show();

        recyclerView=findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseFirestore.getInstance();
        userArrayList=new ArrayList<>();
        myAdapter=new MyAdapter(userArrayList);
        recyclerView.setAdapter(myAdapter);

        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d:list){
                            user obj=d.toObject(user.class);
                            userArrayList.add(obj);
                        }
                        myAdapter.notifyDataSetChanged();

                    }
                });

    }


   // private void EventChangeListener() {
       // db.collection("bbmp_data").addSnapshotListener(new EventListener<QuerySnapshot>() {
              //      @Override
                 //   public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                   //     if(error!=null){
                            //if (progressDialog.isShowing())
                              //  progressDialog.dismiss();
                      //      Log.e("Firestore error",error.getMessage());
                       //     return;

}
//                        for (DocumentChange dc:value.getDocumentChanges()){
//                           if(dc.getType()==DocumentChange.Type.ADDED){
//                               userArrayList.add(dc.getDocument().toObject(user.class));
//                           }
//                           myAdapter.notifyDataSetChanged();
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//



                  //  }
               // });
   // }
