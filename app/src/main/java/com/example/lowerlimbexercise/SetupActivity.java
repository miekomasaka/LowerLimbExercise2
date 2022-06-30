package com.example.lowerlimbexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }
}*/

public class SetupActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        findViewById(R.id.Next2_button).setOnClickListener(this);
    }
    //ボタンが押された時の処理
    public void onClick(View view){
        Intent intent = new Intent(this, SelectexerciseActivity.class);  //インテントの作成
        startActivity(intent);                                 //画面遷移
    }
}