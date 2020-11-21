package com.example.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

// Code need to for binding and second use
//В конструкторе данного класса происходит заполнение layout list_item_crime
class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
{
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private Crime mCrime;
    private ImageView mSolvedImageView;
    public static  UUID mLastCrimeChanged;


    public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_crime, parent, false));
        mTitleTextView = itemView.findViewById(R.id.crime_title);
        mDateTextView = itemView.findViewById(R.id.crime_date);
        itemView.setOnClickListener(this);
        mSolvedImageView = itemView.findViewById(R.id.crime_solved);
    }

    //Метод необходимый объекту ViewHolder, вызывается каждый раз когда в
    // СrimeHolder должен отображаться новый объект
    //Данный метод будет вызываться каждый раз, когда RecycleView потребует связать объект CrimeHolder

    public void bind(Crime crime){
        mCrime = crime;
        mTitleTextView.setText(mCrime.getmTitle());
        mDateTextView.setText( DateFormat.format( "E, H:mm, d/M/y"  , mCrime.getmDate()).toString());
        mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.INVISIBLE);
        Log.i("ImageStatusOnBinding", crime.isSolved().toString());
    }

    //Событие отвечающее за запуск подробного описания класса Crime
    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), mCrime.getmTitle() + " clicked!", Toast.LENGTH_SHORT).show();
        Intent intent = CrimePagerActivity.newIntent(view.getContext(), mCrime.getmID());
        //Если событие клик произошло - вероятно данные были изменены, лучше сделать проверку, но пока не знаю
        // как во всяком случае, лучше обновить 1 объект чем все
        view.getContext().startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(view.getContext(), mCrime.getmTitle() + " LongClicked!", Toast.LENGTH_SHORT).show();
        return true;
    }

}
