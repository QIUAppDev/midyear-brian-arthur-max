package com.example.brian.subwaytime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usrQueryObj = findViewById(R.id.userQueryInput);
        final ListView outputUsrQuery = findViewById(R.id.listView);

        //TODO make the sql database that holds all the data we have

        usrQueryObj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO function here to update the list
                String temp = "user changed text to:"+usrQueryObj.getText().toString();
                Log.d("aaaagh",temp);
                //works^

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }



}
