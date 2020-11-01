package com.example.myntra;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat colorBlindSwitch;
    private static final int ISHIARA_TEST_RESULT_CODE = 1000;
    boolean resultEvaluated = false;
    boolean flag = false;
    int disease;
    int RecordAudioRequestCode = 1000;
    SpeechRecognizer speechRecognizer;
    boolean colorblind;
    int women = R.drawable.womenicon;
    int men = R.drawable.menicon;

    int e = R.drawable.e;
    int h = R.drawable.h;
    int g = R.drawable.g;
    int c = R.drawable.c;
    int b = R.drawable.b;
    int f = R.drawable.f;


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    Intent speechRecognizerIntent;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        boolean blind = extras.getBoolean("blind");
        disease = extras.getInt("disease");

        if (blind) {
            tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        Utils.speak(tts, "Which category you want to choose? Men or the Women");
                    } else {
                        Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                    }
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
        findViewById(R.id.male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean t = colorBlindSwitch.isChecked();
                Intent i = new Intent(MainActivity.this, Male_dress1.class);
                i.putExtra("flag", flag);
                i.putExtra("disease", disease);
                i.putExtra("colorblind", t);
                startActivity(i);
            }
        });
        findViewById(R.id.female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean t = colorBlindSwitch.isChecked();
                Intent i = new Intent(MainActivity.this, Dress1.class);
                i.putExtra("flag", flag);
                i.putExtra("disease", disease);
                i.putExtra("colorblind", t);
                startActivity(i);


            }
        });
        colorBlindSwitch = findViewById(R.id.colorBlindSwitch);
        colorBlindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startActivityForResult(new Intent(MainActivity.this, Ishihara.class), ISHIARA_TEST_RESULT_CODE);
//
                } else {
                    disease = 0;
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), men), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.male)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), women), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.female)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), e), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.e)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), h), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.h)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), g), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.g)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), c), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.c)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), b), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.b)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), f), disease, new Utils.ImageReceived() {
                        @Override
                        public void onImageReceivedSuccess(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.f)).setImageBitmap(bitmap);
                        }

                        @Override
                        public void onImageReceivedError(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (tts != null)
            tts.stop();
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ISHIARA_TEST_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    int score = data.getIntExtra("result", 0);
                    disease = data.getIntExtra("disease", 0);
                    Toast.makeText(MainActivity.this, "Result Received" + score, Toast.LENGTH_LONG).show();
                    resultEvaluated = true;
                    Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), men), disease, new Utils.ImageReceived() {
                        @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.male)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), women), disease, new Utils.ImageReceived() {
                    @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.female)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), e), disease, new Utils.ImageReceived() {
                    @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.e)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), h), disease, new Utils.ImageReceived() {
                    @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.h)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), g), disease, new Utils.ImageReceived() {
                    @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.g)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), c), disease, new Utils.ImageReceived() {
                    @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.c)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), b), disease, new Utils.ImageReceived() {
                    @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.b)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                Utils.fetchColourBlindImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), f), disease, new Utils.ImageReceived() {
                    @Override
                    public void onImageReceivedSuccess(Bitmap bitmap) {
                        ((ImageView) findViewById(R.id.f)).setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageReceivedError(VolleyError e) {
                        e.printStackTrace();
                    }
                });
                }

            } else {
                colorBlindSwitch.setChecked(false);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        boolean blind = extras.getBoolean("blind");
        if (blind == true && requestCode == RecordAudioRequestCode && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            Log.d("RESULT", result.get(0));
            if (result.get(0).toLowerCase().contains("women") || result.get(0).toLowerCase().contains("female") || result.get(0).toLowerCase().contains("ladki") || result.get(0).toLowerCase().contains("girl")) {
                Intent intent = new Intent(MainActivity.this, Dress1.class);
                intent.putExtra("blind", true);
                finish();
                startActivity(intent);
            } else if (result.get(0).toLowerCase().contains("men") || result.get(0).toLowerCase().contains("male") || result.get(0).toLowerCase().contains("boy") || result.get(0).toLowerCase().contains("ladke")) {
                Intent intent = new Intent(MainActivity.this, Male_dress1.class);
                intent.putExtra("blind", true);
                finish();
                startActivity(intent);
            }
        }

    }


    TextToSpeech tts;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_1:
                return true;
            case R.id.menu_2:
                return true;
            case R.id.menu_3:
                return true;
            case R.id.menu_4:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}