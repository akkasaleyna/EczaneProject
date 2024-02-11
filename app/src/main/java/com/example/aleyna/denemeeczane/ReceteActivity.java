package com.example.aleyna.denemeeczane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ReceteActivity extends AppCompatActivity {

    private Button buttonEczBul2, buttonFiyatGor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recete);

        buttonEczBul2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(ReceteActivity.this,SonucEczaneActivity.class);
                startActivity(intent);
            }
        });

        buttonFiyatGor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 intent= new Intent(ReceteActivity.this,FiyatActivity.class);
                 startActivity(intent);
            }
        });
    }
}
