package com.example.bhonesh.railwayapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fare_inquiry extends AppCompatActivity {


    EditText editText,editText5,editText6,editText7,editText8,editText9,editText11,editText12;
    TextView textView22;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_inquiry);

        submit=(Button)findViewById(R.id.submit);
        editText=(EditText)findViewById(R.id.editText);
        editText5=(EditText)findViewById(R.id.editText5);
        editText6=(EditText)findViewById(R.id.editText6);
        editText7=(EditText)findViewById(R.id.editText7);
        editText8=(EditText)findViewById(R.id.editText8);
        editText9=(EditText)findViewById(R.id.editText9);
        editText11=(EditText)findViewById(R.id.editText11);
        editText12=(EditText)findViewById(R.id.editText12);




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = editText.getText().toString();
                String b = editText5.getText().toString();
                String c = editText6.getText().toString();
                String d = editText7.getText().toString();
                String e = editText8.getText().toString();
                String f = editText9.getText().toString();
                String g = editText11.getText().toString();

                String url = "https://api.railwayapi.com/v2/fare/train/"+a+"/source/"+b+"/dest/"+c+"/age/"+d+"/pref/"+e+"/quota/"+f+"/date/"+g+"/apikey/jlmkkybs4g/";


                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject jObject  = null;
                                try {
                                    jObject = new JSONObject(response);
                                     String traintfare = jObject.getString("fare");
                                  editText12.setText(traintfare);
                                     Log.e("hi",traintfare);





                                } catch (JSONException e) {
                                    e.printStackTrace();


                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error != null){
                                    //Log.d(TAG, error.toString());
                                    //Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                );
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



            }
        });


    }
}
