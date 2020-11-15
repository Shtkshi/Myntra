package com.example.myntra;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final String serverUrl = "http://192.168.1.240:5000";
    // colour toh bharo isme, kisse kya chnage hona chaiye
    static ArrayList<Map<String, String>> colour = new ArrayList<Map<String, String>>() {
        {
            add(new HashMap<String, String>());
            add(new HashMap<String, String>() {// yaha 4 hone chahiye na
                {
                    put("#FFFFFF", "#123456");
                    put("#FFC0CB", "#654321");
                    put("#000000", "#132436");
                    put("#F7913C", "#142536");
                    put("#2162F9", "#321654");
                    put("#132436", "#000000");
                    put("#FF0000", "#86CB13");
                }
            });
            add(new HashMap<String, String>() {
                {
                    put("#FFFFFF", "#123456");
                    put("#FFC0CB", "#654321");
                    put("#000000", "#132436");
                    put("#132436", "#000000");
                    put("#F7913C", "#142536");
                    put("#2162F9", "#321654");
                    put("#FF0000", "#86CB13");
                }
            });
            add(new HashMap<String, String>() {
                {
                    put("#FFFFFF", "#13CB86");
                    put("#FFC0CB", "#0000FF");
                    put("#000000", "#FF0000");
                    put("#F7913C", "#800000");
                    put("#2162F9", "#FFFF00");
                    put("#FF0000", "#000000");
                }
            });
        }
    }; // add for all dis haaan samjha okay aaj papa itni jaldi kyu so gye? thek gaye okay ayr iemae vo api wala bhi change karna hai

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
        VolleyMultipartRequest req = new VolleyMultipartRequest(Request.Method.POST, serverUrl + "/updateImage?disease=" + disease, new Response.Listener<NetworkResponse>() {
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


    public static void change(ViewGroup layout, int disease, AppCompatActivity activity) {
        if (disease == 0 || layout == null) {
            return;
        }
        changeLayout(layout, disease);
        String hexColorTitle = String.format("#%06X", (0xFFFFFF & activity.getTitleColor()));
/*
        int hexColorBGint = 0;
        if (activity.getSupportActionBar().getCustomView().getBackground() instanceof ColorDrawable) {
            hexColorBGint = ((ColorDrawable) activity.getSupportActionBar().getCustomView().getBackground()).getColor();
        } else if (layout.getBackground() instanceof GradientDrawable) {
            hexColorBGint = ((GradientDrawable) activity.getSupportActionBar().getCustomView().getBackground()).getColor().getDefaultColor();
        }
        String hexColorBG = String.format("#%06X", (0xFFFFFF & hexColorBGint));
*/
        activity.getSupportActionBar().setTitle(Html.fromHtml("<font color="+colour.get(disease).get(hexColorTitle)+">ActionBarTitle </font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
        int hexColorBGint = Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(activity, R.color.colorPrimary)));
        String hexColorBG = String.format("#%06X", (0xFFFFFF & hexColorBGint));
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor(colour.get(disease).get(hexColorBG)));

        // Set BackgroundDrawable

        activity.getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    private static void changeLayout(ViewGroup layout, int disease) {
        if (disease == 0 || layout == null) {
            return;
        }
        int hexColorBGint = 0;
        if (layout.getBackground() instanceof ColorDrawable) {
            hexColorBGint = ((ColorDrawable) layout.getBackground()).getColor();
        } else if (layout.getBackground() instanceof GradientDrawable) {
            hexColorBGint = ((GradientDrawable) layout.getBackground()).getColor().getDefaultColor();
        }
        String hexColorBG = String.format("#%06X", (0xFFFFFF & hexColorBGint));
        Log.d("color1", hexColorBG );
        int c = Color.parseColor(colour.get(disease).get(hexColorBG));
        Log.d("color22", String.valueOf(c) );
        layout.setBackgroundColor(c);

        int len = layout.getChildCount();
        for (int i = 0; i < len; i++) {
            if (layout.getChildAt(i) instanceof ViewGroup) {
                //changeLayout(layout, disease);
            } else {
                if (layout.getChildAt(i) instanceof TextView) {
                    String hexColor = String.format("#%06X", (0xFFFFFF & ((TextView) layout.getChildAt(i)).getCurrentTextColor()));
                    ((TextView) layout.getChildAt(i)).setTextColor(Color.parseColor(colour.get(disease).get(hexColor)));
                }
                hexColorBGint = 0;
                if (layout.getChildAt(i).getBackground() instanceof ColorDrawable) {
                    hexColorBGint = ((ColorDrawable)  layout.getChildAt(i).getBackground()).getColor();
                } else if (layout.getChildAt(i).getBackground() instanceof GradientDrawable) {
                    hexColorBGint = ((GradientDrawable) layout.getChildAt(i).getBackground()).getColor().getDefaultColor();
                }
                hexColorBG = String.format("#%06X", (0xFFFFFF & hexColorBGint));
                layout.getChildAt(i).setBackgroundColor(Color.parseColor(colour.get(disease).get(hexColorBG)));

            }
        }

    }
}
