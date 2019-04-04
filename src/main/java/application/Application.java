package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        RoomsMapper mapper = new RoomsMapper();

        try {
            //Just for logging/testing
            mapper.readJsonWithObjectMapper();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}