package com.example.lowerlimbexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/*public class SelectexerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectexercise);
    }
}*/

public class SelectexerciseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectexercise);
 //       findViewById(R.id.Next2_button).setOnClickListener(this);
        ImageButton ancleexImageButton = (ImageButton)findViewById(R.id.imageButton);
        ancleexImageButton.setImageResource(R.drawable.ancleicon);

        // perform click event on button's
        ancleexImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Home Button",Toast.LENGTH_LONG).show();// display the toast on home button click
                //Intent intent = new Intent( getApplication(),AncleexerciseActivity.class);
                Intent intent = new Intent(getApplication(), AncleExerciseActivity.class);

                startActivity(intent);                                 //画面遷移
            }
        });

    }

}