package com.azcltd.android.test.likhodedov;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by d.lihodedov on 04.04.2017.
 */
public class TownAdapter extends ArrayAdapter<Town>  {
    private static String TAG = MainActivity.class.getSimpleName();
public static boolean isMultiple=false;
    public TownAdapter(Context context, ArrayList<Town> towns) {
        super(context, 0, towns);
    }

    @Override
    public View getView(final int position,View convertView, final ViewGroup parent) {
        final Town town = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listlayout, parent, false);
        }

        TextView townName = (TextView) convertView.findViewById(R.id.TownName);
        final ImageView viewImage = (ImageView) convertView.findViewById(R.id.PreViewImage);
        townName.setText(town.name);
        viewImage.setImageBitmap(MainActivity.Imagecash.get(town.image_url + town.name + town.id));
        //convertView.setBackgroundDrawable(parent.getContext().getResources().getDrawable(R.drawable.item_color));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "id: " + town.getId());
                        Intent myIntent = new Intent(parent.getContext(), DetailActivity.class);
                        myIntent.putExtra("Id", town.getId());
                        Log.e(TAG, "Send id " + town.getId());
                        parent.getContext().startActivity(myIntent);
                }
            });
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               if(event.getAction() == MotionEvent.ACTION_DOWN)
                    v.setBackgroundColor(parent.getContext().getResources().getColor(R.color.item_checked));
                if(event.getAction() == MotionEvent.ACTION_POINTER_DOWN)
                    v.setBackgroundColor(Color.TRANSPARENT);
                if((event.getAction() == MotionEvent.ACTION_MOVE)&&(event.getAction() == MotionEvent.ACTION_UP))
                    v.setBackgroundColor(parent.getContext().getResources().getColor(R.color.item_checked));
                if(event.getAction() == MotionEvent.ACTION_CANCEL) v.setBackgroundColor(Color.TRANSPARENT);
                if(event.getAction() == MotionEvent.ACTION_UP) v.setBackgroundColor(Color.TRANSPARENT);

                return false;}

        });
            return convertView;
        }

    @Override
    public boolean isEnabled(int position) {
        //Log.e(TAG,"hello"+position);
        return true;

    }
   private int selectedItem;
    public void setSelectedItem(int position) {
        selectedItem = position;

    }}