/* -------------------------------------------------------------------------------------------------
|  BillboardEditor                                                                                  |
|                                                                                                   |
|  By Billy Currie                                                                                  |
|                                                                                                   |
|  Java class to build the Billboard Editing application                                            |
|  User can specify title, image and subtext for the billboard, as well as associated colours       |
|                                                                                                   |
-------------------------------------------------------------------------------------------------- */

package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class BillboardEditor extends JFrame implements Runnable, ActionListener {

    // Boolean deciding if new billboard or existing
    Boolean newBillboard;
    Boolean validFlag;

    // Strings of text defining Billboard
    String billboardID;
    String titleBillboard;
    String titleRBillboard;
    String titleGBillboard;
    String titleBBillboard;
    String imagePathBillboard;
    boolean urlBillboard;
    String subtextBillboard;
    String subtextRBillboard;
    String subtextGBillboard;
    String subtextBBillboard;
    String xmlBillboard;
    String backgroundRBillboard;
    String backgroundGBillboard;
    String backgroundBBillboard;

    // Panels for the Box layout
    JPanel pnl1;
    JPanel pnl2;
    JPanel pnl3;
    JPanel pnl4;
    JPanel pnlBtn;

    // Create primary buttons
    JButton btnCreateBillboard;
    JButton btnCancel;
    JButton btnPreview;
    JButton btnSave;

    // Create a text field for the name of the billboard
    JTextField name;

    // Text fields to get the title elements
    JTextField title;
    JTextField titleColorR;
    JTextField titleColorG;
    JTextField titleColorB;

    // Text fields for the image path and associated checkboxes
    JTextField imagePath;
    JLabel imagePathLabel;
    JCheckBox imageUrlData;
    JCheckBox noImage;
    JButton searchComputer;

    // Text aea for the subtext and associated text fields
    JTextArea subtext;
    JLabel subtextLabel;
    JTextField subtextColorR;
    JTextField subtextColorG;
    JTextField subtextColorB;

    // Get the colours for background and set colour field labels
    JLabel backgroundColorLabel;
    JTextField backgroundColorR;
    JTextField backgroundColorG;
    JTextField backgroundColorB;
    JLabel colorLabel;
    JLabel colorLabel2;
    JLabel RLabel;
    JLabel GLabel;
    JLabel BLabel;

    // Miscellaneous variables
    JLabel editorTitle;
    private String[] userData;
    String billboardName;


    /**
     * Billboard Editor Constructor Method. Initialises information about the billboards
     *
     * @param title - Window Title
     * @param userDataInput - Array containing session key and user ID for user performing the request
     * @param billboard - String array containing information about the billboard to be displayed
     * @param newBillboard - boolean flag to indicate whether the class is being called from the edit or create menus
     * @return N/A
     */

    public BillboardEditor (String title, String[] userDataInput, String[] billboard, Boolean newBillboard) throws HeadlessException {
        super(title);

        this.userData = userDataInput;
        this.billboardID = billboard[0];
        this.billboardName = billboard[2];
        this.xmlBillboard = billboard[3];
        this.newBillboard = newBillboard;
    }

    /**
     * Billboard Editor Overloaded Constructor Method. Initialises information about the billboards upon creation
     *
     * @param title - Window Title
     * @param userDataInput - Array containing session key and user ID for user performing the request
     * @param billboardName - billboard title, passed in on creation
     * @param newBillboard - boolean flag to indicate whether the class is being called from the edit or create menus
     * @return N/A
     */

    public BillboardEditor (String title, String[] userDataInput, String billboardName, Boolean newBillboard) throws HeadlessException {
        super(title);

        this.userData = userDataInput;
        this.billboardName = billboardName;
        this.newBillboard = newBillboard;
    }

    /**
     * Set GUI method, used to set preliminary fields defining the JFrame appearance and operation
     *
     * @return N/A
     */

    public void SetGUI ()  {
        // Set preliminaries
        setVisible(true);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create panels that define the layout
        pnl1 = createPanel(Color.WHITE);
        pnl2 = createPanel(Color.WHITE);
        pnl3 = createPanel(Color.WHITE);
        pnl4 = createPanel(Color.ORANGE);
        pnlBtn = createPanel(Color.GRAY);

        // Set sizes of the panels

        setLayout(new BorderLayout());
        pnl1.setPreferredSize(new Dimension(50,200));
        pnl2.setPreferredSize(new Dimension(50,200));
        pnl3.setPreferredSize(new Dimension(400,400));
        pnl4.setPreferredSize(new Dimension(400,50));
        pnlBtn.setPreferredSize(new Dimension(400,50));


        // Set where the panels are located

        this.getContentPane().add(pnl1, BorderLayout.WEST);
        this.getContentPane().add(pnl2, BorderLayout.EAST);
        this.getContentPane().add(pnl3, BorderLayout.CENTER);
        this.getContentPane().add(pnl4, BorderLayout.NORTH);
        this.getContentPane().add(pnlBtn, BorderLayout.SOUTH);

        // Set Layout of Buttons
        pnlBtn.setLayout(new GridBagLayout());

        // Create the core buttons
        btnCreateBillboard = createButton("Save Billboard");
        btnCancel = createButton("Cancel");
        btnPreview = createButton("Preview");
        btnSave = createButton("Cache Billboard");

        // Set the grid bag constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 100;
        constraints.weighty = 100;

        // Add buttons to the panel
        addToPanel(pnlBtn, btnCreateBillboard, constraints,3, 0, 2, 1);
        addToPanel(pnlBtn, btnCancel, constraints, 3, 2, 2, 1);
        addToPanel(pnlBtn, btnPreview, constraints, 0, 0, 2, 1);
        addToPanel(pnlBtn, btnSave, constraints, 0, 2, 2, 1);

        // Set Layout of textfields
        pnl3.setLayout(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.anchor = GridBagConstraints.NORTHWEST;
        constraints1.insets = new Insets(6,3,6,3);

        // Create the name text field
        name = createTextField(billboardName);
        name.setEnabled(false);
        name.setColumns(30);
        JLabel nameLabel = new JLabel("Name");

        // Create the title textfield and the colours associated
        title = createTextField("Insert billboard title");
        title.setColumns(30);
        JLabel titleLabel = new JLabel("Title");
        titleColorR = createTextField("0");
        titleColorR.setColumns(3);
        titleColorG = createTextField("0");
        titleColorG.setColumns(3);
        titleColorB = createTextField("0");
        titleColorB.setColumns(3);

        // Create the imagePath text field and associated checkboxes
        imagePath = createTextField("Specify the images path/ a byte representation");
        imagePath.setColumns(30);
        imagePathLabel = new JLabel("Image Path");
        imageUrlData = new JCheckBox("Image a byte array?");
        noImage = new JCheckBox("No image?");
        searchComputer = createButton("Browse PC");

        // Create the subtext fields and associated colour fields
        subtext = createTextArea("Billboard subtext");
        subtext.setColumns(30);
        subtextLabel = new JLabel("Billboard Subtext");
        subtextColorR = createTextField("0");
        subtextColorR.setColumns(3);
        subtextColorG = createTextField("0");
        subtextColorG.setColumns(3);
        subtextColorB = createTextField("0");
        subtextColorB.setColumns(3);

        // Create the labels for the colours
        colorLabel = new JLabel("Color");
        colorLabel2 = new JLabel("(0-255)");
        RLabel = new JLabel("R");
        GLabel = new JLabel("G");
        BLabel = new JLabel("B");

        // Create the background colour labels
        backgroundColorLabel = new JLabel("Background Color");
        backgroundColorR = createTextField("255");
        backgroundColorR.setColumns(3);
        backgroundColorG = createTextField("255");
        backgroundColorG.setColumns(3);
        backgroundColorB = createTextField("255");
        backgroundColorB.setColumns(3);


        // Add all the components to the panels they are associated with
        addToPanel(pnl3, name, constraints1,3, 1, 2, 1);
        addToPanel(pnl3, nameLabel, constraints1, 0, 1, 2, 1);

        addToPanel(pnl3, title, constraints1,3, 2, 2, 1);
        addToPanel(pnl3, titleLabel, constraints1, 0, 2, 2, 1);
        addToPanel(pnl3, titleColorR, constraints1, 5, 2, 2, 1);
        addToPanel(pnl3, titleColorG, constraints1, 7, 2, 2, 1);
        addToPanel(pnl3, titleColorB, constraints1, 9, 2, 2, 1);

        addToPanel(pnl3, colorLabel, constraints1, 5, 0, 2, 1);
        addToPanel(pnl3, colorLabel2, constraints1, 7, 0, 2, 1);
        addToPanel(pnl3, RLabel, constraints1, 5, 1, 2, 1);
        addToPanel(pnl3, GLabel, constraints1, 7, 1, 2, 1);
        addToPanel(pnl3, BLabel, constraints1, 9, 1, 2, 1);

        addToPanel(pnl3, imagePath, constraints1, 3, 3, 2, 1);
        addToPanel(pnl3, imagePathLabel, constraints1, 0, 3, 2, 1);
        addToPanel(pnl3, imageUrlData, constraints1, 3, 5, 2, 1);
        addToPanel(pnl3, noImage, constraints1, 0, 4, 2, 1);
        addToPanel(pnl3, searchComputer, constraints1, 3, 4, 2,1);

        addToPanel(pnl3, subtext, constraints1, 3, 6, 2, 1);
        addToPanel(pnl3, subtextLabel, constraints1, 0, 6, 2, 1);
        addToPanel(pnl3, subtextColorR, constraints1, 5, 6, 2, 1);
        addToPanel(pnl3, subtextColorG, constraints1, 7, 6, 2, 1);
        addToPanel(pnl3, subtextColorB, constraints1, 9, 6, 2, 1);

        addToPanel(pnl3, backgroundColorLabel, constraints1, 0, 7, 2, 1);
        addToPanel(pnl3, backgroundColorR, constraints1, 5, 7, 2, 1);
        addToPanel(pnl3, backgroundColorG, constraints1, 7, 7, 2, 1);
        addToPanel(pnl3, backgroundColorB, constraints1, 9, 7, 2, 1);

        // Set a title for the editor
        editorTitle = new JLabel();
        editorTitle.setText("<html><h1>Billboard Editor</h1></html>");
        editorTitle.setBounds(0, 20, 200, 50);
        addToPanel(pnl4, editorTitle, constraints1, 9, 5, 2, 1);
    }

    /**
     * Method to prepopulate GUI fields if the user is editing an existing billboard
     *
     * @return N/A
     */

    private void PrepopulateGUIFields () throws ParserConfigurationException, IOException, SAXException {
        // Create document builder objects
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse XML as bytes and convert to document object
        ByteArrayInputStream bais = new ByteArrayInputStream(xmlBillboard.getBytes());
        Document document = builder.parse(bais);
        bais.close();

        // Root billboard element
        Element documentElement = document.getDocumentElement();
        String attributeValue = documentElement.getAttribute("background");

        // Set background color to default to white if the element is empty
        if (attributeValue.isEmpty()) {
            backgroundRBillboard = "255";
            backgroundGBillboard = "255";
            backgroundBBillboard = "255";

            backgroundColorR.setText(backgroundRBillboard);
            backgroundColorG.setText(backgroundGBillboard);
            backgroundColorB.setText(backgroundBBillboard);
        } else {
            // Parse the hex value as integers then strings
            backgroundRBillboard = String.valueOf(Integer.parseInt(attributeValue.substring(1,3), 16));
            backgroundGBillboard = String.valueOf(Integer.parseInt(attributeValue.substring(3,5), 16));
            backgroundBBillboard = String.valueOf(Integer.parseInt(attributeValue.substring(5,7), 16));

            backgroundColorR.setText(backgroundRBillboard);
            backgroundColorG.setText(backgroundGBillboard);
            backgroundColorB.setText(backgroundBBillboard);
        }

        // Get the child notes of the billboard root
        NodeList nl = documentElement.getChildNodes();

        // Foreach child node
        for (int i =0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName() == "message") {
                    // Set title fields according to the title tag
                    titleBillboard = element.getTextContent();
                    title.setText(titleBillboard);
                    String dummyTitleColor = element.getAttribute("colour");
                    titleRBillboard = String.valueOf(Integer.parseInt(dummyTitleColor.substring(1,3), 16));
                    titleGBillboard = String.valueOf(Integer.parseInt(dummyTitleColor.substring(3,5), 16));
                    titleBBillboard = String.valueOf(Integer.parseInt(dummyTitleColor.substring(5,7), 16));

                    titleColorR.setText(titleRBillboard);
                    titleColorG.setText(titleGBillboard);
                    titleColorB.setText(titleBBillboard);
                } else if (element.getTagName() == "picture") {
                    // Set the url fields
                    if ((element.hasAttribute("url")) && (!element.hasAttribute("data"))) {
                        // Set the image fields according to the URL tag
                        imagePathBillboard = element.getAttribute("url");
                        imagePath.setText(imagePathBillboard);
                        imageUrlData.setSelected(false);
                        if (imagePathBillboard == "") {
                            noImage.setSelected(true);
                        } else {
                            noImage.setSelected(false);
                        }
                    } else if ((element.hasAttribute("data")) && (!element.hasAttribute("url"))) {
                        // Set the image fields according to the Data tag
                        imagePathBillboard = element.getAttribute("data");
                        imageUrlData.setSelected(true);
                        if (imagePathBillboard == "") {
                            noImage.setSelected(true);
                        } else {
                            noImage.setSelected(false);
                        }
                    }
                } else if (element.getTagName() == "information") {
                    // Set the subtext fields according to the information tags
                    subtextBillboard = element.getTextContent();
                    subtext.setText(subtextBillboard);
                    String dummySubtextColor = element.getAttribute("colour");
                    subtextRBillboard = String.valueOf(Integer.parseInt(dummySubtextColor.substring(1,3), 16));
                    subtextGBillboard = String.valueOf(Integer.parseInt(dummySubtextColor.substring(3,5), 16));
                    subtextBBillboard = String.valueOf(Integer.parseInt(dummySubtextColor.substring(5,7), 16));

                    subtextColorR.setText(subtextRBillboard);
                    subtextColorG.setText(subtextGBillboard);
                    subtextColorB.setText(subtextBBillboard);
                }
            }
        }
    }

    /**
     * Add to panel helper method that adds a component to a JPanel
     *
     * @param jp - JPanel object to be added to
     * @param c - component that is to be added to the JPanel
     * @param constraints - constraints of the GridBagLayout
     * @param x - integer defining which column of the grid the component will be added to
     * @param x - integer defining which row of the grid the component will be added to
     * @param w - integer defining how many columns the component will occupy
     * @param h - integer defining how many rows the component will occupy
     * @return N/A
     */

    private void addToPanel(JPanel jp, Component c, GridBagConstraints constraints,
                            int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }

    /**
     * Helper method to create a JTextArea component
     *
     * @param placeholder - Placeholder text that is initialised in the text area
     * @return jtextarea - A JTextArea object
     */

    private JTextArea createTextArea (String placeholder) {
        JTextArea jtextarea = new JTextArea(placeholder, 5, 20);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        jtextarea.setBorder(border);
        jtextarea.setLineWrap(true);
        jtextarea.setWrapStyleWord(true);
        return jtextarea;
    }


    /**
     * Helper method to create a JTextField component
     *
     * @param placeholder - Placeholder text that is initialised in the text field
     * @return jtextfield - A JTextField object
     */

    private JTextField createTextField(String placeholder) {
        JTextField jtextfield = new JTextField(placeholder);
        jtextfield.addActionListener(this);
        return jtextfield;
    }


    /**
     * Helper method to create a JButton component
     *
     * @param text - Text that the JButton will be shown with
     * @return jbutton - A JButton object
     */

    private JButton createButton(String text) {
        JButton jbutton = new JButton();
        jbutton.setText(text);
        jbutton.addActionListener(this);
        return jbutton;
    }


    /**
     * Helper method to create a JPanel
     *
     * @param color - Color object that sets the background colour of the JPanel
     * @return panel - A JPanel object
     */

    private static JPanel createPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        return panel;
    }

    /**
     * Helper method to get the text currently residing in the text fields and update
     * the local variables associated with them
     *
     * @return true/false - Return false if any of the fields are entered incorrectly
     */

    private boolean GetTextFields() {
        // Get the text fields and buttons and set the associated values accordingly
        titleBillboard = title.getText();
        titleRBillboard = titleColorR.getText();
        titleGBillboard = titleColorG.getText();
        titleBBillboard = titleColorB.getText();

        imagePathBillboard = imagePath.getText();

        if (imageUrlData.isSelected()) {
            urlBillboard = false;
        } else {
            urlBillboard = true;
        }

        if (noImage.isSelected()) {
            imagePathBillboard = "";
        }

        subtextBillboard = subtext.getText();
        subtextRBillboard = subtextColorR.getText();
        subtextGBillboard = subtextColorG.getText();
        subtextBBillboard = subtextColorB.getText();

        backgroundRBillboard = backgroundColorR.getText();
        backgroundGBillboard = backgroundColorG.getText();
        backgroundBBillboard = backgroundColorB.getText();

        if ((titleRBillboard.length() > 0) && (titleGBillboard.length() > 0) && (titleBBillboard.length() > 0)) {
            // Convert title colours to ints
            int titleRInt = -1;
            int titleGInt = -1;
            int titleBInt = -1;
            try {
                titleRInt = Integer.parseInt(titleRBillboard);
                titleGInt = Integer.parseInt(titleGBillboard);
                titleBInt = Integer.parseInt(titleBBillboard);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Title colour must be integers in the range of 0-255 inclusive");
                return false;
            }

            if (((titleRInt < 0) || (titleRInt > 255)) || ((titleGInt < 0) || (titleGInt > 255)) || ((titleBInt < 0) || (titleBInt > 255))) {
                JOptionPane.showMessageDialog(this, "Title colour must be integers in the range of 0-255 inclusive");
                return false;
            }
        }

        if ((subtextRBillboard.length() > 0) && (subtextGBillboard.length() > 0) && (subtextBBillboard.length() > 0)) {
            // Convert title colours to ints
            int subtextRInt = -1;
            int subtextGInt = -1;
            int subtextBInt = -1;
            try {
                subtextRInt = Integer.parseInt(subtextRBillboard);
                subtextGInt = Integer.parseInt(subtextGBillboard);
                subtextBInt = Integer.parseInt(subtextBBillboard);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Subtext colour must be integers in the range of 0-255 inclusive");
                return false;
            }

            if (((subtextRInt < 0) || (subtextRInt > 255)) || ((subtextGInt < 0) || (subtextGInt > 255)) || ((subtextBInt < 0) || (subtextBInt > 255))) {
                JOptionPane.showMessageDialog(this, "Subtext colour must be integers in the range of 0-255 inclusive");
                return false;
            }
        }

        if ((backgroundRBillboard.length() > 0) && (backgroundGBillboard.length() > 0) && (backgroundBBillboard.length() > 0)) {
            // Convert title colours to ints
            int backgroundRInt = -1;
            int backgroundGInt = -1;
            int backgroundBInt = -1;
            try {
                backgroundRInt = Integer.parseInt(backgroundRBillboard);
                backgroundGInt = Integer.parseInt(backgroundGBillboard);
                backgroundBInt = Integer.parseInt(backgroundBBillboard);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Background colours must be integers in the range of 0-255 inclusive");
                return false;
            }

            if (((backgroundRInt < 0) || (backgroundRInt > 255)) || ((backgroundGInt < 0) || (backgroundGInt > 255)) || ((backgroundBInt < 0) || (backgroundBInt > 255))) {
                JOptionPane.showMessageDialog(this, "Background colours must be integers in the range of 0-255 inclusive");
                return false;
            }
        }

        if (!noImage.isSelected()) {
            if (urlBillboard == true) {
                BufferedImage myPicture;
                try {
                    myPicture = ImageIO.read(new File(imagePathBillboard));
                } catch (IOException e){
                    JOptionPane.showMessageDialog(this, "Invalid Url.");
                    return false;
                }
            } else if (urlBillboard == false) {
                byte[] imageByte;
                BufferedImage myPicture;
                try {
                    imageByte = Base64.getDecoder().decode(imagePathBillboard);
                    myPicture = ImageIO.read(new ByteArrayInputStream(imageByte));
                } catch (IOException e){
                    JOptionPane.showMessageDialog(this, "Invalid Byte Array.");
                    return false;
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Invalid Byte Array.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Sends a request to create a new billboard to the server. All exceptions occurring as a result are handled in the method.
     * @param billboardName - name of the billboard to be created
     * @param xmlBillboard - xml string that stores billboard info
     * @param userData - A string array that stores the user information
     * @return int 1 - Successful 0 - Fail
     */

    private int CreateBillboardRequest(String billboardName, String xmlBillboard, String[] userData) {
        try {
            // Initialise request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("keyId", userData[1]);
            requestBody.put("billboardName", billboardName);
            requestBody.put("content", xmlBillboard);
            requestBody.put("key", userData[0]);

            //Send Request
            ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "create", requestBody);
            ServerResponse response = request.getResponse();

            //If Successful, return 1, else return 0 and give error dialog.
            if (response.status().equals("ok")) {
                return (1);
            } else {
                //Check if error was due to session key expiry and process accordingly
                if (response.status().equals("Session key invalid")) {
                    dispose();
                    Login.create();
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    return (0);
                }
                //If not then treat as generic error
                JOptionPane.showMessageDialog(null, "Error, request rejected by server\n" +
                        response.status());
                return (0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Create Billboard | " + e.getMessage());
            return (0);
        }
    }

    /**
     * Sends a request to edit an existing billboard in the server. All exceptions occuring as a result are handled in the method.
     * @param billboardID - name of the billboard to be created
     * @param xmlBillboard - xml string that stores billboard info
     * @param userData - a string array containing the users data
     * @return int 1 - Successful 0 - Fail
     */

    private int EditBillboardRequest(String billboardID, String xmlBillboard, String[] userData) {
        try {
            // Initialise request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("keyId", userData[1]);
            requestBody.put("billboardId", billboardID);
            requestBody.put("newContent", xmlBillboard);
            requestBody.put("key", userData[0]);

            //Send Request
            ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "edit billboard", requestBody);
            ServerResponse response = request.getResponse();

            //If Successful, return 1, else return 0 and give error dialog.
            if (response.status().equals("ok")) {
                return (1);
            } else {
                //Check if error was due to session key expiry and process accordingly
                if (response.status().equals("Session key invalid")) {
                    dispose();
                    Login.create();
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    return (0);
                }
                //If not then treat as generic error
                JOptionPane.showMessageDialog(null, "Error, request rejected by server\n" +
                        response.status());
                return (0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \nEdit Billboard | " + e.getMessage());
            return (0);
        }
    }

    /**
     * Overridden method from Runnable interface that sets GUI on default and pre-populates fields
     *
     * @return n/a
     */

    @Override
    public void run() {
        // Set GUI upon bootup
        SetGUI();

        if (newBillboard == false) {
            try {
                PrepopulateGUIFields();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Overridden method from ActionListener interface that responds to JButton requests
     *
     * @param e - Action event object passed in when an event occurs
     * @return n/a
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Check for button objects
        if (source == btnCancel) {
            // Return to main menu or the list billboards menu
            dispose();
            if(newBillboard == true) {
                MainMenu.create(userData);
            }
            else{
                ListBillboards.create(userData);
            }
        } else if (source == btnSave) {
            // Cache the text that is entered
            validFlag = GetTextFields();
            try {
                // Build XML string from fields
                xmlBillboard = XMLBuilder.WriteXML(titleBillboard, titleRBillboard, titleGBillboard, titleBBillboard,
                        imagePathBillboard, urlBillboard,
                        subtextBillboard, subtextRBillboard, subtextGBillboard, subtextBBillboard,
                        backgroundRBillboard, backgroundGBillboard, backgroundBBillboard);
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            } catch (TransformerException ex) {
                ex.printStackTrace();
            }
        } else if (source == btnPreview) {
            // Cache the billboard before preview
            ActionEvent btnSaveSim = new ActionEvent(btnSave, 1234, "CommandToPerform");
            actionPerformed(btnSaveSim);

            // Check if the fields are valid
            if (validFlag) {
                JOptionPane.showMessageDialog(null, "Previewer Will Launch in Fullscreen Mode.\n" +
                        "Press Escape to Exit Preview");
                BillboardPreviewer.create(xmlBillboard);
            }
        } else if (source == searchComputer) {
             // Set up file chooser object to return an image path as a byte array
             FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpeg", "png", "bmp", "jpg");
             JFileChooser j = new JFileChooser();
             j.setFileFilter(filter);
             j.setAcceptAllFileFilterUsed(false);
             int approveVal = j.showSaveDialog(BillboardEditor.this);

             if (approveVal == JFileChooser.APPROVE_OPTION) {
                 String fileName = j.getSelectedFile().getName();
                 String tempDirectory = j.getCurrentDirectory().toString();
                 String tempImagePath = tempDirectory + "\\" + fileName;

                 try {
                     FileInputStream fis = new FileInputStream(tempImagePath);
                     byte[] bytes = fis.readAllBytes();
                     String s = Base64.getEncoder().encodeToString(bytes);
                     imagePath.setText(s);
                     imageUrlData.setSelected(true);
                 } catch (FileNotFoundException ex) {
                     ex.printStackTrace();
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }

             }

        } else if (source == btnCreateBillboard){
            JOptionPane.showMessageDialog(null, "Press ok to send billboard to server");

            // Cache billboard first
            ActionEvent btnSaveSim = new ActionEvent(btnSave, 1234, "CommandToPerform");
            actionPerformed(btnSaveSim);

            // Check if valid
            if (validFlag) {
                if(newBillboard == true) {
                    // Send new billboard to server and return to main menu if successful
                    int successful = CreateBillboardRequest(billboardName, xmlBillboard, userData);
                    dispose();
                    if (successful > 0) {
                        MainMenu.create(userData);
                    }
                } else if (newBillboard == false) {
                    // Send edited billboard to server and return to list billboards if successful
                    int successful = EditBillboardRequest(billboardID, xmlBillboard, userData);
                    dispose();
                    if (successful > 0) {
                        MainMenu.create(userData);
                    }
                }
                else{
                    // Return to list billboards if anything unaccounted for happens
                    ListBillboards.create(userData);
                }
            }
        }
    }

    /**
     * Method to create the billboard editor from the edit billboard menu
     *
     * @params userData - String array containing the user data and associated session information
     * @params billboardName - String array that contains information about the billboard being edited
     * @return n/a
     */

    public static void create(String[] userData, String[] billboard) {
        boolean newBillboard = false;
        SwingUtilities.invokeLater(new BillboardEditor("Billboard Editor", userData, billboard, newBillboard));
    }

    /**
     * Method to create the billboard editor from the create billboard menu
     *
     * @params userData - String array containing the user data and associated session information
     * @params billboardName - String that contains the name of the billboard to be created
     * @return n/a
     */

    public static void create(String[] userData, String billboardName) {
        boolean newBillboard = true;
        SwingUtilities.invokeLater(new BillboardEditor("Billboard Editor", userData, billboardName, newBillboard));
    }
}
