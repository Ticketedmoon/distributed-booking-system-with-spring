import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

public class RestClient {

    private static ObjectMapper mapper = new ObjectMapper();
    //TODO change to vm ip or add logic to choose between.
    private static String uri = "http://localhost:8080/rooms/";
    static RestTemplate restTemplate = new RestTemplate();

    public HashMap<String, HashMap<String, Object>> restTemplateGetAllRooms(){
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
    public HashMap<String, Object> restTemplateGetRoom(String room) {
        String responseEntity = restTemplate.getForObject(uri + room, String.class);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        try {
            return mapper.readValue(responseEntity, typeRef);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean restTemplateRoomAvailableAtTime(String room, int day, String time) {
        String responseEntity = restTemplate.getForObject(uri + room + "/" + day + "/" + time, String.class);
        TypeReference<Boolean> typeRef = new TypeReference<Boolean>() {};
        try {
            return mapper.readValue(responseEntity, typeRef);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public HashMap<String, Object> restTemplateBookRoom(String room, int day, String timeslot){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        //TODO placeholder for something trackable, e.g (K: UUID/Threadname, V: "Room+DAY+SLOT")
        map.add("steve",room+day+timeslot);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        String putResource = uri + room + "/" + day + "/" + timeslot;

        //TODO get proper response format
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

        //TODO just for debugging.
       /* try{
            JsonNode putRoot = mapper.readTree(responseEntity1.getBody());
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(putRoot);
            System.out.println(indented);

        }catch (IOException e){
            e.printStackTrace();
        }
        */
    }
}
