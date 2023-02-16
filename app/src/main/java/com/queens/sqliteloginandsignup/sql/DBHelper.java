package com.queens.sqliteloginandsignup.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StepCounter.db";
    public static final String CONTACTS_TABLE_NAME = "step";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_DATE = "date";
    public static final String CONTACTS_COLUMN_STEPS = "steps";
    public static final String CONTACTS_COLUMN_NAME = "name";
    private HashMap hp;
    int cursorCount;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table step " +
                        "(id integer primary key, date string,steps string, name string)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS step");
        onCreate(db);
    }

    public boolean insertContact (String date, String steps,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_DATE, date);
        contentValues.put(CONTACTS_COLUMN_STEPS, steps);
        contentValues.put(CONTACTS_COLUMN_NAME, name);
     long result =   db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public void deleteCourse(String date) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(CONTACTS_TABLE_NAME, "date=?", new String[]{date});
        db.close();
    }

    public void deletestep(String step) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(CONTACTS_TABLE_NAME, "steps=?", new String[]{step});
        db.close();
    }

    public void deletename(String name) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(CONTACTS_TABLE_NAME, "name=?", new String[]{name});
        db.close();
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from step where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String date, String steps,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_DATE, date);
        contentValues.put(CONTACTS_COLUMN_STEPS, steps);
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    @SuppressLint("Range")
    public boolean isTodayDataAvailable(String date){

        Boolean yesOrNo = false;
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from step", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_DATE )));
            res.moveToNext();
        }

        for (int i = 0; i<= array_list.size()-1; i++){
            if (Objects.equals(array_list.get(i), date)){
                yesOrNo = true;
                break;
            }
        }

        return yesOrNo;
    }


    @SuppressLint("Range")
    public boolean isnameavailable(String name){

        Boolean yesOrNo = false;
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from step", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME )));
            res.moveToNext();
        }

        for (int i = 0; i<= array_list.size()-1; i++){
            if (Objects.equals(array_list.get(i), name)){
                yesOrNo = true;
                break;
            }
        }

        return yesOrNo;
    }





    @SuppressLint("Range")
    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from step", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(+res.getColumnIndex(CONTACTS_COLUMN_DATE))+"-"+res.getString(+res.getColumnIndex(CONTACTS_COLUMN_NAME)) + "-" + res.getString(res.getColumnIndex(CONTACTS_COLUMN_STEPS)) );
            res.moveToNext();
        }
        return array_list;
    }


}