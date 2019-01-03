package com.example.android.cmpgeeks.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragmentDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    private TimePickerFragmentDialog.DialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), this, hour, minute, true);//Yes 24 hour time

        mTimePicker.setTitle("Select Time");
        return mTimePicker;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TimePickerFragmentDialog.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement DialogListener");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.applyText(hourOfDay,minute);
    }


    public interface DialogListener {
        void applyText( int hour,int minute) ;
    }


}
