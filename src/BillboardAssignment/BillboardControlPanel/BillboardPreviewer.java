package BillboardAssignment.BillboardControlPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
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

public class BillboardPreviewer extends JFrame implements ActionListener, Runnable {

    // Details about the billboard
    String xmlBillboard;

    // Dimension of the screen
    Dimension screenSize;

    // Contents of xmlBillboard
    String title;
    String image;
    Boolean imageUrlFlg;
    String subtext;

    // Flags to indicate the fields are present in the XML
    boolean titleFlg = false;
    boolean imageFlg = false;
    boolean subtextFlg = false;

    // Colours of the XML fields
    Color backgroundColor;
    String titleColor;
    String subtextColor;

    // JPanels that comprise the application
    JPanel pnl1;
    JPanel pnl2;
    JPanel pnl3;
    JPanel pnl4;
    JPanel pnl5;

    /**
     * Billboard Previewer class constructor
     *
     * @param title - title of the Billboard Previewer application
     * @param xmlBillboard - a string containing XML that stores information about the billboard for viewing
     * @return n/a
     */

    public BillboardPreviewer(String title, String xmlBillboard) throws HeadlessException {
        super(title);
        this.xmlBillboard = xmlBillboard;
    }

    /**
     * SetGUI method - initialises preliminaries required to ensure operability and function of the program
     *
     * @return n/a
     */

