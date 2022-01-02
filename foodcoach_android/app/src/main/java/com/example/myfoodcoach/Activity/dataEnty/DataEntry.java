package com.example.myfoodcoach.Activity.dataEnty;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DataEntry {

    private Map<String, Object> hashMap = new HashMap<>();
    public Map<String, Object> getHashMap() {return hashMap;}
    public void setValue(String key, List<Integer> value) {
        hashMap.put(key, value);
    }

    public void setValue(String key, String value) {
        hashMap.put(key, value);
    }

    public void setValue(String key, String[] values) {
        hashMap.put(key, values);
    }

    public void setValue(String key, Number[] values) {
        hashMap.put(key, values);
    }

    public void setValue(String key, Number value) {
        hashMap.put(key, (value != null) ? value.toString() : null);
    }

    public void setValue(String key, Boolean value) {
        hashMap.put(key, value);
    }

    public void setValue(String key, com.anychart.chart.common.dataentry.DataEntry value) {
        hashMap.put(key, value);
    }

    public void setValue(String key, com.anychart.chart.common.dataentry.DataEntry[] values) {
        hashMap.put(key, values);
    }

    public Object getValue(String key) {
        return hashMap.get(key);
    }

    public Set<String> keySet() {
        return hashMap.keySet();
    }

    public String generateJs() {
        StringBuilder js = new StringBuilder();

        js.append("{");
        for (String key : hashMap.keySet()) {
            Object value = hashMap.get(key);

            if (value == null) {
                js.append(String.format(Locale.US, "%s: %s,", key, "null"));
                continue;
            }

            if (value instanceof com.anychart.chart.common.dataentry.DataEntry) {
                js.append(String.format(Locale.US, "%s: %s,", key, ((com.anychart.chart.common.dataentry.DataEntry) value).generateJs()));
                continue;
            }

            if (value instanceof com.anychart.chart.common.dataentry.DataEntry[]) {
                js.append(String.format(Locale.US, "%s: %s,", key, toString((com.anychart.chart.common.dataentry.DataEntry[]) value)));
                continue;
            }

            if (value.getClass().isArray()) {
                String sValue;
                if (value instanceof Number[]) {
                    sValue = toString((Number[]) value);
                } else {
                    sValue = toString((String[]) value);
                }
                js.append(String.format(Locale.US, "%s: %s,", key, sValue));
            } else {
                if (value instanceof Number || value instanceof Boolean) {
                    js.append(String.format(Locale.US, "%s: %s,", key, value));
                } else {
                    js.append(String.format(Locale.US, "%s: '%s',", key, value));
                }
            }
        }
        if (hashMap.size() > 0)
            js.setLength(js.length() - 1);
        js.append("}");

        return js.toString();
    }

    @NonNull
    private String toString(String[] array) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (String item : array) {
            result.append("\'").append(item).append("\',");
        }
        if (array.length > 0)
            result.setLength(result.length() - 1);
        result.append("]");

        return result.toString();
    }

    @NonNull
    private String toString(Number[] array) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (Number item : array) {
            result.append(item).append(",");
        }
        if (array.length > 0)
            result.setLength(result.length() - 1);
        result.append("]");

        return result.toString();
    }

    @NonNull
    private String toString(com.anychart.chart.common.dataentry.DataEntry[] array) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (com.anychart.chart.common.dataentry.DataEntry item : array) {
            result.append(item.generateJs()).append(",");
        }
        if (array.length > 0)
            result.setLength(result.length() - 1);
        result.append("]");

        return result.toString();
    }

}
