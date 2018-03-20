package com.example.bhonesh.railwayapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bhonesh.railwayapi.MySingleton;
import com.example.bhonesh.railwayapi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Display extends AppCompatActivity {
    final String TAG = "check";
    private String train;
    Button seat_avail,train_between,pnr_check,route,fare;
    TextView days,runst,trainnameT,trainnumberT;
    ProgressDialog pd,pd2;
    List<String> list1=new ArrayList<>();
    AutoCompleteTextView simpleAutoCompleteTextView,simpleAutoCompleteTextView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        train_between=(Button)findViewById(R.id.train_between);
        pnr_check=(Button)findViewById(R.id.pnr_check);
        fare=(Button)findViewById(R.id.fare);
        route=(Button)findViewById(R.id.route);
        seat_avail=(Button)findViewById(R.id.seat_avail);

        train_between.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Display.this,train_between.class);
                startActivity(i);
            }
        });
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Display.this,train_route.class);
                startActivity(i);
            }
        });
        fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Display.this,fare_inquiry.class);
                startActivity(i);
            }
        });
        seat_avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Display.this,seat_enquiry.class);
                startActivity(i);
            }
        });

        /*
        String url = "https://api.railwayapi.com/v2/route/train/"+train+"/apikey/jlmkkybs4g/";
//        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Log.d(TAG, response);
                        JSONObject jObject  = null;
                        try {
                            jObject = new JSONObject(response);
                            JSONObject jsonObject = jObject.getJSONObject("train");
                            // String traintname = jsonObject.getString("name");
                            trainnameT.setText(jsonObject.getString("name").toString());
                            trainnumberT.setText(jsonObject.getString("number").toString());
                            JSONArray jsonarray = new JSONArray(jsonObject.getString("days"));
                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String runs       = jsonobject.getString("runs");
                                String day       = jsonobject.getString("code");
                                days.append("  "+day.substring(0,1).trim()+"  ");
                                runst.append("  "+runs.trim()+"  ");
                                pd.hide();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pd.hide();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Log.d(TAG, error.toString());
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );
         MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

*/

        pnr_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i= new Intent(Display.this,pnr_check.class);
                startActivity(i);
            }
        });


    }

}
