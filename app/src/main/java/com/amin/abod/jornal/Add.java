package com.amin.abod.jornal;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Add extends AppCompatActivity {

    public static String imageType = "Default";   //to store the type of the image instead of the image itself then call it back later in List
    public static String readText  = "";

    Operation operation = new Operation(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button saveBtn = findViewById(R.id.saveChangesButton);
        final EditText amountText = findViewById(R.id.amountText);
        final TextView showBalance = findViewById(R.id.showBalance);
        final RadioButton Pos = findViewById(R.id.radioButtonPos);
        final RadioButton Neg = findViewById(R.id.radioButtonNeg);
        final ImageView resImg = findViewById(R.id.Restaurant);
        final ImageView cafeImg = findViewById(R.id.Cafe);
        final ImageView gasImg = findViewById(R.id.Gas);
        final ImageView enterImg = findViewById(R.id.Entertainment);
        final View group = findViewById(R.id.group);



        //toggle radio buttons initially depending on balance (preferred)
        //if you are broke probably you are going to choose the + button and vise-versa
        if(operation.calculateTotalBalance() > 0) {
            Neg.toggle();
            group.setVisibility(View.VISIBLE);
        }else{
            Pos.toggle();
            group.setVisibility(View.GONE);
        }

        //Set visibility of group images Gone when it is Positive operation
        Pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group.setVisibility(View.GONE);
            }
        });
        //Set visibility of group images Visible when it is Negative operation
        Neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group.setVisibility(View.VISIBLE);
            }
        });

        resImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // a way around making custom RadioButtons.
                resImg.setBackgroundColor(Color.GRAY);
                gasImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                cafeImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                enterImg.setBackgroundColor(Color.parseColor("#ffffe2"));

                imageType = "res";
            }
        });

        cafeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                gasImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                cafeImg.setBackgroundColor(Color.GRAY);
                enterImg.setBackgroundColor(Color.parseColor("#ffffe2"));

                imageType = "cafe";
            }
        });

        gasImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                gasImg.setBackgroundColor(Color.GRAY);
                cafeImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                enterImg.setBackgroundColor(Color.parseColor("#ffffe2"));

                imageType = "gas";
            }
        });

        enterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                gasImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                cafeImg.setBackgroundColor(Color.parseColor("#ffffe2"));
                enterImg.setBackgroundColor(Color.GRAY);

                imageType = "enter";
            }
        });

        double balance = operation.calculateTotalBalance();
        String showingBalance = String.valueOf(balance);
        showBalance.setText(showingBalance);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here where we add to DB
                boolean hint;       //helps in Toast messages

                readText = amountText.getText().toString().trim();         //read amount of money to be added or withdraw.

                if(!TextUtils.isEmpty(readText) && Pos.isChecked()){       //to indicate Positive operation & not an empty amount field.
                    hint = operation.insertPosOperation(Double.parseDouble(readText));
                    if (hint){//say succeeded operation.
                        Toast.makeText(getApplicationContext(),"تم تسجيل العملية",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"هناك خطأ",Toast.LENGTH_SHORT).show();
                    }
                }else if(!TextUtils.isEmpty(readText) && Neg.isChecked()){      //to indicate Negative operation & not an empty amount field.
                    double validity = (int) operation.calculateTotalBalance();
                    if (validity > 0) {    //check if there is enough balance.
                        hint = operation.insertNegOperation(Double.parseDouble(readText),imageType);//returning value tells us if things are being set alright or no
                        imageType = "Default";
                        if (hint) {
                            Toast.makeText(getApplicationContext(), "تم تسجيل العملية", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "هناك خطأ", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"رصيدك غير كافٍ",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"لاتوجد تغييرات للحفظ",Toast.LENGTH_LONG).show();//case empty Field or not toggled buttons
                }

                finish();   //to not even press the Back button .. just chill dear client :)

            }
        });
    }
}