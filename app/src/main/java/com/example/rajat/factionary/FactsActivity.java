package com.example.rajat.factionary;

import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class FactsActivity extends AppCompatActivity {
    ListView factview;
    ImageView factimage;
    Button backbutton;
    TextView titleoffact;
    ArrayAdapter ad;
    factDBAdapter adp;
    TextToSpeech tts;
    Cursor cr;
    MyListView mlv;
    ArrayList<String> factlist;

    void goback(View view){
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
    ArrayList<String> getfactlist(Cursor cr,ArrayList<String>factlist){

        while (cr.moveToNext()){

            String fact_content=cr.getString(cr.getColumnIndex("fact_content"));
            int fact_id=cr.getInt(cr.getColumnIndex("_id"));
            factlist.add(fact_id+"::"+fact_content);
        }
        cr.close();
        return factlist;
    }
    void addfactstolistview(String category){
        factlist=new ArrayList<>();
        adp=new factDBAdapter(getApplicationContext());
        adp.open();
                cr=adp.getfactsbycategory(category);
                if(cr!=null)
                    factlist=getfactlist(cr,factlist);
                else
                    Toast.makeText(this,"No facts exists",Toast.LENGTH_SHORT).show();

        Collections.shuffle(factlist);
         mlv= new MyListView(this,factlist,tts);
        //ad =new ArrayAdapter(this,android.R.layout.simple_list_item_1,factlist);
        factview.setAdapter(mlv);
        mlv.notifyDataSetChanged();
        adp.close();
        if(category.equals("space")){
            factimage.setBackgroundResource(R.drawable.astronaut);
        }
        else if (category.equals("sports")){
            factimage.setBackgroundResource(R.drawable.sports);
        }
        else if(category.equals("random")){
            factimage.setBackgroundResource(R.drawable.randomimage);
        }
        else if(category.equals("science")){
            factimage.setBackgroundResource(R.drawable.science);
        }
        else if(category.equals("technology")){
            factimage.setBackgroundResource(R.drawable.technology);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        tts.speak("welcome to "+" factionary", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                } else
                    Log.e("error", "Initilization Failed!");

            }
        });


        titleoffact=findViewById(R.id.titleoffact);
        factview=findViewById(R.id.factsview);
        //factview.setFriction(ViewConfiguration.getScrollFriction()*10);
        factimage=findViewById(R.id.imageViewFact);
        backbutton=findViewById(R.id.backbutton);

        Intent i=getIntent();
        String categoryoffact=i.getStringExtra("fact");
        Log.i("category",categoryoffact);
        titleoffact.setText(categoryoffact);
        addfactstolistview(categoryoffact);


    }
    @Override
    protected void onPause() {


        if (tts != null) {

            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }


    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if(keycode==KeyEvent.KEYCODE_VOLUME_DOWN){
           if(mlv!=null){
               int rd=new Random().nextInt(factlist.size());
               String spk=factlist.get(rd);
               mlv.converttexttospeech(spk);
           }

        }
        if(keycode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
            finish();
        }
        return true;
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_share) {
//            Intent myIntent = new Intent(Intent.ACTION_SEND);
//            myIntent.setType("text/plain");
//            String shareBody = "*shared from FACTionary app*\n"+"Hello from rajat";
//            String shareSub = "Facts";
//            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//            startActivity(Intent.createChooser(myIntent, "Share using"));
//            return true;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
