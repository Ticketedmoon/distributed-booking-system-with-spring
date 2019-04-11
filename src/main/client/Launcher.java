public class Launcher {

    public static void launch(String serverName, int port) {
        BookingWindow  bookingWindow = new BookingWindow(1200, 500);
        bookingWindow.initialize("127.0.0.1", 8080);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s - %5$s%6$s%n");
        new ClientLogWriter();

        try {

            if (args.length == 1) {
                // Server Properties that have been passed in
                String[] serverProperties = args[0].split(":");
                String serverID = serverProperties[0];
                int serverPort = Integer.parseInt(serverProperties[1]);
                javax.swing.SwingUtilities.invokeLater(() -> launch(serverID, serverPort));
            } else {
                // Default Local server properties
                javax.swing.SwingUtilities.invokeLater(() -> launch("127.0.0.1", 8080));
            }
        } catch (Exception e) {
            System.err.println("Error: A valid server IP or server Port was not passed in correctly");
        }
    }
}
