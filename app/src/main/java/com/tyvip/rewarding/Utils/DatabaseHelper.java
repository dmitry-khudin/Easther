package com.tyvip.rewarding.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.tyvip.rewarding.Models.BoxModel;

/**
 * Created by bryden on 12/8/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        //必须通过super调用父类当中的构造函数
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, int version){
        this(context,name,null,version);
    }

    public DatabaseHelper(Context context, String name){
        this(context,name,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                Constants.TABLE_NAME + "("
                + Constants.COLUMN_ID + " INTEGER PRIMARY KEY,"
                + Constants.COLUMN_TITLE + " TEXT,"
                + Constants.COLUMN_DESC + " TEXT,"
                + Constants.COLUMN_IMAGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void addProduct(BoxModel product) {

        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE, product.getTitle());
        values.put(Constants.COLUMN_DESC, product.getDescription());
        values.put(Constants.COLUMN_IMAGE, product.getImgURL());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Constants.TABLE_NAME, null, values);
        db.close();
    }

    public List<BoxModel> getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<BoxModel> list = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[] { Constants.COLUMN_ID,
                        Constants.COLUMN_TITLE,
                        Constants.COLUMN_DESC,
                        Constants.COLUMN_IMAGE }, null, null, null,
                null, null);
        while(cursor.moveToNext())
        {
            BoxModel model = new BoxModel();
            model.setId(cursor.getInt(0));
            model.setTitle(cursor.getString(1));
            model.setDescription(cursor.getString(2));
            model.setImgURL(cursor.getString(3));
            list.add(model);
        }
        return list;
    }
    public BoxModel findProduct(String productname) {
        String query = "Select * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.COLUMN_TITLE + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        BoxModel product = new BoxModel();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setId((cursor.getInt(0)));
            product.setTitle(cursor.getString(1));
            product.setDescription((cursor.getString(2)));
            product.setImgURL((cursor.getString(3)));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }
}
