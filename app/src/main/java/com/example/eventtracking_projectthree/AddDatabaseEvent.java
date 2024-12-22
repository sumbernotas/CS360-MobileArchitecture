package com.example.eventtracking_projectthree;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDatabaseEvent extends AppCompatActivity {

    Button addEvent, eTime, eDate, request, exitScreen;
    String alarm;
    private final int SMS_PERMISSION_CODE = 1;

    EditText eventTitle, eventNote;

    long notificationC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_edit_screen);

        // BUTTON to request permissions from user
        //requestB = (Button) findViewById(R.id.request_button);

        //permission button
        request = (Button) findViewById(R.id.buttonRequest);
        request = (Button) findViewById(R.id.buttonRequest);

        //time and date input
        eDate.setOnClickListener(view -> selectDate());
        eTime.setOnClickListener(view -> selectTime());

        //add button disable
        eventTitle.addTextChangedListener(textWatcher);
        eventNote.addTextChangedListener(textWatcher);
        eTime.addTextChangedListener(textWatcher);
        eDate.addTextChangedListener(textWatcher);

        // button Id and input
        eventTitle = findViewById(R.id.editTextEventName);
        eventNote = findViewById(R.id.editTextEventNotes);
        eTime = findViewById(R.id.buttonAddTime);
        eDate = findViewById(R.id.buttonAddDate);
        addEvent = findViewById(R.id.buttonNewEventSave);
        exitScreen = findViewById(R.id.buttonNewEventExitScreen);

        exitScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDatabaseEvent.this, DatabaseHome.class);
                startActivity(intent);
            }
        });

        //add event button
        addEvent.setOnClickListener(view -> {

            //adding the event to the database
            AppDatabase db = new AppDatabase(AddDatabaseEvent.this);

            //checking for permission
            requestPermission();



            notificationC = db.addReminder(eventTitle.getText().toString().trim(),
                    eDate.getText().toString().trim(),
                    eventNote.getText().toString().trim(),
                    eTime.getText().toString().trim());


            // Alarm is set here unless permission was not granted
            if (ContextCompat.checkSelfPermission(AddDatabaseEvent.this,
                    android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
                try {
                    setAlarm(eventTitle.getText().toString().trim(),
                            eDate.getText().toString().trim(),
                            eventNote.getText().toString().trim(),
                            eTime.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(AddDatabaseEvent.this, "For this feature you need to give permission", Toast.LENGTH_SHORT).show();
            }

            finish();

        });

        //This wil let them give permission if they have already denied it
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddDatabaseEvent.this,
                        android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddDatabaseEvent.this, "You gave permission already", Toast.LENGTH_SHORT).show();

                } else {
                    requestPermission();
                }

            }
        });

    }

    //set alarm

    private void setAlarm(String date, String time, String text, String description) throws ParseException {

        Intent intent = new Intent(AddDatabaseEvent.this, Alarm.class);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("event", text);
        intent.putExtra("description", description);

        PendingIntent pending = PendingIntent.getBroadcast(
                AddDatabaseEvent.this,
                (int)notificationC,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //overwrite event
        AlarmManager alarmM = (AlarmManager) getSystemService(ALARM_SERVICE);
        String dateTime = date + " " + alarm;
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("d-M-yyyy hh:mm");
        try{
            Date dateToSet = format.parse(dateTime);
            assert dateToSet != null;

            alarmM.set(AlarmManager.RTC_WAKEUP,
                    dateToSet.getTime(),
                    pending);
            Toast.makeText(getApplicationContext(), "Alarm set", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //date and time set
    private void selectTime() {
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            alarm = i + ":" + i1;
            eTime.setText(formatTime(i, i1));
        }, hr, min, false);
        timePickerDialog.show();
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        int Y = cal.get(Calendar.YEAR);
        int M = cal.get(Calendar.MONTH);
        int D = cal.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (datePicker, year, month, day) ->
                eDate.setText(String.format(
                        "%d-%d-%d", day, month + 1, year)),
                Y, M, D);
        datePickerDialog.show();
    }

    public String formatTime(int hr, int min) {

        String T;
        String RM;

        if (min / 10 == 0) {
            RM = "0" + min;
        } else {
            RM = "" + min;
        }

        //reading time
        if (hr == 0) {
            T = "12" + ":" + RM + " AM";
        } else if (hr < 12) {
            T = hr + ":" + RM + " AM";
        } else if (hr == 12) {
            T = "12" + ":" + RM + " PM";
        } else {
            int temp = hr - 12;
            T = temp + ":" + RM + " PM";
        }
        return T;
    }

    // Makes sure all text is filled in
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String title = eventTitle.getText().toString().trim();
            String description = eventNote.getText().toString().trim();
            String date = eDate.getText().toString().trim();
            String time = eTime.getText().toString().trim();

            addEvent.setEnabled(!title.isEmpty() && !description.isEmpty() &&
                    !date.isEmpty() && !time.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    //check permissions
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)){

            new AlertDialog.Builder(this)
                    .setTitle("SMS messages")
                    .setMessage("You need to give permissions for reminders")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    AddDatabaseEvent.this,
                                    new String[] {android.Manifest.permission.READ_SMS},
                                    SMS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        }else{
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_SMS},
                    SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int request, @NonNull String[] permissions, @NonNull int[] grant) {
        super.onRequestPermissionsResult(request,
                permissions,
                grant);
        if (request == SMS_PERMISSION_CODE) {
            if (grant.length > 0 && grant[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
