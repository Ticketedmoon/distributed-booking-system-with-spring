import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientRequestFactory {
    //TODO create all Client Requests on startup but only run when testMode is clicked.
    private AtomicInteger startAmountClients;
    private ScheduledExecutorService clientExecutor;
    private String [] rooms = {"L221", "CG04", "T101", "XG14"};
    private String [] timeSlots = {"9-10", "10-11", "11-12", "12-1", "1-2"};
    private ArrayList<ClientRequests> bookingRequests;

    public ClientRequestFactory(int amountClients){
        clientExecutor = Executors.newScheduledThreadPool(amountClients);
    }

    private ClientRequests generateClientRequest(){
        String room = rooms[ThreadLocalRandom.current().nextInt(0,4)];
        int day = ThreadLocalRandom.current().nextInt(1,7);
       // String room = rooms[0];
       // int day = 0;
        String timeSlot = timeSlots[ThreadLocalRandom.current().nextInt(0, 5)];
        int requestTime = ThreadLocalRandom.current().nextInt(1, 20 +1);

        return new ClientRequests(room, day, timeSlot, requestTime);
    }

    public ArrayList<ClientRequests> generateClientRequests(int amount){
        bookingRequests = new ArrayList<>();
        for(int i  = 0; i < amount; i++){
            ClientRequests clientRequest = this.generateClientRequest();
            bookingRequests.add(clientRequest);
        }
        return bookingRequests;
    }

    public void scheduleClientRequests(ArrayList<ClientRequests> bookingRequests) {
        for (ClientRequests requests : bookingRequests) {
            clientExecutor.schedule(requests, requests.getRequestTime(), TimeUnit.SECONDS);
        }
    }
}
