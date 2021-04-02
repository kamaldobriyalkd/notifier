package com.rla.withu_thesarthi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;


import java.util.ArrayList;
import java.util.Locale;


public class HomeScreen extends AppCompatActivity {
    SearchView mySearchView;
    ListView myList;
    UFOBeaconManager ufoBeaconManager;

    ImageView location;
    ImageView mylocation;
    TextView curLoc;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    String currentLocation;

    private ArrayList<UFODevice> ufoDevicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ufoBeaconManager = new UFOBeaconManager(this);
        setContentView(R.layout.activity_home_screen);
        mySearchView = (SearchView) findViewById(R.id.searchView);
         location = (ImageView) findViewById(R.id.imageView);
         mylocation = (ImageView) findViewById(R.id.imageView3);
         curLoc = (TextView) findViewById(R.id.textView3);
        myList = (ListView) findViewById(R.id.myList);
        list = new ArrayList<String>();
        list.add("Admin Block");
        list.add("Principal Office");
        list.add("Staff Room");
        list.add("Sports Room");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        myList.setAdapter(adapter);

        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);
                return false;
            }
        });


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mySearchView.setQuery(myList.getItemAtPosition(i).toString().trim(), false);
            }
        });

        mySearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (hasWindowFocus()){
                    Intent intent = new Intent(getApplicationContext(), path.class);
                    intent.putExtra("currentLocation",currentLocation);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ufoDevicesList = new ArrayList<>();
        stopScanning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ufoDevicesList = new ArrayList<>();
        stopScanning();
    }

    public void btnSpeech(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hey Speak Something");
        try {
            startActivityForResult(intent, 1);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mySearchView.setQuery(result.get(0), false);
                }
        }
    }

    public void startScanning() {

        ufoBeaconManager.startScan(new OnScanSuccessListener() {
            @Override
            public void onSuccess(final UFODevice ufodevice) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(ufodevice.getDistanceInString()=="Immediate"){
                        if (ufoDevicesList != null && !ufoDevicesList.contains(ufodevice)) {
                            ufoDevicesList.add(ufodevice);
                            currentLocation = CurrentLocation.curLoc(ufodevice);
                            curLoc.setText(currentLocation);
                            }
                        }
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(final int code, final String message) {

                // Log.e("startScan", "Error code:- " + code + " Message:- " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeScreen.this, code + ": " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void stopScanning() {
        ufoBeaconManager.stopScan(new OnSuccessListener() {
            @Override
            public void onSuccess(boolean isStop) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //log
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //log
                    }
                });
            }
        });
    }
}