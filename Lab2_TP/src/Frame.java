import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


class Country implements Comparable<Country> {
    private ImageIcon flagIcon;
    private String name;
    private String path;

    Country(String name) {
        this.name = name;
        this.path = "res/plain/flag_"+name+".png";
    }
    Country(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }
    public ImageIcon getFlagIcon() {
        if (flagIcon == null)
            flagIcon = new ImageIcon(path);
        return flagIcon;
    }
    @Override
    public int compareTo(Country o) {
        return name.compareTo(o.name);
    }
    @Override
    public String toString() {
        return name;
    }
}

class Trip {
    private Country country;
    private String description;
    private double price;

    Trip(Country country, String description, double price) {
        this.country = country;
        this.description = description;
        this.price = price;
    }

    public Country getCountry() {
        return country;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public Object[] getData() {
        return new Object[]{country, description, price, false};
    }
}

class Student implements Comparable<Student> {
    private int year;
    private int group;
    private String name;

    Student(int year, int group, String name) {
        this.year = year;
        this.group = group;
        this.name = name;
    }

    public int getYear() {
        return year;
    }
    public int getGroup() {
        return group;
    }
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparingInt(Student::getYear)
                .thenComparingInt(Student::getGroup)
                .thenComparing(Student::getName)
                .compare(this, o);
    }
    @Override
    public String toString() {
        return getName();
    }
}

class FacultyNode extends DefaultMutableTreeNode {
    FacultyNode(Object userObject) {
        super(userObject);
    }
}
class YearNode extends DefaultMutableTreeNode {
    YearNode(Object userObject) {
        super(userObject);
    }
}
class GroupNode extends DefaultMutableTreeNode {
    GroupNode(Object userObject) {
        super(userObject);
    }
}
class StudentNode extends DefaultMutableTreeNode {
    StudentNode(Object userObject) {
        super(userObject);
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }
}


