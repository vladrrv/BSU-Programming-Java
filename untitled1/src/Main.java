import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import static javax.swing.JOptionPane.showMessageDialog;


class Book implements Comparable<Book> {
    String author;
    String name;
    double price;

    public Book(String author, String name, double price) {
        this.author = author;
        this.name = name;
        this.price = price;
    }
    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public int compareTo(Book o) {
        return Comparator.comparing(Book::getAuthor).thenComparing(Book::getName).thenComparingDouble(Book::getPrice).compare(this, o);
    }
}

class Bookstore {
    String address;
    int number;

    public Bookstore() {
    }

    public Bookstore(String address, int number) {
        this.address= address;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Bookstore at " + address+ ", number " + number;
    }
}

class BookstoreNode {
    Object n;
    BookstoreNode(Object o) {
        n = o;
    }

    @Override
    public String toString() {
        return n.toString();
    }
}

class AuthorNode {
    Object n;
    AuthorNode(Object o) {
        n = o;
    }

    @Override
    public String toString() {
        return n.toString();
    }
}
class NameNode {
    Object n;
    NameNode(Object o) {
        n = o;
    }

    @Override
    public String toString() {
        return n.toString();
    }
}
class PriceNode {
    Object n;

    PriceNode(Object o) {
        n = o;
    }

    @Override
    public String toString() {
        return n.toString();
    }
}

class Frame extends JFrame {
    JPanel bookPanel = new JPanel();

