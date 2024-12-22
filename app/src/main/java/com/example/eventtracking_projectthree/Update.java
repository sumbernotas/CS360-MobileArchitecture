package com.example.eventtracking_projectthree;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Update extends AppCompatActivity {

    EditText eTitle, eNote;
    String id, title, description, date, time;
    Button updateEvent, deleteEvent, eTime, eDate, exitScreen;
    String alarm;
    int notificationC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_block);

        // id for buttons
        eTitle = findViewById(R.id.editTextEventName);
        eNote = findViewById(R.id.editTextEventNotes);
        eDate = findViewById(R.id.buttonChangeDate);
        eTime = findViewById(R.id.buttonChangeTime);
        updateEvent = findViewById(R.id.buttonEventUpdate);
        deleteEvent = findViewById(R.id.buttonDeleteEvent);
        exitScreen = findViewById(R.id.buttonUpdateEventExitScreen);

        //date and time input
        eTime.setOnClickListener(view -> {selectTime();});
        eDate.setOnClickListener(view -> selectDate());

        //disable update
        eTitle.addTextChangedListener(textWatcher);
        eNote.addTextChangedListener(textWatcher);
        eTime.addTextChangedListener(textWatcher);
        eDate.addTextChangedListener(textWatcher);

        //get data
        getAndSetIntentData();

        exitScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update.this, DatabaseHome.class);
                startActivity(intent);
            }
        });

        //update event button
        updateEvent.setOnClickListener(view -> {

            AppDatabase db = new AppDatabase(Update.this);
            db.updateData(id, eTitle.getText().toString().trim(),
                    eNote.getText().toString().trim(),
                    eDate.getText().toString().trim(),
                    eTime.getText().toString().trim());

            // set alarm
            if (ContextCompat.checkSelfPermission(Update.this,
                    Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
                try {
                    setAlarm(eTitle.getText().toString().trim(),
                            eNote.getText().toString().trim(),
                            eDate.getText().toString().trim(),
                            eTime.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(Update.this, "For this feature you need to give permission",
                        Toast.LENGTH_SHORT).show();
            }

            finish();

        });
        deleteEvent.setOnClickListener(view -> confirmDialog());

    }

    //makes sure everything is filled
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String date = eDate.getText().toString().trim();
            String time = eTime.getText().toString().trim();
            String title = eTitle.getText().toString().trim();
            String description = eNote.getText().toString().trim();

            updateEvent.setEnabled(!title.isEmpty() && !description.isEmpty() &&
                    !date.isEmpty() && !time.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    //delete event
    void confirmDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Delete " + title + "?");
        build.setMessage("You want to delete? " + title + "?");
        build.setPositiveButton("Yes", (dialogInterface, i) -> {
            AppDatabase db = new AppDatabase(Update.this);
            db.deleteOneRow(id);
            finish();
        });
        build.setNegativeButton("No", (dialogInterface, i) -> {

        });
        build.create().show();

    }

    //setting data for the buttons and text

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("time")){

            //gets data from intent
            id = getIntent().getStringExtra("id");
            int i = Integer.parseInt(id);
            notificationC = (int)i;
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");

            //sets intent data
            eDate.setText(date);
            eTime.setText(time);
            eTitle.setText(title);
            eNote.setText(description);

        }else{
            Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();
        }
    }

    //sets alarm and notifications

    private void setAlarm(String date, String time,String text, String description) throws ParseException {

        Intent intent = new Intent(Update.this,
                Alarm.class);
        intent.putExtra("event", text);
        intent.putExtra("description", description);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(Update.this,
                notificationC,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        String dateTime = date + " " + alarm;
        DateFormat format = new SimpleDateFormat("d-M-yyyy hh:mm");

        Date dateToSet = null;

        try{
            dateToSet = format.parse(dateTime);
            assert dateToSet != null;

            Manager.set(AlarmManager.RTC_WAKEUP,
                    dateToSet.getTime(),
                    pendingIntent);
            Toast.makeText(getApplicationContext(), "Alarm is set", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //sets date and time

    private void selectTime() {
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog PickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            alarm = i + ":" + i1;
            eTime.setText(formatTime(i, i1));
        }, hr, min, false);
        PickerDialog.show();
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        int Y = cal.get(Calendar.YEAR);
        int M = cal.get(Calendar.MONTH);
        int D = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog PickerDialog = new DatePickerDialog(this, this::onDateSet, Y, M, D);
        PickerDialog.show();
    }

    public String formatTime(int hr, int min) {

        String T;
        String RM;

        if (min / 10 == 0) {
            RM = "0" + min;
        } else {
            RM = "" + min;
        }

        if (hr == 0) {
            time = "12" + ":" + RM + " AM";
        } else if (hr < 12) {
            time = hr + ":" + RM + " AM";
        } else if (hr == 12) {
            time = "12" + ":" + RM + " PM";
        } else {
            int temp = hr - 12;
            time = temp + ":" + RM + " PM";
        }
        return time;
    }

    @SuppressLint("SetTextI18n")
    private void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eDate.setText(String.format("%d-%d-%d", day, month + 1, year));
    }
}
