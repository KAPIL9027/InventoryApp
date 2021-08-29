package com.example.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProductProvider extends ContentProvider {
    public static final int PRODUCTS = 100;
    public static final int PRODUCTS_ID = 101;
    public ProductHelper ph;
    public static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_TABLE, PRODUCTS);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_TABLE + "/#", PRODUCTS_ID);

    }

    @Override
    public boolean onCreate() {
        ph = new ProductHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = ph.getReadableDatabase();
        int matcher = uriMatcher.match(uri);
        Cursor cursor;
        switch(matcher)
        {
            case PRODUCTS:
                cursor = db.query(InventoryContract.ProductEntry.CONSTANT_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                Log.e("Query method","Query method1  working");
                break;
            case PRODUCTS_ID:
                selection = InventoryContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(InventoryContract.ProductEntry.CONSTANT_TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                Log.e("Query method","Query method_________ working");
                break;
            default: Log.e("Query method","Query method not working");
                     throw new IllegalArgumentException("Unknown uri pattern!");

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return InventoryContract.LIST_CONTENT;
            case PRODUCTS_ID:
                return InventoryContract.ITEM_CONTENT;
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, values);
            default:
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = ph.getWritableDatabase();
        switch (match) {
            case PRODUCTS:
                return db.delete(InventoryContract.ProductEntry.CONSTANT_TABLE_NAME, null, null);
            case PRODUCTS_ID: {
                selection = InventoryContract.ProductEntry.CONSTANT_PRODUCT_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(InventoryContract.ProductEntry.CONSTANT_TABLE_NAME, selection, selectionArgs);

            }
            default:
                return -1;


        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = ph.getWritableDatabase();
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, values, selection, selectionArgs);
            case PRODUCTS_ID:
                selection = InventoryContract.ProductEntry.CONSTANT_PRODUCT_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, values, selection, selectionArgs);
            default:
                return 0;


        }
    }

    private Uri insertProduct(Uri uri, ContentValues cv) {

        SQLiteDatabase db = ph.getWritableDatabase();
        long rows = db.insert(InventoryContract.ProductEntry.CONSTANT_TABLE_NAME, null, cv);
        return ContentUris.withAppendedId(uri, rows);
    }

    private int updateProduct(Uri uri, ContentValues cv, String selection, String[] selectionArgs) {
        if (cv.size() == 0)
            throw new NullPointerException("No content to be updated");

        SQLiteDatabase db = ph.getWritableDatabase();
        return db.update(InventoryContract.ProductEntry.CONSTANT_TABLE_NAME, cv, selection, selectionArgs);


    }
}
