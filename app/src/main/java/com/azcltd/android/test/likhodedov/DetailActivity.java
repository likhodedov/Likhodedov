package com.azcltd.android.test.likhodedov;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private String TAG = DetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = DetailActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.background_activity));
        }
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        int id=intent.getIntExtra("Id",0);
       Town town=MainActivity.CityArray.get(id-1);
        final ImageView viewImage=(ImageView)findViewById(R.id.DetailView);
        viewImage.setImageBitmap(MainActivity.Imagecash.get(town.image_url+town.name+town.id));

        TextView townName=(TextView)findViewById(R.id.City_name);
        townName.setText(town.name);
        TextView townDescr=(TextView)findViewById(R.id.City_Descr);
        townDescr.setText(town.description);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Log.e(TAG,"Activity killed");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



}
