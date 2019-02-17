import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PaintGUI extends JFrame {
    private JPanel contentPane;
    private JButton saveToFileButton;
    private JButton openFileButton;
    private JButton chooseColorButton;
    private JPanel paintPane;
    private JButton newCanvasButton;
    private JScrollPane scrollPane;
    private JButton saveButton;
    private ArrayList<JButton> colorButtonArray;
    private JToolBar colorBar;
    private JLabel coordLabel;
    private Canvas canvas;
    private File currentFile;
    private String defaultFileName;
    private boolean isSaved;
    private class Canvas extends JLabel {
        private Color penColor;
        private int x;
        private int y;
        private int width;
        private int height;
        private BufferedImage image;
        private Graphics2D graphics;
        private void customCursor() {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage("resources/pencil.gif");
            Point hotspot = new Point(0,16);
            Cursor cursor = toolkit.createCustomCursor(image, hotspot, "pencil");
            setCursor(cursor);
        }
        Canvas(int width, int height) {
            super();
            customCursor();
            this.width = width;
            this.height = height;
            setSize();
            image = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
            graphics = image.createGraphics();
            graphics.fillRect(0,0,width,height);
            setIcon(new ImageIcon(image));
            setPenColor(Color.black);
            addListeners();
        }
        Canvas(BufferedImage image) {
            super();
            customCursor();
            this.width = image.getWidth();
            this.height = image.getHeight();
            setSize();
            this.image = image;
            graphics = image.createGraphics();
            setIcon(new ImageIcon(image));
            setPenColor(Color.black);
            addListeners();
        }
        private void setSize() {
            setBounds(0,0,width,height);
            setMaximumSize(getSize());
            setMaximumSize(getSize());
        }
        private void addListeners() {
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    graphics.setColor(penColor);
                    graphics.drawLine(x, y, e.getX(), e.getY());
                    mouseMoved(e);
                    repaint();
                }
                @Override
                public void mouseMoved(MouseEvent e) {
                    x = e.getX();
                    y = e.getY();
                    setCoordLabel(x, y);
                }
            });
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isSaved) {
                        isSaved = false;
                        setTitle();
                    }
                    x = e.getX();
                    y = e.getY();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    graphics.setColor(penColor);
                    x = e.getX();
                    y = e.getY();
                    graphics.drawLine(x, y, x, y);
                    repaint();
                }
            });
        }
        public void setPenColor(Color color) {
            penColor = color;
        }
        public void saveImageToFile(File file) throws IOException {
            ImageIO.write(image, "png", file);
        }
        public Color getPenColor() {
            return penColor;
        }
        @Override
        public int getWidth() {
            return width;
        }
        @Override
        public int getHeight() {
            return height;
        }
    }

    public PaintGUI() {
        isSaved = true;
        defaultFileName = "untitled.png";
        setTitle();
        createColorButtons(new ArrayList<>(Arrays.asList(Color.BLUE,Color.CYAN,Color.GREEN,Color.YELLOW,Color.RED,Color.MAGENTA,Color.PINK)));
        setButtonIcons();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirm()) {
                    close();
                    dispose();
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setContentPane(contentPane);
        setMinimumSize(new Dimension(800,600));
        chooseColorButton.addActionListener(e -> chooseColor());
        newCanvasButton.addActionListener(e -> newCanvas());
        saveToFileButton.addActionListener(e -> saveToFile());
        saveButton.addActionListener(e -> save());
        openFileButton.addActionListener(e -> openFile());
        paintPane.setLayout(new BoxLayout(paintPane,BoxLayout.Y_AXIS));
    }
    private void createColorButtons(ArrayList<Color> colorArray) {
        colorButtonArray = new ArrayList<>();
        for (Color color : colorArray) {
            JButton button = new JButton();
            colorBar.add(button);
            setColorLabel(button, color);
            button.addActionListener(e ->  {
                if (canvas != null) {
                    canvas.setPenColor(color);
                    setColorLabel(chooseColorButton, color);
                }
            });
            colorButtonArray.add(button);
            button.setEnabled(false);
        }
    }
    private void setButtonIcons() {
        try {
            setColorLabel(chooseColorButton, Color.black);
            newCanvasButton.setIcon(
                    new ImageIcon(ImageIO.read(
                            new File("resources/New.png"))));
            openFileButton.setIcon(
                    new ImageIcon(ImageIO.read(
                            new File("resources/Open.png"))));
            saveToFileButton.setIcon(
                    new ImageIcon(ImageIO.read(
                            new File("resources/SaveAs.png"))));
            saveButton.setIcon(
                    new ImageIcon(ImageIO.read(
                            new File("resources/Save.png"))));
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Resources Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setColorLabel(JButton button, Color color) {
        BufferedImage image = new BufferedImage(24,24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0,0,24,24);
        button.setIcon(new ImageIcon(image));
    }
    private void setButtonsEnabled(boolean isEnabled) {
        for (JButton button : colorButtonArray) {
            button.setEnabled(isEnabled);
        }
        chooseColorButton.setEnabled(isEnabled);
        saveToFileButton.setEnabled(isEnabled);
        saveButton.setEnabled(isEnabled);
    }
    private boolean confirm() {
        if (!isSaved) {
            int o = JOptionPane.showConfirmDialog(this, "Image is unsaved. Do you want to save it?");
            switch (o) {
                case JOptionPane.YES_OPTION: return saveToFile();
                case JOptionPane.NO_OPTION: return true;
                case JOptionPane.CANCEL_OPTION: return false;
                default: return false;
            }
        }
        return true;
    }
    private void close() {
        if (canvas != null) {
            paintPane.remove(canvas);
            currentFile = null;
            setTitle();
            setColorLabel(chooseColorButton, Color.black);
            setButtonsEnabled(false);
            scrollPane.updateUI();
        }
    }
    private void setTitle() {
        StringBuilder title = new StringBuilder("Simple Paint");
        if (currentFile != null) {
            title.append(" (");
            title.append(currentFile.getAbsolutePath());
            if (!isSaved) title.append("*");
            title.append(") [");
            title.append(canvas.getWidth());
            title.append("x");
            title.append(canvas.getHeight());
            title.append("]");
        }
        else if (!isSaved) {
            title.append(" (" + defaultFileName + "*) [");
            title.append(canvas.getWidth());
            title.append("x");
            title.append(canvas.getHeight());
            title.append("]");
        }
        super.setTitle(title.toString());
    }
    private void setCoordLabel(int x, int y) {
        coordLabel.setText("X: " + String.valueOf(x) + ", Y: " + String.valueOf(y));
    }
    private void newCanvas() {
        if (!confirm()) return;
        SetCanvasSizeDialog dialog = new SetCanvasSizeDialog(this);
        if (dialog.isSet()) {
            close();
        }
        else {
            return;
        }
        int width = dialog.getInputWidth();
        int height = dialog.getInputHeight();
        canvas = new Canvas(width, height);
        isSaved = false;
        setButtonsEnabled(true);
        setTitle();
        paintPane.add(canvas);
        paintPane.repaint();
        scrollPane.updateUI();
    }
    private void chooseColor() {
        if (canvas != null) {
            Color color = JColorChooser.showDialog(this, "Choose Pen Color", canvas.getPenColor());
            if (color != null) {
                canvas.setPenColor(color);
                setColorLabel(chooseColorButton, color);
            }
        }
    }
    private void save() {
        if (isSaved) return;
        if (currentFile == null) {
            saveToFile();
        }
        else {
            try {
                canvas.saveImageToFile(currentFile);
                isSaved = true;
                setTitle();
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "File Write Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean saveToFile() {
        if (canvas == null) return true;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Image to File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "gif", "jpeg", "jpg"));
        if (currentFile != null) {
            fileChooser.setSelectedFile(currentFile);
        }
        fileChooser.showSaveDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file != null) {
            try {
                canvas.saveImageToFile(file);
                currentFile = file;
                isSaved = true;
                setTitle();
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "File Write Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return isSaved;
    }
    private void openFile() {
        if (!confirm()) return;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "gif", "jpeg", "jpg"));
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file == null) return;
        close();
        isSaved = true;
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) throw new IOException("Incorrect file format!");
            canvas = new Canvas(image);
            paintPane.add(canvas);
            paintPane.repaint();
            currentFile = file;
            setTitle();
            setButtonsEnabled(true);
            scrollPane.updateUI();
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
