import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
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
    private String roomName;

    private CustomTableCellRenderer renderer;

    public TableView(RestClient restClient) {
        this.restClient = restClient;
        this.renderer = new CustomTableCellRenderer();
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    @PostConstruct
    public JTable getNewTableDisplay(HashMap<String, Object> rooms) {
        ArrayList<HashMap<String, Object>> days =  (ArrayList<HashMap<String, Object>>) rooms.get("days");
        LinkedHashMap<String, LinkedHashMap<String, Integer>> mappingDayToTimes = new LinkedHashMap<>();
        for(HashMap<String, Object> item : days) {
            mappingDayToTimes.put((String) item.get("day"), (LinkedHashMap<String, Integer>) item.get("timeslotCapacity"));
        }

        JTable table = new JTable(toTableModel(mappingDayToTimes));

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
                String value = String.format("Time: %s", day_times[row]);
                model.setValueAt(value, row, col);
            }
        }

        return model;
    }

    public void build_table(Object[][] data, String [] columnNames, JPanel window) {
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

                String timePeriod = ((String) table.getValueAt(row, col)).substring(5);
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
                String.format("Confirm Room Booking for Room %s at %s", roomName, timePeriod), "Booking Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            System.out.println("Room Not Booked - No option selected");
            renderer.setCellColour(row, col, Color.green);
        } else if (response == JOptionPane.YES_OPTION) {
            if (restClient.restTemplateRoomAvailableAtTime(roomName, col, timePeriod)) {
                restClient.restTemplateBookRoom(roomName, col, timePeriod);
                renderer.setCellColour(row, col, Color.green);
                System.out.println(String.format("Room with ID %s Booked for time period %s", roomName, timePeriod));
            }

            // If after we book the room is full | I.E there are no more slots left, then colour becomes red...
            if (!restClient.restTemplateRoomAvailableAtTime(roomName, col, timePeriod)) {
                System.out.println(String.format("Room with ID %s for time period %s fully booked!", roomName, timePeriod));
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
