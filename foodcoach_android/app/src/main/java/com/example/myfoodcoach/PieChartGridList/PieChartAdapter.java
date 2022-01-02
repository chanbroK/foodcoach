package com.example.myfoodcoach.PieChartGridList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.Pie;
import com.example.myfoodcoach.R;

import java.util.ArrayList;
import java.util.List;

public class PieChartAdapter extends BaseAdapter {
    private List<PieChartItem> listItems;

    public void addItem(PieChartItem item) { listItems.add(item); }

    public PieChartAdapter() { listItems = new ArrayList<>(); }

    @Override
    public int getCount() { return listItems.size(); }

    @Override
    public Object getItem(int position) { return listItems.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.piechart_item, parent, false);
        }else{
            return convertView;
        }
        AnyChartView anyChartView = convertView.findViewById(R.id.pie_chart_item);
        Pie pie = AnyChart.pie();
        pie.data(listItems.get(position).getData());
        anyChartView.setChart(pie);

        return anyChartView;
    }
}
