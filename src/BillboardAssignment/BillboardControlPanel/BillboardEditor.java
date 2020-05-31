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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BillboardEditor extends JFrame implements Runnable, ActionListener {
    public static int WIDTH = 640;
    public static int HEIGHT = 480;

    // Boolean deciding if new billboard or existing
    Boolean newBillboard;
    // Strings of text defining Billboard
    String titleBillboard;
    String imagePathBillboard;
    String subtextBillboard;
    String xmlBillboard;

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
    JCheckBox imagePathCheckBox;
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

    JLabel editorTitle;
    private String[] userData;
    String billboardName;


    public BillboardEditor (String title, String[] userDataInput, String billboardNameInput, Boolean newBillboard) throws HeadlessException {
        super(title);
        this.userData = userDataInput;
        this.billboardName = billboardNameInput;
        this.newBillboard = newBillboard;
    }

    public void SetGUI () {
        // Set preliminaries
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create panels that define the layout
        pnl1 = createPanel(Color.WHITE);
        pnl2 = createPanel(Color.WHITE);
        pnl3 = createPanel(Color.WHITE);
        pnl4 = createPanel(Color.WHITE);
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

        btnCreateBillboard = createButton("Save Billboard");
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
        name.setColumns(20);
        JLabel nameLabel = new JLabel("Name");

        title = createTextField("Insert billboard title");
        title.setColumns(20);
        JLabel titleLabel = new JLabel("Title");
        titleColorR = createTextField("R");
        titleColorR.setColumns(3);
        titleColorG = createTextField("G");
        titleColorG.setColumns(3);
        titleColorB = createTextField("B");
        titleColorB.setColumns(3);

        imagePath = createTextField("Specify the images path");
        imagePath.setColumns(20);
        imagePathLabel = new JLabel("Image Path");
        searchComputer = createButton("Browse PC");


        subtext = createTextArea("Billboard subtext");
        subtextLabel = new JLabel("Billboard Subtext");
        subtextColorR = createTextField("R");
        subtextColorR.setColumns(3);
        subtextColorG = createTextField("G");
        subtextColorG.setColumns(3);
        subtextColorB = createTextField("B");
        subtextColorB.setColumns(3);

        colorLabel = new JLabel("Color (0-255)");

        backgroundColorLabel = new JLabel("Background Color");
        backgroundColorR = createTextField("R");
        backgroundColorR.setColumns(3);
        backgroundColorG = createTextField("G");
        backgroundColorG.setColumns(3);
        backgroundColorB = createTextField("B");
        backgroundColorB.setColumns(3);


        addToPanel(pnl3, name, constraints1,3, 1, 2, 1);
        addToPanel(pnl3, nameLabel, constraints1, 0, 1, 2, 1);

        addToPanel(pnl3, title, constraints1,3, 2, 2, 1);
        addToPanel(pnl3, titleLabel, constraints1, 0, 2, 2, 1);
        addToPanel(pnl3, titleColorR, constraints1, 5, 2, 2, 1);
        addToPanel(pnl3, titleColorG, constraints1, 7, 2, 2, 1);
        addToPanel(pnl3, titleColorB, constraints1, 9, 2, 2, 1);

        addToPanel(pnl3, colorLabel, constraints1, 7, 0, 2, 1);

        addToPanel(pnl3, imagePath, constraints1, 3, 3, 2, 1);
        addToPanel(pnl3, imagePathLabel, constraints1, 0, 3, 2, 1);
        addToPanel(pnl3, searchComputer, constraints1, 3, 4, 2,1);

        addToPanel(pnl3, subtext, constraints1, 3, 5, 2, 1);
        addToPanel(pnl3, subtextLabel, constraints1, 0, 5, 2, 1);
        addToPanel(pnl3, subtextColorR, constraints1, 5, 5, 2, 1);
        addToPanel(pnl3, subtextColorG, constraints1, 7, 5, 2, 1);
        addToPanel(pnl3, subtextColorB, constraints1, 8, 5, 2, 1);

        addToPanel(pnl3, backgroundColorLabel, constraints1, 0, 6, 2, 1);
        addToPanel(pnl3, backgroundColorR, constraints1, 5, 6, 2, 1);
        addToPanel(pnl3, backgroundColorG, constraints1, 7, 6, 2, 1);
        addToPanel(pnl3, backgroundColorB, constraints1, 9, 6, 2, 1);

        editorTitle = new JLabel();
        editorTitle.setText("<html><h1>Billboard Editor</h1></html>");
        editorTitle.setBounds(0, 20, 200, 50);
        addToPanel(pnl4, editorTitle, constraints1, 9, 5, 2, 1);

        if (newBillboard == false) {
            existingBillboardText();
        }
    }

    private void existingBillboardText () {
        return;
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


    @Override
    public void run() {
        SetGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnCancel) {
            dispose();
            MainMenu.create(userData);
        } else if (source == btnSave) {
            System.out.println(2);
        } else if (source == btnPreview) {
            String billboard = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<billboard background = \"#0000FF\">\n" +
                    "    <message>Billboard with message, GIF and information</message>\n" +
                    "    <picture url=\"https://cloudstor.aarnet.edu.au/plus/s/A26R8MYAplgjUhL/download\" />\n" +
                    "    <information colour=\"#60B9FF\">This billboard has a message tag, a picture tag (linking to a URL with a GIF image) and an information tag. The picture is drawn in the centre and the message and information text are centred in the space between the top of the image and the top of the page, and the space between the bottom of the image and the bottom of the page, respectively.</information>\n" +
                    "</billboard>\n";
            BillboardViewer.create(billboard);
        } else if (source == searchComputer) {
             System.out.println("TODO: Search Computer files");
             
        } else if (source == btnCreateBillboard){
            JOptionPane.showMessageDialog(null, "Billboard Created Successfully!\n" +
                    "NOT REALLY, PLEASE INTEGRATE TO SERVER");
            dispose();
            MainMenu.create(userData);
        }
    }

    public static void create(String[] userData, String billboardName, boolean newBillboard) {
        SwingUtilities.invokeLater(new BillboardEditor("Billboard Editor", userData, billboardName, newBillboard));
    }
}
