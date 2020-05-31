package BillboardAssignment.BillboardControlPanel;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BillboardViewer extends JFrame implements ActionListener, Runnable {
    private int WIDTH = 800;
    private int HEIGHT = 450;

    String xmlBillboard;

    public BillboardViewer (String title, String xmlBillboard) throws HeadlessException {
        super(title);
        this.xmlBillboard = xmlBillboard;
    }

    public void SetGUI() throws ParserConfigurationException, IOException, SAXException {
        // Set preliminaries
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream bais = new ByteArrayInputStream(xmlBillboard.getBytes());
        Document document = builder.parse(bais);
        bais.close();

        Element documentElement = document.getDocumentElement();
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