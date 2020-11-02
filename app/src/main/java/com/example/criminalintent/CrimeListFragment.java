package com.example.criminalintent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private void updateUI(){
        CrimeLab crimeLab= CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {

            //Сомневаюсь, что придуманный мной метод работает быстрее чем данный
            //mAdapter.notifyItemChanged(crimes.indexOf( crimeLab.getCrime(CrimeHolder.mLastCrimeChanged) ));
            mAdapter.notifyDataSetChanged();

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false );
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        //Linear layout необходим для работы RecyclerView и отвечает за позицианирование объектов и поведение прокрутки
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }



    //Класс recyclerview взаимодействует с адаптером, когда создает новый объект класса ViewHolder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater  = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }



        @Override
        public int getItemCount() {
            return mCrimes.size();
        }



    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
