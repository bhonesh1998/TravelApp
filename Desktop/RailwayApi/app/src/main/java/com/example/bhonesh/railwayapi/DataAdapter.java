package com.example.bhonesh.railwayapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bhonesh.railwayapi.R;
import com.example.bhonesh.railwayapi.data;

import java.util.ArrayList;

public class DataAdapter extends BaseAdapter{
        Context activity;
        ArrayList<data> customListDataModelArrayList;
        LayoutInflater layoutInflater = null;


public DataAdapter(Context activity, ArrayList<data> list){
        this.activity=activity;
        this.customListDataModelArrayList = list;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


     @Override
      public int getCount() {
        return customListDataModelArrayList.size();
                 }

    @Override
      public Object getItem(int i) {
        return customListDataModelArrayList.get(i);
        }
    @Override
       public long getItemId(int i) {
        return i;
        }


    private static class ViewHolder{
        TextView sta_code,distance,schdep,sta_name,scharr;
    }
    ViewHolder viewHolder = null;
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View vi=convertView;
        final int pos = position;
        if(vi == null){

            // create  viewholder object for list_rowcell View.
            viewHolder = new ViewHolder();

            vi = layoutInflater.inflate(R.layout.list_row,null);
            viewHolder.sta_name = (TextView) vi.findViewById(R.id.sta_name);
            viewHolder.sta_code = (TextView) vi.findViewById(R.id.sta_code);
            viewHolder.scharr = (TextView) vi.findViewById(R.id.textView11);
            viewHolder.schdep = (TextView) vi.findViewById(R.id.textView12);
            viewHolder.distance = (TextView) vi.findViewById(R.id.textView13);
            vi.setTag(viewHolder);
        }else {


            viewHolder= (ViewHolder) vi.getTag();
        }


        viewHolder.sta_name.setText(customListDataModelArrayList.get(pos).getSta_name());
        viewHolder.sta_code.setText(customListDataModelArrayList.get(pos).getSta_code());
        viewHolder.scharr.setText(customListDataModelArrayList.get(pos).getScharr());
        viewHolder.schdep.setText(customListDataModelArrayList.get(pos).getDepp());
        viewHolder.distance.setText(customListDataModelArrayList.get(pos).getDistance());


        return vi;

    }








}