package com.amin.abod.jornal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Operation extends SQLiteOpenHelper{

    public static final String DB_Name ="MyPocket.db";  //why did i name it like this ?
    public static final String OPERATIONS_Table ="Operations"; //table to store all the operations in

    public Operation(Context context) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL("create table "+ OPERATIONS_Table + " ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            " amount INTEGER," +
            " type INTEGER," +
            " image TEXT,"+
            " Date TEXT ) " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists "+ OPERATIONS_Table);       //I need to come back for this one later :)
        onCreate(sqLiteDatabase);
    }

    public boolean insertPosOperation(double amountAdded ){
        long result ;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //AMOUNT
        cv.put("type", 1);          //type specifier .. 0 = negative, 1 = positive
        cv.put("amount",amountAdded);


        //DATE
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa | EEE "); //time format
        String currentDateandTime = sdf.format(new Date());
        cv.put("Date",currentDateandTime);

        //IMAGE


        //CHECK
        result = db.insert(OPERATIONS_Table,null,cv);
        if (result == -1){
            return false;
        }else
            return true;
    }

    public boolean insertNegOperation(double amountAdded ,String imageType){
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //AMOUNT
        amountAdded = amountAdded * -1;     //it is positive make it negative (simple maths) .. helps in counting the total balance
        cv.put("amount",amountAdded);
        cv.put("type",0);       //type specifier .. 0 for negative, 1 for positive



        //DATE
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa | EEE "); //time format
        String currentDateAndTime = sdf.format(new Date());
        cv.put("Date",currentDateAndTime);

        //ImageType
        cv.put("image",imageType);

        //CHECK
        result = db.insert(OPERATIONS_Table,null,cv);
        if (result == -1){
            return false;
        }else
            return true;
    }

    //calculates the total balance in the account by summing all rows in column " amount " (positive + (- minus) = result )
    public double calculateTotalBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM( amount ) FROM " + OPERATIONS_Table, null);

        boolean b = cursor.moveToFirst();
        if (b){
            double total = cursor.getDouble(0);
            cursor.close();
            return total;
        }
        return Double.MAX_VALUE +1;  //flag on something is wrong .. make it brake the bounds of DOUBLE values
    }

    public Cursor getRecords(int choice){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (choice == 1) {
            return db.query(false,OPERATIONS_Table,null,"type =?",new String[]{"1"},null,null,null,null);    //Negative operations query
        }else if (choice == 0){
            return db.query(false,OPERATIONS_Table,null,"type =?",new String[]{"0"},null,null,null,null);    //Positive operations query
        }else {
            //will think about it later in sha'a Allah if needed
        }
        //cursor.close(); //doing this caused errors :\
        return cursor;
    }

    //delete row having this amount of money and at this Date (this method was before i use the ID to query).
    public void deleteRaw(String desiredAmount, String desiredDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String operationID = getOperationID(desiredAmount, desiredDate);    //Find ID
        db.delete(OPERATIONS_Table,"_id=?", new String[]{operationID});   //Delete row with that ID
    }

    //this method is to query about the desired amount and Date then return its id, null otherwise .(to be handled)
    private String getOperationID(String desiredAmount, String desiredDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor ;

        cursor = db.query(true, OPERATIONS_Table,new String[]{"_id"}, "amount=? AND Date=?", new String[]{desiredAmount, desiredDate}, null, null, null, null);

        //check Table if empty or not
        if (cursor.moveToFirst()){  //moveToFirst is method that returns true if it is applicable to move to first entry in the returned table
            return cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        } else {    //can't move to first , means no rows found which means nothing to be deleted so return null as an indicator of error
            Log.e("Class Operation - Method getOperationID()","couldn't obtain ID (empty table)");
            return null;
        }
    }

    public boolean deleteRawByID(double desiredID, boolean type) {
        SQLiteDatabase db = this.getReadableDatabase();
        int NumberOfDeletedRaws;
        if(type){
             NumberOfDeletedRaws = db.delete(OPERATIONS_Table, "_id=? AND type=?", new String[]{String.valueOf(desiredID) , String.valueOf(1)});
        }else{
             NumberOfDeletedRaws = db.delete(OPERATIONS_Table, "_id=? AND type=?", new String[]{String.valueOf(desiredID) , String.valueOf(0)});
        }

        if(NumberOfDeletedRaws == 1){
            return true;
        }else{
            return false;
        }
    }
    //method to delete(Drop) OPERATIONS table
    public void DeleteAllOperations() {
        SQLiteDatabase db = this.getReadableDatabase();
//        db.execSQL("DELETE * FROM TEBLE"+ OPERATIONS_Table);
        db.delete(OPERATIONS_Table, null,null);
    }

}
