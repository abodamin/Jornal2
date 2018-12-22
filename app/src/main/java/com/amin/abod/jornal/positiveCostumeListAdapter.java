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

public class positiveCostumeListAdapter extends CursorAdapter {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_costume_list);
//
//    } //MAYBE I SHOULDN'T

    public positiveCostumeListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.costume_list2, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        //find Views to inflate
        ImageView iconOpType = view.findViewById(R.id.iconOpType);
        TextView Amount = (TextView) view.findViewById(R.id.Amount);
        TextView operationDate = (TextView) view.findViewById(R.id.operationDate);
        final Operation operation = new Operation(context);

        //use the cursor to query the DB and get desired rows .. amountInflater will take the result and fill the text view (Amount)
        String amountInflater = cursor.getString(cursor.getColumnIndexOrThrow("amount"));//!@#$%^&
        //query about Date column index and get it.
        String dateInflater = cursor.getString(cursor.getColumnIndexOrThrow("Date"));
        //ID inflater
        final String IDinflater = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        Amount.setText(amountInflater);
        operationDate.setText(dateInflater);

        iconOpType.setImageResource(R.drawable.up);

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
                        boolean good = operation.deleteRawByID(Double.parseDouble(IDinflater), true);
                        if (good){
                            Toast.makeText(context,"تم الحذف بنجاح",Toast.LENGTH_SHORT).show();
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