    public void SetGUI() throws ParserConfigurationException, IOException, SAXException {
        // Set preliminaries
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setResizable(false);
        setVisible(true);

        // Add key listener to close upon esc key
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });

        // Add mouse listener to close upon mouse click
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        // Set the components
        SetComponents();
    }

    /**
     * Set Components method to parse XML file and set JPanel and JComponents appropriately on the JFrame.
     *
     * @return n/a
     */

    private void SetComponents () throws ParserConfigurationException, IOException, SAXException {

        // Create document builder object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Read the xml string as a byte array and parse to document
        ByteArrayInputStream bais = new ByteArrayInputStream(xmlBillboard.getBytes());
        Document document = builder.parse(bais);
        bais.close();

        // Get the root billboard element of the document
        Element documentElement = document.getDocumentElement();
        String attributeValue = documentElement.getAttribute("background");

        // Set background to a default if it is empty
        if (attributeValue.isEmpty()) {
            backgroundColor = Color.WHITE;
        } else {
            backgroundColor = Color.decode(attributeValue);
        }

        // Get the child nodes (message, picture and information)
        NodeList nl = documentElement.getChildNodes();

        // For each node
        for (int i =0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName() == "message") {
                    // Set the title attributes accrodingly
                    title = element.getTextContent();
                    titleColor = element.getAttribute("colour");
                    titleFlg = true;
                } else if (element.getTagName() == "picture") {
                    if ((element.hasAttribute("url")) && (!element.hasAttribute("data"))) {
                        // Set the image path to the url and indicate it is a URL
                        image = element.getAttribute("url");
                        imageUrlFlg = true;
                        imageFlg = true;
                    } else if ((element.hasAttribute("data")) && (!element.hasAttribute("url"))) {
                        // Set the image path to a Byte64 byte array string and indicate it is a data attribute
                        image = element.getAttribute("data");
                        imageUrlFlg = false;
                        imageFlg = true;
                    }
                } else if (element.getTagName() == "information") {
                    // Set the subtext fields accordingly
                    subtext = element.getTextContent();
                    subtextColor = element.getAttribute("colour");
                    subtextFlg = true;
                }
            }
        }

        // If the title or subtext are null set to empty strings
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
        this.setBackground(backgroundColor);

        // Get the screen size and set to fullscreen
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);

        if ((titleFlg == true) && (imageFlg == false) && (subtextFlg == false)) {
            // Rule: title to occupy the center of the billboard if on its own
            setLayout(new BorderLayout());
            pnl4.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
            this.getContentPane().add(pnl4, BorderLayout.CENTER);
        } else if ((titleFlg == false) && (imageFlg == true) && (subtextFlg == false)) {
            // Rule: image to occupy the center scaled to half the screen width and
            //       height if on its own
            setLayout(new BorderLayout());
            pnl1.setPreferredSize(new Dimension(screenSize.width / 4,screenSize.height));
            pnl2.setPreferredSize(new Dimension(screenSize.width / 4,screenSize.height));
            pnl3.setPreferredSize(new Dimension(screenSize.width / 2,screenSize.height / 2));
            pnl4.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 4));
            pnl5.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 4));

            this.getContentPane().add(pnl1, BorderLayout.WEST);
            this.getContentPane().add(pnl2, BorderLayout.EAST);
            this.getContentPane().add(pnl3, BorderLayout.CENTER);
            this.getContentPane().add(pnl4, BorderLayout.NORTH);
            this.getContentPane().add(pnl5, BorderLayout.SOUTH);
        } else if ((titleFlg == false) && (imageFlg == false) && (subtextFlg == true)) {
            // Rule: if subtext is on its own set to occupy 75% of the width and 50% of the height (at maximum)
            setLayout(new BorderLayout());
            pnl1.setPreferredSize(new Dimension(screenSize.width / 8,screenSize.height));
            pnl2.setPreferredSize(new Dimension(screenSize.width / 8,screenSize.height));
            pnl5.setPreferredSize(new Dimension(6 * screenSize.width / 8,screenSize.height / 2));
            pnl4.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 4));
            pnl3.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 4));

            this.getContentPane().add(pnl1, BorderLayout.WEST);
            this.getContentPane().add(pnl2, BorderLayout.EAST);
            this.getContentPane().add(pnl5, BorderLayout.CENTER);
            this.getContentPane().add(pnl4, BorderLayout.NORTH);
            this.getContentPane().add(pnl3, BorderLayout.SOUTH);
        } else if ((titleFlg == true) && (imageFlg == true) && (subtextFlg == false)) {
            // Rule: if title and text are supplied set title to top 1/3rd and image to bottom 2/3rds of JFrame
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            pnl3.setPreferredSize(new Dimension(screenSize.width,2* screenSize.height / 3));
            pnl4.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 3));

            addToPanel(this, pnl4, c, 1, 0, 1, 1);
            addToPanel(this, pnl3, c, 1, 1, 1, 2);

        } else if ((titleFlg == true) && (imageFlg == false) && (subtextFlg == true)) {
            // Rule: if title and subtext are supplied let each occupy the top and bottom halves of the screen
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;

            pnl4.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 2));
            pnl5.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 2));

            addToPanel(this, pnl4, c, 1, 0, 1, 1);
            addToPanel(this, pnl5, c, 1, 1, 1, 1);

        } else if ((titleFlg == false) && (imageFlg == true) && (subtextFlg == true)) {
            // Rule: if image and subtext fields are present let subtext occupy top 1/3rd of the screen and image occupy the bottom 2/3rds
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            pnl3.setPreferredSize(new Dimension(screenSize.width,2* screenSize.height / 3));
            pnl5.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 3));

            addToPanel(this, pnl3, c, 1, 0, 1, 1);
            addToPanel(this, pnl5, c, 1, 1, 1, 1);
        } else if ((titleFlg == true) && (imageFlg == true) && (subtextFlg == true)) {
            // Rule: if all fields are present set each to occupy 1/3rd of the vertical dimension
            setLayout(new BorderLayout());
            pnl1.setPreferredSize(new Dimension(screenSize.width/3, screenSize.height));
            pnl2.setPreferredSize(new Dimension(screenSize.width/3,screenSize.height));
            pnl3.setPreferredSize(new Dimension(screenSize.width/3, screenSize.height / 3));
            pnl4.setPreferredSize(new Dimension(screenSize.width, screenSize.height / 3));
            pnl5.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 3));

            this.getContentPane().add(pnl1, BorderLayout.WEST);
            this.getContentPane().add(pnl2, BorderLayout.EAST);
            this.getContentPane().add(pnl3, BorderLayout.CENTER);
            this.getContentPane().add(pnl4, BorderLayout.NORTH);
            this.getContentPane().add(pnl5, BorderLayout.SOUTH);
        } else {
            // Set random layout if all these cases were to fail
            setLayout(new BorderLayout());
            pnl1.setPreferredSize(new Dimension(100,100));
            pnl2.setPreferredSize(new Dimension(100,100));
            pnl3.setPreferredSize(new Dimension(100, 100));
            pnl4.setPreferredSize(new Dimension(100, 200));
            pnl5.setPreferredSize(new Dimension(100,200));

            this.getContentPane().add(pnl1, BorderLayout.WEST);
            this.getContentPane().add(pnl2, BorderLayout.EAST);
            this.getContentPane().add(pnl3, BorderLayout.CENTER);
            this.getContentPane().add(pnl4, BorderLayout.NORTH);
            this.getContentPane().add(pnl5, BorderLayout.SOUTH);
        }

        if (titleFlg == true) {
            // Set associated title panel
            JLabel htmlTitle = new JLabel();
            pnl4.setLayout(new GridBagLayout());
            htmlTitle.setText(String.format("<html><font size='12' color='%s'>%s</font></html>", titleColor, title));
            htmlTitle.setHorizontalAlignment(JLabel.CENTER);
            htmlTitle.setVerticalAlignment(JLabel.CENTER);
            htmlTitle.setBounds(0, 20, 200, 50);
            pnl4.add(htmlTitle);
        }

        if (subtextFlg == true) {
            // Set associated subtext panel
            JLabel htmlTitle1 = new JLabel();
            pnl5.setLayout(new GridBagLayout());
            htmlTitle1.setText(String.format("<html><body style='width: 1000px; text-align:center'><font size='6' color='%s'>%s</font></html>", subtextColor, subtext));
            htmlTitle1.setHorizontalAlignment(JLabel.CENTER);
            htmlTitle1.setVerticalAlignment(JLabel.CENTER);
            htmlTitle1.setPreferredSize(screenSize);
            pnl5.add(htmlTitle1);
        }

        if (imageFlg == true) {
            pnl3.setLayout(new GridBagLayout());
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
                    Image scaledImage = myPicture.getScaledInstance(screenSize.width,screenSize.height, Image.SCALE_SMOOTH);
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
                    Image scaledImage = myPicture.getScaledInstance(screenSize.width / 3,screenSize.height / 3, Image.SCALE_SMOOTH);
                    JLabel picLabel = new JLabel(new ImageIcon(scaledImage), JLabel.CENTER);
                    pnl3.add(picLabel, JLabel.CENTER);
                }
            }
        }
    }

    /**
     * Method to add a JPanel to the JFrame
     *
     * @param jf - JFrame object to be assigned to
     * @param panel - JPanel  object to be added to the JFrame
     * @param constraints - GridBagConstraints object with default constraints
     * @param x - integer defining the column the JPanel is to be added to in the grid
     * @param y - integer defining the row the JPanel is to be added to in the grid
     * @param w - integer defining the number of columns the JPanel will occupy
     * @param h - integer defining the column of rows the JPanel will occupy
     *
     * @return n/a
     */

    private void addToPanel(JFrame jf, JPanel panel, GridBagConstraints constraints,
                            int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jf.add(panel, constraints);
    }

    /**
     * Method to add a JPanel to the JFrame
     *
     * @param color - Color object that defines the background colour of the JPanel
     *
     * @return panel - the JPanel being made
     */

    private static JPanel createPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        return panel;
    }

    /**
     * Overridden method from Runnable interface that sets GUI on default
     *
     * @return n/a
     */

    @Override
    public void run() {
        try {
            SetGUI();
        } catch (ParserConfigurationException e) {
            JOptionPane.showMessageDialog(null, "ParserConfigurationException");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IOException");
        } catch (SAXException e) {
            JOptionPane.showMessageDialog(null, "SAXException");
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "NullPointerException");
        }
    }

    /**
     * Overridden method from ActionListener interface that listens for an action event
     *
     * @return n/a
     */


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /**
     * Create method that create the JFrame for the Billboard Previewer
     *
     * @param billboard - xml string associated with the billboard that will be parsed
     * @return n/a
     */

    public static void create(String billboard) {
        SwingUtilities.invokeLater(new BillboardPreviewer("Billboard Viewer", billboard));
    }
}