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

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.Billboard;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.BillboardManager;
import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import org.w3c.dom.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static BillboardAssignment.BillboardServer.Server.Tests.TestUserControllers.requestBodyWithKey;

public class BillboardEditor extends JFrame implements Runnable, ActionListener {
    public static int WIDTH = 1200;
    public static int HEIGHT = 700;

    // Boolean deciding if new billboard or existing
    Boolean newBillboard;
    Boolean validFlag;
    // Strings of text defining Billboard
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

    // Label for the header title
    JLabel heading;

    JButton btnCreateBillboard;
    JButton btnCancel;
    JButton btnPreview;
    JButton btnSave;

    JTextField name;
    JLabel nameLabel;

    JTextField title;
    JLabel titleLabel;
    JTextField titleColorR;
    JTextField titleColorG;
    JTextField titleColorB;

    JTextField imagePath;
    JLabel imagePathLabel;
    JCheckBox imageUrlData;
    JCheckBox noImage;
    JButton searchComputer;

    JTextArea subtext;
    JLabel subtextLabel;
    JFormattedTextField subtextColor;
    JTextField subtextColorR;
    JTextField subtextColorG;
    JTextField subtextColorB;

    JLabel backgroundColorLabel;
    JTextField backgroundColorR;
    JTextField backgroundColorG;
    JTextField backgroundColorB;
    JLabel colorLabel;
    JLabel colorLabel2;
    JLabel RLabel;
    JLabel GLabel;
    JLabel BLabel;

    JLabel editorTitle;
    private String[] userData;
    String billboardName;


    public BillboardEditor (String title, String[] userDataInput, String billboardNameInput, Boolean newBillboard) throws HeadlessException {
        super(title);
        this.userData = userDataInput;
        this.billboardName = billboardNameInput;
        this.newBillboard = newBillboard;
    }

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

        btnCreateBillboard = createButton("Create Billboard");
        btnCancel = createButton("Cancel");
        btnPreview = createButton("Preview");
        btnSave = createButton("Save");

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 100;
        constraints.weighty = 100;

        addToPanel(pnlBtn, btnCreateBillboard, constraints,3, 0, 2, 1);
        addToPanel(pnlBtn, btnCancel, constraints, 3, 2, 2, 1);
        addToPanel(pnlBtn, btnPreview, constraints, 0, 0, 2, 1);
        addToPanel(pnlBtn, btnSave, constraints, 0, 2, 2, 1);

        // Set Layout of textfields

        pnl3.setLayout(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.anchor = GridBagConstraints.NORTHWEST;
        constraints1.insets = new Insets(6,3,6,3);

        name = createTextField(billboardName);
        name.setEnabled(false);
        name.setColumns(30);
        JLabel nameLabel = new JLabel("Name");

        title = createTextField("Insert billboard title");
        title.setColumns(30);
        JLabel titleLabel = new JLabel("Title");
        titleColorR = createTextField("0");
        titleColorR.setColumns(3);
        titleColorG = createTextField("0");
        titleColorG.setColumns(3);
        titleColorB = createTextField("0");
        titleColorB.setColumns(3);

        imagePath = createTextField("Specify the images path/ a byte representation");
        imagePath.setColumns(30);
        imagePathLabel = new JLabel("Image Path");
        imageUrlData = new JCheckBox("Image a byte array?");
        noImage = new JCheckBox("No image?");
        searchComputer = createButton("Browse PC");


        subtext = createTextArea("Billboard subtext");
        subtext.setColumns(30);
        subtextLabel = new JLabel("Billboard Subtext");
        subtextColorR = createTextField("0");
        subtextColorR.setColumns(3);
        subtextColorG = createTextField("0");
        subtextColorG.setColumns(3);
        subtextColorB = createTextField("0");
        subtextColorB.setColumns(3);

        colorLabel = new JLabel("Color");
        colorLabel2 = new JLabel("(0-255)");
        RLabel = new JLabel("R");
        GLabel = new JLabel("G");
        BLabel = new JLabel("B");

        backgroundColorLabel = new JLabel("Background Color");
        backgroundColorR = createTextField("255");
        backgroundColorR.setColumns(3);
        backgroundColorG = createTextField("255");
        backgroundColorG.setColumns(3);
        backgroundColorB = createTextField("255");
        backgroundColorB.setColumns(3);

        /*
        if (newBillboard == false) {


        }
*/


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

        editorTitle = new JLabel();
        editorTitle.setText("<html><h1>Billboard Editor</h1></html>");
        editorTitle.setBounds(0, 20, 200, 50);
        addToPanel(pnl4, editorTitle, constraints1, 9, 5, 2, 1);

    }

