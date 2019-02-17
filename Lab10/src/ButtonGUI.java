import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;

public class ButtonGUI extends JFrame implements MouseMotionListener {
    private JPanel contentPane;
    private JTextField xCoord;
    private JTextField yCoord;
    private JPanel buttonPanel;
    private JButton btn;
    private int x;
    private int y;
    public ButtonGUI() {
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400,300));
        btn = new JButton("C");
        btn.setBounds(20,20,100,20);
        btn.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String text = btn.getText();
                char key = e.getKeyChar();
                if (key == 8 && text.length() > 0) {
                    btn.setText(text.substring(0, text.length()-1));
                }
                else if (Character.isLetterOrDigit(key)) {
                    btn.setText(text + key);
                }
            }
        });
        DragSource.getDefaultDragSource().addDragSourceMotionListener(dsde -> showMouseCoord());
        btn.setTransferHandler(new TransferHandler("text"));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = (int)buttonPanel.getMousePosition().getX();
                y = (int)buttonPanel.getMousePosition().getY();
            }
        });
        btn.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                showMouseCoord();
                if (e.isControlDown()) {
                    Point mousePos = buttonPanel.getMousePosition();
                    int xNew;
                    int yNew;
                    if (mousePos != null) {
                        xNew = (int) mousePos.getX();
                        yNew = (int) mousePos.getY();
                    }
                    else {
                        xNew = x;
                        yNew = y;
                    }
                    int dx = xNew - x;
                    int dy = yNew - y;
                    Rectangle r = btn.getBounds();
                    int rx = r.x + dx;
                    int ry = r.y + dy;
                    if (rx + btn.getWidth() > buttonPanel.getWidth()) {
                        rx = buttonPanel.getWidth() - btn.getWidth();
                    }
                    if (ry + btn.getHeight() > buttonPanel.getHeight()) {
                        ry = buttonPanel.getHeight() - btn.getHeight();
                    }
                    if (rx < 0) {
                        rx = 0;
                    }
                    if (ry < 0) {
                        ry = 0;
                    }
                    btn.setBounds(rx, ry, r.width, r.height);
                    x = xNew;
                    y = yNew;
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                showMouseCoord();
            }
        });
        buttonPanel.setLayout(null);
        buttonPanel.add(btn);
        addMouseMotionListener(this);
        buttonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int xNew = e.getX();
                int yNew = e.getY();
                if (xNew+btn.getWidth() > buttonPanel.getWidth()) {
                    xNew = buttonPanel.getWidth() - btn.getWidth();
                }
                if (yNew+btn.getHeight() > buttonPanel.getHeight()) {
                    yNew = buttonPanel.getHeight() - btn.getHeight();
                }
                btn.setBounds(xNew,yNew,100,20);
            }
        });
        buttonPanel.addMouseMotionListener(this);
    }
    private void showMouseCoord() {
        Point pt = getMousePosition();
        if (pt != null) {
            xCoord.setText(String.valueOf(pt.getX()));
            yCoord.setText(String.valueOf(pt.getY()));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        showMouseCoord();
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        showMouseCoord();
    }

}
