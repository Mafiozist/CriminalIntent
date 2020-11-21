package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.database.CrimeBaseHelper;
import com.example.criminalintent.database.CrimeCursorWrapper;
import com.example.criminalintent.database.CrimeDbScheme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.example.criminalintent.database.CrimeDbScheme.CrimeTable;

//Предназначен для простой передачи данных между контроллерами
//Данный класс живет намного дольше чем любой адаптер, но есть опасность уничтожения при очистки памят ОС
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public static  CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public Crime getCrime(UUID id){

        try (CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " =?",
                new String[]{id.toString()}
        )) {
            if (cursor.getCount() == 0) return null;

            cursor.moveToFirst();
            return cursor.getCrime();
        }

    }

    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        try (CrimeCursorWrapper cursor = queryCrimes(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        return crimes;
    }

    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.Name, null, values);

    }
    public void updateCrime(Crime crime){
        String uuidStr = crime.getmID().toString();
        ContentValues contentValues = getContentValues(crime);


        //3 - аргумент это условие where
        //Но при использовании =? ваш код будет делать именно то, что положено: значение
        // интерпретируется как строковые данные, а не как код.
        mDatabase.update(CrimeTable.Name, contentValues,
                CrimeTable.Cols.UUID + " =? ", new String[]{uuidStr});

    }

    public void remove(Crime crime){
        mDatabase.delete(CrimeTable.Name, CrimeTable.Cols.UUID + " =? ", new String[]{crime.getmID().toString()});
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getmID().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getmTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getmDate().toString());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved().toString());
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.Name,
                null,//выбираются все столбцы
                whereClause,
                whereArgs,
                null, //groupBy
                null,//Having
                null//Orderby
        );
        return new CrimeCursorWrapper(cursor);
    }

    //Этот метод только возвращает файл, но не создает его

    public File getPhotoFile(Crime crime){
        File filesdir = mContext.getFilesDir();
        return new File(filesdir, crime.getPhotoFilename());
    }

}
