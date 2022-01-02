package com.example.myfoodcoach.logic;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class TraceClient {
    public List<String> URIs;
    public List<Map<String, String>> eventListMap;
    private URL epcisURL;

    public TraceClient() {
        try {
            this.epcisURL = new URL("http://dfpl.sejong.ac.kr:8081/epcis/query");
//            this.epcisURL = new URL("https://webhook.site/601ba01d-9107-465c-92fb-616ce38ea05e");
            this.URIs = new ArrayList<>();
            this.eventListMap = new ArrayList<>();
        } catch (MalformedURLException e) {
            System.out.println("epcis server url is not valid");
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Map<String, String>> getBeforeTrace(String startURI) {
        this.URIs.add(startURI);
        //recursive query
        this.query(startURI);
        //remove duplication
        Set<Map<String, String>> eventSetMap = new HashSet<>(this.eventListMap);
        this.eventListMap = new ArrayList<>(eventSetMap);
        //sort result by eventTime
        //Collections.sort(list, new Comparator<HashMap<String, Object>>() {
        //         @Override
        //         public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
        //            String name1 = (String) o1.get("name");
        //            String name2 = (String) o2.get("name");
        //            return name1.compareTo(name2);
        //         }
        //      });
        this.eventListMap.sort((o1, o2) -> {
            String date1 = o1.get("eventTime");
            String date2 = o2.get("eventTime");
            return date1.compareTo(date2);
        });
        return this.eventListMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Node> getMATCH_anyEPC(String URI) {
        String body = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:query='urn:epcglobal:epcis-query:xsd:1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<query:Poll>"
                + "<queryName>SimpleEventQuery</queryName>"
                + "<params>"
                + "<param>"
                + "<name>MATCH_anyEPC</name>"
                + "<value xsi:type='query:ArrayOfString'>"
                + "<string>"
                + URI
                + "</string>"
                + "</value>"
                + "</param>"
                + "</params>"
                + "</query:Poll>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
        try {
            HttpURLConnection connection = (HttpURLConnection) this.epcisURL
                    .openConnection();
            connection.setRequestMethod("POST");
            // using xml
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream()));
            bw.write(body);
            bw.flush();
            bw.close();

            // response http code
            int responseCode = connection.getResponseCode();
            if (responseCode == 400) {
                System.out.println("400 ERROR");
            } else if (responseCode == 500) {
                System.out.println("500 ERROR");
            } else {
                System.out.println("200 SUCCESS");
            }
            // response to string
            String epcisString = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining());
            // string to List<Node>
            List<Node> result = new ArrayList<>();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(epcisString)));
            connection.disconnect();
            if (doc == null) {
                System.out.println("response document is null");
                return null;
            } else {
                NodeList eventNodeList = doc.getElementsByTagName("EventList").item(0).getChildNodes();
                for (int i = 0; i < eventNodeList.getLength(); i++) {
                    if (eventNodeList.item(i).getNodeName().contains("Event")) {
                        result.add(eventNodeList.item(i));
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, String> nodeToHashMap(Node eventNode, HashMap<String, String> eventMap) {
        NodeList eventChildList = eventNode.getChildNodes();
        eventMap.put("eventType", eventNode.getNodeName() + "Type");
        for (int i = 0; i < eventChildList.getLength(); i++) {
            String nodeName = eventChildList.item(i).getNodeName();
            if ("eventID".equals(nodeName)) {
                eventMap.put("eventID", eventChildList.item(i).getTextContent());
            } else if ("eventTime".equals(nodeName)) {
                eventMap.put("eventTime", eventChildList.item(i).getTextContent());
            } else if ("ext0:latitude".equals(nodeName)) {
                eventMap.put("latitude", eventChildList.item(i).getTextContent());
            } else if ("ext0:longitude".equals(nodeName)) {
                eventMap.put("longitude", eventChildList.item(i).getTextContent());
            } else if ("action".equals(nodeName)) {
                eventMap.put("action", eventChildList.item(i).getTextContent());
            }
        }
        return eventMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void query(String URI) {
        List<Node> eventNodeList = this.getMATCH_anyEPC(URI);
        if (eventNodeList != null && !eventNodeList.isEmpty()) {
            eventNodeList.forEach(eventNode -> {
                HashMap<String, String> eventMap = new HashMap<>();
                String nodeName = eventNode.getNodeName();
                if ("AggregationEvent".equals(nodeName) || "TransactionEvent".equals(nodeName)) {
                    this.handleAggregationEventAndTransactionEvent(eventNode);
                } else if ("TransformationEvent".equals(nodeName)) {
                    this.handleTranformationEvent(eventNode);
                }
                // Add data
                this.eventListMap.add(this.nodeToHashMap(eventNode, eventMap));

            });
        } else {
            System.out.println("eventList is null");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleTranformationEvent(Node eventNode) {
        for (int i = 0; i < eventNode.getChildNodes().getLength(); i++) {
            if (eventNode.getChildNodes().item(i).getNodeName().equals("inputEPCList")) {
                NodeList inputEpcNodeList = eventNode.getChildNodes().item(i).getChildNodes();
                for (int j = 0; j < inputEpcNodeList.getLength(); j++) {
                    if (inputEpcNodeList.item(j).getNodeName().equals("epc")) {
                        String inputEpc = inputEpcNodeList.item(j).getTextContent();
                        if (!this.URIs.contains(inputEpc)) {
                            this.URIs.add(inputEpc);
                            this.query(inputEpc);
                        }
                    }
                }
            } else if (eventNode.getChildNodes().item(i).getNodeName().equals("outputEPCList")) {
                NodeList outputEpcNodeList = eventNode.getChildNodes().item(i).getChildNodes();
                for (int j = 0; j < outputEpcNodeList.getLength(); j++) {
                    if (outputEpcNodeList.item(j).getNodeName().equals("epc")) {
                        String outputEpc = outputEpcNodeList.item(j).getTextContent();
                        if (!this.URIs.contains(outputEpc)) {
                            this.URIs.add(outputEpc);
                            this.query(outputEpc);
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleAggregationEventAndTransactionEvent(Node eventNode) {
        for (int i = 0; i < eventNode.getChildNodes().getLength(); i++) {
            if (eventNode.getChildNodes().item(i).getNodeName().equals("parentID")) {
                String parentId = eventNode.getChildNodes().item(i).getTextContent();
                if (!this.URIs.contains(parentId)) {
                    this.URIs.add(parentId);
                    this.query(parentId);
                }
            } else if (eventNode.getChildNodes().item(i).getNodeName().equals("childEPCs")) {
                NodeList childNodeList = eventNode.getChildNodes().item(i).getChildNodes();
                for (int j = 0; j < childNodeList.getLength(); j++) {
                    if (childNodeList.item(j).getNodeName().equals("epc")) {
                        String childEpc = childNodeList.item(j).getTextContent();
                        if (!this.URIs.contains(childEpc)) {
                            this.URIs.add(childEpc);
                            this.query(childEpc);
                        }
                    }
                }
            }
        }
    }

}
