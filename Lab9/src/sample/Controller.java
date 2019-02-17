package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.File;
import java.io.IOException;

public class Controller {
    private Series linear;
    private Series exponential;
    @FXML
    public TabPane pane;
    @FXML
    public TextArea outputL;
    @FXML
    public TextArea outputE;
    @FXML
    public TextField nL;
    @FXML
    public TextField nE;
    @FXML
    public TextField inputA0;
    @FXML
    public TextField inputD;
    @FXML
    public TextField inputB0;
    @FXML
    public TextField inputQ;
    @FXML
    public TextField sumL;
    @FXML
    public TextField sumE;
    @FXML
    public Label errorL;
    @FXML
    public Label errorE;
    public void initialize() {
        errorL.setText("");
        errorE.setText("");
    }
    public void generateLinear() {
        String a0 = inputA0.getCharacters().toString();
        String d = inputD.getCharacters().toString();
        int n;
        try { n = Integer.parseInt(nL.getCharacters().toString()); }
        catch (NumberFormatException e) { n = 5; }
        try {
            errorL.setText("");
            linear = new Linear(Double.parseDouble(a0), Double.parseDouble(d), n);
            outputL.setText(linear.toString());
        }
        catch (NumberFormatException e) {
            errorL.setText("Invalid input!");
        }
    }
    public void generateExponential() {
        String b0 = inputB0.getCharacters().toString();
        String q = inputQ.getCharacters().toString();
        int n;
        try { n = Integer.parseInt(nE.getCharacters().toString()); }
        catch (NumberFormatException e) { n = 5; }
        try {
            errorE.setText("");
            exponential = new Exponential(Double.parseDouble(b0), Double.parseDouble(q), n);
            outputE.setText(exponential.toString());
        }
        catch (NumberFormatException e) {
            errorE.setText("Invalid input!");
        }
    }
    public void saveExponential(ActionEvent ae) {
        if (exponential != null) {
            Node source = (Node) ae.getSource();
            Window stage = source.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Series to File");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null && exponential != null) {
                try {
                    exponential.toFile(file.getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    public void saveLinear(ActionEvent ae) {
        if (linear != null) {
            Node source = (Node) ae.getSource();
            Window stage = source.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Series to File");
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    linear.toFile(file.getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    public void sumLinear() {
        if (linear != null) {
            sumL.setText(String.valueOf(linear.getSum()));
        }
    }
    public void sumExponential() {
        if (exponential != null) {
            sumE.setText(String.valueOf(exponential.getSum()));
        }
    }
}
