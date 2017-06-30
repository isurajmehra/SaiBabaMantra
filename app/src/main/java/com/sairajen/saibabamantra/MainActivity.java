package com.sairajen.saibabamantra;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button openGalleryBtn, startChantBtn;
    private ImageView imageView1, imageView2, imageView3, mainImageView;
    private EditText editText;
    private TextView readMoreTxt;
    private AdView adView;

    public static Bitmap bitmap;

    private String picturePath;
    private int count = 0;

    private final static int GALLERY_ACTIVITY_CODE = 200;
    private final static int RESULT_CROP = 201;

    private final static String INTENT_COUNT = "count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        initViews();

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.banner_ad));
        adView = (AdView) findViewById(R.id.adViewMainActivity);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(Device.getId(MainActivity.this)).build();
        adView.loadAd(adRequest);

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sai1);
        mainImageView.setImageBitmap(bitmap);

        openGalleryBtn.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        startChantBtn.setOnClickListener(this);
        readMoreTxt.setOnClickListener(this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    startChant();
                }
                return false;
            }
        });

    }

    private void startChant() {
        if (editText.getText().toString().trim() != null && !editText.getText().toString().trim().equals("")) {
            count = Integer.parseInt(editText.getText().toString().trim());
            if (count>0 && count < 1000) {
                Intent intent = new Intent(MainActivity.this, MediaPlayerActivity.class);
                intent.putExtra(INTENT_COUNT, count);
                startActivity(intent);
            } else {
                Helper.showDialog(MainActivity.this,"ERROR","please enter count value betweeen 0 and 1000","OK",android.R.drawable.stat_sys_warning);
            }
        } else {
            Helper.showDialog(MainActivity.this,"ERROR","please enter valid count value before proceeding","OK",android.R.drawable.stat_sys_warning);
        }
    }

    private void initViews() {
        openGalleryBtn = (Button) findViewById(R.id.openGalleryBtn);
        startChantBtn = (Button) findViewById(R.id.startChantBtn);
        mainImageView = (ImageView) findViewById(R.id.mainImageView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        editText = (EditText) findViewById(R.id.editText);
        readMoreTxt = (TextView) findViewById(R.id.readMoreTextView);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        MainActivity.this.setTitle("Sai Mantra");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openGalleryBtn:
                Intent i = new Intent(MainActivity.this,GalleryUtil.class);
                startActivityForResult(i,GALLERY_ACTIVITY_CODE);
                break;
            case R.id.imageView1:
                mainImageView.setImageDrawable(getResources().getDrawable(R.drawable.sai1));
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sai1);
                break;
            case R.id.imageView2:
                mainImageView.setImageDrawable(getResources().getDrawable(R.drawable.sai2));
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sai2);
                break;
            case R.id.imageView3:
                mainImageView.setImageDrawable(getResources().getDrawable(R.drawable.sai3));
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sai3);
                break;
            case R.id.startChantBtn:
                startChant();
                break;
            case R.id.readMoreTextView:
                startActivity(new Intent(MainActivity.this,About.class));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                picturePath = data.getStringExtra("picturePath");
                performCrop(picturePath);
            }
        }
    }

    private void performCrop(String picUri) {
        try{
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");

            //set crop properties
            cropIntent.putExtra("crop","true");
            cropIntent.putExtra("aspectX",1);
            cropIntent.putExtra("aspectY",1);
            cropIntent.putExtra("outputX",280);
            cropIntent.putExtra("outputY",280);

            cropIntent.putExtra("return-data",true);

            startActivityForResult(cropIntent,RESULT_CROP);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Helper.showDialog(MainActivity.this,"ERROR","Your device does not support crop feature"
            , "OK",android.R.drawable.stat_sys_warning);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                Helper.shareApp(MainActivity.this);
                break;
            case R.id.menu_more_app:
                Helper.openLink(MainActivity.this,"http://www.saihere.com/more-app.html");
                break;
            case R.id.menu_about_us:
                startActivity(new Intent(MainActivity.this,About.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (adView != null)
            adView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (adView != null) {
            adView.resume();
        }
        super.onResume();
        picturePath = "";
        count = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }
}
