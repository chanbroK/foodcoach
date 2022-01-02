package com.example.myfoodcoach.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.myfoodcoach.PieChartGridList.PieChartAdapter;
import com.example.myfoodcoach.PieChartGridList.PieChartItem;
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

public class ShowNutritionInfo_2 extends AppCompatActivity {
    private String requestQueryHead = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
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
    final static int NUMBER_OF_NUTRIENT = 10;
    final static String[] kindOfNutrient = {"#calories", "#carbohydrateContent", "#cholesterolContent", "#fatContent",
            "#proteinContent", "#saturatedFatContent", "#servingSize", "#sodiumContent",
            "#sugarContent", "#transFatContent"};
    private GridView gridView;
    private Button btn_get_total_nutrient;
    private PieChartAdapter adapter;
    private List<String> totalTags;
    private List<Integer> totalNutrition;
    private List<DataEntry> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutrition_info);
        gridView = findViewById(R.id.grid_view_show_nutrition_info);
        btn_get_total_nutrient = findViewById(R.id.btn_get_total_nutrient);
        adapter = new PieChartAdapter();
        totalTags = null;
        totalNutrition = null;

        data = new ArrayList<>();
        initListOfDataEntry(data);

        String[] barcodeArray = getIntent().getStringArrayExtra("barcodeList");
        int[] quantityArray = getIntent().getIntArrayExtra("quantityList");

        showNutritionDataChart(barcodeArray, quantityArray);

        btn_get_total_nutrient.setOnClickListener(e -> {
            Log.d("BTS", "total Nutrtion Data" + totalNutrition.toString());
            Toast.makeText(this, "total Nutrition Data", Toast.LENGTH_LONG);
        });

    }

    private void showNutritionDataChart(String[] barcodeArray, int[] quantityArray) {
        for (int i = 0; i < barcodeArray.length; i++) {
            postObject(barcodeArray[i]);
        }
    }

    private String postObject(String queryName) {
        RequestQueue queue = Volley.newRequestQueue(ShowNutritionInfo_2.this);
        // String url = "http://203.250.148.67/epcis/query";
//        String url = "http://192.168.0.159/epcis/query";
        String url = "http://192.168.0.159/epcis/query";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = convertStringToXMLDocument(response);
                getNutritionData(doc, queryName);
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
                String httpPostBody = requestQueryHead + queryName + requestQueryTail;
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
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getNutritionData(Document doc, String productURI) {
        List<String> nutritionTags = new ArrayList<>();
        List<Integer> nutrients = new ArrayList<>();
        if (doc == null) {
            Log.d("StringToXML", "NULL");
        } else {
            NodeList attributeList = doc.getElementsByTagName("attribute");
            for (int i = 0; i < attributeList.getLength(); i++) {
                String attributionId = attributeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                int startPos = attributionId.indexOf("#");
                if (startPos != -1) {   // -1 is default value of String.indexOf()
                    String tag = attributionId.substring(startPos);
                    String data = attributeList.item(i).getTextContent();

                    nutritionTags.add(tag);
                    nutrients.add(Integer.parseInt(data));
                }
            }
            updateTotalData(nutritionTags, nutrients);
            createNutritionDataChart(nutritionTags, nutrients);
            addDataEntry(data, nutrients, productURI);
        }
    }

    private void updateTotalData(List<String> tags, List<Integer> nutrients) {
        if (totalTags == null) {
            totalTags = tags;
            totalNutrition = nutrients;
        } else {
            for (int i = 0; i < nutrients.size(); i++) {
                Integer updateNutrition = totalNutrition.get(i) + nutrients.get(i);
                totalNutrition.set(i, updateNutrition);
            }
        }
    }

    private void createNutritionDataChart(List<String> tags, List<Integer> nutrients) {
        // PieChart Item 생성
        PieChartItem item = new PieChartItem();
        item.addData(tags, nutrients);
        // Adapter Item 추가
        adapter.addItem(item);

        gridView.setAdapter(adapter);
    }

    private void drawPolarChart(){

    }
    private void initListOfDataEntry(List<DataEntry> dataEntries){
        for (int i = 0; i < NUMBER_OF_NUTRIENT; i++){
            DataEntry newDataEntry = new DataEntry();
            newDataEntry.setValue("x", kindOfNutrient[i]);
            dataEntries.add(newDataEntry);
        }
    }

    private void addDataEntry(List<DataEntry> dataEntries, List<Integer> nutrients, String URI){
        for (int i = 0; i < NUMBER_OF_NUTRIENT; i++){
            dataEntries.get(i).setValue(URI, nutrients.get(i));
        }
    }


    private void setElement(int[] arr, List<Integer> list){
        for (int i = 0; i < arr.length; i++)
            list.add(arr[i]);
    }
}