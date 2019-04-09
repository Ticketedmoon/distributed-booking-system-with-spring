public class Launcher {

    public static void launch() {
        BookingWindow  bookingWindow = new BookingWindow(1000, 500);
        bookingWindow.initialize();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> launch());
    }
}
