package com.example.eventtracking_projectthree;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DatabaseHome extends AppCompatActivity {

    public static final int REQUEST = 1;
    RecyclerView dataView;
    Button addEvent;
    Custom Adaptor;
    AppDatabase db;
    ArrayList<String> eID, eTitle, eNotes, eDate, eTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_screen);

        // build a recycler view to display events from db
        dataView = findViewById(R.id.dataView);
        addEvent = findViewById(R.id.buttonAddEvent);

        // action button to add event
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(DatabaseHome.this, AddDatabaseEvent.class);
                startActivity(intent);
            }
        });

        //create arrays
        db = new AppDatabase(DatabaseHome.this);
        eID = new ArrayList<>();
        eTitle = new ArrayList<>();
        eNotes = new ArrayList<>();
        eDate = new ArrayList<>();
        eTime = new ArrayList<>();

        //data to fill arrays
        storeDataInArrays();

        //this will build the rows
        Adaptor = new Custom(DatabaseHome.this, this, eID, eTitle,
                eNotes, eDate, eTime);
        dataView.setAdapter(Adaptor);
        dataView.setLayoutManager(new LinearLayoutManager(DatabaseHome.this));

    }

    @Override
    protected void onActivityResult(int request, int result, @Nullable Intent data){
        super.onActivityResult(request, result, data);
        if(request == 1){
            recreate();
        }
    }

    //creates rws for the recycler
    void storeDataInArrays(){
        Cursor c = db.readAllData();
        if(c.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
        else{
            while (c.moveToNext()){
                eID.add(c.getString(0));
                eTitle.add(c.getString(1));
                eNotes.add(c.getString(2));
                eDate.add(c.getString(3));
                eTime.add(c.getString(4));
            }
        }
    }
}