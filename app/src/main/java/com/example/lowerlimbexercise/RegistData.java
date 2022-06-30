package com.example.lowerlimbexercise;

import android.os.Bundle;

import java.util.Calendar;

public class RegistData extends AccountDataUtilities{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void SetData(AncleexData ad){
        System.out.println("++RegistData:SetData()");


        //DBへデータを追加
        mDbh.insertData(ad);
        System.out.println("--RegistData:SetData()");

    }
}
