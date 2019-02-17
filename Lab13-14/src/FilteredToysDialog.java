import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FilteredToysDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonClose;
    private JTable table;

    public FilteredToysDialog(ToyList toyList) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Filtered Toys");
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonClose);
        buttonClose.addActionListener(e -> dispose());

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Name");
        model.addColumn("Cost");
        model.addColumn("Age Bounds");
        table.setModel(model);
        if (toyList != null) {
            for (Toy toy : toyList) {
                model.addRow(toy.getData());
            }
        }
        setVisible(true);
    }

}
