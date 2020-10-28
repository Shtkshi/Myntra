package com.example.myntra;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    int RecordAudioRequestCode = 1000;
    SpeechRecognizer speechRecognizer;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    Intent speechRecognizerIntent; //chala? hi chala? video bheji to do bara chalaya ai permisison magta hai? vo pehle hi mang liya hai


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //initRecog();
    }

    boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS && !done) {
                    done = true;
                    Utils.speak(tts, "Do you want to enable audio mode? Please speak your choice after this message ends."); // smajha? haan oka
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createDialog();
                    }
                }, 2000);
            }
        }, "com.google.android.tts");
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                tts.stop();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Speak Up");
                try {
                    startActivityForResult(intent, RecordAudioRequestCode);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, your device doesn't support speech input.",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(String utteranceId) {
            }
        });
    }

    TextToSpeech tts;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RecordAudioRequestCode && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("RESULT", result.get(0));
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("blind", result.get(0).toLowerCase().contains("yes") || result.get(0).toLowerCase().contains("true")||result.get(0).toLowerCase().contains("yeah")||result.get(0).toLowerCase().contains("yup")||result.get(0).toLowerCase().contains("on"));
            finish();
            startActivity(intent);
        }

    }

    public void createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_blind, null, false);//why null? samjha?
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        dialogView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("blind", true);
                finish();
                startActivity(intent);

                alertDialog.cancel();

            }
        });
        dialogView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("blind", false);
                finish();
                startActivity(intent);

            }
        });
        alertDialog.show();
    }

}