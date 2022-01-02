package org.dfpl.traceability;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        TraceClient client = new TraceClient();
//        List<Map<String, String>> result = client.getBeforeTrace("urn:epc:id:sgtin:0000003.000001.1");
        List<Map<String, String>> result = client.getBeforeTrace("urn:epc:idpat:sgtin:9557062.036135.*");
        System.out.println("[Event total count]: " + result.size());
        String json = new ObjectMapper().writeValueAsString(result);
        System.out.println("[Result to JSON]: " + json);
        System.out.println("[Used URIs]: " + new ObjectMapper().writeValueAsString(client.URIs));
    }
}
