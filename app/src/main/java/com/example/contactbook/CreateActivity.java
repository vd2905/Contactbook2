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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactbook.databinding.ActivityCreateBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import model.Database;

public class CreateActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    ActivityCreateBinding binding;
    Database database;
    int id;
    String name;
    String number;
    String imageName,imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new Database(CreateActivity.this);

        binding = ActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(galleryIntent,CAMERA_REQUEST);
            }
        });

        id = getIntent().getIntExtra("id",0);
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        imagePath = getIntent().getStringExtra("imagePath");

        if(getIntent().getExtras()!=null)
        {
            binding.ename.setText(""+name);
            binding.enumber.setText(""+number);
            loadImageFromStorage(imagePath);
        }

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePath=imagePath+"/"+imageName;
                String name=binding.ename.getText().toString();
                String number=binding.enumber.getText().toString();

                if(getIntent().getExtras()==null)
                {
                    database = new Database(CreateActivity.this);
                    database.saveContact(name,number,imagePath);
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
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.addImage.setImageBitmap(bitmap);
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
    private void loadImageFromStorage(String path) {
        try {
            File f=new File(path);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            binding.addImage.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}