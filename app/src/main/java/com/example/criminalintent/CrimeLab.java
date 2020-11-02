package com.example.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Предназначен для простой передачи данных между контроллерами
//Данный класс живет намного дольше чем любой адаптер, но есть опасность уничтожения при очистки памят ОС
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<Crime>();
        for (int i = 0; i< 100; i++){
            Crime crime = new Crime();
            crime.setmTitle("Crime #" + i);
            crime.setmSolved(i % 2 == 0);
            crime.setmRequiresPolice( i % 5 == 0);
            mCrimes.add(crime);
        }
    }

    public static  CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes){
            if(crime.getmID().equals(id)) return crime;
        }


        return null;
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }


    /*public int getItemViewType(int postition){
        if(mCrimes.get(postition).ismRequiresPolice())
            return postition;

        return 0;
    }*/

}
