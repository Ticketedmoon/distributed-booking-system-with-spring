import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BookingWindow extends JFrame {

    private JButton show_rooms_button;
    private JButton test_button;
    private JButton help_button;

    private JPanel menu;
    private JPanel window;
    private RestClient restClient;

    private String[] columnNames = { "Rooms", "Capacity"};

    public BookingWindow(int sizeX, int sizeY) {
        super("University Room Booking System");

        // Spawn the RestClient before anything happens - more efficient for loading.
        try {
            restClient = new RestClient();
        }
        finally {
            this.setSize(sizeX, sizeY);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);

            this.menu = new JPanel(new BorderLayout());
            this.window = new JPanel(new BorderLayout());
            getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.lightGray));
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
        test_button = new JButton("Test Mode");
        help_button = new JButton("Help");
        JButton [] design_buttons = {show_rooms_button, test_button, help_button};

        for (JButton button : design_buttons) {
            button.setBackground(Color.lightGray);
            button.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));
        }

        buttons.add(show_rooms_button);
        buttons.add(test_button);
        buttons.add(help_button);

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

            // Had to remove your ForEach Nice loop thing for the sake of this table
            for(Map.Entry entry : rooms.entrySet()) {
                data[room_count][0] = entry.getKey();
                data[room_count][1] = ((HashMap) entry.getValue()).get("capacity");
                room_count++;

                System.out.println(entry.getKey());
                System.out.println(((HashMap) entry.getValue()).get("capacity"));
            }

            // Set button colour
            setActiveButtonColour(show_rooms_button, test_button, help_button);

            // Build Table of Rooms
            build_room_table(data);
        });

        test_button.addActionListener(e -> {
            setActiveButtonColour(test_button, show_rooms_button, help_button);
        });

        help_button.addActionListener(e -> {
            setActiveButtonColour(help_button, test_button, show_rooms_button);
        });

    }

    private void build_room_table(Object[][] data) {
        //create table with data
        JTable table = new JTable(data, columnNames);

        //add the table to the frame
        window.add(new JScrollPane(table));
        setVisible(true);

    }

    // Note: Whatever button is passed first will be designated as the active one.
    private void setActiveButtonColour(JButton active, JButton inactiveA, JButton inactiveB) {
        // Set button colour
        active.setBackground(Color.ORANGE);
        inactiveA.setBackground(Color.lightGray);
        inactiveB.setBackground(Color.lightGray);
    }

}
