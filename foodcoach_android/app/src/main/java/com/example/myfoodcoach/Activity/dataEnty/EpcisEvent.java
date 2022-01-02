package com.example.myfoodcoach.Activity.dataEnty;

import java.util.Map;

public class EpcisEvent {
    public String latitude, longitude, eventTime, action, event;

    public EpcisEvent() {

    }

    public EpcisEvent(Map<String, String> inputEvent) {
        this.latitude = inputEvent.get("latitude");
        this.longitude = inputEvent.get("longitude");
        this.eventTime = inputEvent.get("eventTime");
        this.action = inputEvent.get("action");
        this.event = inputEvent.get("event");
    }


}