    ArrayList<Book> list = new ArrayList<>();
    Bookstore bookstore = new Bookstore();
    DefaultTreeModel model = new DefaultTreeModel(null);
    Frame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800,400));
        setTitle("lab 11");
        setContentPane(bookPanel);
        bookPanel.setLayout(new BorderLayout());
        third();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void third() {
        JButton addButton = new JButton("Add");
        JButton openButton = new JButton("Open file");
        JButton saveButton = new JButton("Save file");
        JTextArea priceArea = new JTextArea(1, 6);
        JTextArea nameArea = new JTextArea(1, 30);
        JTextArea authorArea = new JTextArea(1, 20);
        JLabel priceLabel = new JLabel("Price: ");
        JLabel nameLabel = new JLabel("Name: ");
        JLabel authorLabel = new JLabel("Author: ");
        JPanel addPanel = new JPanel();
        bookPanel.add(addPanel, BorderLayout.SOUTH);
        JPanel servicePanel = new JPanel();
        servicePanel.add(openButton);
        servicePanel.add(saveButton);
        bookPanel.add(servicePanel, BorderLayout.NORTH);
        addPanel.add(addButton);
        addPanel.add(priceLabel);
        addPanel.add(priceArea);
        addPanel.add(nameLabel);
        addPanel.add(nameArea);
        addPanel.add(authorLabel);
        addPanel.add(authorArea);
        JTree tree = new JTree();
        tree.setEditable(true);
        tree.setModel(model);
        model.setRoot( new DefaultMutableTreeNode(new BookstoreNode(bookstore)));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if (((DefaultMutableTreeNode)value).getUserObject() instanceof AuthorNode) {
                    setIcon(new ImageIcon("src/author.png"));
                }
                else if (((DefaultMutableTreeNode)value).getUserObject() instanceof NameNode) {
                    setIcon(new ImageIcon("src/book.jpg"));
                }
                else if (((DefaultMutableTreeNode)value).getUserObject() instanceof PriceNode) {
                    setIcon(new ImageIcon("src/price.png"));
                }
                else if (((DefaultMutableTreeNode)value).getUserObject() instanceof BookstoreNode) {
                    setIcon(new ImageIcon("src/shop.png"));
                }
                return this;
            }
        };
        tree.setCellRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(tree);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String n = priceArea.getText();
                String g = nameArea.getText();
                String y = authorArea.getText();
                for (int i = 0; i < ((DefaultMutableTreeNode)model.getRoot()).getChildCount(); i++) {
                    DefaultMutableTreeNode authorNode = (DefaultMutableTreeNode)(((DefaultMutableTreeNode)model.getRoot()).getChildAt(i));
                    if (authorNode.getUserObject().toString().equals(y)) {
                        for (int j = 0; j < authorNode.getChildCount(); j++) {
                            DefaultMutableTreeNode nameNode = (DefaultMutableTreeNode) authorNode.getChildAt(j);
                            if (nameNode.getUserObject().toString().equals(g)) {
                                DefaultMutableTreeNode priceNode = new DefaultMutableTreeNode(new PriceNode(n));
                                nameNode.add(priceNode);
                                tree.updateUI();
                                tree.setSelectionPath(new TreePath(priceNode.getPath()));
                                return;
                            }
                        }
                        DefaultMutableTreeNode prNode = new DefaultMutableTreeNode(new PriceNode(n));
                        DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode(new NameNode(g));
                        authorNode.add(nameNode);
                        nameNode.add(prNode);
                        tree.updateUI();
                        tree.setSelectionPath(new TreePath(prNode.getPath()));
                        return;
                    }
                }
                DefaultMutableTreeNode authorNode = new DefaultMutableTreeNode(new AuthorNode(y));
                ((DefaultMutableTreeNode)model.getRoot()).add(authorNode);
                DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode(new NameNode(g));
                DefaultMutableTreeNode prNode = new DefaultMutableTreeNode(new PriceNode(n));
                authorNode.add(nameNode);
                nameNode.add(prNode);
                list.add(new Book(y, g, Double.valueOf(n)));
                tree.setSelectionPath(new TreePath(prNode.getPath()));
                tree.updateUI();
                tree.setSelectionPath(new TreePath(prNode.getPath()));
                return;
            }
        });
        JButton remove = new JButton("Remove");
        addPanel.add(remove);
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreePath[] paths = tree.getSelectionPaths();
                for (TreePath path : paths) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if (node.getParent() != null) {
                        model.removeNodeFromParent(node);
                    }
                    else {
                        DefaultMutableTreeNode n = new DefaultMutableTreeNode("EMPTY TREE");
                        model.setRoot(n);
                    }
                }
            }
        });
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                fileopen.setFileFilter(new FileNameExtensionFilter("XML files", "XML"));
                int ret = fileopen.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    read(file);
                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileopen = new JFileChooser();
                    fileopen.setFileFilter(new FileNameExtensionFilter("XML file", "XML"));
                    int ret = fileopen.showSaveDialog(null);
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File file = fileopen.getSelectedFile();
                        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                        Document doc = docBuilder.newDocument();
                        Element root = doc.createElement("bookstore");
                        for (Book book : list) {
                            Element eBook = doc.createElement("book");
                            Element eAuthor = doc.createElement("author");
                            eAuthor.setTextContent(book.getAuthor());
                            Element eName = doc.createElement("name");
                            eName.setTextContent(book.getName());
                            Element ePrice = doc.createElement("price");
                            ePrice.setTextContent(String.valueOf(book.getPrice()));
                            eBook.appendChild(eAuthor);
                            eAuthor.appendChild(eName);
                            eAuthor.appendChild(ePrice);
                        }
                        doc.appendChild(root);
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(file);
                        transformer.transform(source, result);
                    }
                }
                catch (Exception ex) {
                    showMessageDialog(null, ex.getMessage());
                }
            }
        });
        bookPanel.add(scrollPane, BorderLayout.CENTER);
    }
    public void read(File inputFile) {
        try {
            list.clear();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            bookstore = new Bookstore(doc.getDocumentElement().getAttribute("address"), Integer.valueOf(doc.getDocumentElement().getAttribute("number")));
            NodeList nList = doc.getElementsByTagName("book");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    list.add(new Book(eElement.getElementsByTagName("author").item(0).getTextContent(),
                            eElement.getElementsByTagName("name").item(0).getTextContent(), Double.valueOf(eElement.getElementsByTagName("price").item(0).getTextContent())));
                }
            }
            createTree();
        } catch (Exception e) {
            showMessageDialog(null, e.getMessage());
        }
    }

    public void createTree() {
        Collections.sort(list);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new BookstoreNode(bookstore));
        String author = "", name = "";
        DefaultMutableTreeNode authorNode = new DefaultMutableTreeNode();
        DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode();
        for (Book s : list) {
            if (!author.equals(s.getAuthor())) {
                author = s.getAuthor();
                authorNode = new DefaultMutableTreeNode(new AuthorNode(author));
                root.add(authorNode);
                name = "";
            }
            if (!name.equals(s.getName())) {
                name = s.getName();
                nameNode = new DefaultMutableTreeNode(new NameNode(name));
                authorNode.add(nameNode);
            }
            DefaultMutableTreeNode priceNode = new DefaultMutableTreeNode(new PriceNode(s.getPrice()));
            nameNode.add(priceNode);
        }
        model.setRoot(root);
    }

}

public class Main {
    public static void main(String[] args) {
        new Frame();
    }
}