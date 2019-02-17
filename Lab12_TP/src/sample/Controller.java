package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private class MyHandler extends DefaultHandler {
        private double average, total;
        int count;
        private boolean isPrice, isQuantity;

        @Override
        public void startDocument() {
            average = 0;
            total = 0;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals("price")) {
                isPrice = true;
            }
            if (qName.equals("quantity")) {
                isQuantity = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (isPrice) {
                total += Double.valueOf(new String(ch, start, length));
                ++count;
                isPrice = false;
            }
            if (isQuantity) {
                count += Integer.valueOf(new String(ch, start, length));
                isQuantity = false;
            }
        }

        @Override
        public void endDocument() {
            average = total / count;
        }

        double getAverage() {
            return average;
        }

        double getTotal() {
            return total;
        }

        int getCount() {
            return count;
        }
    }

    @FXML private Button saveButton;
    @FXML private CheckBox checkBox;
    @FXML private RadioButton rbBinary;
    @FXML private RadioButton rbDOM;
    @FXML private RadioButton rbSAX;
    @FXML private TableView<ShipItem> tableView;
    @FXML private TableColumn<ShipItem, String> titleColumn;
    @FXML private TableColumn<ShipItem, Integer> quantityColumn;
    @FXML private TableColumn<ShipItem, Double> priceColumn;
    @FXML private TextField tfID;
    @FXML private TextField tfShipTo;
    @FXML private TextField tfAddress;
    @FXML private TextField tfCity;
    @FXML private TextField tfCountry;
    private FileChooser fcOpenSave, fcExport;
    private ShipOrder shipOrder;
    private Validator validator;
    private Transformer transformerTXT, transformerHTML;

    @FXML private void initialize() {
        try {
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(getClass().getResource("shiporder.xsd"));
            validator = schema.newValidator();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerTXT = transformerFactory.newTransformer(new StreamSource(getClass().getResource("transformTXT.xslt").toString()));
            transformerHTML = transformerFactory.newTransformer(new StreamSource(getClass().getResource("transformHTML.xslt").toString()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        shipOrder = new ShipOrder();
        fcOpenSave = new FileChooser();
        fcOpenSave.setTitle("Select File");
        fcOpenSave.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("Binary Files", "*.dat"));
        fcExport = new FileChooser();
        fcExport.setTitle("Export XML");
        fcExport.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(t-> {
            ShipItem item = tableView.getSelectionModel().getSelectedItem();
            item.setTitle(t.getNewValue());
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Integer object) {
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                return Integer.valueOf(string);
            }
        }));
        quantityColumn.setOnEditCommit(t-> {
            ShipItem item = tableView.getSelectionModel().getSelectedItem();
            item.setQuantity(t.getNewValue());
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string);
            }
        }));
        priceColumn.setOnEditCommit(t-> {
            ShipItem item = tableView.getSelectionModel().getSelectedItem();
            item.setPrice(t.getNewValue());
        });
    }

    @FXML private void add() {
        ShipItem item = new ShipItem("", 0, 0);
        shipOrder.getList().add(item);
        tableView.getItems().add(item);
    }
    @FXML private void remove() {
        ShipItem item = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(item);
        shipOrder.getList().remove(item);
    }

    @FXML private void toggle() {
        if (rbSAX.isSelected()) {
            saveButton.setDisable(true);
        }
        else {
            saveButton.setDisable(false);
        }
        if (rbBinary.isSelected()) {
            checkBox.setSelected(false);
            checkBox.setDisable(true);
        } else {
            checkBox.setDisable(false);
        }
    }

    @FXML private void setOrderID() {
        shipOrder.setId(Integer.valueOf(tfID.getText()));
    }
    @FXML private void setShipTo() {
        shipOrder.setShipTo(tfShipTo.getText());
    }
    @FXML private void setAddress() {
        shipOrder.setAddress(tfAddress.getText());
    }
    @FXML private void setCity() {
        shipOrder.setCity(tfCity.getText());
    }
    @FXML private void setCountry() {
        shipOrder.setCountry(tfCountry.getText());
    }

    @FXML private void open() {
        File file = fcOpenSave.showOpenDialog(tableView.getScene().getWindow());
        if (file == null) return;
        if (rbDOM.isSelected())
            openDOM(file);
        else if (rbSAX.isSelected())
            openSAX(file);
        else if (rbBinary.isSelected())
            openBinary(file);
    }

    @FXML private void save() {
        File file = fcOpenSave.showSaveDialog(tableView.getScene().getWindow());
        if (file == null) return;
        if (rbDOM.isSelected())
            saveDOM(file);
        else if (rbBinary.isSelected())
            saveBinary(file);
    }

    @FXML private void convert() {
        File sourceFile = fcOpenSave.showOpenDialog(tableView.getScene().getWindow());
        if (sourceFile == null) return;
        if (checkBox.isSelected() && !validate(sourceFile)) return;
        Source source = new StreamSource(sourceFile);
        File destinationFile = fcExport.showSaveDialog(tableView.getScene().getWindow());
        if (destinationFile == null) return;
        try {
            if (fcExport.getSelectedExtensionFilter().getDescription().equals("Text Files")) {
                transformerTXT.transform(source, new StreamResult(destinationFile));
            } else {
                transformerHTML.transform(source, new StreamResult(destinationFile));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateView() {
        tableView.getItems().clear();
        for (ShipItem item : shipOrder.getList()) {
            tableView.getItems().add(item);
        }
        String
                id = String.valueOf(shipOrder.getId()),
                shipto = shipOrder.getShipTo(),
                address = shipOrder.getAddress(),
                city = shipOrder.getCity(),
                country = shipOrder.getCountry();
        tfID.setText(id);
        tfShipTo.setText(shipto);
        tfAddress.setText(address);
        tfCity.setText(city);
        tfCountry.setText(country);
    }

    private boolean validate(File file) {
        try {
            Source source = new StreamSource(file);
            validator.validate(source);
            return true;
        } catch (IOException | SAXException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Invalid file");
            alert.setHeaderText(ex.getClass().getName());
            alert.showAndWait();
            return false;
        }
    }
    private void openDOM(File file) {
        if (checkBox.isSelected() && !validate(file)) return;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = docFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            Element root = doc.getDocumentElement();
            root.normalize();
            List<ShipItem> list = new ArrayList<>();
            String
                    id = root.getAttribute("orderid"),
                    shipto = root.getAttribute("shipto"),
                    address = root.getAttribute("address"),
                    city = root.getAttribute("city"),
                    country = root.getAttribute("country");
            shipOrder = new ShipOrder(list, Integer.valueOf(id), shipto, address, city, country);
            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String
                            title = element.getElementsByTagName("title").item(0).getTextContent(),
                            quantity = element.getElementsByTagName("quantity").item(0).getTextContent(),
                            price = element.getElementsByTagName("price").item(0).getTextContent();
                    list.add(new ShipItem(title, Integer.valueOf(quantity), Double.valueOf(price)));
                }
            }
            updateView();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void saveDOM(File file) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = shipOrder.createElement(doc);
            for (ShipItem item : shipOrder.getList()) {
                Element element = item.createElement(doc);
                root.appendChild(element);
            }
            doc.appendChild(root);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result;
            result = new StreamResult(file);
            transformer.transform(source, result);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openBinary(File file) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            shipOrder = (ShipOrder) ois.readObject();
            updateView();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void saveBinary(File file) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(shipOrder);
            outputStream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openSAX(File file) {
        if (checkBox.isSelected() && !validate(file)) return;
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = saxParserFactory.newSAXParser();
            MyHandler handler = new MyHandler();
            parser.parse(file, handler);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Count: "+handler.getCount()+
                    "\nTotal Sum: "+handler.getTotal()+
                    "\nAverage: "+handler.getAverage(), ButtonType.OK);
            alert.setTitle("Info");
            alert.setHeaderText("Some Information");
            alert.showAndWait();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

