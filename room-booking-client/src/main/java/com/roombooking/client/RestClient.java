package com.roombooking.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    private static ObjectMapper mapper = new ObjectMapper();
    private  String uri;
    private static RestTemplate restTemplate = new RestTemplate();

    public RestClient(String serverIP, int port){
        this.uri = "http://" + serverIP + ":" + port + "/rooms/";
    }

    @SuppressWarnings("Duplicates")
    public HashMap<String, HashMap<String, Object>> getAllRooms(){
        String responseEntity = restTemplate.getForObject(uri, String.class);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        try {
            return mapper.readValue(responseEntity, typeRef);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Note: Same as method above but has room parameter.
    @SuppressWarnings("Duplicates")
    public HashMap<String, Object> getRoom(String room) {
        String responseEntity = restTemplate.getForObject(uri + room, String.class);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        try {
            return mapper.readValue(responseEntity, typeRef);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean roomAvailableAtTime(String room, int day, String time) {
        String responseEntity = restTemplate.getForObject(uri + room + "/" + day + "/" + time, String.class);
        TypeReference<Boolean> typeRef = new TypeReference<Boolean>() {};
        try {
            return mapper.readValue(responseEntity, typeRef);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void bookRoom(String room, int day, String timeSlot){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(Thread.currentThread().getName(),room + "," + day + "," + timeSlot);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        String putResource = uri + room + "/" + day + "/" + timeSlot;
        ResponseEntity<String> responseEntity = restTemplate.exchange(putResource, HttpMethod.PUT, request, String.class);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        try {
            mapper.readValue(responseEntity.getBody(), typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
