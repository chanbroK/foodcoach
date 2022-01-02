package com.example.myfoodcoach.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.myfoodcoach.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ConvertStringToXML_And_ParsingData extends AppCompatActivity {
    private String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "    <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "        <soapenv:Body>\n" +
            "            <ns2:queryResults xmlns:ns2=\"urn:epcglobal:epcis:xsd:1\" xmlns:ns3=\"http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader\">\n" +
            "                <queryName>SimpleMasterDataQuery</queryName>\n" +
            "                <resultsBody>\n" +
            "                    <VocabularyList>\n" +
            "                        <Vocabulary type=\"urn:epcglobal:epcis:vtype:EPCClass\">\n" +
            "                            <VocabularyElementList>\n" +
            "                                <VocabularyElement id=\"urn:epc:idpat:sgtin:8802876.020432.*\">\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#calories\">1056</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#carbohydrateContent\">141</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#cholesterolContent\">5</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#fatContent\">50</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#proteinContent\">11</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#saturatedFatContent\">22</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#servingSize\">208</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#sodiumContent\">390</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#sugarContent\">19</attribute>\n" +
            "                                    <attribute id=\"http://schema.org/NutritionInformation#transFatContent\">0</attribute>\n" +
            "                                </VocabularyElement>\n" +
            "                            </VocabularyElementList>\n" +
            "                        </Vocabulary>\n" +
            "                    </VocabularyList>\n" +
            "                </resultsBody>\n" +
            "            </ns2:queryResults>\n" +
            "        </soapenv:Body>\n" +
            "    </soapenv:Envelope>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_string_to_x_m_l__and__parsing_data);

        Document doc = convertStringToXMLDocument(test);

        if (doc == null){
            Log.d("StringToXML", "NULL");
        }else {
            Log.d("StringToXML", doc.getFirstChild().getNodeName());
            NodeList attributeList = doc.getElementsByTagName("attribute");
            for (int i = 0; i < attributeList.getLength(); i++){
                String attributionId = attributeList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                int startPos = attributionId.indexOf("#");
                String tag = attributionId.substring(startPos);
                Log.d("StringToXML", tag);
                Log.d("StringToXML", attributeList.item(i).getTextContent());
            }

        }
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
}