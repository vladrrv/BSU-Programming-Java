import javax.swing.*;
import java.awt.event.*;

public class AddToyDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField;
    private JSpinner spinnerCost;
    private JSpinner spinnerLower;
    private JSpinner spinnerUpper;

    private boolean isSet;
    private String name;
    private int cost;
    private int lower;
    private int upper;

    public AddToyDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Add Toy");
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                boolean isEmpty = textField.getText().isEmpty();
                if (buttonOK.isEnabled() == isEmpty) {
                    buttonOK.setEnabled(!isEmpty);
                }
            }
        });
        spinnerCost.addChangeListener(e -> {
            int value = (int)spinnerCost.getValue();
            if (value < 0) {
                spinnerCost.setValue(0);
            }
        });
        spinnerLower.addChangeListener(e -> {
            int valueLower = (int)spinnerLower.getValue();
            int valueUpper = (int)spinnerUpper.getValue();
            if (valueLower < 0) {
                spinnerLower.setValue(0);
            }
            if (valueLower > valueUpper) {
                spinnerUpper.setValue(valueLower);
            }
        });
        spinnerUpper.addChangeListener(e -> {
            int valueLower = (int)spinnerLower.getValue();
            int valueUpper = (int)spinnerUpper.getValue();
            if (valueUpper < 0) {
                spinnerLower.setValue(0);
            }
            if (valueLower > valueUpper) {
                spinnerLower.setValue(valueUpper);
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onOK() {
        name = textField.getText();
        if (name.isEmpty()) name = "--";
        cost = (int) spinnerCost.getValue();
        lower = (int) spinnerLower.getValue();
        upper = (int) spinnerUpper.getValue();
        isSet = true;
        dispose();
    }
    private void onCancel() {
        isSet = false;
        dispose();
    }
    public Toy getToy() {
        return new Toy(name, cost, lower, upper);
    }
    public boolean isSet() {
        return isSet;
    }
}
