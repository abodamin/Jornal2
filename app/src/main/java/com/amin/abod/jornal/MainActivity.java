package com.amin.abod.jornal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {
Operation operation2 = new Operation(this);
minusCostumeListAdapter m;
positiveCostumeListAdapter p;
ListView minusList;
ListView posList;
Button addBtn;
FloatingActionButton FAB;
static public boolean refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minusList = findViewById(R.id.minusList);
        posList = findViewById(R.id.posList);

        FAB = findViewById(R.id.FAB);

        refresh = false;

        m = new minusCostumeListAdapter(this,operation2.getRecords(0));
        minusList.setAdapter(m);

        p = new positiveCostumeListAdapter(this, operation2.getRecords(1));
        posList.setAdapter(p);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Add.class));
            }
        });



    }



    @Override
    protected void onResume() {
        super.onResume();
        refresh = true;
        refreshLists(refresh);
        refresh = false;
    }

    public void refreshLists(boolean refresh) {

        if (refresh) {
            m = new minusCostumeListAdapter(this, operation2.getRecords(0));
            minusList.setAdapter(m);

            p = new positiveCostumeListAdapter(this, operation2.getRecords(1));
            posList.setAdapter(p);

        }
    }
}