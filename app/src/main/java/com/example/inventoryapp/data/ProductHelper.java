package com.example.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 5;

    public ProductHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SQL = "CREATE TABLE "+InventoryContract.ProductEntry.CONSTANT_TABLE_NAME+
                               "("+InventoryContract.ProductEntry.CONSTANT_PRODUCT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                InventoryContract.ProductEntry.CONSTANT_PRODUCT_NAME+" TEXT NOT NULL, "+
                InventoryContract.ProductEntry.CONSTANT_PRODUCT_IMAGE+" BLOB , "+
                InventoryContract.ProductEntry.CONSTANT_PRODUCT_PRICE+" TEXT NOT NULL, "+
                InventoryContract.ProductEntry.CONSTANT_PRODUCT_QUANTITY+" INTEGER );";
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
