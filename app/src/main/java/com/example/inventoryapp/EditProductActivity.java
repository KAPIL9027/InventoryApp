package com.example.inventoryapp;
import com.example.inventoryapp.data.InventoryContract;
import com.example.inventoryapp.data.ProductHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inventoryapp.data.ProductHelper;

import java.io.ByteArrayOutputStream;

public class EditProductActivity extends AppCompatActivity {
    int quantity = 0;
    Button addImageButton;
    Button increaseQuantity;
    Button decreaseQuantity;
    Button orderButton;
    EditText quantityText;
    EditText nameText;
    EditText priceText;
    ImageView iv;
    Uri uri;
    ProductHelper ph;
    boolean productChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productChanged  = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        orderButton = (Button) findViewById(R.id.order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_SENDTO);
                Uri uri2 = Uri.parse("mailto:");
                intent1.setData(uri2);
                intent1.putExtra(Intent.EXTRA_SUBJECT,"Product Order");
                String s = "Product Name: "+nameText.getText().toString()+
                            "\nProduct Price: "+priceText.getText().toString()+
                            "\nQuantity: "+quantityText.getText().toString();
                intent1.putExtra(Intent.EXTRA_TEXT,s);
                    startActivity(intent1);
            }
        });
        ph = new ProductHelper(getApplicationContext());
        nameText = (EditText) findViewById(R.id.product_name);
        nameText.setOnTouchListener(mTouchListener);
        priceText = (EditText) findViewById(R.id.product_price);
        priceText.setOnTouchListener(mTouchListener);
       addImageButton = (Button) findViewById(R.id.add_image_button);
       iv = (ImageView) findViewById(R.id.imageView);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //
                Intent pickImage = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage , 1);
            }
        });
         increaseQuantity = (Button) findViewById(R.id.increase_quantity);
         decreaseQuantity = (Button) findViewById(R.id.decrease_quantity);
         quantityText = (EditText) findViewById(R.id.product_quantity);
        quantityText.setOnTouchListener(mTouchListener);
        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = quantity + 1;
                quantityText.setText(""+quantity);
            }

        });
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = quantity - 1;
                quantityText.setText(""+quantity);
            }
        });
        Intent intent = getIntent();
       uri = intent.getData();
        Button delete_button = (Button) findViewById(R.id.delete_button);
        if(uri == null)
        delete_button.setVisibility(View.GONE);
        if(uri != null)
        {

          setTitle("Edit Product");
          Cursor cursor = getContentResolver().query(uri,null,null,null,null);
          if(cursor.moveToFirst()) {
              String name = cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_NAME));
              String price = cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_PRICE));
              int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_QUANTITY));
              byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.ProductEntry.CONSTANT_PRODUCT_IMAGE));
              Bitmap product_image = getImage(image);
              nameText.setText(name);
              priceText.setText(price);
              quantityText.setText(""+quantity);
              iv.setImageBitmap(product_image);
          }
            cursor.close();
            delete_button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  int result = deleteItem();
                  if(result != -1)
                      finish();
              }
          });

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editproductmeny,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.save_button:
                 getData();
                 finish();
                return true;
            case android.R.id.home:
                if(!productChanged)
                {
                    NavUtils.navigateUpFromSameTask(EditProductActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardChanges = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(EditProductActivity.this);
                    }
                };
                unSavedChanges(discardChanges);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {

            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    iv.setImageURI(selectedImage);
                }
                break;
        }
    }
    private void getData()
    {

        String name = nameText.getText().toString();
        String price = priceText.getText().toString();
        int quantity = Integer.parseInt(quantityText.getText().toString());
        Bitmap bm=((BitmapDrawable)iv.getDrawable()).getBitmap();
        byte[] image = getBytes(bm);
        if(TextUtils.isEmpty(name))
        {
           throw new NullPointerException("Product name is required");
        }
       if(TextUtils.isEmpty(price))
       {
           throw new NullPointerException("Product price is required");
        }
        ContentValues cv = new ContentValues();
        cv.put(InventoryContract.ProductEntry.CONSTANT_PRODUCT_NAME,name);
        cv.put(InventoryContract.ProductEntry.CONSTANT_PRODUCT_PRICE,price);
        cv.put(InventoryContract.ProductEntry.CONSTANT_PRODUCT_QUANTITY,quantity);
        cv.put(InventoryContract.ProductEntry.CONSTANT_PRODUCT_IMAGE,image);
        Uri uri1 = null;
        int row = -1;
        if(uri == null)
         uri1 = getContentResolver().insert(InventoryContract.CONTENT_URI,cv);
        else
           row = getContentResolver().update(uri,cv,null,null);
        if(uri1 != null)
        Toast.makeText(getApplicationContext(),"Product added to the inventory.",Toast.LENGTH_SHORT).show();
        if(row != -1)
            Toast.makeText(getApplicationContext(),"Product updated.",Toast.LENGTH_SHORT).show();
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public int deleteItem()
    {
       int row = getContentResolver().delete(uri,null,null);
       if(row == -1)
         Toast.makeText(getApplicationContext(),"Product Not Deleted.",Toast.LENGTH_SHORT).show();
         else
             Toast.makeText(getApplicationContext(),"Product Deleted.",Toast.LENGTH_LONG).show();
         return row;
    }
    @Override
    public void onBackPressed() {
        if(!productChanged)
        {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener  doc = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        unSavedChanges(doc);
    }
    public void unSavedChanges(DialogInterface.OnClickListener onClickListener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Discard",onClickListener);
        builder.setMessage("Discard Changes");
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

}