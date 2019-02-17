import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SetCanvasSizeDialog extends JDialog {
    private JPanel contentPane;
    private JSpinner spinnerWidth;
    private JSpinner spinnerHeight;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel widthLabel;
    private JLabel heightLabel;
    private int height;
    private int width;
    private boolean isSet;
    SetCanvasSizeDialog(Component parent) {
        setContentPane(contentPane);
        isSet = false;
        setTitle("Set Canvas Size");
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(createButton);
        createButton.addActionListener(e -> create());
        cancelButton.addActionListener(e -> dispose());
        spinnerWidth.addChangeListener(e -> check());
        spinnerHeight.addChangeListener(e -> check());
        spinnerHeight.setValue(300);
        spinnerWidth.setValue(500);
        try {
            widthLabel.setIcon(
                    new ImageIcon(ImageIO.read(
                            new File("resources/LeftRight.png"))));
            heightLabel.setIcon(
                    new ImageIcon(ImageIO.read(
                            new File("resources/UpDown.png"))));
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Resources Error", JOptionPane.ERROR_MESSAGE);
        }
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    private void check() {
        int heightValue = (int)spinnerHeight.getValue();
        int widthValue = (int)spinnerWidth.getValue();
        if (heightValue <= 0 || widthValue <= 0 || heightValue*widthValue <= 0) {
            createButton.setEnabled(false);
        }
        else {
            createButton.setEnabled(true);
        }
    }
    private void create() {
        height = (int) spinnerHeight.getValue();
        width = (int) spinnerWidth.getValue();
        isSet = true;
        dispose();
    }
    public boolean isSet() {
        return isSet;
    }
    public int getInputHeight() {
        return height;
    }
    public int getInputWidth() {
        return width;
    }
}
