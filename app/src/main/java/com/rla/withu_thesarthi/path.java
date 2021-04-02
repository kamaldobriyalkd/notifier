package com.rla.withu_thesarthi;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Locale;

public class path extends AppCompatActivity {
    EditText textSpeech;
    Button btn_speechText;
    TextToSpeech toSpeech;
    ListView destList;
    TextView whereto;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Button endButton;
    RelativeLayout pathLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        textSpeech = (EditText) findViewById(R.id.TextForSpeech);
        destList = (ListView) findViewById(R.id.destList);
        whereto = (TextView) findViewById(R.id.whereTo);
        endButton = (Button) findViewById(R.id.endButton);
        pathLayout = (RelativeLayout) findViewById(R.id.pathLayout);
        list = new ArrayList<String>();
        list.add("Admin Block");
        list.add("Principal Office");
        list.add("Staff Room");
        list.add("Sports Room");
        list.add("Main Gate");
        list.add("Enterance");
        list.add("Stage");
        list.add("Men's Washroom");
        list.add("Women's Washroom");
        list.add("PC11");
        list.add("Canteen");
        list.add("5");
        list.add("12");
        list.add("Girl's Common Room");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        destList.setAdapter(adapter);

        destList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                whereto.setText(destList.getItemAtPosition(i).toString().trim());
                destList.setVisibility(View.GONE);
                endButton.setVisibility(View.VISIBLE);
                pathLayout.setVisibility(View.VISIBLE);

            }
        });

        btn_speechText = (Button) findViewById(R.id.btn_SpeechText);
        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i!=TextToSpeech.ERROR){
                    toSpeech.setLanguage(Locale.UK);
                }
            }
        });
        btn_speechText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSpeech.speak(textSpeech.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}