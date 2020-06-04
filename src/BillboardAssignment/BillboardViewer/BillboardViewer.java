package BillboardAssignment.BillboardViewer;

import BillboardAssignment.BillboardControlPanel.Login;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.Billboard;
import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import static BillboardAssignment.BillboardServer.Tests.TestUserControllers.requestBodyWithKey;

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

    Timer timer;

    public BillboardViewer (String title) throws HeadlessException {
        super(title);
        xmlBillboard = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <message>Billboard with message, GIF and information</message>\n" +
                "    <picture \n" +
                "\tdata=\"iVBORw0KGgoAAAANSUhEUgAAACAAAAAQCAIAAAD4YuoOAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwOTowMjoxNyArMDkwMHlQ1XMAAAAHdElNRQfkAxkAAyQ8nibjAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAS5JREFUeNq1kb9KxEAQxmcgcGhhJ4cnFwP6CIIiPoZwD+ALXGFxj6BgYeU7BO4tToSDFHYWZxFipeksbMf5s26WnAkJki2+/c03OzPZDRJNYcgVwfsU42cmKi5YjS1s4p4DCrkBPc0wTlkdX6bsG4hZQOj3HRDLHqh08U4Adb/zgEMtq5RuH3Axd45PbftdB2wO5OsWc7pOYaOeOk63wYfdFtL5qldB34W094ZfJ+4RlFldTrmW/ZNbn2g0of1vLHdZq77qSDCaSAsLf9kXh9w44PNoR/YSPHycEmbIOs5QzBJsmDHrWLPeF24ZkCe6ZxDCOqHcmxmsr+hsicahss+n8vYb8NHZPTJxi/RGC5IqbRwqH6uxVTX+5LvHtvT/V/R6PGh/iF4GHoBAwz7RD26spwq6Amh/AAAAAElFTkSuQmCC\"/>\n" +
                "    <information>This billboard has a message tag, a picture tag (linking to a URL with a GIF image) and an information tag. The picture is drawn in the centre and the message and information text are centred in the space between the top of the image and the top of the page, and the space between the bottom of the image and the bottom of the page, respectively.</information>\n" +
                "</billboard>";

    }



    public void SetGUI(boolean alreadyCalled) throws ParserConfigurationException, IOException, SAXException {
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

        SetComponents();

        timer = new Timer(15000, this);
        timer.start();
    }

    private void SetComponents () throws ParserConfigurationException, IOException, SAXException {

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
            backgroundColor = Color.decode(attributeValue);
        }

        NodeList nl = documentElement.getChildNodes();

        for (int i =0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName() == "message") {
                    title = element.getTextContent();
                    titleColor = element.getAttribute("colour");
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

        if (imageUrlFlg != null) {
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
            SetGUI(false);
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
        Object source = e.getSource();

        if (source == timer) {
            xmlBillboard = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<billboard>\n" +
                    "    <message colour=\"#60B9FF\">Billboard with message, GIF and information</message>\n" +
                    "    <picture \n" +
                    "\tdata=\"iVBORw0KGgoAAAANSUhEUgAAACAAAAAQCAIAAAD4YuoOAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwOTowMjoxNyArMDkwMHlQ1XMAAAAHdElNRQfkAxkAAyQ8nibjAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAS5JREFUeNq1kb9KxEAQxmcgcGhhJ4cnFwP6CIIiPoZwD+ALXGFxj6BgYeU7BO4tToSDFHYWZxFipeksbMf5s26WnAkJki2+/c03OzPZDRJNYcgVwfsU42cmKi5YjS1s4p4DCrkBPc0wTlkdX6bsG4hZQOj3HRDLHqh08U4Adb/zgEMtq5RuH3Axd45PbftdB2wO5OsWc7pOYaOeOk63wYfdFtL5qldB34W094ZfJ+4RlFldTrmW/ZNbn2g0of1vLHdZq77qSDCaSAsLf9kXh9w44PNoR/YSPHycEmbIOs5QzBJsmDHrWLPeF24ZkCe6ZxDCOqHcmxmsr+hsicahss+n8vYb8NHZPTJxi/RGC5IqbRwqH6uxVTX+5LvHtvT/V/R6PGh/iF4GHoBAwz7RD26spwq6Amh/AAAAAElFTkSuQmCC\"/>\n" +
                    "    <information colour=\"#FF0000\">This billboard has a message tag, a picture tag (linking to a URL with a GIF image) and an information tag. The picture is drawn in the centre and the message and information text are centred in the space between the top of the image and the top of the page, and the space between the bottom of the image and the bottom of the page, respectively.</information>\n" +
                    "</billboard>";
            System.out.println(xmlBillboard);

            try {
                SetComponents();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SAXException ex) {
                ex.printStackTrace();
            }
            revalidate();
        }
    }

    public static void create() {
        SwingUtilities.invokeLater(new BillboardViewer("Billboard Viewer"));
    }

    public static void main(String[] args) {
        create();
    }
}