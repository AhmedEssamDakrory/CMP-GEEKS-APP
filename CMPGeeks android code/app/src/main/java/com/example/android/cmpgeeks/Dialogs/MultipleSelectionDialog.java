package com.example.android.cmpgeeks.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.TimePicker;

import com.example.android.cmpgeeks.CreateUpdateActivity.CreateUpdateCourse;
import com.example.android.cmpgeeks.R;

import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("ValidFragment")
public class MultipleSelectionDialog extends DialogFragment {
    private MultipleSelectionDialog.DialogListener listener;
    private String[] items;
    boolean[] checkedItemsD;
    ArrayList<Integer> mUserItems = new ArrayList<>();


    @SuppressLint("ValidFragment")
    public MultipleSelectionDialog(ArrayList<String> array) {

        items = new String[array.size()];
        for (int i = 0 ; i < array.size() ; i++){
            items[i]=array.get(i);
        }
    }

    public MultipleSelectionDialog(String[] items) {
        this.items = items;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        checkedItemsD = new boolean[items.length];
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setTitle("items");
        mBuilder.setMultiChoiceItems(items, checkedItemsD, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems.add(position);
                } else {
                    mUserItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                listener.applyText(mUserItems);
            }
        });

        mBuilder.setNegativeButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItemsD.length; i++) {
                    checkedItemsD[i] = false;
                    mUserItems.clear();
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        return mDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MultipleSelectionDialog.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement DialogListener");
        }
    }



    public interface DialogListener {
        void applyText(ArrayList<Integer> selectedIndexs);
    }


}
