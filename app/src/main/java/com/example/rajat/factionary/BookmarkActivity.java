package com.example.rajat.factionary;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {
    ArrayList<String>bookmarks;
    ArrayList<Integer> bookmarkids;
    ArrayAdapter ad;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    ListView lv;
    factDBAdapter adp;
    void createalertdialog(final int i){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose an operation");
        final String[] choices={"Copy fact to clip board","Remove fact from book marks"};
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which){
                    case 0:
                        myClip = ClipData.newPlainText("text", bookmarks.get(i));
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(getApplicationContext(), "Text Copied to Clipboard",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        adp.open();
                        //Log.i("bookmark id is",l+" "+bookmarks.get(i)+" "+bookmarkids.get(i));
                        long row=adp.deletebookmark(bookmarkids.get(i));
                        Log.i("no of rows affected",row+"");
                        bookmarks.remove(i);
                        bookmarkids.remove(i);
                        ad.notifyDataSetChanged();
                        adp.close();
                        break;

                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
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
                createalertdialog(i);
                return true;
            }
        });

    }
}
