package com.amin.abod.jornal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.VIBRATOR_SERVICE;

public class minusCostumeListAdapter extends CursorAdapter {

    Operation operation3;
    public minusCostumeListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.costume_list2, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Operation operation = new Operation(context);
        //find Views to inflate/fill
        TextView Amount = (TextView) view.findViewById(R.id.Amount);
        TextView operationDate = (TextView) view.findViewById(R.id.operationDate);
        ImageView iconOpType = view.findViewById(R.id.iconOpType);

        //use the cursor to query the DB and get desired rows .. e.g. amountInflater will take the result and fill the text view (Amount)
        String amountInflater = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
        String dateInflater = cursor.getString(cursor.getColumnIndexOrThrow("Date"));
        final String IDinflater = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String imageTypeInflater = cursor.getString(cursor.getColumnIndexOrThrow("image"));

        Amount.setText(amountInflater);
        operationDate.setText(dateInflater);

        switch (imageTypeInflater){
            case "res":
                iconOpType.setImageResource(R.drawable.restaurant);
                break;
            case "cafe":
                iconOpType.setImageResource(R.drawable.coffee);
                break;
            case "gas":
                iconOpType.setImageResource(R.drawable.gas);
                break;
            case "enter":
                iconOpType.setImageResource(R.drawable.entertainment3);
                break;

            default:
                iconOpType.setImageResource(R.drawable.decrease);

        }


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(R.string.DeleteAlertTitle);
                builder.setMessage(R.string.DeletingAlertMessage);
                builder.setCancelable(true);
                ShakeItBaby();
                builder.setPositiveButton("نعم احذف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean good = operation.deleteRawByID(Double.parseDouble(IDinflater), false);
                        if (good){
                            Toast.makeText(context,"تم الحذف بنجاح",Toast.LENGTH_SHORT).show();
                            MainActivity.refresh = true;
                        }else{
                            Toast.makeText(context," لم يتم الحذف",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("لا ,لاتحذف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create();
                builder.show();


                return true;
            }

            private void ShakeItBaby() {
                if (Build.VERSION.SDK_INT >= 26) {
                    ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                }
            }
        });

    }

}