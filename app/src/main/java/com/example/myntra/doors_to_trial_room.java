package com.example.myntra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class doors_to_trial_room extends AppCompatActivity {
    Button door;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doors_to_trial_room);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        final int picture=extras.getInt("productId");
        findViewById(R.id.trial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doors_to_trial_room.this, UploadActivity.class);
                intent.putExtra("productId", picture);
                startActivity(intent);
            }
        });
    }
}