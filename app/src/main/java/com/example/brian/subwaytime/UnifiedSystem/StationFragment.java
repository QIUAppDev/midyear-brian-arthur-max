package com.example.brian.subwaytime.UnifiedSystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class StationFragment extends DialogFragment {

    //UnifedMain implements this interface and uses listener to recieve updates from the dialog
    public interface StationFragmentListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    StationFragmentListener listener;

    //attempts to initialize the listener that UnifedMain will eventually use
    //onAttach uses Context and converts to Activity par Android's depreciating direct usage of Activity in onAttach
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Activity activity = (Activity) context;
        if (context instanceof Activity){
            activity = (Activity) context;
        }

        try{
            listener = (StationFragmentListener) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }


    }

    //creates the actual dialog
    //the listener is called such that UnifedMain is notified that the user has tapped
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder sample_builder = new AlertDialog.Builder(getActivity());
        sample_builder.setView("activity_main");
        sample_builder.setMessage("This is a sample prompt. No new networks should be scanned while this prompt is up");
        sample_builder.setCancelable(true);
        sample_builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogPositiveClick(StationFragment.this);
            }
        });
        sample_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogNegativeClick(StationFragment.this);
            }
        });
        return sample_builder.create();

    }
}
