package com.example.myfoodcoach.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
import com.anychart.charts.Polar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.PolarSeriesType;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.ScaleTypes;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.scales.Linear;

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

public class ShowNutritionInfoByPolarChart extends AppCompatActivity {
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
    final static int NUMBER_OF_NUTRIENT = 9;
    final static String[] kindOfNutrient = {"#calories", "#carbohydrateContent", "#cholesterolContent", "#fatContent",
            "#fiberContent", "#proteinContent", "#saturatedFatContent", "#sodiumContent",
            "#sugarContent"};
    final static int[] NUTRIENT_STANDARDS = {2000, 324, 300, 54,
            25, 55, 15, 2000,
            100};
    private List<DataEntry> data;
    private List<String> productsURI;
    private Button btn_draw_polar_chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutrition_info_by_polar_chart);

        productsURI = new ArrayList<>();
        data = new ArrayList<>();
        btn_draw_polar_chart = findViewById(R.id.btn_draw_polar_chart);

        getSupportActionBar().hide();
        initListOfDataEntry(data);

        String[] barcodeArray = getIntent().getStringArrayExtra("barcodeList");
        int[] quantityArray = getIntent().getIntArrayExtra("quantityList");

        showNutritionDataChart(barcodeArray, quantityArray);

        btn_draw_polar_chart.setOnClickListener(e -> {
            drawPolarChart();
        });

    }

    private void showNutritionDataChart(String[] barcodeArray, int[] quantityArray) {
        for (int i = 0; i < barcodeArray.length; i++) {
            Log.d("SIZE", String.valueOf(productsURI.size()));
            productsURI.add(barcodeArray[i]);
            postObject(barcodeArray[i]);
        }
    }

    private String postObject(String queryName) {
        RequestQueue queue = Volley.newRequestQueue(ShowNutritionInfoByPolarChart.this);
        // String url = "http://203.250.148.67/epcis/query";
        String url = "http://223.195.36.78:80/epcis/query";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = convertStringToXMLDocument(response);
                getNutritionData(doc, queryName); // 예를 들어 바코드 3개를 처리한다면, 3번 요청했습니다.네
                Log.d("VOLLEY", response.toString());
                drawPolarChart(); //제가 잘못 생각했습니다..ㅎ; 이 부분이 주석처리가 되어서 draw함수 호출 부분이 없다고 생각했습니다.

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
                    int nutrient = Integer.parseInt(data);
                    for (int j = 0; j < NUMBER_OF_NUTRIENT; j++){
                        double temp = 0.0;
                        if (kindOfNutrient[j].equals(tag)) {
                            temp = (nutrient) / (NUTRIENT_STANDARDS[j] / 3.0) * 100;
                            nutrient = (int) temp;
                            break;
                        }
                    }
                    nutrients.add(nutrient);
                }
            }
            addDataEntry(data, nutrients, String.valueOf(productsURI.indexOf(productURI) + 1));
        }
    }


    private void drawPolarChart() {
        AnyChartView anyChartView = findViewById(R.id.any_chart_view2);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar2));

        Polar polar = AnyChart.polar();

        Set set = Set.instantiate();
        set.data(data);

        int cntProducts = productsURI.size();  // 식료품 개수
        List<Mapping> seriesDataList = new ArrayList<>();
        for (int i = 0; i < cntProducts; i++) {
            Mapping seriesData;
            String valueOfMapAs = "{ x: 'x', value: 'value" + (i + 1) + "' }";
            seriesData = set.mapAs(valueOfMapAs);
            polar.column(seriesData);
        }

        polar.title("Meal Nutrition Ingredients");

        polar.sortPointsByX(true)
                .defaultSeriesType(PolarSeriesType.COLUMN)
                .yAxis(false)
                .xScale(ScaleTypes.ORDINAL);

        polar.title().margin().bottom(20d);

        ((Linear) polar.yScale(Linear.class)).stackMode(ScaleStackMode.VALUE);

        polar.tooltip()
                .valuePrefix("$")
                .displayMode(TooltipDisplayMode.UNION);

        anyChartView.setChart(polar);

    }

    private void initListOfDataEntry(List<DataEntry> dataEntries) {
        for (int i = 0; i < NUMBER_OF_NUTRIENT; i++) {
            DataEntry newDataEntry = new DataEntry();
            newDataEntry.setValue("x", kindOfNutrient[i]);
            dataEntries.add(newDataEntry);
        }
    }

    private void addDataEntry(List<DataEntry> dataEntries, List<Integer> nutrients, String URI) {
        for (int i = 0; i < NUMBER_OF_NUTRIENT; i++) {
            nutrients.get(i);
            dataEntries.get(i).setValue("value" + URI, nutrients.get(i));
            Log.d("ADD-DATA", URI);
        }
    }

}