package com.example.brian.subwaytime;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ResultViewModel viewModel; //interacts with database (i.e. makes the necssary queries)
    private RecyclerViewAdapter recyclerViewAdapter; //the middleman between the recyclerview front-end and the backend
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usrQueryObj = findViewById(R.id.userQueryInput);

        recyclerView = (RecyclerView) findViewById(R.id.listView);

        //instantiates the adapter and links recyclerview with it
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<derpwork>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        //instantiates the ResultViewModel
        viewModel = ViewModelProviders.of(this).get(ResultViewModel.class);

        //tells the app to observe changes made to the ui (i.e. the
        viewModel.getOutput_list().observe(MainActivity.this, new Observer<List<derpwork>>() {
            @Override
            public void onChanged(@Nullable List<derpwork> itemAndPeople) {
                recyclerViewAdapter.addItems(itemAndPeople);
            }
        });

        //TODO make the sql database that holds all the data we have

        //test stuff here
            final WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        usrQueryObj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO function here to update the list
                //this remains unchanged
                String temp = "user changed text to:"+usrQueryObj.getText().toString();
                Log.d("aaaagh",temp);
                //works^
                WifiInfo info = manager.getConnectionInfo();
                Log.d("wifiinfo", info.toString());


                //this is the part I added
                String query = usrQueryObj.getText().toString();
                viewModel.query_for_search_result(query);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    @Override
    public void onClick(View v) {
        Log.d("click","click!");
        //derpwork borrowModel = (derpwork) v.getTag();
        //viewModel.deleteItem(borrowModel);
        //return true;
    }

}
