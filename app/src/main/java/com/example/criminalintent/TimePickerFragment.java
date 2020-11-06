package com.example.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_HOUR = "hour";
    public static final String EXTRA_HOUR = "com.example.criminalintent.hour";
    private static final String ARG_MINUTE = "hour";
    public static final String EXTRA_MINUTE = "com.example.criminalintent.minute";

    private TimePicker mTimePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR, (int) getArguments().getSerializable(ARG_HOUR));
        calendar.set(Calendar.MINUTE, (int) getArguments().getSerializable(ARG_MINUTE));

        //Устанавливаем сохраненное время
        mTimePicker = view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setHour(calendar.get(Calendar.HOUR));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        sendResult(Activity.RESULT_OK, hour, minute);
                    }
                }).create();
    }

    private void sendResult(int resultCode, int Hour, int Minute){
        if(getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, Hour);
        intent.putExtra(EXTRA_MINUTE, Minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static TimePickerFragment newInstance(int hour, int minute){
        Bundle bundle = new Bundle();

        bundle.putSerializable(ARG_HOUR, hour);
        bundle.putSerializable(ARG_MINUTE, minute);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
