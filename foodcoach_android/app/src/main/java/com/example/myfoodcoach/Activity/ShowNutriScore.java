package com.example.myfoodcoach.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfoodcoach.R;
import com.example.myfoodcoach.logic.BasketScore;
import com.example.myfoodcoach.logic.FoodInfo;
import com.example.myfoodcoach.logic.NutriScore;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ShowNutriScore extends AppCompatActivity {

    final static int NUMBER_OF_NUTRIENT = 9;
    final static String[] kindOfNutrient = {"#calories", "#carbohydrateContent", "#cholesterolContent", "#fatContent",
            "#fiberContent", "#proteinContent", "#saturatedFatContent", "#sodiumContent",
            "#sugarContent", "#Category"};
    final static int[] NUTRIENT_STANDARDS = {2000, 324, 300, 54,
            25, 55, 15, 2000,
            100};
    String[] barcodeArray;
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
    private ImageView crop_image_view;
    private ImageView basketScoreView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    private ArrayList<MyData> myDataset;
    private HashMap<String, Integer> barcodeQuantityMap;
    private ArrayList<FoodInfo> foodInfos;
    private Double basketScore;
    private SeekBar seekBar;
    private TextView basketTextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nutri_score);
        barcodeQuantityMap = new HashMap<>();
        barcodeArray = getIntent().getStringArrayExtra("barcodeList");
        int[] quantityArray = getIntent().getIntArrayExtra("quantityList");
        for (int i = 0; i < barcodeArray.length; i++) {
            barcodeQuantityMap.put(barcodeArray[i], quantityArray[i]);
        }
        String cropImageUri = getIntent().getStringExtra("cropImageUri");
        System.out.println(cropImageUri);
        foodInfos = new ArrayList<>();
        crop_image_view = (ImageView) findViewById(R.id.nutri_score_crop_image_view);
        crop_image_view.setImageURI(Uri.parse(cropImageUri));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManger);
        myDataset = new ArrayList<>();
        mAdapter = new CardViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        basketTextView = findViewById(R.id.basketText);
        seekBar = findViewById(R.id.seekBar);

        showNutriScore(barcodeArray);

        getSupportActionBar().hide();

    }

    private void showNutriScore(String[] barcodeArray) {
        for (int i = 0; i < barcodeArray.length; i++) {
            postObject(barcodeArray[i]);
        }


    }

    private String postObject(String queryName) {
        RequestQueue queue = Volley.newRequestQueue(ShowNutriScore.this);
        String url = "http://dfpl.sejong.ac.kr:8081/epcis/query";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = convertStringToXMLDocument(response);
                getNutritionData(doc, queryName);
                Log.d("VOLLY SUCC", doc.toString());
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
                System.out.println(httpPostBody);
                return httpPostBody.getBytes();
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return null;
    }

    private void getNutritionData(Document doc, String productURI) {
        Map<String, String> nutritionInfoMap = new HashMap<>();
        if (doc == null) {
            Log.d("StringToXML", "NULL");
        } else {
            NodeList attributeList = doc.getElementsByTagName("attribute");
            for (int i = 0; i < attributeList.getLength(); i++) {
                Node thisnode = attributeList.item(i);
                String attributionId = attributeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                int startPos = attributionId.indexOf("#");
                if (startPos != -1) {   // -1 is default value of String.indexOf()
                    if (attributionId.substring(startPos).equals("#category")) { // extension field
                        for (int j = 0; j < thisnode.getChildNodes().getLength(); j++) {
                            Node tmp = thisnode.getChildNodes().item(j);
                            if (tmp.getTextContent() != null && !tmp.getTextContent().trim().isEmpty())
                                continue;
                        }
                    } else if (attributionId.substring(startPos).equals("#productName")) { // extension field
                        for (int j = 0; j < thisnode.getChildNodes().getLength(); j++) {
                            Node tmp = thisnode.getChildNodes().item(j);
                            if (tmp.getTextContent() != null && !tmp.getTextContent().trim().isEmpty())
                                continue;
                        }
                    }
                    String tag = attributionId.substring(startPos);
                    String data = attributeList.item(i).getTextContent();
                    if (data.equals("null"))
                        data = "0";
                    if (true) {
                        data = data.replaceAll("\n", "");
                        data = data.trim();
                        nutritionInfoMap.put(tag, data);
                    }
                }
            }
            System.out.println(nutritionInfoMap);
            setNutriScore(nutritionInfoMap, productURI);
        }
    }

    private void setNutriScore(Map<String, String> nutritionInfoMap, String productURI) {
        Double energyVal = Double.parseDouble(nutritionInfoMap.get("#calories"));
        Double sugarVal = Double.parseDouble(nutritionInfoMap.get("#sugarContent"));
        Double fatVal = Double.parseDouble(nutritionInfoMap.get("#fatContent"));
        Double saturatedFatVal = Double.parseDouble(nutritionInfoMap.get("#saturatedFatContent"));
        Double sodiumVal = Double.parseDouble(nutritionInfoMap.get("#sodiumContent"));
        Double fiberVal = Double.parseDouble(nutritionInfoMap.get("#fiberContent"));
        Double proteinVal = Double.parseDouble(nutritionInfoMap.get("#proteinContent"));
        String image;
        try {
            if (nutritionInfoMap.get("#image") == null)
                throw new Exception();
            image = nutritionInfoMap.get("#image");
        } catch (Exception e) {
            image = "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";
        }

        String category = nutritionInfoMap.get("#category");
        String productName = nutritionInfoMap.get("#productName");
        // TODO quantity 현재 1로 넣어져 있지만, Basket Level NutriScore를 위해서 필요함. 추가 해야된다. (OK)
        // TODO productName 현재 "empty"로 넣어져 있지만, 화면에 URI 대신 상품명을 출력해야함.(OK)
        Integer quantity = barcodeQuantityMap.get(productURI); // quantity add
        FoodInfo foodInfo = new FoodInfo(energyVal, sugarVal, fatVal, saturatedFatVal, sodiumVal, fiberVal, proteinVal, category, quantity, productName, image);
        this.foodInfos.add(foodInfo);
        Integer totalScore = NutriScore.getNutriScore(foodInfo);
        if (totalScore < 0)
            myDataset.add(new MyData(productName, R.mipmap.a, image));
        else if (totalScore < 3)
            myDataset.add(new MyData(productName, R.mipmap.b, image));
        else if (totalScore < 11)
            myDataset.add(new MyData(productName, R.mipmap.c, image));
        else if (totalScore < 19)
            myDataset.add(new MyData(productName, R.mipmap.d, image));
        else
            myDataset.add(new MyData(productName, R.mipmap.e, image));
        mAdapter.notifyItemInserted(0);

        if (foodInfos.size() == barcodeArray.length) {
            System.out.println("foodInfos = " + foodInfos);
            basketScore = BasketScore.getBasketScore(foodInfos);
            System.out.println("basketScore = " + basketScore);
            basketTextView.setText(basketTextView.getText() + String.valueOf(Math.round(basketScore)));
            seekBar.setProgress(Integer.parseInt(String.valueOf(Math.round(100 / 45 * basketScore))));
            //seekBar.setProgress(100);

        }
    }

    private void setBasketScore(List<FoodInfo> inputData) {

    }

}