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
    private int serverPort;
    private String serverName;
    private ClientRequestFactory requestFactory = new ClientRequestFactory(amountClients);
    private ExecutorService serverAliveVerifier = Executors.newSingleThreadExecutor();
    private ArrayList<ClientRequests> clientRequests;
    boolean isBookingServerAlive = false;

    public BookingWindow(int sizeX, int sizeY, String serverName, int serverPort) {
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
            this.serverName = serverName;
            this.serverPort = serverPort;
            this.menu = new JPanel(new BorderLayout());
            this.window = new JPanel(new BorderLayout());
            this.viewableTable = new TableView(restClient);
            this.getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.lightGray));
            this.setSize(sizeX, sizeY);
            setVisible(true);
        }
    }

    public void initialize() {
        createCentreWindow();
        buildWindowPanels();
        createMenuButtons();
        serverAliveVerifier.submit(this::checkIfServerAlive);
    }

    private void checkIfServerAlive() {
        isBookingServerAlive = hostAvailabilityCheck(serverName, serverPort);
        while(!isBookingServerAlive) {
            try {
                System.out.println("Checking if server is alive...");
                showDisplayTextOnWindow(String.format("Server with IP %s:%d currently not available - retrying every 3 seconds...", serverName, serverPort), Color.RED);
                changeModeOfButtons(false);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                isBookingServerAlive = hostAvailabilityCheck(serverName, serverPort);
                removeDisplayTextOnWindow();
            }
        }
        enablePostServerConnectFunctions(serverName, serverPort);
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
        removeDisplayTextOnWindow();
    }

    // Note: JTable requires the rooms be a 2D array, hence the fragment below.
    private void initializeActionListeners() {

        // Show All Rooms Button Listener
        show_rooms_button.addActionListener(e -> {
            buildDefaultTablePane();
        });

        // Individual Rooms
        L221_button.addActionListener(e -> {
            try {
                loadRoomTableView("L221", L221_button);
            } catch (Exception ex) {
                System.out.println("Tried to Access L221 - Server unavailable");
                resetTableViewIfServerUnavailable();
            }
        });

        XG14_button.addActionListener(e -> {
            try {
                loadRoomTableView("XG14", XG14_button);
            } catch (Exception ex) {
                System.out.println("Tried to Access XG14 - Server unavailable");
                resetTableViewIfServerUnavailable();
            }
        });

        T101_button.addActionListener(e -> {
            try {
                loadRoomTableView("T101", T101_button);
            } catch (Exception ex) {
                System.out.println("Tried to Access T101 - Server unavailable");
                resetTableViewIfServerUnavailable();
            }
        });

        CG04_button.addActionListener(e -> {
            try {
                loadRoomTableView("CG04", CG04_button);
            } catch (Exception ex) {
                System.out.println("Tried to Access CG04 - Server unavailable");
                resetTableViewIfServerUnavailable();
            }
        });

        // Other Functions
        test_button.addActionListener(e -> {
            viewableTable.updateTableView(null, window);
            showDisplayTextOnWindow(String.format("Test Mode Activated, Spawning %d virtual client request threads", amountClients), Color.RED);
            setActiveButtonColour(test_button);

            try {
                requestFactory.scheduleClientRequests(clientRequests);
            } catch (Exception exception) {
                System.out.println("Tried to Access Test Mode - Server unavailable");
                resetTableViewIfServerUnavailable();
            }
        });
    }

    private void loadRoomTableView(String roomName, JButton activeButton) {
        HashMap<String, Object> room_details = restClient.getRoom(roomName);
        viewableTable.setRoomName(roomName);
        JTable screenTable = viewableTable.getNewTableDisplay(room_details);
        viewableTable.updateTableView(screenTable, window);
        setActiveButtonColour(activeButton);
        removeDisplayTextOnWindow();
    }

    private void resetTableViewIfServerUnavailable() {
        viewableTable.updateTableView(null, window);
        serverAliveVerifier.submit(this::checkIfServerAlive);
        showDisplayTextOnWindow(String.format("Server with IP %s:%d currently not available - retrying every 3 seconds...", serverName, serverPort), Color.RED);
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
        setActiveButtonColour(show_rooms_button);
        return new Object[]{data, columnNames};
    }

    private void showDisplayTextOnWindow(String text, Color color) {
        removeDisplayTextOnWindow();
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
    private void setActiveButtonColour(JButton active) {
        // Set button colour
        active.setBackground(Color.ORANGE);
        // Other buttons disable
        JButton [] buttons = new JButton[]{show_rooms_button, test_button, L221_button, XG14_button, T101_button, CG04_button};
        for (JButton button : buttons) {
            if (button != active) {
                button.setBackground(Color.lightGray);
            }
        }
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
