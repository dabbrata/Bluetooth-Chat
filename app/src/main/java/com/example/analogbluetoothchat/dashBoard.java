package com.example.analogbluetoothchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class dashBoard extends AppCompatActivity {

    Button buttonOn,buttonOff,buttonNext;
    BluetoothAdapter myBluetoothAdapter;
    Intent buttonEnabling;
    int reqCodeForEnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getSupportActionBar().hide();

        buttonOn = (Button)findViewById(R.id.button);
        buttonOff = (Button)findViewById(R.id.button2);
        buttonNext = (Button)findViewById(R.id.button3);
        buttonEnabling = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        reqCodeForEnable = 1;

        bluetoothOnMethod();
        bluetoothOffMethod();
        nextPage();
    }

    private void nextPage() {
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashBoard.this,MainChatActivity.class);
                startActivity(i);
            }
        });
    }

    private void bluetoothOffMethod() {
        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter.isEnabled()){
                    Toast.makeText(dashBoard.this, "Bluetooth is disabled", Toast.LENGTH_SHORT).show();
                    myBluetoothAdapter.disable();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == reqCodeForEnable) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Bluetooth enabling cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void bluetoothOnMethod() {
        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter == null){
                    Toast.makeText(dashBoard.this, "Bluetooth does not support in this device", Toast.LENGTH_SHORT).show();
                }else{
                    if(!myBluetoothAdapter.isEnabled()){
                        startActivityForResult(buttonEnabling,reqCodeForEnable);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(dashBoard.this,toMultiplePage.class);
        startActivity(intent);
        finish();
    }

}