package com.example.thien_record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import com.example.thien_record.listener.OnDatabaseChangedListener;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private Context mContext;

    private static final String LOG_TAG = "Database";

    private static OnDatabaseChangedListener monDatabaseChangedListener;

    public static abstract class DatabaseItems implements BaseColumns {
        public static final String TABLE_NAME = "Recordings";

        public static final String COLUMN_NAME_RECORDING_NAME = "recording_name";
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        public static final String COLUMN_NAME_TIME_ADDED = "time_added";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseItems.TABLE_NAME + " (" +
                    DatabaseItems._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    DatabaseItems.COLUMN_NAME_RECORDING_NAME + TEXT_TYPE + COMMA_SEP +
                    DatabaseItems.COLUMN_NAME_RECORDING_FILE_PATH + TEXT_TYPE + COMMA_SEP +
                    DatabaseItems.COLUMN_NAME_RECORDING_LENGTH + " INTEGER " + COMMA_SEP +
                    DatabaseItems.COLUMN_NAME_TIME_ADDED + " INTEGER " + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseItems.TABLE_NAME;

    public Database(@Nullable Context context) {
        super(context, "data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (email TEXT PRIMARY KEY , password TEXT)");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
    }

//Table User:
    //inserting database
    public boolean insert(String email , String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email" , email);
        contentValues.put("password",password);
        long ins = db.insert("User" , null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    //checking email
    public boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ?" , new String[]{email});
        if(cursor.getCount() > 0 ) return false;
        else return true;
    }

    //check email and pass
    public boolean emailPass(String email , String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ? AND password = ?" , new String[]{email,password});
        if(cursor.getCount() > 0 ) return true;
        else return false;
    }

//Table recording;

    private static OnDatabaseChangedListener onDatabaseChangedListener;

    public ArrayList<RecordingItems> getAllAudio()
    {
       ArrayList<RecordingItems> arrayList = new ArrayList<>();
       SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseItems.TABLE_NAME, null);
        if(c != null) {
            while (c.moveToNext()) {
                String name = c.getString(1);
                String path = c.getString(2);
                int length = (int) c.getLong(3);
                long timeAdd = c.getLong(4);

                RecordingItems recordingItems = new RecordingItems(name, path, length, timeAdd);
                arrayList.add(recordingItems);
            }
            c.close();
            return arrayList;
        }
        else return null;

    }


    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { DatabaseItems._ID };
        Cursor c = db.query(DatabaseItems.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public Context getContext(){
        return mContext;
    }


    public boolean addRecording(RecordingItems recordingItems) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DatabaseItems.COLUMN_NAME_RECORDING_NAME, recordingItems.getmName());
            cv.put(DatabaseItems.COLUMN_NAME_RECORDING_FILE_PATH, recordingItems.getmFilePath());
            cv.put(DatabaseItems.COLUMN_NAME_RECORDING_LENGTH, recordingItems.getmLength());
            cv.put(DatabaseItems.COLUMN_NAME_TIME_ADDED, recordingItems.getmTime());
            db.insert(DatabaseItems.TABLE_NAME, null, cv);

            if(onDatabaseChangedListener != null){
                onDatabaseChangedListener.onNewDatabaseEntryAdd(recordingItems);
            }

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static  void setOnDatabaseChangedListener(OnDatabaseChangedListener listener){
        onDatabaseChangedListener = listener;
    }

}
