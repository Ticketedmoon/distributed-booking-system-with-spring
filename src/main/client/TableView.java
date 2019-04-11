import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TableView {

    private JTable currentTable;
    private JScrollPane tableView;
    private RestClient restClient;

    // Variables below are used for cohesive UI design and communication of intent to the user.
    private String roomName;
    private String [] timetableDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private CustomTableCellRenderer renderer;
    private JPanel currentWindow;

    public TableView(RestClient restClient) {
        this.restClient = restClient;
        this.renderer = new CustomTableCellRenderer();
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    public JTable getNewTableDisplay(HashMap<String, Object> rooms) {
        ArrayList<HashMap<String, Object>> days =  (ArrayList<HashMap<String, Object>>) rooms.get("days");
        LinkedHashMap<String, LinkedHashMap<String, Integer>> mappingDayToTimes = new LinkedHashMap<>();
        for(HashMap<String, Object> item : days) {
            mappingDayToTimes.put((String) item.get("day"), (LinkedHashMap<String, Integer>) item.get("timeslotCapacity"));
        }

        JTable table = new JTable(toTableModel(mappingDayToTimes));
        for(int i = 0; i < days.size(); i ++)
        {
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        setTableColourByTimeAvailability(table, mappingDayToTimes);
        addTableMouseListener(table);
        return table;
    }

    private void setTableColourByTimeAvailability(JTable table, LinkedHashMap<String, LinkedHashMap<String, Integer>> dayToTimes) {
        ArrayList<LinkedHashMap<String, Integer>> daysToTimesSerialised = new ArrayList<>(dayToTimes.values());
        for (int row = 0; row < daysToTimesSerialised.size(); row++) {
            LinkedHashMap<String, Integer> timesToCapacities = daysToTimesSerialised.get(row);
            Object [] time_periods = timesToCapacities.keySet().toArray();
            for (int col = 0; col < timesToCapacities.size(); col++) {
                int slotCapacity = timesToCapacities.get(time_periods[col]);
                if (slotCapacity > 0) {
                    renderer.setCellColour(col, row, Color.green);
                }
                else {
                    renderer.setCellColour(col, row, Color.red);
                }
                table.getColumnModel().getColumn(row).setCellRenderer(renderer);
            }
        }
        table.updateUI();
    }

    public TableModel toTableModel(LinkedHashMap<String, LinkedHashMap<String, Integer>> map) {
        ObjectMapper oMapper = new ObjectMapper();
        DefaultTableModel model = new DefaultTableModel(map.keySet().toArray(), 5);
        LinkedHashMap[] dayMap = oMapper.convertValue(map.values().toArray(), LinkedHashMap[].class);
        int size = map.values().size();
        for(int col = 0; col < size; col++) {
            Object [] day_times = dayMap[col].keySet().toArray();
            Object [] day_capacities = dayMap[col].values().toArray();
            for(int row = 0; row < size-2; row++) {
                String value = String.format("Time: %s, Slots: %s", day_times[row], day_capacities[row]);
                model.setValueAt(value, row, col);
            }
        }

        return model;
    }

    public void build_table(Object[][] data, Object [] columnNames, JPanel window) {
        //create table with data
        currentTable = new JTable(data, columnNames);
        updateTableView(currentTable, window);
    }

    public void updateTableView(JTable tableOnWindow, JPanel window) {
        if (tableView != null)
            window.remove(tableView);

        if (tableOnWindow != null) {
            tableOnWindow.setGridColor(Color.blue);
            tableOnWindow.setRowSelectionAllowed(false);
            tableOnWindow.setRowHeight(30);
            tableOnWindow.setEnabled(false);
        }

        tableView = new JScrollPane(tableOnWindow);
        window.add(tableView);
        window.updateUI(); // @Shaun - This was the 'flush' method we were looking for
        currentWindow = window;
    }

    /** Do Booking here, if user clicks cell that is green (available) --
        we can call a dialog box to ask if they want to book it...
     */
    private void addTableMouseListener(JTable table) {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                int row = table.rowAtPoint(event.getPoint());
                int col = table.columnAtPoint(event.getPoint());

                System.out.println(String.format("Cell-Row: %d - Cell-Column %d", row, col));

                String timePeriod = ((String) table.getValueAt(row, col)).substring(5).split(",")[0];

                System.out.println(timePeriod);

                // Note: Cell turns orange when selected
                renderer.setCellColour(row, col, Color.white);
                table.getColumnModel().getColumn(col).setCellRenderer(renderer);
                table.updateUI();

                // Update the UI again after the dialog box has closed.
                // Why? Because if there is 1 seat left for some room at some time period, and somebody takes it
                // we want the UI to update and display that time-period in red.
                displayBookingDialogBox(row, col, timePeriod);
                table.updateUI();
            }
        });
    }

    private void displayBookingDialogBox(int row, int col, String timePeriod) {
        int response = JOptionPane.showConfirmDialog(null,
                String.format("Confirm Room Booking for Room %s on %s at %s", roomName, timetableDays[col], timePeriod), "Booking Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Room not booked");
            System.out.println("Room Not Booked - No option selected");
            if(restClient.roomAvailableAtTime(roomName,col,timePeriod))
                renderer.setCellColour(row, col, Color.green);
            else
                renderer.setCellColour(row,col, Color.red);

        } else if (response == JOptionPane.YES_OPTION) {
            if (restClient.roomAvailableAtTime(roomName, col, timePeriod)) {
                restClient.bookRoom(roomName, col, timePeriod);
                renderer.setCellColour(row, col, Color.green);

                JOptionPane.showMessageDialog(null, String.format("Room %s booked on %s for time %s \n ",
                        roomName, timetableDays[col], timePeriod));
                System.out.println(String.format("Room with ID %s Booked on %s for time period %s", roomName, timetableDays[col], timePeriod));

                HashMap<String, Object> room_details = restClient.getRoom(this.roomName);
                JTable table = getNewTableDisplay(room_details);
                updateTableView(table, currentWindow);
            }
            else {
                renderer.setCellColour(row, col, Color.red);
                JOptionPane.showMessageDialog(null, String.format("Room %s is fully booked on day %s at time %s \n " +
                        "Please select another room or time", roomName, timetableDays[col], timePeriod));
            }

            // If after we book the room is full | I.E there are no more slots left, then colour becomes red...
            if (!restClient.roomAvailableAtTime(roomName, col, timePeriod)) {
                System.out.println(String.format("Room with ID %s on %s for time period %s fully booked!", roomName, timetableDays[col], timePeriod));
                renderer.setCellColour(row, col, Color.red);
            }
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.out.println("Dialog Box Closed...");
            renderer.setCellColour(row, col, Color.green);
        }
    }

    public void setRoomName(String name) {
        this.roomName = name;
    }
}
