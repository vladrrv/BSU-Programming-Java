import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class DateTable extends JTable {
    private TableCellEditor editor;
    private DateTableModel model;
    private TableCellRenderer renderer1, renderer2, renderer3;
    DateTable(int rowCount, int columnCount) {
        model = new DateTableModel(rowCount, columnCount);
        setModel(model);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(true);
        JTextField editorField = new JTextField();
        editor = new DefaultCellEditor(editorField) {
            @Override
            public boolean stopCellEditing() {
                repaint();
                return super.stopCellEditing();
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                editorField.setText(null);
                String formula = model.getFormulaAt(row, column);
                if (formula == null) return editorField;
                editorField.setText(formula);
                return editorField;
            }
        };
        renderer1 = new DefaultTableCellRenderer() {
            @Override
            public Color getBackground() {
                return new Color(225,235,215);
            }

            @Override
            public int getHorizontalAlignment() {
                return JLabel.CENTER;
            }
        };
        renderer2 = new DefaultTableCellRenderer() {
            @Override
            public Color getBackground() {
                return new Color(240,240,240);
            }

            @Override
            public int getHorizontalAlignment() {
                return JLabel.CENTER;
            }
        };
        renderer3 = new DefaultTableCellRenderer() {
            @Override
            public Color getBackground() {
                return new Color(255,255,255);
            }

            @Override
            public int getHorizontalAlignment() {
                return JLabel.CENTER;
            }
        };
    }
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (row == 0 || column == 0) return renderer1;
        if (row % 2 == 0) return renderer2;
        else return renderer3;
    }
    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        return editor;
    }
}