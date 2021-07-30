package com.rla.withu_thesarthi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class path extends AppCompatActivity {

    TextToSpeech toSpeech;
    TextView whereto;
    TextView from;
    Button endButton;
    RelativeLayout pathLayout;

    private ArrayList<UFODevice> ufoDevicesList = new ArrayList<>();
    UFOBeaconManager ufoBeaconManager;

    String CurrentLoc = "";
    String Destination ="";
    String strJson="";

    TextView direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        whereto = (TextView) findViewById(R.id.whereTo);
        from = (TextView) findViewById(R.id.from);
        endButton = (Button) findViewById(R.id.endButton);
        pathLayout = (RelativeLayout) findViewById(R.id.pathLayout);
        Intent intent = getIntent();
        Destination = intent.getStringExtra("final");
        whereto.setText(Destination);
        CurrentLoc = intent.getStringExtra("initial");
        from.setText(CurrentLoc);
        direction = (TextView) findViewById(R.id.direction);
/*        strJson="{ \"Directions\" :[{\"Initial\":\"Entrance\",\"Final\":\"Principal Office\",\"Instructions\":[" +
                "\"Move Forward 20 steps\",\"Turn right and move forward 10 steps\",\"You are at stairs You are at Stairs There are 10 stairs\",\"You reached Principal Office\"]," +
                "\"Stations\":[\"Front Building\",\"Stage Stairs\",\"Principal Office\"]}] }";*/


        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    toSpeech.setLanguage(Locale.UK);
                }
            }
        });


        // voice instruction
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(intent);
                toSpeech.speak("Directions ended for " + Destination, TextToSpeech.QUEUE_FLUSH, null);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        strJson="{ \"Directions\" :[{\"Initial\":\"Entrance\",\"Final\":\"Stage\",\"Instructions\":[" +
                "\"Move Forward 20 steps\",\"You reached Stage\"],\"Stations\":[\"Entrance\",\"Stage\"]}] }";

        try {
            JSONObject  jsonRootObject = new JSONObject(strJson);
            JSONArray jsonArray = jsonRootObject.optJSONArray("Direction");
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cur = jsonObject.optString("Initial").toString();
                String des = jsonObject.optString("Final").toString();
                if(cur==CurrentLoc && des==Destination){
                    JSONArray instructions = jsonObject.optJSONArray("Instructions");
                    JSONArray stations = jsonObject.optJSONArray("Stations");
                    for(int j=0; j < instructions.length(); j++){
                        JSONObject st = stations.getJSONObject(j);
                        JSONObject inst = instructions.getJSONObject(j);
                        while (!st.equals(CurrentLoc)){
                            startScanning();
                        }
                        if (st.equals(CurrentLoc)){
                            direction.setText(inst.toString());
                        }
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
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
                                String location = CurrentLocation.curLoc(ufodevice);
                                CurrentLoc=location;
                                stopScanning();
                            }
                        }
                    }
                });

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(path.this, code + ": " + message, Toast.LENGTH_SHORT).show();
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