class Frame extends JFrame {
    private JTabbedPane tabbedPanel;
    private JList<Country> countriesList;
    private Map<Country, String> countries;
    private List<Student> students;
    private DefaultMutableTreeNode root;
    private ImageIcon studIcon;
    private ImageIcon grIcon;
    private ImageIcon yIcon;
    Frame() {
        setComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500,300));
        setTitle("Demo");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void generateCountries() {
        countries = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File("countries.txt"));
            String name, capital;
            while (scanner.hasNextLine()) {
                name = scanner.next();
                capital = scanner.next();
                countries.put(new Country(name), capital);
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error while reading file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void generateStudents() {
        studIcon = new ImageIcon("res/icons/male.png");
        grIcon = new ImageIcon("res/icons/address book.png");
        yIcon = new ImageIcon("res/icons/company.png");
        students = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("students.txt"));
            String name;
            int year, group;
            while (scanner.hasNextLine()) {
                name = scanner.next();
                year = scanner.nextInt();
                group = scanner.nextInt();
                students.add(new Student(year, group, name));
            }
        }
        catch (InputMismatchException | IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error while reading file!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setComponents() {
        tabbedPanel = new JTabbedPane();
        setContentPane(tabbedPanel);
        setCountries();
        setTrips();
        setStudents();
    }
    private void setCountries() {
        generateCountries();
        JPanel countriesPanel = new JPanel();
        countriesPanel.setLayout(new BorderLayout());
        tabbedPanel.addTab("Countries", countriesPanel);
        DefaultListModel<Country> listModel = new DefaultListModel<>();
        countriesList = new JList<>(listModel);
        countriesList.setCellRenderer((list, value, index, isSelected, cellHasFocus)-> {
            JLabel label = new JLabel(value.getName(), value.getFlagIcon(), JLabel.LEFT);
            label.setIconTextGap(10);
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            label.setFont(list.getFont());
            label.setEnabled(list.isEnabled());
            if (isSelected && cellHasFocus)
                label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            else
                label.setBorder(null);
            return label;
        });
        JScrollPane scrollPane = new JScrollPane(countriesList);
        countriesPanel.add(scrollPane, BorderLayout.CENTER);
        for (Country country : countries.keySet()) {
            listModel.addElement(country);
        }
        JLabel countryLabel = new JLabel("", SwingConstants.CENTER);
        countriesPanel.add(countryLabel, BorderLayout.NORTH);
        countriesList.addListSelectionListener(e->{
            Country country = countriesList.getSelectedValue();
            countryLabel.setText(country.getName() + ", " + countries.get(country));
            countryLabel.setIcon(country.getFlagIcon());
        });
    }
    private void setTrips() {
        JPanel tripsPanel = new JPanel();
        tripsPanel.setLayout(new BorderLayout());
        tabbedPanel.addTab("Trips", tripsPanel);
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip(new Country("Belarus"), "Country for living", 1000));
        trips.add(new Trip(new Country("USA"), "usa", 10000));

        Object[] columns = {"Country", "Description", "Cost", "Selected"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        for (Trip trip : trips) {
            tableModel.addRow(trip.getData());
        }
        JTable tripsTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return Country.class;
                    case 1: return String.class;
                    case 2: return Double.class;
                    default: return Boolean.class;
                }
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 0) {
                    return new DefaultCellEditor(new JTextField()) {
                        @Override
                        public Object getCellEditorValue() {
                            String name = ((JTextField)this.getComponent()).getText();
                            if (name.isEmpty()) return null;
                            return new Country(name);
                        }
                    };
                }
                return super.getCellEditor(row, column);
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                int lastRowIndex = getRowCount()-1, lastColIndex = getColumnCount()-1;
                if (row == lastRowIndex && column == lastColIndex)
                    return new DefaultTableCellRenderer();
                return super.getCellRenderer(row, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return row != getRowCount()-1;
            }

        };
        tripsTable.setRowHeight(48);
        tripsTable.setDefaultRenderer(Country.class, (table, value, isSelected, hasFocus, row, column)->{
            JLabel label;
            if (value != null) {
                label = new JLabel(((Country) value).getFlagIcon(), JLabel.CENTER);
            }
            else {
                label = new JLabel();
            }
            label.setIconTextGap(10);
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            }
            else {
                label.setBackground(table.getBackground());
                label.setForeground(table.getForeground());
            }
            label.setFont(table.getFont());
            label.setEnabled(table.isEnabled());
            if (isSelected && hasFocus)
                label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            else
                label.setBorder(null);
            return label;
        });
        JScrollPane scrollPane = new JScrollPane(tripsTable);
        tripsPanel.add(scrollPane, BorderLayout.CENTER);
        JButton buttonAdd = new JButton("Add");
        tableModel.addRow(new Object[]{null, "Total", 0.0, null});
        buttonAdd.addActionListener(e->{
            trips.add(new Trip(null, "", 0.0));
            tableModel.insertRow(tableModel.getRowCount()-1, new Object[]{null, "", 0.0, false});
        });
        tripsPanel.add(buttonAdd, BorderLayout.SOUTH);
        tableModel.addTableModelListener(e->{
            int
                    lastRowIndex = tableModel.getRowCount() - 1,
                    row = e.getLastRow(),
                    col = e.getColumn();
            if (col >= 0 && row >= 0 && col < 3 && row < lastRowIndex) {
                Trip toChange = trips.get(row);
                Object toSet = tableModel.getValueAt(row, col);
                switch (col) {
                    case 0: toChange.setCountry((Country)toSet); break;
                    case 1: toChange.setDescription((String)toSet); break;
                    case 2: toChange.setPrice((Double)toSet); break;
                }
            }
            if (e.getColumn() >= 2 && e.getLastRow() < lastRowIndex) {
                double sum = 0;
                for (int i = 0; i < lastRowIndex; i++) {
                    if ((Boolean) (tableModel.getValueAt(i, 3)))
                        sum += (Double) tableModel.getValueAt(i, 2);
                }
                tableModel.setValueAt(sum, lastRowIndex, 2);
            }
        });
    }
    private void setStudents() {
        generateStudents();
        JPanel studentsPanel = new JPanel();
        studentsPanel.setLayout(new BorderLayout());
        tabbedPanel.addTab("Students", studentsPanel);
        root = new FacultyNode("FPMI");
        updateStudents();
        JTree tree = new JTree(root);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                TreePath path = tree.getPathForRow(row);
                DefaultTreeCellRenderer component = (DefaultTreeCellRenderer)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (path == null) return component;
                if (path.getLastPathComponent() instanceof StudentNode) {
                    component.setIcon(studIcon);
                }
                else if (path.getLastPathComponent() instanceof GroupNode) {
                    component.setIcon(grIcon);
                }
                else {
                    component.setIcon(yIcon);
                }
                return component;
            }
        };

        tree.setCellRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(tree);
        studentsPanel.add(scrollPane, BorderLayout.CENTER);
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();

        tree.setEditable(true);
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON_OR_INSERT);

        tree.setTransferHandler(new TreeTransferHandler());
        tree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                    DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (selNode == null) return;
                    if (selNode instanceof GroupNode) {
                        DefaultMutableTreeNode newNode = new StudentNode("New Student");
                        newNode.setAllowsChildren(false);
                        model.insertNodeInto(newNode, selNode, selNode.getChildCount());
                        TreeNode[] nodes = model.getPathToRoot(newNode);
                        TreePath path = new TreePath(nodes);
                        tree.scrollPathToVisible(path);
                        tree.setSelectionPath(path);
                        tree.startEditingAtPath(path);
                    }
                    else if (selNode instanceof YearNode) {
                        DefaultMutableTreeNode newNode = new GroupNode("New Group");
                        newNode.setAllowsChildren(true);
                        model.insertNodeInto(newNode, selNode, selNode.getChildCount());
                        TreeNode[] nodes = model.getPathToRoot(newNode);
                        TreePath path = new TreePath(nodes);
                        tree.scrollPathToVisible(path);
                        tree.setSelectionPath(path);
                        tree.startEditingAtPath(path);
                    }
                    else if (!(selNode instanceof StudentNode)) {
                        DefaultMutableTreeNode newNode = new YearNode("New Year");
                        newNode.setAllowsChildren(true);
                        model.insertNodeInto(newNode, selNode, selNode.getChildCount());
                        TreeNode[] nodes = model.getPathToRoot(newNode);
                        TreePath path = new TreePath(nodes);
                        tree.scrollPathToVisible(path);
                        tree.setSelectionPath(path);
                        tree.startEditingAtPath(path);

                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (selNode != null && !(selNode instanceof FacultyNode)) {
                        model.removeNodeFromParent(selNode);
                    }
                    else if (selNode instanceof FacultyNode) {
                        model.setRoot(null);
                    }

                }
            }
        });

    }
    private void updateStudents() {
        Collections.sort(students);
        int curYear = 0, curGroup = 0;
        DefaultMutableTreeNode yearNode = new DefaultMutableTreeNode(), groupNode = new DefaultMutableTreeNode();
        for (Student student : students) {
            DefaultMutableTreeNode studentNode = new StudentNode(student);
            studentNode.setAllowsChildren(false);
            if (curYear != student.getYear()) {
                curYear = student.getYear();
                curGroup = 0;
                yearNode = new YearNode("Year "+curYear);
                root.add(yearNode);
            }
            if (curGroup != student.getGroup()) {
                curGroup = student.getGroup();
                groupNode = new GroupNode("Group "+curGroup);
                yearNode.add(groupNode);
            }
            groupNode.add(studentNode);
        }
    }
}



