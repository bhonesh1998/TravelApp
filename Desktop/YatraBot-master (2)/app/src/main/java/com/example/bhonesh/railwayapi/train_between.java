package com.example.bhonesh.railwayapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class train_between extends AppCompatActivity {
    ListView train_list;
    List<String> uploadList=new ArrayList<>();
    String source_code,dest_code,source_name,dest_name;
    ArrayAdapter s_adapter,d_adapter;
    int one=0,two=0;
    AutoCompleteTextView simpleAutoCompleteTextView2,simpleAutoCompleteTextView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_between);

        train_list=(ListView)findViewById(R.id.trainlist);
        simpleAutoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.source);
        simpleAutoCompleteTextView3 = (AutoCompleteTextView) findViewById(R.id.dest);

        //for train auto complete
        simpleAutoCompleteTextView2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = String.valueOf(s);
                if (str.length() != 0) {
                    String url3 = "https://api.railwayapi.com/v2/suggest-station/name/" + str + "/apikey/jlmkkybs4g/";

                    //for auto completion
                    //  pd2.show();
                    StringRequest stringRequest3 = new StringRequest(Request.Method.GET,
                            url3,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("res", response);
                                    JSONObject jObject = null;
                                    try {
                                        jObject = new JSONObject(response);
                                        JSONArray jsonarray = new JSONArray(jObject.getString("stations"));
                                        String y[] = new String[jsonarray.length()];
                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            String name = jsonobject.getString("name");
                                            String number = jsonobject.getString("code");
                                            y[i] = name+"("+number+")";

                                        }
                                        s_adapter = new ArrayAdapter(train_between.this
                                                , android.R.layout.simple_list_item_1, y);
                                        //pd2.cancel();
                                        //    simpleAutoCompleteTextView.setAdapter(adapter);
                                        simpleAutoCompleteTextView2.setThreshold(1);//start searching from 1 character
                                        simpleAutoCompleteTextView2.setAdapter(s_adapter);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //pd.hide();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error != null) {
                                        //Log.d(TAG, error.toString());
                                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                    );

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest3);
                }else{
                    /*String x[] = {};

                    ArrayAdapter adapter = new ArrayAdapter(Display.this, android.R.layout.simple_list_item_1, x);

                    simpleAutoCompleteTextView.setAdapter(adapter);
                    simpleAutoCompleteTextView.setThreshold(1);//start searching from 1 character
                    simpleAutoCompleteTextView.setAdapter(adapter);*/



                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        simpleAutoCompleteTextView3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //   pd2 = new ProgressDialog(Display.this);
                //   pd2.setMessage("Loading . . .");


                String str = String.valueOf(s);
                if (str.length() != 0) {
                    String url3 = "https://api.railwayapi.com/v2/suggest-station/name/" + str + "/apikey/jlmkkybs4g/";

                    //for auto completion
                    //  pd2.show();
                    StringRequest stringRequest3 = new StringRequest(Request.Method.GET,
                            url3,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("res", response);
                                    JSONObject jObject = null;
                                    try {
                                        jObject = new JSONObject(response);
                                        JSONArray jsonarray = new JSONArray(jObject.getString("stations"));
                                        String y[] = new String[jsonarray.length()];
                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            String name = jsonobject.getString("name");
                                            String number = jsonobject.getString("code");
                                            y[i] = name+"("+number+")";

                                        }
                                       d_adapter = new ArrayAdapter(train_between.this
                                                , android.R.layout.simple_list_item_1, y);
                                        //pd2.cancel();
                                        //    simpleAutoCompleteTextView.setAdapter(adapter);
                                        simpleAutoCompleteTextView3.setThreshold(1);//start searching from 1 character
                                        simpleAutoCompleteTextView3.setAdapter(d_adapter);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //pd.hide();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error != null) {
                                        //Log.d(TAG, error.toString());
                                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                    );

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest3);
                }else{
                    /*String x[] = {};

                    ArrayAdapter adapter = new ArrayAdapter(Display.this, android.R.layout.simple_list_item_1, x);

                    simpleAutoCompleteTextView.setAdapter(adapter);
                    simpleAutoCompleteTextView.setThreshold(1);//start searching from 1 character
                    simpleAutoCompleteTextView.setAdapter(adapter);*/



                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        simpleAutoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                source_name= s_adapter.getItem(position).toString();
                /*Toast.makeText(MainActivity.this,
                        adapter.getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();*/
                if(source_name.length()!=0){
                    source_code = source_name.substring(source_name.indexOf('(')+1,source_name.indexOf(')'));
                    one=1;
                }else
                    one=0;

                if(one==1 && two==1){


                    String t_url = "https://api.railwayapi.com/v2/between/source/" + source_code + "/dest/" + dest_code + "/date/20-03-2018/apikey/jlmkkybs4g/";
                   uploadList = new ArrayList<>();



                    StringRequest stringRequest4 = new StringRequest(Request.Method.GET,
                            t_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("resssss", response);
                                    JSONObject jObject = null;
                                    try {
                                        jObject = new JSONObject(response);
                                        JSONArray jsonarray = new JSONArray(jObject.getString("trains"));
                                        String x[] = new String[jsonarray.length()];
                                        for (int i = 0; i < jsonarray.length(); i++) {

                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            String name = jsonobject.getString("name");
                                            Log.e("hi", name);
                                            uploadList.add(name);
                                            //x[i] = name;

                                        }
                                        String[] uploads = new String[uploadList.size()];

                                        for (int i = 0; i < uploads.length; i++) {
                                            uploads[i] = uploadList.get(i);
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                                        train_list.setAdapter(adapter);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //pd.hide();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error != null) {
                                        // Log.d(TAG, error.toString());
                                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                    );

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest4);
                }
            }
        });

        simpleAutoCompleteTextView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

               dest_name= d_adapter.getItem(position).toString();
                /*Toast.makeText(MainActivity.this,
                        adapter.getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();*/

                if(dest_name.length()!=0){
                    dest_code = dest_name.substring(dest_name.indexOf('(')+1,dest_name.indexOf(')'));
                    two=1;
                }else
                    two=0;

                if(one==1 && two==1) {

                    String t_url = "https://api.railwayapi.com/v2/between/source/" + source_code + "/dest/" + dest_code + "/date/20-03-2018/apikey/jlmkkybs4g/";
                    uploadList = new ArrayList<>();
                    


                    StringRequest stringRequest4 = new StringRequest(Request.Method.GET,
                            t_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("resssss", response);
                                    JSONObject jObject = null;
                                    try {
                                        jObject = new JSONObject(response);
                                        JSONArray jsonarray = new JSONArray(jObject.getString("trains"));
                                        String x[] = new String[jsonarray.length()];
                                        for (int i = 0; i < jsonarray.length(); i++) {

                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            String name = jsonobject.getString("name");
                                            Log.e("hi", name);
                                            uploadList.add(name);
                                            //x[i] = name;

                                        }
                                        String[] uploads = new String[uploadList.size()];

                                        for (int i = 0; i < uploads.length; i++) {
                                            uploads[i] = uploadList.get(i);
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                                        train_list.setAdapter(adapter);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //pd.hide();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error != null) {
                                        // Log.d(TAG, error.toString());
                                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                    );

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest4);
                }



            }
        });
       // source_name=simpleAutoCompleteTextView2.getText().toString();
       // dest_name=simpleAutoCompleteTextView3.getText().toString();

Log.e("codes",source_code+""+dest_code);






    }
}
