package com.example.criminalintent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//Задача этого фрагмента - отображение информации содержащейся в классе Crime т.е.
// выдача конкретной информации о конкретном преступлении
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolved;

    //Отвечает за получение crimeID из Bundle(Сохранений)
    private static final String ARG_CRIME_ID = "crime_id";
    //Отвечает за получение date из Bundle(Сохранений)
    private static final String DIALOG_DATE = "date";
    //Отвечает за получение time из Bundle(Сохранений)
    private static final String DIALOG_TIME = "time";
    //коды возврата для даты и времени
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Получаем crimeId по ключуи ищем схожий объект в crimeLab
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    //Получает UUID, создает пакет аргументов, создает экземпляр фрагмента, а
    // затем присоединяет аргументы к фрагменту.
    public static CrimeFragment newIstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override //Именно этот метод используется для заполнения отображения layout
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setmTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getmDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog =  DatePickerFragment.newInstance(mCrime.getmDate());

                //Создание target фрагмента, это связь востанавливается сама, после уничтожения по коду
                //Иначе пришлось бы перегружать методы OnActivitySave и т.д.
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mDateButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fm = getFragmentManager();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mCrime.getmDate());

                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerFragment dialog = TimePickerFragment.newInstance(hour, minute);
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
                return true;
            }
        });


        mSolved = view.findViewById(R.id.crime_solved);
        mSolved.setChecked(mCrime.isSolved());
        mSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setmSolved(b);
            }
        });

        updateDateTime();
        return view;
    }

    //258
    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        switch (requestCode){
            //Блок установки даты посредством dialog_time
            case REQUEST_DATE:
                    Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                    mCrime.setmDate(date);
                    updateDateTime();
                break;

                //Блок установки времени посредством dialog_time
            case REQUEST_TIME:
                    Calendar calendar =  Calendar.getInstance();
                    calendar.setTime(mCrime.getmDate());
                    calendar.set(Calendar.HOUR, (int) data.getSerializableExtra(TimePickerFragment.EXTRA_HOUR));
                    calendar.set(Calendar.MINUTE, (int) data.getSerializableExtra(TimePickerFragment.EXTRA_MINUTE));
                    mCrime.setmDate(calendar.getTime());
                    updateDateTime();
                break;
        }

    }

    private void updateDateTime(){
        mDateButton.setText(mCrime.getmDate().toString());
    }



}
