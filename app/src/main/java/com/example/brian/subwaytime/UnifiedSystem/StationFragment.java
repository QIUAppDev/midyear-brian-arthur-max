package com.example.brian.subwaytime.UnifiedSystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class StationFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder sample_builder = new AlertDialog.Builder(getActivity());
        sample_builder.setMessage("This is a sample prompt. No new networks should be scanned while this prompt is up");
        sample_builder.setCancelable(true);
        sample_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        return sample_builder.create();

    }
}
