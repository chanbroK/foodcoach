package com.example.myfoodcoach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.myfoodcoach.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ShowURI extends AppCompatActivity {
    private String requestQueryHead ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "                  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "                  xmlns:query=\"urn:epcglobal:epcis-query:xsd:1\">\n" +
            "    <soapenv:Header/>\n" +
            "    <soapenv:Body>\n" +
            "        <query:Poll>\n" +
            "            <queryName>SimpleMasterDataQuery</queryName>\n" +
            "            <params>\n" +
            "                <param>\n" +
            "                    <name>EQ_name</name>\n" +
            "                    <value xsi:type=\"query:ArrayOfString\">\n" +
            "                        <string>";
    private String requestQueryTail = "</string>\n" +
            "                    </value>\n" +
            "                </param>\n" +
            "                <!-- includeAttributes and includeChidren are mandatory parameters -->\n" +
            "                <param>\n" +
            "                    <name>includeAttributes</name>\n" +
            "                    <value xsi:type=\"xsd:boolean\">true</value>\n" +
            "                </param>\n" +
            "                <param>\n" +
            "                    <name>includeChildren</name>\n" +
            "                    <value xsi:type=\"xsd:boolean\">true</value>\n" +
            "                </param>\n" +
            "            </params>\n" +
            "        </query:Poll>\n" +
            "    </soapenv:Body>\n" +
            "</soapenv:Envelope>";
    private LinearLayout lin_lay_show_uri;
    private Button btn_get_nutrition_data;
    private TextView tv_nutrition_data;
    List<String> nutrtionTags;
    List<Integer> nutritions;
    AnyChartView anyChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_u_r_i);

        lin_lay_show_uri = findViewById(R.id.lin_lay_show_uri);
        tv_nutrition_data = findViewById(R.id.tv_nutrition_data);

        anyChartView = findViewById(R.id.any_char_view);
        nutrtionTags = new ArrayList<>();
        nutritions = new ArrayList<>();

        String[] barcodeList = getIntent().getStringArrayExtra("barcodeList");
        int[] quantityList = getIntent().getIntArrayExtra("quantityList");

        Log.d("BarcodeArray", String.valueOf(barcodeList.length));
        Log.d("QuantityArray", String.valueOf(quantityList.length));

        for (int i = 0; i < barcodeList.length; i++){
            TextView tv_barcode = new TextView(this);
            TextView tv_quantity = new TextView(this);
            tv_barcode.setText(barcodeList[i]);
            tv_quantity.setText(String.valueOf(quantityList[i]));

            lin_lay_show_uri.addView(tv_barcode);
            lin_lay_show_uri.addView(tv_quantity);
        }

        btn_get_nutrition_data = findViewById(R.id.btn_get_nutrition_data);
        btn_get_nutrition_data.setOnClickListener(v -> {
            for (int i = 0; i < barcodeList.length; i++){
                // urn:epc:idpat:sgtin:8802876.020432.*
                // getNutritionData(barcodeList[i]);
                getNutritionData("urn:epc:idpat:sgtin:8802876.020432.*");
            }
        });

    }

    private String getNutritionData(String queryName){
        RequestQueue queue = Volley.newRequestQueue(ShowURI.this);
        String url = "http://203.250.148.67/epcis/query";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = convertStringToXMLDocument(response);
                createNutritionDataText(doc);
                Log.d("VOLLEY", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/xml");

                return headers;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody=requestQueryHead + queryName + requestQueryTail;

                return httpPostBody.getBytes();
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return null;
    }
    private static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private void createNutritionDataText(Document doc){
        if (doc == null){
            Log.d("StringToXML", "NULL");
        }else {
            Log.d("StringToXML", doc.getFirstChild().getNodeName());
            NodeList attributeList = doc.getElementsByTagName("attribute");
            for (int i = 0; i < attributeList.getLength(); i++){
                String attributionId = attributeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                int startPos = attributionId.indexOf("#");
                if (startPos != -1) {   // -1 is default value of String.indexOf()
                    String tag = attributionId.substring(startPos);
                    String data = attributeList.item(i).getTextContent();

                    TextView tv_tag = new TextView(this);
                    tv_tag.setText(tag + " : " + data);
                    lin_lay_show_uri.addView(tv_tag);
                    /////////////////////////////////////////////////
                    nutrtionTags.add(tag);
                    nutritions.add(Integer.parseInt(data));
                    Log.d("StringToXML", tag);
                    Log.d("StringToXML", data);
                }
            }

        }

        setupPieChart();
    }
    private void setupPieChart(){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0 ; i < nutrtionTags.size(); i++){
            dataEntries.add(new ValueDataEntry(nutrtionTags.get(i), nutritions.get(i)));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }
}