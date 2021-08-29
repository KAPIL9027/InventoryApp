package com.example.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inventoryapp.data.InventoryContract;

import org.w3c.dom.Text;

public class ProductAdapter extends CursorAdapter {

    public ProductAdapter(Context context, Cursor cursor)
    {
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView iv = (ImageView) view.findViewById(R.id.list_image);
        TextView textName = (TextView) view.findViewById(R.id.item_name);
        TextView textPrice = (TextView) view.findViewById(R.id.item_price);
        TextView textQuantity = (TextView) view.findViewById(R.id.item_quantity);
        byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_IMAGE));
        String name = cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_NAME));
        String price = cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_QUANTITY));
        if(TextUtils.isEmpty(name))
            Log.e("Adapter","Not working");
        Bitmap bm = EditProductActivity.getImage(image);
        iv.setImageBitmap(bm);
        textName.setText(name);
        textPrice.setText(price);
        textQuantity.setText(""+quantity);



    }
}
