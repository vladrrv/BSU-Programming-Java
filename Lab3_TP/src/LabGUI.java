import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

class WrongEntryException extends RuntimeException {
    WrongEntryException(String message) {
        super(message);
    }
}
class DiagramEntry implements Comparable<DiagramEntry> {
    private static int valuesSum = 0;
    private String key;
    private int value;
    private double angle;
    private Color color;
    private JLabel legendLabel;
    DiagramEntry(String key, int value, Color color) throws WrongEntryException {
        if (value < 0) throw new WrongEntryException("Negative value in '" + key + "'");
        this.key = key;
        this.value = value;
        this.angle = 0;
        this.color = color;
        valuesSum += value;
    }

    public static void resetValuesSum() {
        DiagramEntry.valuesSum = 0;
    }

    String getKey() {
        return key;
    }
    int getValue() {
        return value;
    }
    double getAngle() {
        if (angle == 0) {
            angle = value * 360 / valuesSum;
        }
        return angle;
    }
    public Color getColor() {
        return color;
    }
    public JLabel getLegendLabel() {
        if (legendLabel == null) {
            BufferedImage image = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            g.setColor(getColor());
            g.fillRect(0, 0, 24, 24);
            legendLabel = new JLabel(getKey(), new ImageIcon(image), JLabel.RIGHT);
        }
        return legendLabel;
    }
    @Override
    public int compareTo(DiagramEntry o) {
        return Comparator.comparingInt(DiagramEntry::getValue).compare(this, o);
    }
}

class LabGUI extends JFrame {
    private JTabbedPane contentPane;
    private double clockDegree;
    private int direction;
    private double degree;
    private double speedParam;
    private int sliderValue;
    private int orbitRadius;
    private int scale;
    private Image moon, earth, moonScaled, earthScaled;
    private int earthWidth, earthHeight, earthWidthScaled, earthHeightScaled;
    private int moonWidth, moonHeight, moonWidthScaled, moonHeightScaled;

