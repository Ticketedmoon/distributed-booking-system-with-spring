package application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public Rooms readRooms(RoomsMapper roomsMapper){
        try {
            return roomsMapper.readJsonWithObjectMapper();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public RoomsMapper readRoomJson(){
        RoomsMapper roomsMapper = new RoomsMapper();
        return roomsMapper;
    }
}
