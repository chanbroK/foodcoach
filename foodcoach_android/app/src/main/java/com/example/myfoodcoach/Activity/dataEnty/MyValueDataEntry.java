package com.example.myfoodcoach.Activity.dataEnty;

import java.util.List;

public class MyValueDataEntry extends DataEntry{
    public MyValueDataEntry(String x, List<Integer> value) {
        setValue("x", x);
        if (value != null){
            int cnt = super.getHashMap().size();
            setValue("value"+cnt, value);
        }
    }
}
