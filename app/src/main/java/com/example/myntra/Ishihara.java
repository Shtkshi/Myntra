package com.example.myntra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Ishihara extends AppCompatActivity {
    Button submitIshihara;
    boolean eval = false;
    int count = 0;
    int[] drawables = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine, R.drawable.ten, R.drawable.eleven, R.drawable.twelve, R.drawable.thirteen, R.drawable.forteen, R.drawable.fifteen, R.drawable.sixteen, R.drawable.seventeen, R.drawable.eighteen, R.drawable.nineteen, R.drawable.twenty, R.drawable.twentyone};
    String[] solution = {"12", "8", "6", "29", "57", "5", "3", "15", "74", "2", "6", "97", "45", "5", "7", "16", "73", "26", "42", "35", "96"};
    int True = 0, ones = 0, tens = 0, False = 0;
    int disease = Utils.Disease.None.getNumVal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ishihara);
        ((ImageView) findViewById(R.id.mainFrame)).setImageDrawable(ContextCompat.getDrawable(Ishihara.this, drawables[0]));
        findViewById(R.id.colorBlindInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.frame2).setVisibility(View.GONE);
                findViewById(R.id.frame1).setVisibility(View.VISIBLE);
            }
        });
        submitIshihara = findViewById(R.id.submitIshiara);
        submitIshihara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitIshihara.getText().toString().toLowerCase().contains("skip")) {
                    findViewById(R.id.frame2).setVisibility(View.GONE);
                    findViewById(R.id.frame1).setVisibility(View.VISIBLE);
                    submitIshihara.setText("Next >");
                    return;
                }
                if (submitIshihara.getText().toString().toLowerCase().contains("return")) {
                    finish();
                    return;
                }

                String editAns = ((TextInputEditText) findViewById(R.id.answer)).getText().toString();
                if (editAns.isEmpty()) {
                    Toast.makeText(Ishihara.this, "Please enter some text.", Toast.LENGTH_LONG).show();
                    return;
                }
                editAns = editAns.trim();
                eval = editAns.equals(solution[count]);
                if (eval) {
                    Toast.makeText(Ishihara.this, "Correct answer", Toast.LENGTH_SHORT).show();
                    True++;
                } else {
                    if ((editAns.charAt(editAns.length() - 1)) == (solution[count].charAt(solution[count].length() - 1))) {
                        ones++;
                    }
                    if ((editAns.charAt(0)) == (solution[count].charAt(0))) {
                        tens++;
                    }
                    False++;
                    Toast.makeText(Ishihara.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                }
                if (count == drawables.length - 1) {
                    // test finish do something kya karu? tu nahi, main hi thinking
                    findViewById(R.id.frame1).setVisibility(View.GONE);
                    findViewById(R.id.frame2).setVisibility(View.VISIBLE);
                    if (False >= 4 && ones == False) {
                        disease = Utils.Disease.DName.getNumVal();
                    } else if (False >= 4 && tens == False) {
                        disease = Utils.Disease.PName.getNumVal();
                    } else {
                        disease = Utils.Disease.TName.getNumVal();
                    }
                    ((AppCompatTextView) findViewById(R.id.colorBlindInfo)).setText("You were correct on " + String.valueOf(True) + "/" + drawables.length + " images.");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", True);
                    resultIntent.putExtra("disease", disease);
                    setResult(Activity.RESULT_OK, resultIntent);
                    submitIshihara.setText("Return to the main menu.");
                    return;
                }
                if (count == drawables.length - 2) {
                    submitIshihara.setText("Submit");
                }
                count++;
                ((TextInputEditText) findViewById(R.id.answer)).setText("");
                ((ImageView) findViewById(R.id.mainFrame)).setImageDrawable(ContextCompat.getDrawable(Ishihara.this, drawables[count]));
                eval = false;
            }
        });
    }
}
