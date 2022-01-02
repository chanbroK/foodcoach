package com.example.myfoodcoach.Activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodcoach.Activity.dataEnty.EpcisEvent;
import com.example.myfoodcoach.R;
import com.example.myfoodcoach.logic.TraceClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Map<String, String>> result;
    private FloatingActionButton fab;
    private List<EpcisEvent> eventList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String URI = getIntent().getStringExtra("id");

        class myThread extends Thread {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                TraceClient client = new TraceClient();
                result = client.getBeforeTrace(URI);
            }
        }
        myThread thread = new myThread();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(result.size());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();

        eventList = new ArrayList<>();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            for (Map<String, String> val : result) {
                eventList.add(new EpcisEvent(val));
            }
            Snackbar.make(v, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        result.forEach(event -> {
            LatLng target = new LatLng(Double.parseDouble(event.get("latitude")), Double.parseDouble(event.get("longitude")));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(target);
            String eventType = event.get("eventType");
            String action = event.get("action");
            //event 는 뒤에 type , action 은 대문자
            markerOptions.title("Info");
            System.out.println(eventType + "" + action);

            if (eventType.contains("AggregationEventType") && action.contains("ADD")) {
                markerOptions.snippet("Packing Product");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            } else if (eventType.contains("AggregationEventType") && action.contains("DELETE")) {
                markerOptions.snippet("Unpacking Product");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            } else if (eventType.contains("TransactionEventType") && action.contains("ADD")) {
                markerOptions.snippet("Load Product In Vehicle");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            } else if (eventType.contains("TransactionEventType") && action.contains("DELETE")) {
                markerOptions.snippet("Unload Product In Vehicle");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            } else if (eventType.contains("TransformationEvent")) {
                markerOptions.snippet("In Processing");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            } else if (eventType.contains("ObjectEvent")) {
                markerOptions.snippet("Observing");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            }
            mMap.addMarker(markerOptions);
        });
        PolylineOptions polylineOptions = new PolylineOptions();

        result.forEach(e -> {
            polylineOptions.add(new LatLng((Double.parseDouble(e.get("latitude"))), Double.parseDouble(e.get("longitude"))));
        });
        Polyline polyline1 = googleMap.addPolyline(polylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(result.get(result.size() - 1).get("latitude")), Double.parseDouble(result.get(result.size() - 1).get("longitude"))), 10));

    }
}