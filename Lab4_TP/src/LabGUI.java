import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegexConstants {
    static final String REG_POS_INT = "[+]?[1-9]\\d*";
    static final String REG_INT = "[+|-]?([1-9]\\d*|0)";
    static final String REG_FLOAT = "[+-]?(((0?\\.\\d*)|([1-9]\\d*\\.\\d*))([e][+-]?\\d+)?)";
    static final String REG_DATE = "(((0[1-9]|[12]\\d|3[01])\\.(0[13578]|1[02])\\.(\\d){4})|((0[1-9]|[12]\\d|30)\\.(0[13456789]|1[012])\\.(\\d){4})|((0[1-9]|1\\d|2[0-8])\\.02\\.(\\d){4})|(29\\.02\\.(\\d){2}([02468][048]|[13579][26])))";
    static final String REG_TIME = "([0-1][0-9]|2[0-3])(:[0-5][0-9]){2}";
    static final String REG_EMAIL = "[A-Za-z0-9-_~!$%&'()*+,.;=:]+@([A-z0-9]+\\.)+[A-z]{2,4}";
}
public class LabGUI {
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField textField;
    @FXML
    private Label label;
    @FXML
    private TextArea textArea;
    @FXML
    private ListView<String> list;

    private ImageView imageYes;
    private ImageView imageNo;
    public void initialize() {
        label.setText("");
        imageYes = new ImageView("yes.png");
        imageNo = new ImageView("no.png");
        String[] data = {"Positive integer", "Integer", "Floating point number", "Date", "Time", "E-mail"};
        for (String s : data) {
            comboBox.getItems().add(s);
        }
    }
    @FXML
    private void textTyped() {
        Pattern pattern;
        String text = textField.getText();
        switch (comboBox.getSelectionModel().getSelectedIndex()) {
        case 0:
            pattern = Pattern.compile(RegexConstants.REG_POS_INT);
            break;
        case 1:
            pattern = Pattern.compile(RegexConstants.REG_INT);
            break;
        case 2:
            pattern = Pattern.compile(RegexConstants.REG_FLOAT);
            break;
        case 3:
            pattern = Pattern.compile(RegexConstants.REG_DATE);
            break;
        case 4:
            pattern = Pattern.compile(RegexConstants.REG_TIME);
            break;
        case 5:
            pattern = Pattern.compile(RegexConstants.REG_EMAIL);
            break;
        default:
            pattern = Pattern.compile("");
        }
        if (pattern.matcher(text).matches()) {
            label.setGraphic(imageYes);
        }
        else {
            label.setGraphic(imageNo);
        }
    }
    @FXML
    private void getDates() {
        list.getItems().clear();
        String text = textArea.getText();
        Matcher m = Pattern.compile("\\b"+RegexConstants.REG_DATE+"\\b").matcher(text);
        while (m.find()) {
            list.getItems().add(m.group());
        }
    }
}
