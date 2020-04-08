package com.example.rajat.factionary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


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
       // Log.i("Id of parent",parent.getAccessibilityClassName()+"");
         final TextView titleText = rowView.findViewById(R.id.facttextview);
        final ImageButton sharebutton=rowView.findViewById(R.id.sharebutton);
        final ImageButton bookmarkbutton=rowView.findViewById(R.id.bookmark);
        fcts=factlist.get(position).split("::");
        fact_id=Integer.parseInt(fcts[0]);
        fact_contents=fcts[1];
        bookmarkbutton.setTag(fact_id);
        titleText.setTag(fact_contents);
        sharebutton.setTag(fact_contents);
        //Log.i("fact contents",fact_id+fact_contents);
        bookmarkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag_of_my_bookmark=Integer.parseInt(bookmarkbutton.getTag().toString());
              //  Log.i("tag of my bookmark",tag_of_my_bookmark+"");
                adp.open();
                Cursor cr=adp.getbookmarkids(tag_of_my_bookmark);
                if(cr.getCount()==0) {
                    long id = adp.addbookmark(tag_of_my_bookmark);
                    Toast.makeText(context.getApplicationContext(),"Bookmark Succesfully added",Toast.LENGTH_SHORT).show();
                //    Log.i("Bookmarks added", id + "");
                }
                else{
                    Toast.makeText(context.getApplicationContext(),"Bookmark already added",Toast.LENGTH_SHORT).show();
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
                        converttexttospeech(titleText.getTag().toString());

                    }
                    else{
                        tts.stop();
                        playpause=true;
                        ib.setImageResource(R.drawable.ic_play);
                    }

                }

            });

        titleText.setText(fact_contents);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "*shared from FACTionary app*\n"+sharebutton.getTag().toString();
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
