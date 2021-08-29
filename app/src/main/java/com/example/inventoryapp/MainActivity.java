package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.data.InventoryContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditProductActivity.class);
                startActivity(intent);

            }
        });

       displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menuinventory,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.delete_all_products:
                 delete();
                 displayDatabaseInfo();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void displayDatabaseInfo() {
        Cursor cursor = getContentResolver().query(InventoryContract.CONTENT_URI,null,null,null,null);
        ListView lv = (ListView) findViewById(R.id.list_view);
        ProductAdapter pca = new ProductAdapter(getApplicationContext(),cursor);
        lv.setAdapter(pca);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditProductActivity.class);
                Uri uri = ContentUris.withAppendedId(InventoryContract.CONTENT_URI,id);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
    public int delete()
    {
        int row = getContentResolver().delete(InventoryContract.CONTENT_URI,null,null);
        if(row == -1)
            Toast.makeText(getApplicationContext(),"Product Not Deleted.",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Inventory Emptied.",Toast.LENGTH_LONG).show();
        return row;
    }
}