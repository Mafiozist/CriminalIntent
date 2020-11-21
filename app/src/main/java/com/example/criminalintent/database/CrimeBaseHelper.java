package com.example.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.criminalintent.database.CrimeDbScheme.CrimeTable;

//Класс используемый для создания и открытия БД, освобождает разработчика от рутинных занятий
public class CrimeBaseHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME= "crimeBase.db";

    public  CrimeBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDbScheme.CrimeTable.Name + "(" +
                        "_id integer primary key autoincrement, " +
                        CrimeTable.Cols.UUID + ", " +
                        CrimeTable.Cols.TITLE + ", " +
                        CrimeTable.Cols.DATE + ", " +
                        CrimeTable.Cols.SOLVED  + ", " +
                        CrimeTable.Cols.SUSPECT +
                        ");"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
