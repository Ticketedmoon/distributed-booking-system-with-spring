import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TableView {

    private JTable screenTable;
    private JScrollPane tableView;

    public JTable getNewTableDisplay(HashMap<String, Object> rooms) {
        ArrayList<HashMap<String, Object>> days =  (ArrayList<HashMap<String, Object>>) rooms.get("days");
        LinkedHashMap<String, LinkedHashMap<String, Integer>> mappingDayToTimes = new LinkedHashMap<>();
        for(HashMap<String, Object> item : days) {
            mappingDayToTimes.put((String) item.get("day"), (LinkedHashMap<String, Integer>) item.get("timeslotCapacity"));
        }

        return new JTable(toTableModel(mappingDayToTimes));
    }

    public TableModel toTableModel(LinkedHashMap<String, LinkedHashMap<String, Integer>> map) {
        ObjectMapper oMapper = new ObjectMapper();
        DefaultTableModel model = new DefaultTableModel(map.keySet().toArray(), 5);
        LinkedHashMap[] dayMap = oMapper.convertValue(map.values().toArray(), LinkedHashMap[].class);
        int size = map.values().size();
        for(int col = 0; col < size; col++) {
            Object [] day_times = dayMap[col].keySet().toArray();
            for(int row = 0; row < size-2; row++) {
                String value = (String) day_times[row];
                model.setValueAt(value, row, col);
            }
        }
        return model;
    }

    public void build_table(Object[][] data, String [] columnNames, JPanel window) {
        //create table with data
        screenTable = new JTable(data, columnNames);
        updateTableView(screenTable, window);
    }

    public void updateTableView(JTable screenTable, JPanel window) {
        if (tableView != null)
            window.remove(tableView);

        if (screenTable != null)
            screenTable.setGridColor(Color.blue);

        tableView = new JScrollPane(screenTable);
        window.add(tableView);
        window.updateUI(); // @Shaun - This was the 'flush' method we were looking for
    }

}
