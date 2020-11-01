package com.example.myntra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final String serverUrl = "http://192.168.1.240:5000"; // set the static server url to this url

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
        if (!png)
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        else
            bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }

    public interface ImageReceived {
        void onImageReceivedSuccess(Bitmap bitmap);

        void onImageReceivedError(VolleyError e);
    }

    public static void fetchColourBlindImage(Context context, final Bitmap bmp, int disease, final ImageReceived imageReceived) {
        if (disease == 0) {
            imageReceived.onImageReceivedSuccess(bmp);
        }
        VolleyMultipartRequest req = new VolleyMultipartRequest(Request.Method.POST, "", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                imageReceived.onImageReceivedSuccess(BitmapFactory.decodeByteArray(response.data, 0, response.data.length));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageReceived.onImageReceivedError(error);
            }
        }) {
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> data = new HashMap<>();
                DataPart part = new VolleyMultipartRequest.DataPart("image0.jpg", toByteArray(bmp, false), "image/jpeg");
                ;
                data.put("1", part);
                return data;
            }
        };
        req.setShouldCache(false);
        req.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                1));
        Volley.newRequestQueue(context).add(req);
    }

    public static void speak(TextToSpeech tts, String message) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "InitText");
        Log.i("TTS", "Initialization success.");
    }

}
