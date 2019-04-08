import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BookingWindow extends JFrame {

    private JButton show_rooms_button;

    private JButton L221_button;
    private JButton XG14_button;
    private JButton T101_button;
    private JButton CG04_button;

    private JButton test_button;
    private JButton help_button;

    private JPanel menu;
    private JPanel window;
    private RestClient restClient;
    private TableView viewableTable;

    public BookingWindow(int sizeX, int sizeY) {
        super("University Room Booking System");

        // Spawn the RestClient before anything happens - more efficient for loading.
        try {
            this.setSize(sizeX, sizeY);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);

            restClient = new RestClient();
        }
        finally {
            this.menu = new JPanel(new BorderLayout());
            this.window = new JPanel(new BorderLayout());
            this.viewableTable = new TableView();
            this.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.lightGray));
            this.setVisible(true);
        }
    }

    public void initialize() {
        createMenuButtons();
        createCentreWindow();
        buildWindowPanels();
        initializeActionListeners();
    }

    private void buildWindowPanels() {
        this.getContentPane().add(menu, BorderLayout.WEST);
        this.getContentPane().add(window, BorderLayout.EAST);
    }

    private void createCentreWindow() {
        window.setLayout ( new BorderLayout() );
        window.setPreferredSize(new Dimension(675, 40));
        window.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));
    }

    private void createMenuButtons(){
        JPanel buttons = new JPanel(new GridLayout(0, 1, 50, 20));
        show_rooms_button = new JButton("All Rooms");
        L221_button = new JButton("L221");
        XG14_button = new JButton("XG14");
        T101_button = new JButton("T101");
        CG04_button = new JButton("CG04");
        test_button = new JButton("Test Mode");
        help_button = new JButton("Help");
        JButton [] design_buttons = {show_rooms_button, L221_button, XG14_button, T101_button, CG04_button,
                test_button, help_button};

        for (JButton button : design_buttons) {
            button.setBackground(Color.lightGray);
            button.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));
            buttons.add(button);
        }

        menu.add(buttons, BorderLayout.NORTH);
    }

    // Note: JTable requires the rooms be a 2D array, hence the bit below.
    // Todo: Put other button action listeners here so they're all in the same space.
    private void initializeActionListeners() {

        // Show All Rooms Button Listener
        show_rooms_button.addActionListener(e -> {
            int room_count = 0;
            HashMap<String, HashMap<String, Object>> rooms = restClient.restTemplateGetAllRooms();
            Object[][] data = new Object[rooms.size()][2];
            String[] columnNames = { "Rooms", "Capacity"};

            // Had to remove your ForEach Nice loop thing for the sake of this table
            for(Map.Entry entry : rooms.entrySet()) {
                data[room_count][0] = entry.getKey();
                data[room_count][1] = ((HashMap) entry.getValue()).get("capacity");
                room_count++;

                System.out.println(entry.getKey());
                System.out.println(((HashMap) entry.getValue()).get("capacity"));
            }

            // Set button colour
            setActiveButtonColour(show_rooms_button, L221_button, XG14_button, T101_button, CG04_button, test_button, help_button);

            // Build Table of Rooms
            viewableTable.build_table(data, columnNames, window);
        });

        // Individual Rooms
        L221_button.addActionListener(e -> {
            HashMap<String, Object> rooms = restClient.restTemplateGetRoom("L221");
            JTable screenTable = viewableTable.getNewTableDisplay(rooms);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(L221_button, test_button, XG14_button, T101_button, CG04_button, show_rooms_button, help_button);
        });

        XG14_button.addActionListener(e -> {
            HashMap<String, Object> rooms = restClient.restTemplateGetRoom("XG14");
            JTable screenTable = viewableTable.getNewTableDisplay(rooms);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(XG14_button, L221_button, help_button, T101_button, CG04_button, test_button, show_rooms_button);
        });

        T101_button.addActionListener(e -> {
            HashMap<String, Object> rooms = restClient.restTemplateGetRoom("T101");
            JTable screenTable = viewableTable.getNewTableDisplay(rooms);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(T101_button, L221_button, XG14_button, test_button, CG04_button, show_rooms_button, help_button);
        });

        CG04_button.addActionListener(e -> {
            HashMap<String, Object> rooms = restClient.restTemplateGetRoom("CG04");
            JTable screenTable = viewableTable.getNewTableDisplay(rooms);
            viewableTable.updateTableView(screenTable, window);
            setActiveButtonColour(CG04_button, L221_button, XG14_button, T101_button, help_button, test_button, show_rooms_button);
        });

        // Other Functions
        test_button.addActionListener(e -> {
            viewableTable.updateTableView(null, window);
            setActiveButtonColour(test_button, L221_button, XG14_button, T101_button, CG04_button, show_rooms_button, help_button);
        });

        help_button.addActionListener(e -> {
            viewableTable.updateTableView(null, window);
            setActiveButtonColour(help_button, L221_button, XG14_button, T101_button, CG04_button, test_button, show_rooms_button);
        });

    }

    // Note: Whatever button is passed first will be designated as the active one.
    private void setActiveButtonColour(JButton active, JButton inactiveA, JButton inactiveB,
                                       JButton inactiveC, JButton inactiveD, JButton inactiveE, JButton inactiveF) {
        // Set button colour
        active.setBackground(Color.ORANGE);
        inactiveA.setBackground(Color.lightGray);
        inactiveB.setBackground(Color.lightGray);
        inactiveC.setBackground(Color.lightGray);
        inactiveD.setBackground(Color.lightGray);
        inactiveE.setBackground(Color.lightGray);
        inactiveF.setBackground(Color.lightGray);
    }
}
