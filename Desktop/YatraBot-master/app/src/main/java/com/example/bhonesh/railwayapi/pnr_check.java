package com.example.bhonesh.railwayapi;

import android.content.SharedPreferences;
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

public class pnr_check extends AppCompatActivity {

    ListView pass_list;
    List<String> uploadList=new ArrayList<>();
    EditText pnr_value,date_value,board_value,reserve_value,tname_value,tnumber_value;
    Button search_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr_check);


        pass_list=(ListView)findViewById(R.id.pass_list);

        pnr_value=(EditText)findViewById(R.id.pnr_value);
        date_value=(EditText)findViewById(R.id.date_value);
        board_value=(EditText)findViewById(R.id.date_value2);
        reserve_value=(EditText)findViewById(R.id.date_value3);
        tname_value=(EditText)findViewById(R.id.date_value4);
        tnumber_value=(EditText)findViewById(R.id.date_value5);


        search_button=(Button)findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String pnr_string = pnr_value.getText().toString();
                String url = "https://api.railwayapi.com/v2/pnr-status/pnr/"+pnr_string+"/apikey/jlmkkybs4g/";



                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                // Log.d(TAG, response);
                                JSONObject jObject  = null,jObject2=null,jObject3=null,jObject4=null;
                                try {
                                    jObject = new JSONObject(response);
                                    //JSONObject jsonObject = jObject.getJSONObject("date");
                                     String date = jObject.getString("doj");
                                     date_value.setText(date);


                                     jObject2=new JSONObject(jObject.getString("boarding_point"));

                                     String bp=jObject2.getString("name");

                                     board_value.setText(bp);
                                    jObject3=new JSONObject(jObject.getString("reservation_upto"));

                                    String ru=jObject3.getString("name");
                                    reserve_value.setText(ru);

                                    jObject4=new JSONObject(jObject.getString("train"));

                                    String tn=jObject4.getString("name");
                                    String tno=jObject4.getString("number");
                                    //String pass_no=jObject4.getString("no");

                                    tname_value.setText(tn);
                                    tnumber_value.setText(tno);



                                    // trainnameT.setText(jsonObject.getString("name").toString());
                                   // trainnumberT.setText(jsonObject.getString("number").toString());
                                    JSONArray jsonarray = new JSONArray(jObject.getString("passengers"));

                                    for(int i=0; i < jsonarray.length(); i++) {

                                        JSONObject jsonobjectpass = jsonarray.getJSONObject(i);
                                        String cur_stat      = jsonobjectpass.getString("current_status");
                                        String book_stat       = jsonobjectpass.getString("booking_status");
                                        String pass_no       = jsonobjectpass.getString("no");


                                        uploadList.add(pass_no+"."+"Current:"+cur_stat+"|Booking:"+book_stat);

                                     //   days.append("  "+day.substring(0,1).trim()+"  ");
                                      //  runst.append("  "+runs.trim()+"  ");
                                       // pd.hide();
                                    }

                                    String[] uploads = new String[uploadList.size()];

                                    for (int i = 0; i < uploads.length; i++) {
                                        uploads[i] = uploadList.get(i);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                                    pass_list.setAdapter(adapter);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //pd.hide();
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
