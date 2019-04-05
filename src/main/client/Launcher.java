public class Launcher {

    public static void launch() {
        BookingWindow  bookingWindow = new BookingWindow(800, 600);
        bookingWindow.initialize();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> launch());

        //RestClient restClient = new RestClient();
        //restClient.getRoomTimetable("L221");
        //restClient.bookRoom("L221", 0, "9-10");

        //restClient.restTemplateGetRoom("L221");
        //restClient.restTemplateBookRoom("L221", 0, "9-10");
    }
}
