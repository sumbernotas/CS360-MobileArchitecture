package com.example.eventtracking_projectthree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import static com.example.eventtracking_projectthree.R.id.date_to_fire;

public class Custom extends RecyclerView.Adapter<Custom.MyViewHolder>{

    private Context context;
    private ArrayList eID, eTitle, eNote, eDate, eTime;
    Activity act;


    Custom(Activity activity, Context context,
           ArrayList event_id,
           ArrayList event_title,
           ArrayList event_description,
           ArrayList event_date,
           ArrayList event_time){
        this.act = activity;
        this.context = context;
        this.eID = event_id;
        this.eTitle = event_title;
        this.eNote = event_description;
        this.eDate = event_date;
        this.eTime = event_time;

    }

    //fills rows and fills the home screen with the data
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_block, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int pos) {

        holder.id.setText(String.valueOf(eID.get(pos)));
        holder.title.setText(String.valueOf(eTitle.get(pos)));
        holder.description.setText(String.valueOf(eNote.get(pos)));
        holder.date.setText(String.valueOf(eDate.get(pos)));
        holder.time.setText(String.valueOf(eTime.get(pos)));

        holder.Layout.setOnClickListener((view) -> {

        });
        holder.itemView.findViewById(R.id.edit_button).setOnClickListener((view -> {
            Intent intent = new Intent(context, Update.class);
            intent.putExtra("id", String.valueOf(eID.get(pos)));
            intent.putExtra("title", String.valueOf(eTitle.get(pos)));
            intent.putExtra("description", String.valueOf(eNote.get(pos)));
            intent.putExtra("date", String.valueOf(eDate.get(pos)));
            intent.putExtra("time", String.valueOf(eTime.get(pos)));
            act.startActivityForResult(intent, 1);
        }));

    }

    @Override
    public int getItemCount() {
        return eID.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, title, description, date, time;
        LinearLayout Layout;

        public MyViewHolder(@NonNull View itemV) {
            super(itemV);
            id = itemView.findViewById(R.id.event_id);
            title = itemView.findViewById(R.id.event_title);
            description = itemView.findViewById(R.id.event_description);
            date = itemView.findViewById(date_to_fire);
            time = itemView.findViewById(R.id.time_to_fire);
            Layout = itemView.findViewById((R.id.event_block));

        }
    }

}