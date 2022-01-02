package com.example.myfoodcoach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipPositionMode;
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

public class ShowNutritionInfo extends AppCompatActivity {
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
            "#sugarContent", "#transFatContent" , "#category"};
    private GridView gridView;
    private Button btn_get_total_nutrient;
    private PieChartAdapter adapter;
    private List<String> totalTags;
    private List<Integer> totalNutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutrition_info);
        gridView = findViewById(R.id.grid_view_show_nutrition_info);
        btn_get_total_nutrient = findViewById(R.id.btn_get_total_nutrient);
        adapter = new PieChartAdapter();
        totalTags = null;
        totalNutrition = null;

        String[] barcodeArray = getIntent().getStringArrayExtra("barcodeList");
        int[] quantityArray = getIntent().getIntArrayExtra("quantityList");

        showNutritionDataChart(barcodeArray, quantityArray);

        btn_get_total_nutrient.setOnClickListener(e -> {
            Log.d("BTS", "total Nutrition Data" + totalNutrition.toString());
            Toast.makeText(this, "total Nutrition Data", Toast.LENGTH_LONG);
        });

    }

    private void showNutritionDataChart(String[] barcodeArray, int[] quantityArray) {
        for (int i = 0; i < barcodeArray.length; i++) {
            postObject(barcodeArray[i]);
        }
    }

    private String postObject(String queryName) {
        RequestQueue queue = Volley.newRequestQueue(ShowNutritionInfo.this);
        // String url = "http://203.250.148.67/epcis/query";
        // http://223.195.36.78/
        // String url = "http://223.195.36.78/epcis/query";
        String url = "http://223.195.36.78:80/epcis/query";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = convertStringToXMLDocument(response);
                getNutritionData(doc);
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

    private void getNutritionData(Document doc) {
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


}