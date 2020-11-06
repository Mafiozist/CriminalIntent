package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;


//он добавляет возвращаемые фрагменты в активность и помогает
// ViewPager идентифицировать представления фрагментов для
//их правильного размещения?????


//В данной реализации используется FragmentStatePagerAdapter т.к. он является более экономной версией по отношениею к FragmentPagerAdapter
//FragmentStatePagerAdapter - вызывает метод remove и удаляет фрагмент, а FragmentPagerAdapter - detach тем самым сохраняя фрагмент в памяти
//244
public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mCrimeFirstButton;
    private Button mCrimeLastButton;

    private static final String EXTRA_CRIME_ID = "com.example.criminalintent.crime_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mCrimeLastButton = findViewById(R.id.crime_last_button) ;
        mCrimeFirstButton = findViewById(R.id.crime_first_button) ;

        mCrimes = CrimeLab.get(this).getCrimes();
        mViewPager = findViewById(R.id.crime_view_pager);
        //Экземпляр фрагмент менеджера нужен, чтобы данный агент мог возрващать фрагменты
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter( fm ) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);

                return CrimeFragment.newIstance(crime.getmID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //Установки элемента выбранного пользователем
        for (int crimeIndex = 0; crimeIndex < mCrimes.size(); ++crimeIndex){
            if (mCrimes.get(crimeIndex).getmID().equals(crimeID)) {
                mViewPager.setCurrentItem(crimeIndex);

                break;
            }
        }

        //mViewPager.setOffscreenPageLimit(2);


        //В будующем сделать блокировку кнопок
        mCrimeFirstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);

            }
        });

        mCrimeLastButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCrimes.size() - 1);

            }
        });

    }


    //Метод необходимый для указаний, какой объект должен отображаться(работает по UUID)
    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);

        return intent;
    }


}
