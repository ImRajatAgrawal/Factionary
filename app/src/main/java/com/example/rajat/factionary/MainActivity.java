package com.example.rajat.factionary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewFlipper viewFlipper;
    void addfactstodb(String filename,String category,factDBAdapter adp){
        adp.open();
        InputStream is= null;   //reads the file
        InputStreamReader ir;
        BufferedReader br;
        try {
            is = getAssets().open(filename);
            ir = new InputStreamReader(is);
            br = new BufferedReader(ir);
            String line;
            adp.open();

            while ((line = br.readLine()) != null) {
                long row = adp.insertfact(line, category);
                Log.i("Rows inserted", row + "");
            }
            is.close();
            ir.close();
            br.close();
            adp.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        }
    void insertfacts(){
        factDBAdapter adp=new factDBAdapter(this);
        addfactstodb("spacefacts.txt","space",adp);
        addfactstodb("sportsfacts.txt","sports",adp);
        addfactstodb("science.txt","science",adp);
        addfactstodb("randomfacts.txt","random",adp);
        addfactstodb("technology.txt","technology",adp);

        /* Cursor c=adp.getallfacts();
        while(c.moveToNext()){
            Log.i("Fact",c.getString(c.getColumnIndex("category"))+": "+c.getString(c.getColumnIndex("fact_content")));
        }
        Log.i("Count of rows",c.getCount()+"");
        c.close();
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setBackgroundColor(getColor(R.color.navbarbackground));
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        viewFlipper=findViewById(R.id.viewflipper);
        int images[]={R.drawable.carosoul1,R.drawable.carosuel2,R.drawable.caroseul3,R.drawable.carosouel4};
        for (int image:images) {
            flipimages(image);
        }

       //insertfacts();
    }
    void flipimages(int image){
        ImageView iv=new ImageView(this);
        iv.setBackgroundResource(image);
        viewFlipper.addView(iv);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent i= new Intent(this,FactsActivity.class);

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            } else if (id == R.id.nav_sciencefacts) {
                i.putExtra("fact","science");
                startActivity(i);

        } else if (id == R.id.nav_spacefacts) {

            i.putExtra("fact","space");
            startActivity(i);


        } else if (id == R.id.nav_technologyfacts) {

            i.putExtra("fact","technology");
            startActivity(i);


        } else if (id == R.id.nav_randomfacts) {
            i.putExtra("fact","random");
            startActivity(i);

        } else if (id == R.id.nav_sportsfacts) {
            i.putExtra("fact","sports");
            startActivity(i);

        }
        else if(id==R.id.nav_bookmarks){
            Intent bookmarkIntent=new Intent(this,BookmarkActivity.class);
            startActivity(bookmarkIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.enableNotification) {
            Toast.makeText(this,"Notification generated ",Toast.LENGTH_SHORT).show();
            Calendar cd=Calendar.getInstance();
            cd.set(Calendar.HOUR_OF_DAY,18);
            cd.set(Calendar.MINUTE,12);
            cd.set(Calendar.SECOND,0);
            Intent intent=new Intent(getApplicationContext(),notificationReciever.class);
            PendingIntent pi=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am= (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,cd.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pi);

        }

        return super.onOptionsItemSelected(item);
    }


}
