package com.example.eventtracking_projectthree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class AppDatabase extends SQLiteOpenHelper {
    private static final String tableName = "events";
    private static final String idC = "_id";
    private static final String eventTitle = "event title";
    private static final String eventDesc = "description";
    private static final String eventDate = "date";
    private static final String eventTime = "time";
    private static final String Dname = "Event.db";
    private static final int DATABASE = 1;

    private Context context;

    AppDatabase(@Nullable Context context) {
        super(context, Dname, null, DATABASE);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + tableName +
                        " (" + idC + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        eventTitle + " TEXT, " +
                        eventDesc + " TEXT, " +
                        eventDate + " TEXT, " +
                        eventTime + " TEXT);";

        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    Cursor readAllData() {
        String Q = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        if(db != null) {
            c = db.rawQuery(Q,null);
        }

        return c;
    }

    long addReminder(String date, String time, String title, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(eventDate, date);
        cv.put(eventTime, time);
        cv.put(eventTitle, title);
        cv.put(eventDesc, description);

        long res = db.insert(tableName, null, cv);
        if (res == -1){
            Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
            return res;
        }
        else {
            Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show();
            return res;

        }
    }

    void updateData(String row_id, String title, String description, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(eventDate, date);
        cv.put(eventTime, time);
        cv.put(eventTitle, title);
        cv.put(eventDesc, description);

        long res = db.update(tableName, cv, "_id=?", new String[]{row_id});

        if (res == -1){
            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();

        }
    }

    void deleteOneRow(String row){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(tableName, "_id=?", new String[]{row});
        if(res == -1){
            Toast.makeText(context, "Failed deletion", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();

        }
    }
}
