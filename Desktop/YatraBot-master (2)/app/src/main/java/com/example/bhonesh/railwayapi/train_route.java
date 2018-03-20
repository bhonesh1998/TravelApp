package com.example.bhonesh.railwayapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class train_route extends AppCompatActivity {

    EditText trainnum,editText3,editText4;
    Button submit;
    ListView route_list;
    DataAdapter adapter;
    List<data> uploadList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_route);

        trainnum=(EditText)findViewById(R.id.editText2);
        submit=(Button)findViewById(R.id.submit);
        editText3=(EditText)findViewById(R.id.editText3);
        editText4=(EditText)findViewById(R.id.editText4);
        route_list=(ListView)findViewById(R.id.route_list);




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = trainnum.getText().toString();
                String url = "https://api.railwayapi.com/v2/route/train/"+s+"/apikey/jlmkkybs4g/";
                uploadList=new ArrayList<data>();
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                 Log.d("g", response);
                                JSONObject jObject  = null;
                                try {
                                    jObject = new JSONObject(response);
                                    JSONObject jsonObject = jObject.getJSONObject("train");
                                    // String traintname = jsonObject.getString("name");
                                    editText3.setText(jsonObject.getString("name").toString());
                                    editText4.setText(jsonObject.getString("number").toString());

                                    //JSONObject jsonObjectroute = jObject.getJSONObject("route");
                                    JSONArray jsonarray = new JSONArray(jObject.getString("route"));

                                    for(int i=0; i < jsonarray.length(); i++) {
                                        String scharr,schdep,distance,sta_code,sta_name;
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                                        if(jsonobject.getString("scharr").toString()==null)
                                                {  scharr=" ";}
                                        else
                                                {
                                                    scharr=jsonobject.getString("scharr").toString();
                                                }

                                                if(jsonobject.getString("schdep").toString()==null)
                                        {  schdep=" ";}
                                        else
                                        {
                                            schdep=jsonobject.getString("schdep").toString();
                                        }


                                         if(jsonobject.getString("distance").toString()==null)
                                         {
                                             distance=" ";
                                         }
                                         else {
                                                    distance=jsonobject.getString("distance").toString();
                                         }

                                        JSONObject jsonObject3 = jsonobject.getJSONObject("station");


                                        if(jsonObject3.getString("name")==null)
                                        {
                                            sta_name=" ";
                                        }
                                        else {
                                            sta_name = jsonObject3.getString("name");
                                        }

                                        if(jsonObject3.getString("code")==null)
                                        {
                                            sta_code=" ";
                                        }
                                        else{
                                            sta_code=jsonObject3.getString("code");
                                        }

                //String final1 = String.valueOf(i+1)+"."+"station_name"+sta_name+"station_code"+sta_code+"scharr"+scharr+"schdep"+schdep+"distance"+distance+"km";

                //Log.e("hi",final1);
Log.e("going to ",sta_code+" "+sta_name+" "+scharr+" "+schdep+" "+distance);
                                        uploadList.add(new data(sta_code,sta_name,scharr,schdep,distance));

                                    }
                                    adapter=new DataAdapter(train_route.this, (ArrayList<data>) uploadList);
                                    route_list.setAdapter(adapter);




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
                                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                );
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        });




    }
}
