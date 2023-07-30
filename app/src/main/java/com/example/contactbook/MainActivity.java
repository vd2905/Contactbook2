package com.example.contactbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.example.contactbook.databinding.ActivityMainBinding;

import java.util.ArrayList;

import model.Contact_model;
import model.Database;
import Adapter.Recyclerview_Adapter;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    RecyclerView recyclerView;
    ArrayList<Contact_model> contactList=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView=findViewById(R.id.recyclerview);
        swipeRefreshLayout=findViewById(R.id.swipeLayout);
        DisplayData();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreateActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void DisplayData()
    {
        Database database = new Database(MainActivity.this);
        Cursor cursor= database.displayContact();
        contactList.clear();
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String number = cursor.getString(2);
            String imagePath = cursor.getString(3);
            Contact_model contact_model =new Contact_model(id,name,number,imagePath);
            contactList.add(contact_model);
        }
        Recyclerview_Adapter adapter = new Recyclerview_Adapter(MainActivity.this,contactList);
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