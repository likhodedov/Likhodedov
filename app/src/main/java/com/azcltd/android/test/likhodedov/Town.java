package com.azcltd.android.test.likhodedov;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by d.lihodedov on 04.04.2017.
 */
public class Town {

    private static String TAG = MainActivity.class.getSimpleName();

    public final int id;
    public final String name;
    public final String description;
    public final String image_url;
    public final String country;
    public final Location location;


    public Town(int id, String name,String description, String image_url, String country, Location location) {
        this.id = id;
        this.description=description;
        this.name = name;
        this.image_url = image_url;
        this.country = country;
        this.location = location;


    }



    public static Town GetTownFromJson(final JSONObject object,Context context){
        final int id=object.optInt("id", 0);
        final String name=object.optString("name",null);
        final String description=object.optString("description",null);
        final String image_url=object.optString("image_url",null);
        final String country=object.optString("country",null);
        Bitmap image = null;
        Location location=new Location(0.0,0.0);
        try {
            JSONObject locationfromjson=object.getJSONObject("location");
            location=new Location(object.optDouble("latitude",0.0),object.optDouble("longitude",0.0));
        }
        catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        try {
            URL url = new URL(MainActivity.url+image_url);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize =2;
            Bitmap temp = BitmapFactory.decodeStream(url.openConnection().getInputStream(),null,opts);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            temp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            image=BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));



        } catch (IOException e) {
            System.out.println("Something wrong with file: "+e);
            image=BitmapFactory.decodeResource(context.getResources(),R.drawable.sorryimage);
        }
        catch (NullPointerException e){
            image=BitmapFactory.decodeResource(context.getResources(),R.drawable.sorryimage);

        }

if (image_url!=null) MainActivity.Imagecash.put(image_url+name+id,image);
//        Log.e(TAG,"CREATE DONE");
        return new Town(id,name,description,image_url,country,location);

    }

    public static boolean isCorrectTown(final JSONObject object){
        final String name=object.optString("name",null);
        final String description=object.optString("description",null);
        return !((description == null) || (name == null));
    }



public int getId(){
    return id;
}


}
