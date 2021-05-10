package org.altbeacon.beaconreference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ServicesOffered extends AppCompatActivity {
    Button button;
    ListView textView;
    TextView text;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_offered);
        String sessionId = getIntent().getStringExtra("s");
        Str(sessionId);
        button=findViewById(R.id.backbutton);
        text=findViewById(R.id.label);
        button.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
              onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void Str(String s) {
        Log.d("jjjjj", s);
        ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, arrayList);
        arrayList.add(s);
        textView = findViewById(R.id.Textview);

        textView.setAdapter(listAdapter);


    }
}