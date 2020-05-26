package BillboardAssignment.BillboardViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DisplayViewer extends JFrame {

    public DisplayViewer() throws HeadlessException {
        setUndecorated(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        GraphicsEnvironment graphics =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        JFrame Billboard = new DisplayViewer();
        device.setFullScreenWindow(Billboard);

        Billboard.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    Billboard.dispose();
            }
        });

        Billboard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Billboard.dispose();
            }
        });
    }
}
