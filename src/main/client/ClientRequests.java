import org.springframework.web.server.ServerErrorException;

import java.util.logging.Logger;

public class ClientRequests implements Runnable {

    private String room;
    private int day;
    private String timeSlot;
    private int requestTime;
    private RestClient restClient;

    private static final Logger LOGGER = Logger.getLogger(ClientRequests.class.getName());

    public ClientRequests(String room, int day, String timeSlot, int requestTime ){
        this.room = room;
        this.day = day;
        this.timeSlot = timeSlot;
        this.requestTime = requestTime;
        this.restClient = new RestClient();
    }

    public int getRequestTime(){
        return this.requestTime;
    }

    public void run() {
        try {
            LOGGER.info(String.format("Client request %s requesting room %s on day %s, timeslot %s, at %d seconds",
                    Thread.currentThread().getId(), room, day, timeSlot, requestTime));
            this.restClient.bookRoom(room, day, timeSlot);
        } catch (Exception e) {
            LOGGER.warning("Client Request Failed --> Server Not Responding");
        }
    }
}