    private void addToPanel(JPanel jp, Component c, GridBagConstraints constraints,
                            int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }

    private JTextArea createTextArea (String placeholder) {
        JTextArea jtextarea = new JTextArea(placeholder, 5, 20);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        jtextarea.setBorder(border);
        jtextarea.setLineWrap(true);
        jtextarea.setWrapStyleWord(true);
        return jtextarea;
    }
    private JTextField createTextField(String placeholder) {
        JTextField jtextfield = new JTextField(placeholder);
        jtextfield.addActionListener(this);
        return jtextfield;
    }
    private JButton createButton(String text) {
        JButton jbutton = new JButton();
        jbutton.setText(text);
        jbutton.addActionListener(this);
        return jbutton;
    }

    private static JPanel createPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        return panel;
    }
    
    private boolean GetTextFields() {
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

        System.out.println(imagePathBillboard);

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
     * Sends a request to create a new billboardto the server. All exceptions occuring as a result are handled in the method.
     * @param billboardName - name of the billboard to be created
     * @param xmlBillboard - xml string that stores billboard info
     * @return int 1 - Successful 0 - Fail
     */
    private int CreateBillboardRequest(String billboardName, String xmlBillboard, String[] userData) {
        try {
            HashMap<String, String> requestBody = requestBodyWithKey();
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
                JOptionPane.showMessageDialog(null, "Error, request rejected by server\n" +
                        response.status());
                return (0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Create Billboard | " + e.getMessage());
            return (0);
        }
    }

    @Override
    public void run() {
        SetGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnCancel) {
            dispose();
            if(newBillboard == true) {
                MainMenu.create(userData);
            }
            else{
                ListBillboards.create(userData);
            }
        } else if (source == btnSave) {
            validFlag = GetTextFields();
        } else if (source == btnPreview) {
            ActionEvent btnSaveSim = new ActionEvent(btnSave, 1234, "CommandToPerform");
            actionPerformed(btnSaveSim);

            if (validFlag) {
                try {
                    xmlBillboard = XMLBuilder.WriteXML(titleBillboard, titleRBillboard, titleGBillboard, titleBBillboard,
                            imagePathBillboard, urlBillboard,
                            subtextBillboard, subtextRBillboard, subtextGBillboard, subtextBBillboard,
                            backgroundRBillboard, backgroundGBillboard, backgroundBBillboard);
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (TransformerException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Previewer Will Launch in Fullscreen Mode.\n" +
                        "Press Escape to Exit Preview");
                BillboardViewer.create(xmlBillboard);
            }
        } else if (source == searchComputer) {
             System.out.println("TODO: Search Computer files");

        } else if (source == btnCreateBillboard){
            JOptionPane.showMessageDialog(null, "Press ok to send billboard to server");

            ActionEvent btnSaveSim = new ActionEvent(btnSave, 1234, "CommandToPerform");
            actionPerformed(btnSaveSim);

            if (validFlag) {
                dispose();
                if(newBillboard == true) {
                    int successful = CreateBillboardRequest(billboardName, xmlBillboard, userData);
                    if (successful > 0) {
                        MainMenu.create(userData);
                    }
                }
                else{
                    ListBillboards.create(userData);
                }
            }
        }
    }

    public static void create(String[] userData, String billboardName, boolean newBillboard) {
        SwingUtilities.invokeLater(new BillboardEditor("Billboard Editor", userData, billboardName, newBillboard));
    }
}
