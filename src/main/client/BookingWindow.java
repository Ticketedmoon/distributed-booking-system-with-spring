import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingWindow extends JFrame {

    private JLabel testModeText;
    private JButton show_rooms_button;
    private JButton L221_button;
    private JButton XG14_button;
    private JButton T101_button;
    private JButton CG04_button;
    private JButton test_button;

    private JPanel menu;
    private JPanel window;
    private RestClient restClient;
    private TableView viewableTable;

    private int amountClients = 1000;
    private ClientRequestFactory requestFactory = new ClientRequestFactory(amountClients);
    private ExecutorService serverAliveVerifier = Executors.newSingleThreadExecutor();
    private ArrayList<ClientRequests> clientRequests;
    boolean isBookingServerAlive = false;

    public BookingWindow(int sizeX, int sizeY) {
        super("University Room Booking System");

        // Spawn the RestClient before anything happens - more efficient for loading.
        try {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            clientRequests = requestFactory.generateClientRequests(amountClients);
            restClient = new RestClient();
        }
        finally {
            this.menu = new JPanel(new BorderLayout());
            this.window = new JPanel(new BorderLayout());
            this.viewableTable = new TableView(restClient);
            this.getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.lightGray));
            this.setSize(sizeX, sizeY);
            setVisible(true);
        }
    }

    public void initialize(String serverName, int port) {
        createCentreWindow();
        buildWindowPanels();
        createMenuButtons();
        serverAliveVerifier.submit(() -> checkIfServerAlive(serverName, port));
    }

    private void checkIfServerAlive(String serverName, int port) {
        isBookingServerAlive = hostAvailabilityCheck(serverName, port);
        while(!isBookingServerAlive) {
            try {
                System.out.println("Checking if server is alive...");
                showDisplayTextOnWindow(String.format("Server with IP %s:%d currently not available - retrying every 3 seconds...", serverName, port), Color.RED);
                changeModeOfButtons(false);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                isBookingServerAlive = hostAvailabilityCheck(serverName, port);
                removeDisplayTextOnWindow();
            }
        }
        enablePostServerConnectFunctions(serverName, port);
    }

    private void enablePostServerConnectFunctions(String serverName, int port) {
        initializeActionListeners();
        changeModeOfButtons(true);
        showDisplayTextOnWindow(String.format("Server with IP %s:%d is Alive and Accepting Requests", serverName, port), Color.green);
    }

    private boolean hostAvailabilityCheck(String SERVER_ADDRESS, int TCP_SERVER_PORT) {
        try (Socket socket = new Socket(SERVER_ADDRESS, TCP_SERVER_PORT)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }

    private void buildWindowPanels() {
        this.getContentPane().add(menu, BorderLayout.WEST);
        this.getContentPane().add(window, BorderLayout.EAST);
    }

    private void createCentreWindow() {
        window.setLayout ( new BorderLayout() );
        window.setPreferredSize(new Dimension(1050, 40));
        window.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.gray));
    }

    private void createMenuButtons(){
        JPanel buttons = new JPanel(new GridLayout(0, 1, 80, 20));
        show_rooms_button = new JButton("All Rooms");
        L221_button = new JButton("L221");
        XG14_button = new JButton("XG14");
        T101_button = new JButton("T101");
        CG04_button = new JButton("CG04");
        test_button = new JButton("Test Mode");
        JButton [] design_buttons = {show_rooms_button, L221_button, XG14_button, T101_button, CG04_button,
                test_button};

        for (JButton button : design_buttons) {
            button.setBackground(Color.lightGray);
            button.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));
            buttons.add(button);
        }

        menu.add(buttons, BorderLayout.NORTH);
    }

    private void buildDefaultTablePane() {
        Object[] rowsColumns = getDefaultScreenProperties();
        Object [][] data = (Object[][]) rowsColumns[0];
        Object [] columnNames = (Object []) rowsColumns[1];
        viewableTable.build_table(data, columnNames, window);
    }

    // Note: JTable requires the rooms be a 2D array, hence the fragment below.
    private void initializeActionListeners() {

        // Show All Rooms Button Listener
        show_rooms_button.addActionListener(e -> {
            // Build Table of Rooms
            buildDefaultTablePane();
            removeDisplayTextOnWindow();
        });

        // Individual Rooms
        L221_button.addActionListener(e -> {
            HashMap<String, Object> room_details = restClient.getRoom("L221");
            viewableTable.setRoomName("L221");
            JTable screenTable = viewableTable.getNewTableDisplay(room_details);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(L221_button, test_button, XG14_button, T101_button, CG04_button, show_rooms_button);
            removeDisplayTextOnWindow();
        });

        XG14_button.addActionListener(e -> {
            HashMap<String, Object> room_details = restClient.getRoom("XG14");
            viewableTable.setRoomName("XG14");
            JTable screenTable = viewableTable.getNewTableDisplay(room_details);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(XG14_button, L221_button, T101_button, CG04_button, test_button, show_rooms_button);
            removeDisplayTextOnWindow();
        });

        T101_button.addActionListener(e -> {
            HashMap<String, Object> room_details = restClient.getRoom("T101");
            viewableTable.setRoomName("T101");
            JTable screenTable = viewableTable.getNewTableDisplay(room_details);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(T101_button, L221_button, XG14_button, test_button, CG04_button, show_rooms_button);
            removeDisplayTextOnWindow();
        });

        CG04_button.addActionListener(e -> {
            HashMap<String, Object> room_details = restClient.getRoom("CG04");
            viewableTable.setRoomName("CG04");
            JTable screenTable = viewableTable.getNewTableDisplay(room_details);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(CG04_button, L221_button, XG14_button, T101_button, test_button, show_rooms_button);
            removeDisplayTextOnWindow();
        });

        // Other Functions
        test_button.addActionListener(e -> {
            viewableTable.updateTableView(null, window);

            // create a label to display text
            showDisplayTextOnWindow(String.format("Test Mode Activated, Spawning %d virtual client request threads", amountClients), Color.RED);
            requestFactory.scheduleClientRequests(clientRequests);
            setActiveButtonColour(test_button, L221_button, XG14_button, T101_button, CG04_button, show_rooms_button);
        });
    }

    private Object[] getDefaultScreenProperties() {
        int room_count = 0;
        HashMap<String, HashMap<String, Object>> rooms = restClient.getAllRooms();
        Object[][] data = new Object[rooms.size()][2];
        String[] columnNames = { "Rooms", "Capacity"};

        // Had to remove your ForEach Nice loop thing for the sake of this table
        for(Map.Entry entry : rooms.entrySet()) {
            data[room_count][0] = entry.getKey();
            data[room_count][1] = ((HashMap) entry.getValue()).get("capacity");
            room_count++;
        }

        // Set button colour
        setActiveButtonColour(show_rooms_button, L221_button, XG14_button, T101_button, CG04_button, test_button);
        return new Object[]{data, columnNames};
    }

    private void showDisplayTextOnWindow(String text, Color color) {
        testModeText = new JLabel(text, SwingConstants.CENTER);
        testModeText.setFont(new Font("Calibri", Font.BOLD, 24));
        testModeText.setForeground(color);
        window.add(testModeText);
        window.updateUI();
    }

    private void removeDisplayTextOnWindow() {
        if (testModeText != null)
            window.remove(testModeText);
        window.updateUI();
    }

    // Note: Whatever button is passed first will be designated as the active one.
    private void setActiveButtonColour(JButton active, JButton inactiveA, JButton inactiveB,
                                       JButton inactiveC, JButton inactiveD, JButton inactiveE) {
        // Set button colour
        active.setBackground(Color.ORANGE);
        inactiveA.setBackground(Color.lightGray);
        inactiveB.setBackground(Color.lightGray);
        inactiveC.setBackground(Color.lightGray);
        inactiveD.setBackground(Color.lightGray);
        inactiveE.setBackground(Color.lightGray);
    }

    private void changeModeOfButtons(boolean enabledMode) {
        JButton [] buttons = new JButton[] { show_rooms_button, L221_button, XG14_button, T101_button,
                CG04_button, test_button };
        for (JButton button : buttons) {
            button.setBackground(Color.lightGray);
            button.setEnabled(enabledMode);
        }
    }
}
