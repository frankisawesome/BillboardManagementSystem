package BillboardAssignment.BillboardControlPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class BillboardViewer extends JFrame implements ActionListener, Runnable {
    private int WIDTH = 800;
    private int HEIGHT = 450;

    String xmlBillboard;

    // Contents of xmlBillboard
    String title;
    String image;
    Boolean imageUrlFlg;
    String subtext;

    Color backgroundColor;
    String titleColor;
    String subtextColor;

    JPanel pnl1;
    JPanel pnl2;
    JPanel pnl3;
    JPanel pnl4;
    JPanel pnl5;

    public BillboardViewer (String title, String xmlBillboard) throws HeadlessException {
        super(title);
        this.xmlBillboard = xmlBillboard;
    }

    public void SetGUI() throws ParserConfigurationException, IOException, SAXException {
        // Set preliminaries
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        setVisible(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream bais = new ByteArrayInputStream(xmlBillboard.getBytes());
        Document document = builder.parse(bais);
        bais.close();

        Element documentElement = document.getDocumentElement();
        String attributeValue = documentElement.getAttribute("background");

        if (attributeValue.isEmpty()) {
            backgroundColor = Color.WHITE;
        } else {
            System.out.println(attributeValue);
            backgroundColor = Color.decode(attributeValue);
        }

        NodeList nl = documentElement.getChildNodes();

        for (int i =0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName() == "message") {
                    title = element.getTextContent();
                    System.out.println(title);
                    titleColor = element.getAttribute("colour");
                    System.out.println(titleColor);
                } else if (element.getTagName() == "picture") {
                    if ((element.hasAttribute("url")) && (!element.hasAttribute("data"))) {
                        image = element.getAttribute("url");
                        imageUrlFlg = true;
                    } else if ((element.hasAttribute("data")) && (!element.hasAttribute("url"))) {
                        image = element.getAttribute("data");
                        imageUrlFlg = false;
                    }
                } else if (element.getTagName() == "information") {
                    subtext = element.getTextContent();
                    subtextColor = element.getAttribute("colour");
                    System.out.println(subtextColor);
                }
            }
        }



        if (title == null) {
            title = "";
        }

        if (subtext == null) {
            subtext = "";
        }


        /*  ----------------------------------------------------------------------------- */

        // Create panels that define the layout
        pnl1 = createPanel(backgroundColor);
        pnl2 = createPanel(backgroundColor);
        pnl3 = createPanel(backgroundColor);
        pnl4 = createPanel(backgroundColor);
        pnl5 = createPanel(backgroundColor);

        // Set sizes of the panels

        setLayout(new BorderLayout());
        pnl1.setPreferredSize(new Dimension(100,100));
        pnl2.setPreferredSize(new Dimension(100,100));
        pnl3.setPreferredSize(new Dimension(100, 100));
        pnl4.setPreferredSize(new Dimension(100, 200));
        pnl5.setPreferredSize(new Dimension(100,200));


        // Set where the panels are located

        this.getContentPane().add(pnl1, BorderLayout.WEST);
        this.getContentPane().add(pnl2, BorderLayout.EAST);
        this.getContentPane().add(pnl3, BorderLayout.CENTER);
        this.getContentPane().add(pnl4, BorderLayout.NORTH);
        this.getContentPane().add(pnl5, BorderLayout.SOUTH);

        // Constraints
       // pnl4.setLayout(new GridBagLayout());

        // Set Top pane to title
        JLabel htmlTitle = new JLabel();
        htmlTitle.setText(String.format("<html><font size='12' color='%s'>%s</font></html>", titleColor, title));
        htmlTitle.setHorizontalAlignment(JLabel.CENTER);
        htmlTitle.setVerticalAlignment(JLabel.CENTER);
        htmlTitle.setBounds(0, 20, 200, 50);
        pnl4.add(htmlTitle);


        JLabel htmlTitle1 = new JLabel();
        htmlTitle1.setText(String.format("<html><body style='width: 1000px; text-align:center'><font size='6' color='%s'>%s</font></html>", subtextColor, subtext));
        htmlTitle1.setHorizontalAlignment(JLabel.CENTER);
        htmlTitle1.setVerticalAlignment(JLabel.CENTER);
        pnl5.add(htmlTitle1);

        if (imageUrlFlg == true) {
            BufferedImage myPicture;
            try {
                myPicture = ImageIO.read(new File(image));
            } catch (IOException e){
                dispose();
                JOptionPane.showMessageDialog(this, "Invalid Url.");
                return;
            }
            Image scaledImage = myPicture.getScaledInstance(500,400, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
            pnl3.add(picLabel);
        } else if (imageUrlFlg == false) {
            byte[] imageByte = Base64.getDecoder().decode(image);
            BufferedImage myPicture;
            try {
                myPicture = ImageIO.read(new ByteArrayInputStream(imageByte));
            } catch (IOException e){
                dispose();
                JOptionPane.showMessageDialog(this, "Invalid Byte Array.");
                return;
            }
            Image scaledImage = myPicture.getScaledInstance(500,400, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
            pnl3.add(picLabel);
        }


    }

    private void addToPanel(JPanel jp, Component c, GridBagConstraints constraints,
                            int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }

    private static JPanel createPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        return panel;
    }

    @Override
    public void run() {
        try {
            SetGUI();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void create(String billboard) {
        SwingUtilities.invokeLater(new BillboardViewer("Billboard Viewer", billboard));
    }
}