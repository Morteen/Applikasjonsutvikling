package com.example.a141168.piedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 141168 on 02.03.2017.
 */

public class PieDbAdapter {


  public static final String DATABASE_NAME = "PIE_DATABASE.db";
    public static final int DATABASE_VERSION = 1;
    static final String PIE_TABLE = "PIE_TABLE";
    public static final String KEY_ROWID = "_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String FAVORITE = "favorite";


    private static final String[] PIE_CULUMS = {KEY_ROWID, NAME, DESCRIPTION, PRICE};

    private static final String CREATE_TABLE_PIE = " CREATE TABLE " + PIE_TABLE + "(" +
            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NAME + " NOT NULL UNIQUE, " +
            DESCRIPTION + " TEXT, " +
            PRICE + " REAL, " +
            FAVORITE + " INTEGER " +
            ")";


    private Context myContext;

    public PieDbAdapter(Context context) {
        this.myContext = context;
    }


    private Databasehelper myDBHelper;
    private SQLiteDatabase myDB;


    public PieDbAdapter open() throws SQLException {
        myDBHelper = new Databasehelper(myContext);
        myDB = myDBHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        if (myDBHelper != null)
            myDBHelper.close();
    }

    public void upgrade() throws SQLException {
        myDBHelper = new Databasehelper(myContext);
        myDB = myDBHelper.getWritableDatabase();
        myDBHelper.onUpgrade(myDB, 1, 2);

    }


    public Long insertPie(ContentValues initialtValues) {
        Long res = myDB.insertWithOnConflict(PIE_TABLE, null, initialtValues, SQLiteDatabase.CONFLICT_IGNORE);

        return res;
    }


    public Boolean updatePie(int id,ContentValues newValues){
        String[] selectionArgs={String.valueOf(id)};
        Boolean ok= myDB.update(PIE_TABLE,newValues,KEY_ROWID+"=?",selectionArgs)>0;
        return ok;
    }
    public Boolean deletePie(int id){
        String[] selectionArgs={String.valueOf(id)};
        Boolean ok= myDB.delete(PIE_TABLE,KEY_ROWID+"=?",selectionArgs)>0;
        return ok;
    }




    public Cursor getPies(){

                //Tabellnavn, Kolonnenavn, where,order,having,
        return myDB.query(PIE_TABLE,PIE_CULUMS,null,null,null,null,null);
    }

    public static Pie getPieFromCursor(Cursor cursor){
        Pie pie= new Pie();
        pie.mId=cursor.getInt(cursor.getColumnIndex(KEY_ROWID));
        pie.mName=cursor.getString(cursor.getColumnIndex(NAME));
        pie.mDescription=cursor.getString(cursor.getColumnIndex(DESCRIPTION));
        pie.mIsFavorite=cursor.getInt(cursor.getColumnIndex(FAVORITE))==1;
        pie.mPrice=cursor.getDouble(cursor.getColumnIndex(PRICE));

        return pie;

    }









    private static class Databasehelper extends SQLiteOpenHelper {


        public Databasehelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_PIE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXIST " + PIE_TABLE);
            onCreate(db);

        }
    }


}
