package com.example.takepic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    ArrayList<user> arrayList;




    public MyAdapter(ArrayList<user> arrayList){
        this.arrayList=arrayList;

    }
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.place.setText(arrayList.get(position).getPlace());
        holder.chiefEngineer.setText(arrayList.get(position).getChiefEngineer());
        holder.telephone.setText(arrayList.get(position).getTelephone());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView place,chiefEngineer,telephone;
        public MyViewHolder(@NonNull View view){
            super(view);
            place=itemView.findViewById(R.id.tvplace);
            chiefEngineer=itemView.findViewById(R.id.tvname);
            telephone=itemView.findViewById(R.id.tvnumber);

        }
    }
    //Context context;
    //ArrayList<user> userArrayList;
    //@NonNull
    //@Override
    //public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      // View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        //return new MyViewHolder(v);
    //}

    //@Override
    //public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
      //   user user=userArrayList.get(position);
        // holder.place.setText(user.assemblyConstituency);
         //holder.chiefEngineer.setText(user.chiefEngineer);
         //holder.telephone.setText(String.valueOf(user.telephone));

    }

    //public MyAdapter(ArrayList<user> userArrayList) {
      //  this.context = context;
        //this.userArrayList = userArrayList;
    //}

    //@Override
    //public int getItemCount() {
      //  return userArrayList.size();
    //}
    //public static class MyViewHolder extends RecyclerView.ViewHolder {
      // TextView place,chiefEngineer,telephone;
        //public MyViewHolder(@NonNull View itemView) {
          //  super(itemView);
            //place=itemView.findViewById(R.id.tvplace);
            //chiefEngineer=itemView.findViewById(R.id.tvname);
            //telephone=itemView.findViewById(R.id.tvnumber);
        //}
    //}
//}
