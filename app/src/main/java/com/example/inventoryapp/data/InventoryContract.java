package com.example.inventoryapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {
    public static final String CONTENT_AUTHORITY = "com.example.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_TABLE = "products";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_TABLE);
    public final static String LIST_CONTENT = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_TABLE;
    public final static String ITEM_CONTENT = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_TABLE;
    private InventoryContract()
    {

    }
    public static final class ProductEntry implements BaseColumns
    {
        public static final String CONSTANT_TABLE_NAME = "products";
        public static final String CONSTANT_PRODUCT_ID = "_id";
        public static final String CONSTANT_PRODUCT_NAME = "name";
        public static final String CONSTANT_PRODUCT_PRICE = "price";
        public static final String CONSTANT_PRODUCT_IMAGE = "image";
        public static final String CONSTANT_PRODUCT_QUANTITY = "quantity";


    }
}
