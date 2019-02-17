import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ToysDemo extends JFrame {
    private JPanel contentPane;
    private JButton openButton;
    private JButton saveButton;
    private JButton addToyButton;
    private JButton filterButton;
    private JSpinner spinnerUpper;
    private JSpinner spinnerLower;
    private JTable table;
    private DefaultTableModel tableModel;
    private ToyList toyList;
    public ToysDemo() {
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setListeners();
        toyList = new ToyList();
        setTable();
        setMinimumSize(new Dimension(500,300));
        setTitle("Toys Demo");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void setTable() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        tableModel.addColumn("Name");
        tableModel.addColumn("Cost");
        tableModel.addColumn("Age Bounds");
    }
    private void setListeners() {
        openButton.addActionListener(e->open());
        saveButton.addActionListener(e->save());
        addToyButton.addActionListener(e->addToy());
        filterButton.addActionListener(e->filter());
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
    }
    private void updateTable() {
        setTable();
        if (toyList != null) {
            for (Toy toy : toyList) {
                tableModel.addRow(toy.getData());
            }
        }
    }
    private void addToy() {
        AddToyDialog dialog = new AddToyDialog();
        if (!dialog.isSet()) return;
        if (toyList == null) toyList = new ToyList();
        Toy toy = dialog.getToy();
        toyList.add(toy);
        tableModel.addRow(toy.getData());
    }
    private void filter() {
        int lower = (int)spinnerLower.getValue();
        int upper = (int)spinnerUpper.getValue();
        if (toyList != null && !toyList.isEmpty()) {
            ToyList filtered = toyList.selectToysInAgeBounds(lower, upper);
            new FilteredToysDialog(filtered);
        }
    }
    private void manualXMLGeneration(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.append("<toys>");
            for (Toy toy : toyList) {
                toy.writeSelf(writer);
            }
            writer.append("</toys>");
            writer.close();
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "File Write Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void autoXMLGeneration(File file) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("toys");
            doc.appendChild(rootElement);
            for (Toy toy : toyList) {
                Element t = doc.createElement("toy");
                Element name = doc.createElement("name");
                name.setTextContent(toy.getName());
                t.appendChild(name);
                Element cost = doc.createElement("cost");
                cost.setTextContent(String.valueOf(toy.getCost()));
                t.appendChild(cost);
                Element upper = doc.createElement("upper");
                upper.setTextContent(String.valueOf(toy.getUpperAgeBound()));
                t.appendChild(upper);
                Element lower = doc.createElement("lower");
                lower.setTextContent(String.valueOf(toy.getLowerAgeBound()));
                t.appendChild(lower);
                rootElement.appendChild(t);
            }
            //write the content into xml file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result =  new StreamResult(file);
            transformer.transform(source, result);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "File Write Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void save() {
        if (toyList == null) return;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Toys to File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.showSaveDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file != null) {
            //autoXMLGeneration(file);
            manualXMLGeneration(file);
        }
    }
    private ToyList readText(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        ToyList newToyList = new ToyList();
        while (scanner.hasNextLine()) {
            String name = scanner.next();
            int cost = scanner.nextInt();
            int upper = scanner.nextInt();
            int lower = scanner.nextInt();
            newToyList.add(new Toy(name, cost, lower, upper));
        }
        return newToyList;
    }
    private ToyList readXML(File file) throws IOException, SAXException, ParserConfigurationException {
        ToyList newToyList = new ToyList();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("toy");

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String
                        name = element.getElementsByTagName("name").item(0).getTextContent(),
                        cost = element.getElementsByTagName("cost").item(0).getTextContent(),
                        upper = element.getElementsByTagName("upper").item(0).getTextContent(),
                        lower = element.getElementsByTagName("lower").item(0).getTextContent();
                Toy toy = new Toy(name, Integer.parseInt(cost), Integer.parseInt(lower), Integer.parseInt(upper));
                newToyList.add(toy);
            }
        }
        return newToyList;
    }
    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file == null) return;
        try {
            String filename = file.getName();
            switch (filename.substring(filename.length()-3)) {
                case "txt": toyList = readText(file); break;
                case "xml": toyList = readXML(file); break;
            }
            updateTable();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}