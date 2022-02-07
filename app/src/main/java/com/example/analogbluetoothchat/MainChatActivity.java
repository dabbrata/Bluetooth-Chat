package com.example.analogbluetoothchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainChatActivity extends AppCompatActivity{
    //BluetoothButton;
    Button dataShow,deviceButton,buttonListen;
    ListView listView,msgList;
    ImageView sendButton,pickFile,pickImage;
    EditText writeText;
    TextView status,rightText;
    BluetoothAdapter myBluetoothAdapter;
    BluetoothDevice[] btArray;
    SendReceive sendReceive;

    boolean is_connect = false;
    DatabaseHelper databaseHelper;
    ArrayAdapter msgArrayAdapter;
    static final int STATE_LISTENING = 1;

    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_BLUETOOTH_ENABLED = 1;
    static final int RESULT_LOAD_IMG = 100;
    static final int PICK_FILE_REQ_CODE = 10;
    Uri imageUri;
    private static final String APP_NAME = "BluetoothChat";
    private static final UUID MY_UUID = UUID.fromString("fbff62b0-ae78-11eb-8529-0242ac130003");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        getSupportActionBar().hide();

        dataShow = (Button)findViewById(R.id.buttonShowData);
        listView = (ListView)findViewById(R.id.listViewId);
        deviceButton = (Button)findViewById(R.id.button5);
        buttonListen = (Button)findViewById(R.id.ListenButtonId);
        sendButton = (ImageView) findViewById(R.id.imageView5);
        status = (TextView)findViewById(R.id.textView3);
        msgList = (ListView)findViewById(R.id.listMessageId);
        rightText = (TextView)findViewById(R.id.textView16);
        //msgBox = (TextView)findViewById(R.id.IncomeingTextId);
        writeText = (EditText)findViewById(R.id.editTextTextPersonName);
        //file.....
        pickFile = (ImageView)findViewById(R.id.pickFileId);
        pickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICK_FILE_REQ_CODE);
            }
        });
        //image.........
        pickImage = (ImageView)findViewById(R.id.pickImageId);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMG);
                //Toast.makeText(MainChatActivity.this, "Picked", Toast.LENGTH_SHORT).show();
            }
        });
       // refreshButton = (ImageButton)findViewById(R.id.imageViewButtonChatPageId);
        dataShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainChatActivity.this);
                builder.setMessage("To check and delete chat history your connection will be lost & you have to reconnect devices!")
                        .setCancelable(true)
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(MainChatActivity.this,DataShow.class));
                            }
                        })

                        .setNegativeButton("Disallow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!myBluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_BLUETOOTH_ENABLED);
        }

        //refresh..........
        /*refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainChatActivity.this,MainChatActivity.class);
                startActivity(i);
            }
        });*/
        //implement listener to find bluetooth devices....
        deviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                BluetoothDevice[] btArray = new BluetoothDevice[bt.size()];
                int index = 0;
                if(bt.size() > 0){
                    for(BluetoothDevice device : bt){
                        btArray[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        buttonListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerClass serverClass = new ServerClass();
                serverClass.start();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientClass clientClass = new ClientClass(btArray[position]);
                clientClass.start();

                status.setText("Connecting...");
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = writeText.getText().toString().trim();
                if(str.isEmpty()){
                    Toast.makeText(MainChatActivity.this, "Write message firstly", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!is_connect){
                    Toast.makeText(MainChatActivity.this, "Establish your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainChatActivity.this, "Messsage sent", Toast.LENGTH_SHORT).show();

                long rowId = databaseHelper.insertData("Me:  "+str);
                if(rowId == -1){
                    Toast.makeText(MainChatActivity.this, "Insertion unsuccessful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainChatActivity.this, "Message successfully inserted", Toast.LENGTH_SHORT).show();
                }
                dataShow();
                String string = String.valueOf(writeText.getText());
                sendReceive.write(string.getBytes());
                writeText.getText().clear();
            }
        });

        dataShow();

    }

    public void dataShow() {
        List<CustomerModel> everyone = databaseHelper.getEveryone();
        //msgArrayAdapter = new ArrayAdapter<CustomerModel>(MainChatActivity.this, android.R.layout.simple_list_item_1, everyone);
        //everyone = new ArrayList<>();
        CustomAdapter customAdapter = new CustomAdapter(MainChatActivity.this, everyone);
        msgList.setAdapter(customAdapter);

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting...");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    is_connect = true;
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tempMsg = new String(readBuffer,0,msg.arg1);
                    //msgBox.setText(tempMsg);

                    long rowIdRcv = databaseHelper.insertData("Other:  "+tempMsg);
                    dataShow();
                    if(rowIdRcv == -1){
                        Toast.makeText(MainChatActivity.this, "Insertion unsuccessful", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainChatActivity.this, "Message successfully inserted", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }

            return true;
        }
    });

    //server class.....
    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;
        //SendReceive sendReceive;
        public ServerClass(){
            try {
                serverSocket = myBluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run()
        {
            BluetoothSocket socket = null;
            while(socket == null){
                try{
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);

                }
                if(socket != null){
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);

                    //code for send receive...
                    SendReceive sendReceive = new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }


    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;
        //SendReceive sendReceive;

        public ClientClass (BluetoothDevice device1)
        {
            device = device1;
            try{
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //extra add constructor.....
        //public ClientClass() {}

        public void run()
        {
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);

                SendReceive sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }


    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tempIn;
            outputStream = tempOut;

        }
        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;
            while(true)
            {
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /*if(resultCode == RESULT_OK){
            if(requestCode == RESULT_LOAD_IMG){
                imageUri = data.getData();
                pickImage.setImageURI(imageUri);
            }
        }*/
        //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //StrictMode.setVmPolicy((builder.build()));

        //BitmapDrawable drawable = (BitmapDrawable);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_LOAD_IMG){
                imageUri = data.getData();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = imageUri;
                try {
                    InputStream stream = getContentResolver().openInputStream(screenshotUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                sharingIntent.setType("image/*");
                sharingIntent.setPackage("com.android.bluetooth");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));

            }
            else if(requestCode == PICK_FILE_REQ_CODE){
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = data.getData();
                try {
                    InputStream stream = getContentResolver().openInputStream(screenshotUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                sharingIntent.setType("*/*");
                sharingIntent.setPackage("com.android.bluetooth");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share file using"));

            }
        }
       // Toast.makeText(this, "IMAGE IS", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainChatActivity.this,dashBoard.class);
        startActivity(intent);
        finish();
    }
}