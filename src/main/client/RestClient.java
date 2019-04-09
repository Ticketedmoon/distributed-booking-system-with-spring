import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RestClient {
    private static ObjectMapper mapper = new ObjectMapper();
    //TODO change to vm ip or add logic to choose between.
    private static String uri = "http://localhost:8080/rooms/";
    static RestTemplate restTemplate = new RestTemplate();

    public HashMap<String, HashMap<String, Object>> getAllRooms(){
        String responseEntity = restTemplate.getForObject(uri, String.class);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        try {
            return mapper.readValue(responseEntity, typeRef);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    // Note: Same as method above but has room parameter.
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

    public HashMap<String, Object> bookRoom(String room, int day, String timeslot){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(UUID.randomUUID().toString(),room + "," + day + "," + timeslot);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        String putResource = uri + room + "/" + day + "/" + timeslot;

        ResponseEntity<String> responseEntity = restTemplate.exchange(putResource, HttpMethod.PUT, request, String.class);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        try {
            return mapper.readValue(responseEntity.getBody(), typeRef);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(responseEntity.getBody());
        return null;
    }
}
