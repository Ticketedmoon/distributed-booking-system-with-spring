import javax.swing.*;
import java.awt.*;

public class BookingWindow extends JFrame {

    private JButton display_button;
    private JButton test_button;
    private JButton help_button;

    public BookingWindow(int sizeX, int sizeY) {
        super("University Room Booking System");
        this.setSize(sizeX, sizeY);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    public void initialize() {
        JPanel buttons = new JPanel(new GridLayout(0, 1));
        display_button = new JButton();
        display_button.setSize(200, 200);
        test_button = new JButton();
        help_button = new JButton();

        buttons.add(display_button);
        buttons.add(test_button);
        buttons.add(help_button);

        JPanel right = new JPanel(new BorderLayout());
        right.add(buttons, BorderLayout.NORTH);
        this.add(right, BorderLayout.WEST);
    }
}
