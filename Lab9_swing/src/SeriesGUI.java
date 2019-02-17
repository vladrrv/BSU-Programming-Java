import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;

public class SeriesGUI extends JFrame {
    private JPanel contentPane;
    private JTextField lInputA;
    private JTextField lInputD;
    private JTextField lInputN;
    private JButton lGenerateButton;
    private JTextArea lOutput;
    private JButton lSumButton;
    private JTextField lSumOutput;
    private JButton lSaveButton;
    private JLabel lErrorLabel;
    private JTextField eInputA;
    private JTextField eInputD;
    private JTextField eInputN;
    private JLabel eErrorLabel;
    private JButton eGenerateButton;
    private JTextArea eOutput;
    private JButton eSumButton;
    private JTextField eSumOutput;
    private JButton eSaveButton;
    private Series series;
    private Linear linear;
    private Exponential exponential;

    public SeriesGUI() {
        setContentPane(contentPane);
        setResizable(false);
        getRootPane().setDefaultButton(lGenerateButton);
        lGenerateButton.addActionListener(e -> generate(e));
        eGenerateButton.addActionListener(e -> generate(e));
        lSumButton.addActionListener(e -> sum(e));
        eSumButton.addActionListener(e -> sum(e));
        lSaveButton.addActionListener(e -> save(e));
        eSaveButton.addActionListener(e -> save(e));
        //lSaveButton.addActionListener(e -> saveLinear());
        //eSaveButton.addActionListener(e -> saveExponential());
        lErrorLabel.setText("");
        eErrorLabel.setText("");
        lErrorLabel.setForeground(Color.red);
        eErrorLabel.setForeground(Color.red);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    private void generate(ActionEvent e) {
        JTextArea output;
        JLabel errorLabel;
        JTextField inputA;
        JTextField inputD;
        JTextField inputN;
        boolean isLinear;
        if (e.getSource() == lGenerateButton) {
            isLinear = true;
            output = lOutput;
            errorLabel = lErrorLabel;
            inputA = lInputA;
            inputD = lInputD;
            inputN = lInputN;
        }
        else {
            isLinear = false;
            output = eOutput;
            errorLabel = eErrorLabel;
            inputA = eInputA;
            inputD = eInputD;
            inputN = eInputN;
        }
        String a0 = inputA.getText();
        String d = inputD.getText();
        int n;
        try {
            n = Integer.parseInt(inputN.getText());
            if (n <= 0) throw new NumberFormatException();
            errorLabel.setText("");
            series = isLinear?
                    new Linear(Double.parseDouble(a0), Double.parseDouble(d), n) :
                    new Exponential(Double.parseDouble(a0), Double.parseDouble(d), n);
            output.setText(series.toString());
        }
        catch (NumberFormatException ex) {
            errorLabel.setText("Invalid input!");
        }
    }
    private void sum(ActionEvent e) {
        JTextField sumOutput;
        if (e.getSource() == lSumButton) {
            sumOutput = lSumOutput;
        }
        else {
            sumOutput = eSumOutput;
        }
        if (series != null) {
            sumOutput.setText(String.valueOf(series.getSum()));
        }
    }
    private void save(ActionEvent e) {
        if (series != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Series to File");
            fileChooser.showSaveDialog(contentPane);
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                try {
                    series.toFile(file.getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

}
