package com.example.takepic;

public class user {
    String place,chiefEngineer,telephone;


    public user(){

    }

    public user(String place,String chiefEngineer,String telephone){
        this.place=place;
        this.chiefEngineer=chiefEngineer;
        this.telephone=telephone;
    }

    public String getPlace(){
        return place;
    }
    public void setPlace(String place){
        this.place=place;
    }
    public String getChiefEngineer(){
        return chiefEngineer;
    }
    public void setChiefEngineer(String chiefEngineer){
        this.chiefEngineer=chiefEngineer;
    }
    public String getTelephone(){
       return telephone;
    }
    public void setTelephone(String telephone){
       this.telephone=telephone;
    }
}
