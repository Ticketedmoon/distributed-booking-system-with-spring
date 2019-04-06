import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;

public class RestClient {

    private static ObjectMapper mapper = new ObjectMapper();
    //TODO change to vm ip or add logic to choose between.
    private static String uri = "http://localhost:8080/rooms/";

    //https://springframework.guru/spring-5-webclient/
    private final WebClient webClient;
    //TODO I think everything leave everything with webclient tbh.
    public RestClient(){
        this.webClient = WebClient.builder()
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getRoomTimetable(String room){
        return webClient.get()
                .uri(room)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String bookRoom(String room, int day, String timeslot){
        return webClient.put()
                .uri(uri + room + "/" + day + "/" + timeslot)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }



    //TODO Im not sure which to use. Whats your experience? It seems webClient relies on our app being non-blocking
    // Shane: Never used either, ill do some research about it
    //So restTemplate might be safer assignment wise.

    static ObjectMapper restTemplateMapper = new ObjectMapper();
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

    public void restTemplateGetRoom(String room){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri + room, String.class);
        //TODO Debugger.
        try {
            JsonNode putRoot = mapper.readTree(responseEntity.getBody());
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(putRoot);
            System.out.println(indented);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void restTemplateBookRoom(String room, int day, String timeslot){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        //TODO placeholder for something trackable, e.g (K: UUID/Threadname, V: "Room+DAY+SLOT")
        map.add("steve",room+day+timeslot);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);

        String putResource = uri + room + "/" + day + "/" + timeslot;
        //TODO get proper response format
        ResponseEntity<String> responseEntity1 = restTemplate.exchange(putResource, HttpMethod.PUT, request, String.class);
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
