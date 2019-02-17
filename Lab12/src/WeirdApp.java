import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class WeirdApp extends JFrame {
    private JPanel contentPane;
    private JList listLeft;
    private JList listRight;
    private JButton buttonLeftToRight;
    private JButton buttonRightToLeft;
    private JPanel buttonsPanel;
    private JPanel radioButtonsPanel;

    public WeirdApp() {
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLists();
        setButtons();
        setRadioButtons();
        setMinimumSize(new Dimension(480,250));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void setLists() {
        DefaultListModel<String> listModelLeft = new DefaultListModel<>();
        DefaultListModel<String> listModelRight = new DefaultListModel<>();
        listLeft.setModel(listModelLeft);
        listRight.setModel(listModelRight);
        listModelLeft.addElement("abcdef");
        listModelLeft.addElement("4523543");
        listModelRight.addElement("fhjlgbh");
        listModelRight.addElement("1234545");
        buttonLeftToRight.addActionListener(e -> {
            List<String> dataToTransfer = listLeft.getSelectedValuesList();
            for (String s : dataToTransfer) {
                listModelRight.addElement(s);
                listModelLeft.removeElement(s);
            }
            if (listModelLeft.isEmpty()) {
                buttonLeftToRight.setEnabled(false);
            }
            if (!listModelRight.isEmpty()) {
                buttonRightToLeft.setEnabled(true);
            }
        });
        buttonRightToLeft.addActionListener(e -> {
            List<String> dataToTransfer = listRight.getSelectedValuesList();
            for (String s : dataToTransfer) {
                listModelLeft.addElement(s);
                listModelRight.removeElement(s);
            }
            if (listModelRight.isEmpty()) {
                buttonRightToLeft.setEnabled(false);
            }
            if (!listModelLeft.isEmpty()) {
                buttonLeftToRight.setEnabled(true);
            }
        });
    }
    private void setButtons() {
        buttonsPanel.setLayout(new GridLayout(4,5));
        MouseListener mouseListener = new MouseAdapter() {
            private String text;
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JButton b = (JButton) e.getSource();
                    text = b.getText();
                    b.setText("Clicked!");
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton b = (JButton)e.getSource();
                b.setText(text);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton b = (JButton)e.getSource();
                b.setBackground(Color.ORANGE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                JButton b = (JButton)e.getSource();
                b.setBackground(null);
            }
        };
        for (int i = 0; i < 20; i++) {
            JButton button = new JButton(""+i);
            button.addMouseListener(mouseListener);
            buttonsPanel.add(button);
        }
    }
    private void setRadioButtons() {
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Germany");
        countries.add("USA");
        countries.add("Russia");
        countries.add("China");
        countries.add("France");
        countries.add("UK");

        radioButtonsPanel.setLayout(new GridLayout(3, 2));
        ButtonGroup bg = new ButtonGroup();
        for (String country : countries) {
            JRadioButton radioButton = new JRadioButton(country);
            radioButton.setIcon(new ImageIcon("res/default_"+country+".png"));
            radioButton.setSelectedIcon(new ImageIcon("res/selected_"+country+".png"));
            radioButton.setRolloverIcon(new ImageIcon("res/rollover_"+country+".png"));
            radioButton.setRolloverSelectedIcon(new ImageIcon("res/rollSel_"+country+".png"));
            radioButton.setPressedIcon(new ImageIcon("res/pressed_"+country+".png"));
            bg.add(radioButton);
            radioButtonsPanel.add(radioButton);
        }
    }
}
