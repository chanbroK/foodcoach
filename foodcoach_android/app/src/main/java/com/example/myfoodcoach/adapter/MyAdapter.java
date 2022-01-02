package com.example.myfoodcoach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myfoodcoach.Activity.dataEnty.EpcisEvent;
import com.example.myfoodcoach.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<EpcisEvent> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public EpcisEvent getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_custom, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView tv_event = (TextView) convertView.findViewById(R.id.event_tv);
        TextView tv_time = (TextView) convertView.findViewById(R.id.time_tv);
        TextView tv_action = (TextView) convertView.findViewById(R.id.action_tv);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        EpcisEvent myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        tv_event.setText(myItem.event);
        tv_time.setText(myItem.eventTime);
        tv_action.setText(myItem.action);

        return convertView;
    }

    public void addItem(String action, String time, String event, String longitude, String latitude) {
        EpcisEvent epcisEvent = new EpcisEvent();
        epcisEvent.action = action;
        epcisEvent.eventTime = time;
        epcisEvent.event = event;
        epcisEvent.latitude = latitude;
        epcisEvent.longitude = longitude;
        mItems.add(epcisEvent);

    }
}
