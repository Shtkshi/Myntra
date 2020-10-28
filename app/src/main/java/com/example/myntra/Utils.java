package com.example.myntra;

import android.graphics.Bitmap;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Utils {
    public enum Disease {
        None(0),
        DName(1),
        PName(2),
        TName(3);

        private int numVal;

        Disease(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    public static byte[] toByteArray(Bitmap bmp, boolean png) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(!png)
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        else
            bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }


    public static void speak(TextToSpeech tts, String message) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "InitText");
        Log.i("TTS", "Initialization success.");
    }

}
