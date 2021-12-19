package com.sd.spartan.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelperTest extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "TODO";
    private static final String SECTION_TODO_TBL = "section_todo_tbl";

    public DatabaseHelperTest(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + SECTION_TODO_TBL + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title VARCHAR," +
                    "date VARCHAR," +
                    "sec_id INTEGER);"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SECTION_TODO_TBL);
        onCreate(db);
    }

    public boolean InsertToDoTbl(String title, String date, int secID ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("sec_id", secID);

        long l = db.insert(SECTION_TODO_TBL, null, contentValues);
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor checkTable( int secId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        res = db.rawQuery("select * from " + SECTION_TODO_TBL+" where sec_id =\""+ secId+"\"" , null );

        return res;
    }

    public boolean DeleteToDoTbl(String id, String secId) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        long l = db1.delete(SECTION_TODO_TBL, "sec_id=? and id=? ", new String[]{ secId, id });
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }


    public boolean UpdateTODoTbl(String title, int secId, String date, String id) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("sec_id", secId);

        long l = db1.update(SECTION_TODO_TBL, contentValues, "id=?", new String[]{ id });
        if (l != -1) {
            return true;
        } else {
            return false;
        }
    }
}