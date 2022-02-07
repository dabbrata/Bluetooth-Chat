package com.example.analogbluetoothchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DataShow extends AppCompatActivity {

    ListView list;
    Button clearButton;
    DatabaseHelper databaseHelper;
    ArrayAdapter msgArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_show);
        getSupportActionBar().hide();
        databaseHelper = new DatabaseHelper(DataShow.this);

        list = (ListView)findViewById(R.id.listViewIdmsg);
        clearButton = (Button)findViewById(R.id.clearId);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DataShow.this);
                builder.setMessage("Do you want to delete all messages?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHelper.delete();
                                dataShow();
                                Toast.makeText(DataShow.this, "Cleared All!", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DataShow.this);
                builder.setMessage("Do you want to delete this message?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CustomerModel clickMsg = (CustomerModel) parent.getItemAtPosition(position);
                                databaseHelper.deleteOne(clickMsg);
                                Toast.makeText(DataShow.this, "Deleted "+clickMsg.toString(), Toast.LENGTH_SHORT).show();
                                dataShow();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


        
        dataShow();

        //Toast.makeText(this, everyone.toString(), Toast.LENGTH_SHORT).show();
    }
    public void dataShow() {
        List<CustomerModel> everyone = databaseHelper.getEveryone();
        msgArrayAdapter = new ArrayAdapter<CustomerModel>(DataShow.this, android.R.layout.simple_list_item_1, everyone);
        list.setAdapter(msgArrayAdapter);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DataShow.this,MainChatActivity.class);
        startActivity(intent);
        finish();
    }

}