package com.example.rajat.factionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;


public class MyListView extends ArrayAdapter {
    Activity context;
    ArrayList<String>factlist;
    TextToSpeech tts;
    ImageButton playbutton;
    ImageButton ib;
    boolean playpause=true;
    String fact_contents;
    String fcts[];
    int fact_id;
    factDBAdapter adp;
    public MyListView(Activity context, ArrayList<String>factlist,TextToSpeech tts) {
        super(context, R.layout.customlistview, factlist);
        this.context=context;
        this.factlist=factlist;
        this.tts=tts;
        adp=new factDBAdapter(getContext());

    }

    @NonNull
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.customlistview, null,true);
        Log.i("Id of parent",parent.getAccessibilityClassName()+"");
         TextView titleText = rowView.findViewById(R.id.facttextview);
        ImageButton sharebutton=rowView.findViewById(R.id.sharebutton);
        ImageButton bookmarkbutton=rowView.findViewById(R.id.bookmark);
        fcts=factlist.get(position).split("::");
        fact_id=Integer.parseInt(fcts[0]);
        fact_contents=fcts[1];
        bookmarkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adp.open();
                Cursor cr=adp.getbookmarkids(fact_id);
                if(cr.getCount()==0) {
                    long id = adp.addbookmark(fact_id);
                    Log.i("Bookmarks added", id + "");

                }
                else{
                    Toast.makeText(context,"Bookmark already added",Toast.LENGTH_SHORT).show();
                }
                adp.close();
            }
        });
        playpause=true;
         playbutton = rowView.findViewById(R.id.playbutton);
            playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        ib=view.findViewById(R.id.playbutton);
                    if(playpause){
                        playpause=false;
                        ib.setImageResource(R.drawable.ic_symbol);
                        converttexttospeech(fact_contents);

                    }
                    else{
                        tts.stop();
                        playpause=true;
                        ib.setImageResource(R.drawable.ic_play);
                    }

                }

            });

        titleText.setText(factlist.get(position));
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "*shared from FACTionary app*\n"+fact_contents;
                String shareSub = "Facts";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                myIntent.createChooser(myIntent, "Share using");
                context.startActivity(myIntent);
            }
        });
        return rowView;
    }


    void converttexttospeech(String spk){
        tts.speak(spk, TextToSpeech.QUEUE_FLUSH, null,null);
    }
}
