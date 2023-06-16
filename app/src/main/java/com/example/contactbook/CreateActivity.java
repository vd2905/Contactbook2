package com.example.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import DBHelper.DBHelper;

public class CreateActivity extends AppCompatActivity {

    TextInputEditText ename,enumber;
    Button btnSave;
    DBHelper dbHelper;
    int id;
    String name;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        dbHelper=new DBHelper(CreateActivity.this);
        ename=findViewById(R.id.ename);
        enumber=findViewById(R.id.enumber);
        btnSave=findViewById(R.id.btnSave);

        if(getIntent().getExtras()!=null)
        {
            id = getIntent().getIntExtra("id",0);
            name = getIntent().getStringExtra("contact");
            number = getIntent().getStringExtra("contact");

            ename.setText(""+name);
            enumber.setText(""+number);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=ename.getText().toString();
                String number=enumber.getText().toString();

                if(getIntent().getExtras()==null)
                {
                    DBHelper dbHelper=new DBHelper(CreateActivity.this);
                    dbHelper.saveContact(name,number);
                    Intent intent = new Intent(CreateActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    DBHelper dbHelper=new DBHelper(CreateActivity.this);
                    dbHelper.updateContact(id,name,number);
                    Intent intent = new Intent(CreateActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CreateActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}