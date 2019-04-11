import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private HashMap<Integer, ArrayList<Color>> mapOfColours;

    public CustomTableCellRenderer() {
        mapOfColours = new HashMap<>();
        this.buildColourMap();
    }

    private void buildColourMap() {
        for(int row = 0; row < 7; row++) {
            mapOfColours.put(row, new ArrayList<>(7));
            for (int col = 0; col < 7; col++) {
                mapOfColours.get(row).add(Color.GRAY);
            }
        }
    }

    public void setCellColour(int row, int col, Color color) {
        mapOfColours.get(row).set(col, color);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {

        Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, 1);
        Color color = mapOfColours.get(row).get(column);
        if (color != null) {
            cell.setBackground(color);
        } else {
            cell.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        }
        return cell;
    }
}