class TreeTransferHandler extends TransferHandler {
    private DataFlavor nodesFlavor;
    private DataFlavor[] flavors = new DataFlavor[1];
    private DefaultMutableTreeNode[] nodesToRemove;
    TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=\"" +
                    javax.swing.tree.DefaultMutableTreeNode[].class.getName() +
                    "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if(!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for (int selRow : selRows) {
            if(selRow == dropRow) {
                return false;
            }
        }
        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        int action = support.getDropAction();
        if(action == MOVE) {
            return haveCompleteNode(tree);
        }
        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode target =
                (DefaultMutableTreeNode)dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode firstNode =
                (DefaultMutableTreeNode)path.getLastPathComponent();
        if (firstNode.getChildCount() > 0 &&
                target.getLevel() < firstNode.getLevel()) {
            return false;
        }
        return true;
    }
    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first =
                (DefaultMutableTreeNode) path.getLastPathComponent();
        int childCount = first.getChildCount();
        // first has children and no children are selected.
        if (childCount > 0 && selRows.length == 1)
            return false;
        // first may have children.
        for (int selRow : selRows) {
            path = tree.getPathForRow(selRow);
            DefaultMutableTreeNode next =
                    (DefaultMutableTreeNode)path.getLastPathComponent();
            if(first.isNodeChild(next)) {
                // Found a child of first.
                if(childCount > selRows.length-1) {
                    // Not all children of first are selected.
                    return false;
                }
            }
        }
        return true;
    }
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            for (TreePath path : paths) {
                if (! (path.getLastPathComponent() instanceof StudentNode)) return null;
            }
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<DefaultMutableTreeNode> copies =
                    new ArrayList<>();
            List<DefaultMutableTreeNode> toRemove =
                    new ArrayList<>();
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            for (int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next =
                        (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if(next.getLevel() < node.getLevel()) {
                    break;
                } else if(next.getLevel() > node.getLevel()) {  // child node
                    copy.add(copy(next));
                    // node already contains child
                } else {                                        // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            DefaultMutableTreeNode[] nodes =
                    copies.toArray(new StudentNode[copies.size()]);
            nodesToRemove =
                    toRemove.toArray(new StudentNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }
    /** Defensive copy used in createTransferable. */
    private DefaultMutableTreeNode copy(TreeNode node) {
        return new StudentNode(node);
    }
    protected void exportDone(JComponent source, Transferable data, int action) {
        if((action & MOVE) == MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Remove nodes saved in nodesToRemove in createTransferable.
            for (DefaultMutableTreeNode nodeToRemove : nodesToRemove) {
                model.removeNodeFromParent(nodeToRemove);
            }
        }
    }
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
    public boolean importData(TransferHandler.TransferSupport support) {
        if(!canImport(support)) {
            return false;
        }
        // Extract transfer data.
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        // Get drop location info.
        JTree.DropLocation dl =
                (JTree.DropLocation) support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent =
                (DefaultMutableTreeNode)dest.getLastPathComponent();
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure for drop mode.
        int index = childIndex;    // DropMode.INSERT
        if (childIndex == -1) {     // DropMode.ON
            index = parent.getChildCount();
        }
        // Add data to model.
        for (DefaultMutableTreeNode node : nodes) {
            model.insertNodeInto(node, parent, index++);
        }
        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {
        DefaultMutableTreeNode[] nodes;
        NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return nodes;
        }
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}
