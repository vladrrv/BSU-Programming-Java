import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ShapeDemo extends JFrame {
    private JTabbedPane tabbedPanel;
    private JPanel panelMain;
    private JMenu menuFile;
    private JMenu menuData;
    private JMenuBar menuBar;
    private JList<String> circleList;
    private JList<String> circleByAreaList;
    private ArrayList<Circle> list;
    private ArrayList<Pair<Point, Point>> set;
    public ShapeDemo() throws HeadlessException {
        setComponents();
        list = new ArrayList<>();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setListeners();
        //list = new ArrayList<>();
        setMinimumSize(new Dimension(500,300));
        setTitle("Demo");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void setComponents() {
        panelMain = new JPanel(new GridLayout(2, 1));
        setContentPane(panelMain);
        tabbedPanel = new JTabbedPane();
        menuBar = new JMenuBar();
        menuData = new JMenu("Data");
        menuFile = new JMenu("File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem fromCorner = new JMenuItem("From Corner");
        JMenuItem onArea = new JMenuItem("On Area");
        JMenuItem points = new JMenuItem("Points");
        JMenuItem search = new JMenuItem("Search");
        JMenuItem count = new JMenuItem("Count");
        openFile.addActionListener(e->open());

        fromCorner.addActionListener(e->showList());
        onArea.addActionListener(e->showListByArea());
        points.addActionListener(e->showPoints());
        search.addActionListener(e->showSearch());
        count.addActionListener(e->count());
        menuData.add(fromCorner);
        menuData.add(onArea);
        menuData.add(points);
        menuData.add(search);
        menuData.add(count);
        menuFile.add(openFile);
        menuBar.add(menuFile);
        menuBar.add(menuData);
        panelMain.add(menuBar);
        panelMain.add(tabbedPanel);

    }
    private void showList() {

        JPanel circleListPanel = new JPanel();;
        tabbedPanel.addTab("Circle list", circleListPanel);
        Collections.sort(list);
        DefaultListModel<String> model = new DefaultListModel<>();
        circleList = new JList<>(model);
        for (Circle c : list) {
            model.addElement(c.toString());
        }
        circleListPanel.add(circleList);
    }
    private void showListByArea() {
        Collections.sort(list, Comparator.comparingDouble(Circle::area));
        JPanel circleAreaPanel = new JPanel();
        DefaultListModel<String> model = new DefaultListModel<>();
        circleByAreaList = new JList<>(model);
        for (Circle c : list) {
            model.addElement(c.toString());
        }
        circleAreaPanel.add(circleByAreaList);
        tabbedPanel.addTab("By area", circleAreaPanel);
    }
    private void makePointsList() {
        set = new ArrayList<>();
        for (Circle c : list) {
            set.add(new Pair<>(c.getP1(),c.getP2()));
        }

    }
    private ArrayList<Point> allPoints() {
        ArrayList<Point> l = new ArrayList<>();
        for (Circle c : list) {
            l.add(c.getP1());
            l.add(c.getP2());
        }
        return l;
    }
    private void showPoints() {
        JList<String> pointsList;
        JPanel pointsPanel = new JPanel();
        tabbedPanel.addTab("Points", pointsPanel);
        makePointsList();
        DefaultListModel<String> model = new DefaultListModel<>();
        pointsList = new JList<>(model);
        for (Pair<Point, Point> c : set) {
            model.addElement(c.getKey().toString()+ "\n" +c.getValue().toString());
        }
        pointsPanel.add(pointsList);

    }
    private void showSearch() {
        double maxArea = Collections.max(list, Comparator.comparingDouble(Circle::area)).area();
        Circle found = null;
        for (Circle c : list) {
            if (c.area()*2 == maxArea)
                found = c;
        }
        if (found != null) {
            JOptionPane.showMessageDialog(this,
                    "Found circle: " + found.toString(),
                    "Info", JOptionPane.PLAIN_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Not Found! ",
                    "Info", JOptionPane.PLAIN_MESSAGE);

        }
    }
    private void count() {
        HashMap<Point, Integer> map = new HashMap<>();

        Integer x = Integer.parseInt(JOptionPane.showInputDialog("Enter x"));
        Integer y = Integer.parseInt(JOptionPane.showInputDialog("Enter y"));
        Point p = new Point(x, y);
        ArrayList<Point> l = allPoints();
        for (Point pt : l) {
            map.put(pt, Collections.frequency(l, pt) );
        }
        if (map.containsKey(p)) {
            JOptionPane.showMessageDialog(this,
                    "Points Number: " + map.get(p),
                    "Info", JOptionPane.PLAIN_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "No points!",
                    "Info", JOptionPane.PLAIN_MESSAGE);
        }

    }
    private ArrayList<Circle> readXML(File file) throws IOException, SAXException, ParserConfigurationException {
        ArrayList<Circle> newList = new ArrayList<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("circle");

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String
                        point1X = element.getElementsByTagName("point1_x").item(0).getTextContent(),
                        point1Y = element.getElementsByTagName("point1_y").item(0).getTextContent(),
                        point2X = element.getElementsByTagName("point2_x").item(0).getTextContent(),
                        point2Y = element.getElementsByTagName("point2_y").item(0).getTextContent();
                Circle circle = new Circle(new Point(Integer.parseInt(point1X), Integer.parseInt(point1Y)),
                        new Point(Integer.parseInt(point2X), Integer.parseInt(point2Y)));
                newList.add(circle);
            }
        }
        return newList;
    }
    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file == null) return;
        try {
            list = readXML(file);
            JOptionPane.showMessageDialog(this,
                    "Total Number: " + list.size(),
                    "Info", JOptionPane.PLAIN_MESSAGE);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setListeners() {/*
        openButton.addActionListener(e->open());
        saveButton.addActionListener(e->save());
        addButton.addActionListener(e-> addItem());
        infoButton.addActionListener(e->info());*/
    }
}
