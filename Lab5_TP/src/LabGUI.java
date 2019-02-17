import javax.swing.*;
import java.awt.*;

class LabGUI extends JFrame {
    private LabGUI() {
        setComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 300));
        setTitle("Lab 5");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void setComponents() {
        DateTable table = new DateTable(16,8);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(table, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        new LabGUI();
    }
}
