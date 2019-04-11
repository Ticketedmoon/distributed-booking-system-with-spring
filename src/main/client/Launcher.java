public class Launcher {

    public static void launch() {
        BookingWindow  bookingWindow = new BookingWindow(1200, 500);
        bookingWindow.initialize();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s - %5$s%6$s%n");
        new ClientLogWriter();
        javax.swing.SwingUtilities.invokeLater(() -> launch());
    }
}
