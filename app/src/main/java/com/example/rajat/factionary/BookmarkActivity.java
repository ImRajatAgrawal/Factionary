package com.example.rajat.factionary;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {
    ArrayList<String>bookmarks;
    ArrayList<Integer> bookmarkids;
    ArrayAdapter ad;
    ListView lv;
    factDBAdapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        lv=findViewById(R.id.bookmarklist);
        bookmarks=new ArrayList<>();
        bookmarkids=new ArrayList<>();
        adp=new factDBAdapter(getApplicationContext());
        adp.open();
        Cursor cr=adp.getfactfrombookmarks();
        while (cr.moveToNext()){
            Log.i("i am here",cr.getCount()+"");
            String fact_content=cr.getString(cr.getColumnIndex("fact_content"));
            String category=cr.getString(cr.getColumnIndex("category"));
            int id=cr.getInt(cr.getColumnIndex("_id"));
            bookmarks.add(fact_content+"\n"+"Category : "+category);
            bookmarkids.add(id);
        }
        adp.close();

        ad=new ArrayAdapter(this,android.R.layout.simple_list_item_1,bookmarks);
        lv.setAdapter(ad);
        ad.notifyDataSetChanged();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                adp.open();
                //Log.i("bookmark id is",l+" "+bookmarks.get(i)+" "+bookmarkids.get(i));
                long row=adp.deletebookmark(bookmarkids.get(i));
                Log.i("no of rows affected",row+"");
                bookmarks.remove(i);
                bookmarkids.remove(i);
                ad.notifyDataSetChanged();
                adp.close();
                return true;
            }
        });

    }
}
