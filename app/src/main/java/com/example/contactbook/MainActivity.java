package com.example.contactbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import model.Contact;
import model.DBHelper;
import Adapter.ListAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Contact> contactList=new ArrayList<>();
    FloatingActionButton btn;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerview);
        swipeRefreshLayout=findViewById(R.id.swipeLayout);
        getData();
        btn=findViewById(R.id.fab);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreateActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getData()
    {
        DBHelper dbHelper=new DBHelper(MainActivity.this);
        Cursor cursor= dbHelper.displayContact();
        contactList.clear();
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String number = cursor.getString(2);
            String imagePath = cursor.getString(3);
            Contact contact=new Contact(id,name,number,imagePath);
            contactList.add(contact);
        }
        ListAdapter adapter = new ListAdapter(MainActivity.this,contactList);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}