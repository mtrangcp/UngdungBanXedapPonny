package tranghtmph26263.fpoly.ungdungbanxedap.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import tranghtmph26263.fpoly.ungdungbanxedap.database.MyDbHelper;
import tranghtmph26263.fpoly.ungdungbanxedap.entity.Discount;

public class DiscountDAO {
    SQLiteDatabase db;
    MyDbHelper dbHelper;

    public DiscountDAO(Context context){
        dbHelper = new MyDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insertNew(Discount obj){
        ContentValues contentValues = new ContentValues();
        contentValues.put("code_name", obj.getCode_name());
        contentValues.put("value", obj.getValue());
        contentValues.put("start_date", obj.getStart_date());
        contentValues.put("expiration_date", obj.getExpiration_date());
        contentValues.put("detail", obj.getDetail());

        long res = db.insert("discount", null, contentValues);
        return  res;
    }

    public int deleteRow(Discount obj){
        int res = db.delete("discount", "id = ?" , new String[]{ obj.getId() +"" });
        return  res;
    }

    public int updateRow(Discount obj){
        ContentValues contentValues= new ContentValues();
        contentValues.put("code_name", obj.getCode_name());
        contentValues.put("value", obj.getValue());
        contentValues.put("start_date", obj.getStart_date());
        contentValues.put("expiration_date", obj.getExpiration_date());
        contentValues.put("detail", obj.getDetail());

        Log.d("zzzzz", "updateRow: "+ obj.getId());

        int res = db.update("discount",contentValues, "id = ?", new String[]{obj.getId()+""});
        return  res;
    }

    public Discount selectOne(int ID){
        db = dbHelper.getReadableDatabase();
        Discount obj = null;

        Cursor cursor = db.rawQuery("select * from discount where id = ?",new String[] { ID + "" });
        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String name= cursor.getString(1);
            int value= cursor.getInt(2);
            String startDate= cursor.getString(3);
            String expirationDate= cursor.getString(4);
            String detail= cursor.getString(5);

            obj = new Discount(id,value, name, detail, startDate, expirationDate);
        }

        return obj;
    }

    public ArrayList<Discount> selectAll(){
        ArrayList<Discount> listDiscount = new ArrayList<Discount>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from discount", null);

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                int id = cursor.getInt(0);
                String name= cursor.getString(1);
                int value= cursor.getInt(2);
                String batDau= cursor.getString(3);
                String hetHan= cursor.getString(4);
                String detail= cursor.getString(5);

                listDiscount.add(new Discount(id,value, name, detail, batDau, hetHan));
                cursor.moveToNext();
            }
        }
        return  listDiscount;
    }
//    public ArrayList<Discount> selectAllForUser(int status){
//        ArrayList<Discount> listDiscount = new ArrayList<Discount>();
//        db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from discount", null);
//
//        if(cursor.moveToFirst()){
//            while (!cursor.isAfterLast()){
//                int id = cursor.getInt(0);
//                String name= cursor.getString(1);
//                int value= cursor.getInt(2);
//                String batDau= cursor.getString(3);
//                String hetHan= cursor.getString(4);
//                String detail= cursor.getString(5);
//
//                listDiscount.add(new Discount(id,value, name, detail, batDau, hetHan));
//                cursor.moveToNext();
//            }
//        }
//        return  listDiscount;
//    }

}
