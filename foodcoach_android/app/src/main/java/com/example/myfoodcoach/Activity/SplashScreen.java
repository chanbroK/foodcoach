package com.example.myfoodcoach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfoodcoach.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {
    public static SharedPreferences sharedPreferences;
    private final String GCP_URL = "https://www.gs1.org/sites/default/files/docs/gcp_length/gcpprefixformatlist.json";

    private Animation anim;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("GCPPrefixFormat", MODE_PRIVATE);

        textView = findViewById(R.id.tv_logo_foodCoach);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_splash_textview);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (sharedPreferences.getString("date", null) == null) {
                    storeGCPData();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView.startAnimation(anim);
    }

    /**
     * GCPData를 Application에 종속되게 저장한다.
     * sharedPreferences는 바코드를 URI로 변환할 때 사용한다.
     */
    private void storeGCPData() {
        sharedPreferences = getPreferences(MODE_PRIVATE);

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(SplashScreen.this);
        // Request a JsonObject response from the provided GCP_URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GCP_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject gcpPrefixFormatList = response.getJSONObject("GCPPrefixFormatList");
                    String date = gcpPrefixFormatList.getString("date");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("date", date);

                    JSONArray entryArray = gcpPrefixFormatList.getJSONArray("entry");
                    for (int i = 0; i < entryArray.length(); i++) {
                        JSONObject entry = entryArray.getJSONObject(i);
                        String prefix = entry.getString("prefix");
                        int gcpLength = entry.getInt("gcpLength");

                        editor.putInt(prefix, gcpLength);
                    }

                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR", "HTTP NETWORK ERROR");
            }
        });

        queue.add(jsonObjectRequest);
    }

}