package com.example.analogbluetoothchat;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<CustomerModel> {
    private Activity context;
    //CustomerModel customerModel = new CustomerModel();
    //DatabaseHelper databaseHelper ;
    private List<CustomerModel> everyone;

    public CustomAdapter(Activity context, List<CustomerModel> everyone) {
        super(context, R.layout.custom_layout, everyone);
        this.context = context;
        this.everyone = everyone;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_layout,null,true);

        CustomerModel customerModel = everyone.get(position);
        TextView tex1=view.findViewById(R.id.textView16);
        TextView tex2=view.findViewById(R.id.textView17);

        String str = customerModel.getName().toString().trim();
        if(str.charAt(0) != 'M'){
            tex1.setText(customerModel.getName());
            tex2.setText(" ");
            tex2.setBackgroundResource(R.drawable.round_white);

        }
        else {
            tex2.setText(customerModel.getName());
            tex1.setText(" ");
            tex1.setBackgroundResource(R.drawable.round_white);
        }
        return view;
    }
}
