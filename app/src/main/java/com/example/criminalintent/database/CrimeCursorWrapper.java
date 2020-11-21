package com.example.criminalintent.database;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.criminalintent.database.CrimeDbScheme.CrimeTable;
import com.example.criminalintent.Crime;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

//Класс необходимый для удобной работы c Cursor
public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuidStr = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        String date = getString(getColumnIndex(CrimeTable.Cols.DATE));
        String isSolved = getString(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidStr));
        crime.setmTitle(title);
        Date tempDate = new Date(date);

        crime.setmDate( tempDate );
        crime.setmSolved(isSolved.equals("true"));
        crime.setSuspect(suspect);
        return crime;
    }


}