    LabGUI() {
        setComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 500));
        setTitle("Lab 3");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setComponents() {
        contentPane = new JTabbedPane();
        setContentPane(contentPane);
        setTask1();
        setTask2();
        setTask3();
    }
    private void setTask1() {
        clockDegree = 0;
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = this.getWidth(), h = this.getHeight(), xCenter = w / 2, yCenter = h / 2;
                int radius = Math.min(w, h) / 3, xCorner = xCenter - radius, yCorner = yCenter - radius;
                double clockAngle = Math.toRadians(clockDegree);
                g.drawOval(xCorner, yCorner, 2 * radius, 2 * radius);
                g.drawLine(xCenter, yCenter, (int) (xCenter + radius * Math.cos(clockAngle)), (int) (yCenter + radius * Math.sin(clockAngle)));
            }
        };
        contentPane.addTab("3.1", panel);
        Timer timer = new Timer(100, e -> {
            clockDegree += 0.6;
            clockDegree %= 360;
            panel.repaint();
        });
        timer.start();
    }
    private void setTask2() {
        JPanel panel = new JPanel(), controlPanel = new JPanel();
        contentPane.addTab("3.2", panel);
        panel.setLayout(new BorderLayout());
        JSlider slider = new JSlider(1, 20);
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Clockwise");
        comboBox.addItem("Counterclockwise");
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));
        controlPanel.add(comboBox);
        controlPanel.add(slider);
        ImageIcon earthIcon = new ImageIcon("earth.png");
        ImageIcon moonIcon = new ImageIcon("moon.png");
        earth = earthIcon.getImage();
        moon = moonIcon.getImage();
        earthWidth = earthIcon.getIconWidth();
        earthHeight = earthIcon.getIconHeight();
        moonWidth = moonIcon.getIconWidth();
        moonHeight = moonIcon.getIconHeight();
        panel.add(controlPanel, BorderLayout.NORTH);
        JPanel paintPane = new JPanel() {
            boolean isInit = false;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = this.getWidth(), h = this.getHeight();
                int xCenter = w / 2, yCenter = h / 2;
                int newRadius = Math.min(w, h) / 2;
                if (!isInit) {
                    orbitRadius = newRadius;
                    isInit = true;
                }
                int newScale = Toolkit.getDefaultToolkit().getScreenSize().height / newRadius;
                double angle = Math.toRadians(degree);
                boolean isScaleChanged = newScale != scale;
                if (isScaleChanged) {
                    speedParam *= (1.0 * orbitRadius / newRadius);
                    scale = newScale;
                    orbitRadius = newRadius;
                    earthWidthScaled = earthWidth / scale;
                    earthHeightScaled = earthHeight / scale;
                    moonWidthScaled = moonWidth / scale;
                    moonHeightScaled = moonHeight / scale;
                    earthScaled = earth.getScaledInstance(earthWidthScaled, earthHeightScaled, Image.SCALE_DEFAULT);
                    moonScaled = moon.getScaledInstance(moonWidthScaled, moonHeightScaled, Image.SCALE_DEFAULT);
                }
                int x = (int) (xCenter - orbitRadius * Math.cos(angle)) - moonWidthScaled / 2;
                int y = (int) (yCenter - orbitRadius * Math.sin(angle)) - moonHeightScaled / 2;
                g.drawImage(earthScaled,
                        xCenter - earthWidthScaled / 2, yCenter - earthHeightScaled / 2,
                        earthWidthScaled, earthHeightScaled,
                        null);
                g.drawImage(moonScaled,
                        x, y,
                        moonWidthScaled, moonHeightScaled,
                        null);
            }
        };
        paintPane.setBackground(new Color(0, 0, 0));
        panel.add(paintPane, BorderLayout.CENTER);
        scale = 0;
        direction = 1;
        speedParam = 1.5;
        sliderValue = 10;
        slider.addChangeListener(e -> {
            speedParam *= 1.0 * slider.getValue() / sliderValue;
            sliderValue = slider.getValue();
        });
        comboBox.addActionListener(e -> {
            direction = (comboBox.getSelectedIndex() == 0) ? 1 : -1;
        });
        Timer timer = new Timer(10, e -> {
            degree += speedParam * direction;
            degree %= 360;
            paintPane.repaint();
        });
        timer.start();
    }
    private void setTask3() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        contentPane.addTab("3.3", panel);
        JPanel legendPane = new JPanel();
        legendPane.setLayout(new BoxLayout(legendPane, BoxLayout.Y_AXIS));
        panel.add(legendPane, BorderLayout.WEST);
        List<DiagramEntry> data = new ArrayList<>();
        JPanel paintPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int
                        w = this.getWidth(),
                        h = this.getHeight(),
                        xCenter = w / 2,
                        yCenter = h / 2,
                        radius = Math.min(w, h) / 3,
                        xCorner = xCenter - radius,
                        yCorner = yCenter - radius;
                double sum = 0;
                for (DiagramEntry entry : data) {
                    sum += entry.getAngle();
                }
                double angle = 0, d = (360-sum)/data.size();
                for (DiagramEntry entry : data) {
                    double delta = entry.getAngle();
                    g.setColor(entry.getColor());
                    g.fillArc(xCorner, yCorner, 2*radius, 2*radius, (int)Math.round(angle), (int)Math.round(delta+d));
                    angle += (int)Math.round(delta+d);
                }
                g.fillArc(xCorner, yCorner, 2*radius, 2*radius, (int)Math.round(angle), 360-(int)Math.round(angle));
            }
        };
        panel.add(paintPane, BorderLayout.CENTER);
        JButton button = new JButton("Generate diagram");
        panel.add(button, BorderLayout.EAST);
        button.addActionListener(e-> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Open File");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION) return;
            File file = fileChooser.getSelectedFile();
            legendPane.removeAll();
            data.clear();
            DiagramEntry.resetValuesSum();
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String key = scanner.next();
                    int value = scanner.nextInt();
                    Random random = new Random();
                    int r = Math.abs(random.nextInt()) % 256, g = Math.abs(random.nextInt()) % 256, b = Math.abs(random.nextInt()) % 256;
                    DiagramEntry entry = new DiagramEntry(key, value, new Color(r, g, b));
                    data.add(entry);
                    legendPane.add(entry.getLegendLabel());
                }
                Collections.sort(data);
            }
            catch (InputMismatchException ex) {
                JOptionPane.showMessageDialog(this, "Invalid file format", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            panel.updateUI();
        });

    }
}



