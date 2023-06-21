package com.example.contactbook;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import model.DBHelper;

public class CreateActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    TextInputEditText ename,enumber;
    Button btnSave;
    ImageView imageView;
    TextView textView;
    DBHelper dbHelper;
    int id;
    String name;
    String number;
    String imageName,imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        dbHelper=new DBHelper(CreateActivity.this);
        ename=findViewById(R.id.ename);
        enumber=findViewById(R.id.enumber);
        btnSave=findViewById(R.id.btnSave);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.txt);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }
        });

        id = getIntent().getIntExtra("id",0);
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        imagePath = getIntent().getStringExtra("imagePath");

        if(getIntent().getExtras()!=null)
        {
            ename.setText(""+name);
            enumber.setText(""+number);
            loadImageFromStorage(imagePath);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePath=imagePath+"/"+imageName;
                String name=ename.getText().toString();
                String number=enumber.getText().toString();

                if(getIntent().getExtras()==null)
                {
                    DBHelper dbHelper=new DBHelper(CreateActivity.this);
                    dbHelper.saveContact(name,number,imagePath);
                    Intent intent = new Intent(CreateActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    DBHelper dbHelper=new DBHelper(CreateActivity.this);
                    dbHelper.updateContact(id,name,number,imagePath);
                    Intent intent = new Intent(CreateActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            imagePath=saveToInternalStorage(bitmap);
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        imageName="img"+new Random().nextInt(100000)+".png";
        File mypath=new File(directory,imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}