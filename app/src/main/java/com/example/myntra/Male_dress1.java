package com.example.myntra;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Objects;

public class Male_dress1 extends AppCompatActivity {
    int disease;
    Boolean colorblind;
    int[] imgID = {R.drawable.male1, R.drawable.male1_d, R.drawable.male1_p,R.drawable.male1_t};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_dress1);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        boolean flag = extras.getBoolean("flag");
        boolean blind=extras.getBoolean("blind");
        findViewById(R.id.tryIt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Male_dress1.this, doors_to_trial_room.class);
                intent.putExtra("productId", R.drawable.male1);
                startActivity(intent);
            }
        });

        if (flag||blind) {
            AudioMode();
        }
        disease=extras.getInt("disease");
        colorblind=extras.getBoolean("colorblind");
        if(colorblind)
            ((ImageView) findViewById(R.id.male_dress1)).setImageResource(imgID[disease]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (tts != null)
            tts.stop();
        super.onBackPressed();
    }

    private void AudioMode() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    initTTs();
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }, "com.google.android.tts");


    }

    TextToSpeech tts;

    public void initTTs() {
        int ttsLang = tts.setLanguage(Locale.US);

        if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "The Language is not supported!");
        } else {
            Log.i("TTS", "Language Supported.");
        }
        tts.speak("This Tshirt has following product specification :Black solid T-shirt, has a round neck, three-quarter sleeves. Material of the Tshirt is Cotton and is regular fit, length is regular. You will get single peice in the purchase. This Tshirt is available in 5 categories.Extra small, Small , Medium, Large and Extra Large",
                TextToSpeech.QUEUE_FLUSH, null, "InitText");
        Log.i("TTS", "Initialization success.");
    }

}