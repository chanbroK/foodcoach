package com.example.myfoodcoach.PieChartGridList;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;

public class PieChartItem {
    private ArrayList<DataEntry> data;

    public PieChartItem() {
        data = new ArrayList<>();
    }

    public void addData(String key, Integer value) {
        data.add(new ValueDataEntry(key, value));
    }

    public void addData(String[] keys, Integer[] values) {
        for (int i = 0; i < keys.length; i++)
            addData(keys[i], values[i]);
    }

    public void addData(List<String> keys, List<Integer> values) {
        for (int i = 0; i < keys.size(); i++)
            addData(keys.get(i), values.get(i));
    }

    public ArrayList<DataEntry> getData() {
        return data;
    }

}
