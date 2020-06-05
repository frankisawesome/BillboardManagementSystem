package BillboardAssignment.BillboardViewer;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.Services.Billboard.Billboard;
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

public class BillboardViewer extends JFrame implements ActionListener, Runnable {
    // Screen size dimensions
    Dimension screenSize;

    // String containing the XML representation of the billboard
    String xmlBillboard;

    // Default billboard;
    String defaultBillboard = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <message colour=\"#60B9FF\">Thankyou for viewing a Billboard Enterprises Billboard</message>\n" +
            "    <picture \n" +
            "\tdata=\"iVBORw0KGgoAAAANSUhEUgAAACAAAAAQCAIAAAD4YuoOAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwOTowMjoxNyArMDkwMHlQ1XMAAAAHdElNRQfkAxkAAyQ8nibjAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAS5JREFUeNq1kb9KxEAQxmcgcGhhJ4cnFwP6CIIiPoZwD+ALXGFxj6BgYeU7BO4tToSDFHYWZxFipeksbMf5s26WnAkJki2+/c03OzPZDRJNYcgVwfsU42cmKi5YjS1s4p4DCrkBPc0wTlkdX6bsG4hZQOj3HRDLHqh08U4Adb/zgEMtq5RuH3Axd45PbftdB2wO5OsWc7pOYaOeOk63wYfdFtL5qldB34W094ZfJ+4RlFldTrmW/ZNbn2g0of1vLHdZq77qSDCaSAsLf9kXh9w44PNoR/YSPHycEmbIOs5QzBJsmDHrWLPeF24ZkCe6ZxDCOqHcmxmsr+hsicahss+n8vYb8NHZPTJxi/RGC5IqbRwqH6uxVTX+5LvHtvT/V/R6PGh/iF4GHoBAwz7RD26spwq6Amh/AAAAAElFTkSuQmCC\"/>\n" +
            "</billboard>";

    // Contents of xmlBillboard
    String title;
    String image;
    Boolean imageUrlFlg;
    String subtext;

    // Flags to check whether these fields are contained in the XML file
    boolean titleFlg = false;
    boolean imageFlg = false;
    boolean subtextFlg = false;

    // Colour of background and text to be displayed
    Color backgroundColor;
    String titleColor;
    String subtextColor;

    // JPanels that are added to the JFrame
    JPanel pnl1;
    JPanel pnl2;
    JPanel pnl3;
    JPanel pnl4;
    JPanel pnl5;

    // Timer object
    Timer timer;

    /**
     * Constructor class for the BillboardViewer
     *
     * @param title The title of the billboard viewer application
     * @throws HeadlessException
     */

    public BillboardViewer (String title) throws HeadlessException {
        super(title);
    }

    /**
     * Method that sets up preliminary operating values and listeners to add functionality to the viewer
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */

    public void SetGUI() throws ParserConfigurationException, IOException, SAXException {
        // Set preliminaries
        setUndecorated(true);
        setResizable(false);
        setVisible(true);

        // Get the size of the screen and set the JFrame to fullscreen
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);

        // Call set components function to populate JFrame
        GetServerResponse();
        SetFields();
        SetComponents();

