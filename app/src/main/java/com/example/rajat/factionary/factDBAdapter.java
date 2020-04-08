package com.example.rajat.factionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class factDBAdapter {

    private  final Context ct;
    public static final String FACT_ID = "_id";
    private SQLiteDatabase db;
    public static final String FACT_CONTENT = "fact_content";

    public static final String FACT_CATEGORY = "category";
    private static final String DATABASE_NAME = "FactsDB";
    private static final String DATABASE_TABLE = "allfacts";
    private static final String DATABASE_TABLE_BOOKMARKS = "bookmarks";


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE_CREATE =
            "create table "+DATABASE_TABLE+" (_id integer primary key autoincrement," +
                    "fact_content text not null, category text not null);";

    private static final String DATABASE_TABLE_BOOKMARKS_CREATE =
            "create table "+DATABASE_TABLE_BOOKMARKS+" (_id integer not null)";

    factsDatabaseHelper helper;
     public factDBAdapter(Context ct){
         this.ct=ct;
         helper=new factsDatabaseHelper(ct);
    }

    private static class factsDatabaseHelper extends SQLiteOpenHelper {

        factsDatabaseHelper(Context ctx){
            super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DATABASE_TABLE_CREATE);
            sqLiteDatabase.execSQL(DATABASE_TABLE_BOOKMARKS_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_BOOKMARKS);
            onCreate(sqLiteDatabase);
        }
    }

    public factDBAdapter open() throws SQLException

    {

        db = helper.getWritableDatabase();

        return this;

    }


    public void close()

    {

        helper.close();
    }
    public long insertfact(String content, String category)

    {

        ContentValues initialValues = new ContentValues();

        initialValues.put(FACT_CONTENT, content);

        initialValues.put(FACT_CATEGORY, category);

        return db.insert(DATABASE_TABLE, null, initialValues);

    }
    public long addbookmark(int id){
         ContentValues initialValues=new ContentValues();
         initialValues.put(FACT_ID,id);
         return  db.insert(DATABASE_TABLE_BOOKMARKS,null,initialValues);
    }
    public Cursor getbookmarkids(int id) throws SQLException{
        Cursor mCursor = db.query(true, DATABASE_TABLE_BOOKMARKS, new
                        String[] {FACT_ID},
                FACT_ID+ "=" + id, null,

                null, null, null, null);

        if (mCursor != null) {

            mCursor.moveToFirst();

        }

        return mCursor;

    }
    public Cursor getfactfrombookmarks() throws SQLException{

        Cursor mCursor = db.rawQuery("SELECT * FROM "+DATABASE_TABLE+" where "+FACT_ID+" in( SELECT "+FACT_ID+" FROM "+DATABASE_TABLE_BOOKMARKS+");",null);
        if (mCursor != null) {

            mCursor.moveToFirst();

        }

        return mCursor;

    }
    public long deletebookmark(int id){
         return db.delete(DATABASE_TABLE_BOOKMARKS,"_id ="+id,null);
    }

    public Cursor getfactsbycategory(String category) throws SQLException

    {

        Cursor mCursor = db.query(true, DATABASE_TABLE, new
                        String[] {FACT_ID, FACT_CONTENT, FACT_CATEGORY},
                 FACT_CATEGORY+ "=" + "'"+category+"'", null,

                null, null, null, null);

        if (mCursor != null) {

            mCursor.moveToFirst();

        }

        return mCursor;

    }
    public Cursor getallfacts()

    {

        return db.query(DATABASE_TABLE, new String[]
                        {FACT_ID, FACT_CONTENT, FACT_CATEGORY}, null, null, null,
                null, null);

    }
}
