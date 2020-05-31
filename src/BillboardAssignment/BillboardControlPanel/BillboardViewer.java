package BillboardAssignment.BillboardControlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillboardViewer extends JFrame implements ActionListener, Runnable {
    private int WIDTH = 800;
    private int HEIGHT = 450;

    public BillboardViewer (String title) throws HeadlessException {
        super(title);

        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void SetGUI () {
        // Set preliminaries
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void run() {
        SetGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    protected static void create() {
        //Creates new frame for Login and sets Visible.
        JFrame frame = new BillboardViewer("Billboard Viewer");
        frame.setVisible(true);
    }
}