        // Add a key listener to close the application on ESC key press
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });

        // Add a mouse listener to close the application on mouse click
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        // Start  a new timer that will cause an action event every 15 seconds
        timer = new Timer(15000, this);
        timer.start();
    }

    /**
     * Method that sets the components and panels that are needed to display the billboard on the JFrame
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */

    private void GetServerResponse () {
        // Get billboard from server
        ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "current");
        ServerResponse<Billboard> response;
        try {
            response = request.getResponse();
            this.xmlBillboard = response.body().xml;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.xmlBillboard = defaultBillboard;
        }
    }

    /**
     * Method to set the variable fields based on the XML document
     *
     * @return n/a
     */

    private void SetFields () throws ParserConfigurationException, IOException, SAXException {
        // Make document builder objects
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Get the bytes of the xmlBillboard string and parse as a document object
        ByteArrayInputStream bais = new ByteArrayInputStream(xmlBillboard.getBytes());
        Document document = builder.parse(bais);
        bais.close();

        // Get the first element that makes the XML string (should be billboard)
        Element documentElement = document.getDocumentElement();
        String attributeValue = documentElement.getAttribute("background");

        // Set a default value if there is no associated colour
        if (attributeValue.isEmpty()) {
            backgroundColor = Color.WHITE;
        } else {
            backgroundColor = Color.decode(attributeValue);
        }

        // Get the child nodes (message, picture, information)
        NodeList nl = documentElement.getChildNodes();

        // Set fields based on the elements in the XML document
        for (int i =0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName() == "message") {
                    // Set the title parameters
                    title = element.getTextContent();
                    titleColor = element.getAttribute("colour");
                    titleFlg = true;
                } else if (element.getTagName() == "picture") {
                    // Set the imagePath parameters
                    if ((element.hasAttribute("url")) && (!element.hasAttribute("data"))) {
                        // Set that the image is a file path format
                        image = element.getAttribute("url");
                        imageUrlFlg = true;
                        imageFlg = true;
                    } else if ((element.hasAttribute("data")) && (!element.hasAttribute("url"))) {
                        // Set that the image is a Base64 byte array format
                        image = element.getAttribute("data");
                        imageUrlFlg = false;
                        imageFlg = true;
                    }
                } else if (element.getTagName() == "information") {
                    // Set the subtext parameters
                    subtext = element.getTextContent();
                    subtextColor = element.getAttribute("colour");
                    subtextFlg = true;
                }
            }
        }

        // If the text fields are null just set them to empty strings
        if (title == null) {
            title = "";
        }
        if (subtext == null) {
            subtext = "";
        }
    }

    /**
     * Method to set the component elements to the JPanel
     *
     * @return n/a
     */

    private void SetComponents ()  {
        /*  --------------------------Create Layout From Fields----------------------------- */

        // Create panels that define the layout
        pnl1 = null;
        pnl2 = null;
        pnl3 = null;
        pnl4 = null;
        pnl5 = null;

        pnl1 = createPanel(backgroundColor);
        pnl2 = createPanel(backgroundColor);
        pnl3 = createPanel(backgroundColor);
        pnl4 = createPanel(backgroundColor);
        pnl5 = createPanel(backgroundColor);
        this.setBackground(backgroundColor);

        // Get the size of the screen and set the JFrame to full screen
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
            pnl3.setPreferredSize(new Dimension(6 * screenSize.width / 8,screenSize.height / 2));
            pnl4.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 4));
            pnl5.setPreferredSize(new Dimension(screenSize.width,screenSize.height / 4));

            this.getContentPane().add(pnl1, BorderLayout.WEST);
            this.getContentPane().add(pnl2, BorderLayout.EAST);
            this.getContentPane().add(pnl3, BorderLayout.CENTER);
            this.getContentPane().add(pnl4, BorderLayout.NORTH);
            this.getContentPane().add(pnl5, BorderLayout.SOUTH);
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
            // Set the title label and add to its associated JPanel
            JLabel htmlTitle = new JLabel();
            pnl4.setLayout(new GridBagLayout());
            htmlTitle.setText(String.format("<html><font size='12' color='%s'>%s</font></html>", titleColor, title));
            htmlTitle.setHorizontalAlignment(JLabel.CENTER);
            htmlTitle.setVerticalAlignment(JLabel.CENTER);
            htmlTitle.setBounds(0, 20, 200, 50);
            pnl4.add(htmlTitle);
        }

        if (subtextFlg == true) {
            // Set the subtext label and add to its associated JPanel
            JLabel htmlTitle1 = new JLabel();
            pnl5.setLayout(new GridBagLayout());
            htmlTitle1.setText(String.format("<html><body style='width: 1000px; text-align:center'><font size='6' color='%s'>%s</font></html>", subtextColor, subtext));
            htmlTitle1.setHorizontalAlignment(JLabel.CENTER);
            htmlTitle1.setVerticalAlignment(JLabel.CENTER);
            htmlTitle1.setPreferredSize(screenSize);
            pnl5.add(htmlTitle1);
        }

        if (imageFlg == true) {
            // Begin to set the image path
            pnl3.setLayout(new GridBagLayout());
            if (imageUrlFlg != null) {
                if (imageUrlFlg == true) {
                    // Read the file path as a buffered image and add to JLabel, then JPanel
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
                    // Read the byte array as a buffered image and add to JLabel, then JPanel
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
     * Method to remove elements to the JPanel
     *
     * @return n/a
     */
    private void RemoveElements () {
        // JPanels that are added to the JFrame

        pnl3.removeAll();
        pnl4.removeAll();
        pnl5.removeAll();
        this.remove(pnl3);
        this.remove(pnl4);
        this.remove(pnl5);
        this.getContentPane().removeAll();

        this.setLayout(null);
        this.getContentPane().setLayout(null);
        pnl3.setLayout(null);
        pnl4.setLayout(null);
        pnl5.setLayout(null);

        // Contents of xmlBillboard
        title = null;
        image = null;
        imageUrlFlg = null;
        subtext = null;

        // Flags to check whether these fields are contained in the XML file
        titleFlg = false;
        imageFlg = false;
        subtextFlg = false;

        // Colour of background and text to be displayed
        backgroundColor = null;
        titleColor = null;
        subtextColor = null;

    }

    /**
     * Method to readd elements to the JPanel
     *
     * @return n/a
     */

    private void ReAddElements () {
        this.revalidate();
        this.getContentPane().revalidate();
        pnl1.revalidate();
        pnl2.revalidate();
        pnl3.revalidate();
        pnl4.revalidate();
        pnl5.revalidate();

        this.getContentPane().repaint();
        this.repaint();
        pnl1.repaint();
        pnl2.repaint();
        pnl3.repaint();
        pnl4.repaint();
        pnl5.repaint();
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
        // Set the constraints to the parameters
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
            // Set GUI rules on startup
            SetGUI();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


    /**
     * Overridden method from ActionListener interface that gets XML file from server, resets components and
     * displays them on the JFrame
     *
     * @param e - Action event object
     * @return n/a
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Sample for a timer action event
        if (source == timer) {
            RemoveElements();
            GetServerResponse();
            try {
                // Set the components every 15 seconds on timer event
                SetFields();
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SAXException ex) {
                ex.printStackTrace();
            }
            // Re paint the JFrame
            SetComponents();
            ReAddElements();
        }
    }

    /**
     * Method to invoke the billboard viewer JFrame
     *
     * @return n/a
     */

    public static void create() {
        SwingUtilities.invokeLater(new BillboardViewer("Billboard Viewer"));
    }

    /**
     * Main method that invokes the billboard viewer JFrame
     *
     * @return n/a
     */

    public static void main(String[] args) {
        create();
    }
}