package BillboardAssignment.BillboardControlPanel;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class FileChooserImplementation extends JFrame implements Runnable {

    JPanel mainPanel;

    static int WIDTH = 640;
    static int HEIGHT = 480;

    public FileChooserImplementation(String title) {
        super(title);
    }

    public void SetGUI() {
        setResizable(false);
        setVisible(true);
        setSize(WIDTH, HEIGHT);

        JFileChooser j = new JFileChooser("C:");
        j.showSaveDialog(null);

        mainPanel = new JPanel();
        mainPanel.add(j);



    }


    @Override
    public void run() {
        SetGUI();
    }

    public static void create() {
        SwingUtilities.invokeLater(new FileChooserImplementation("File Directory"));
    }

    public static void main(String args[]) {
        create();
    }
}
