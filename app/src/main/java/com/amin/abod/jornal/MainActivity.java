package com.amin.abod.jornal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
Operation operation2 = new Operation(this);
minusCostumeListAdapter m;
positiveCostumeListAdapter p;
ListView minusList;
ListView posList;
Button addBtn;
static public boolean refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addButton);

        minusList = findViewById(R.id.minusList);
        posList = findViewById(R.id.posList);

        refresh = false;

        m = new minusCostumeListAdapter(this,operation2.getRecords(0));
        minusList.setAdapter(m);

        p = new positiveCostumeListAdapter(this, operation2.getRecords(1));
        posList.setAdapter(p);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Add.class)); //guess what? never tried this before and made it in one shot BAM الحمدلله
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