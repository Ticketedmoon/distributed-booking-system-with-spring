import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BookingWindow extends JFrame {

    private JButton display_button;
    private JButton test_button;
    private JButton help_button;

    private JPanel menu = new JPanel();
    private JPanel window = new JPanel();


    public BookingWindow(int sizeX, int sizeY) {
        super("University Room Booking System");
        this.setSize(sizeX, sizeY);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.menu = new JPanel(new BorderLayout());
        this.window = new JPanel(new BorderLayout());

        getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.lightGray));
    }

    public void initialize() {
        create_menu_buttons();
        create_centre_window();
        build_panels();
    }

    private void build_panels() {
        this.getContentPane().add(menu, BorderLayout.WEST);
        this.getContentPane().add(window, BorderLayout.EAST);
    }

    private void create_centre_window() {
        window.setLayout ( new BorderLayout() );
        window.setPreferredSize(new Dimension(675, 40));
        window.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));

    }

    private void create_menu_buttons(){
        JPanel buttons = new JPanel(new GridLayout(0, 1, 50, 20));
        display_button = new JButton("All Rooms");
        test_button = new JButton("Test Mode");
        help_button = new JButton("Help");
        JButton [] design_buttons = {display_button, test_button, help_button};

        for (JButton button : design_buttons) {
            button.setBackground(Color.lightGray);
            button.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));
        }

        buttons.add(display_button);
        buttons.add(test_button);
        buttons.add(help_button);

        menu.add(buttons, BorderLayout.NORTH);

        display_button.addActionListener(e -> {
            RestClient restClient = new RestClient();
            HashMap<String, HashMap<String, Object>> rooms = restClient.restTemplateGetAllRooms();
            rooms.forEach((k,v) -> {
                System.out.println(k);
                System.out.println(v.get("capacity"));
            });
            //TODO logic to add new buttons corresponding to rooms and their capacity
        });
    }

}
