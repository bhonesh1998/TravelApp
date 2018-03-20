package com.example.bhonesh.railwayapi;

import org.json.JSONObject;

/**
 * Created by bhonesh on 20/3/18.
 */

public class data {

    String sta_code;
    String distance;
    String schdep;
    String sta_name;
    String scharr;

    public void setSta_code(String sta_code) {
        this.sta_code = sta_code;
    }

    public void setSta_name(String sta_name) {
        this.sta_name = sta_name;
    }

    public void setScharr(String scharr) {
        this.scharr = scharr;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setSchdep(String schdep)
    {
        this.schdep=schdep;
    }

    public String getSta_code() {

        return sta_code;
    }

    public String getSta_name() {
        return sta_name;
    }

    public String getScharr() {
        return scharr;
    }

    public String getDistance() {
        return distance;
    }

    public String getDepp()
    {
        return schdep;
    }


    public data(String sta_code, String sta_name, String scharr, String schdep, String distance) {

        this.sta_code = sta_code;
        this.distance = distance;
        this.schdep=schdep;
        this.sta_name = sta_name;
        this.scharr = scharr;
    }
}
