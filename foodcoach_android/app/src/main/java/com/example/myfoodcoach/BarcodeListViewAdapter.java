package com.example.myfoodcoach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class BarcodeListViewAdapter extends BaseAdapter {

    private ArrayList<BarcodeInfo> data = new ArrayList<>(); // barcodelist

    public ArrayList<BarcodeInfo> getData() {
        return data;
    }

    public String[] getBarcodeArray() {
        String[] result = new String[getCount()];
        for (int i = 0; i < getCount(); i++)
            result[i] = data.get(i).getBarcode();

        return result;
    }

    public int[] getQuantityArray() {
        int[] result = new int[getCount()];
        for (int i = 0; i < getCount(); i++)
            result[i] = data.get(i).getQuantity();

        return result;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.barcode_listview_layout, parent, false);
        }

        EditText barcodeText = convertView.findViewById(R.id.et_barcode);
        TextView quantityText = convertView.findViewById(R.id.tv_quantity);

        BarcodeInfo item = data.get(position);
        barcodeText.setText(item.getBarcode());
        quantityText.setText(String.valueOf(item.getQuantity()));
        if (Integer.parseInt(quantityText.getText().toString()) <= 0)
            quantityText.setText(String.valueOf(1));

        final TextView tv_quantity = convertView.findViewById(R.id.tv_quantity);
        final Button btn_add_quantity = convertView.findViewById(R.id.btn_add_quantity);
        final Button btn_sub_quantity = convertView.findViewById(R.id.btn_sub_quantity);

        btn_add_quantity.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tv_quantity.getText().toString());
            tv_quantity.setText(String.valueOf(++quantity));
            item.setQuantity(Integer.parseInt(tv_quantity.getText().toString()));
        });

        btn_sub_quantity.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tv_quantity.getText().toString());
            tv_quantity.setText(String.valueOf(--quantity));
            item.setQuantity(Integer.parseInt(tv_quantity.getText().toString()));
        });

        return convertView;
    }


}
