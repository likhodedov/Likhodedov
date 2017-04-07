package com.azcltd.android.test.likhodedov;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    static boolean Run_Activity_Flag = false;
    private ProgressDialog pDialog;
    public ListView listView;
    public TownAdapter adapter;
    public static LruCache<String, Bitmap> Imagecash;
    public static ArrayList<Town> CityArray=new ArrayList<Town>();
    private String TAG = MainActivity.class.getSimpleName();
    public static String url="http://azcltd.com/testTask/android/";
    public static GetCities getCitiesAsync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = MainActivity.this.getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.background_activity));
        }
        ActivityManager am = (ActivityManager) this.getSystemService(
                Context.ACTIVITY_SERVICE);
Imagecash=new LruCache<>(am.getMemoryClass()*1024/4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCitiesAsync=new GetCities();
        getCitiesAsync.execute();
        //new GetCities().execute();
//listView.setSelector(R.drawable.item_color);

        }

    private class GetCities extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = handler.makeServiceCall("http://azcltd.com/testTask/android/cities.json");
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "All is good");
                    JSONArray cities = jsonObj.getJSONArray("cities");
                    Log.e(TAG, "JustDoIT");
                    for (int i = 0; i < cities.length(); i++) {
                        JSONObject temp = cities.getJSONObject(i);
                        if(Town.isCorrectTown(temp))
                        CityArray.add(i,Town.GetTownFromJson(temp,getApplicationContext()));
                    }

                }
                catch (final JSONException e) {
                   // Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Oops...");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Data is empty. You can try again later");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            alertDialog.show();

                        }
                    });
        }}
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Oops...");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Couldn't get json from server.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Try again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        getCitiesAsync.cancel(true);
                                        getCitiesAsync= new GetCities();
                                        getCitiesAsync.execute();
                                    }
                                });
                        alertDialog.show();
                    }
                });

            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            adapter=new TownAdapter(MainActivity.this, CityArray);
             listView= (ListView) findViewById(R.id.listView);
listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);

        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        Log.e(TAG,"SAVED");
//        super.onSaveInstanceState(savedInstanceState);}
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.e(TAG,"RESTORED");
//
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        if (getCitiesAsync != null)
            getCitiesAsync.cancel(true);


    }
    @Override
    protected void onStop()
    {

//        getCitiesAsync.cancel(true);
//        getCitiesAsync= new GetCities();
        super.onStop();
    }

}
