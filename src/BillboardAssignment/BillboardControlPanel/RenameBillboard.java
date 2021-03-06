package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class RenameBillboard extends JFrame {
    private JButton buttonBack;
    private JPanel panel1;
    private JButton buttonCreate;
    private JTextField fieldName;
    private JLabel labelTitle;
    private JLabel labelName;
    private String[] UserData;
    private String billboardID;

    /**
     * Rename Billboard window object constructor. Sets up GUI and also contains listeners
     *
     * @param titles        - Window Title
     * @param userDataInput - Array containing session key and user ID for user performing the request
     * @return N/A
     */
    private RenameBillboard(String titles, String[] userDataInput, String billboardIDInput, String currentNameInput) {
        super(titles);
        //Setup GUI
        $$$setupUI$$$();
        this.UserData = userDataInput;
        this.billboardID = billboardIDInput;
        fieldName.setText(currentNameInput);
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        //Listener for the back button, simply disposes and returns
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ListBillboards.create(UserData);
            }
        });

        //Listener for rename button, will query the server and if successful, return to billboard list.
        buttonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Sey up Request
                    HashMap<String, String> requestBody = new HashMap<>();
                    requestBody.put("keyId", UserData[1]);
                    requestBody.put("key", UserData[0]);
                    requestBody.put("billboardId", billboardID);
                    requestBody.put("newName", fieldName.getText());

                    //Send Request
                    ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "rename billboard", requestBody);
                    ServerResponse response = request.getResponse();

                    //Catch any error messages returned by server
                    if (!response.status().equals("ok")) {
                        //If error is an invalid session key, dispose and return to login screen
                        if (response.status().equals("Session key invalid")) {
                            dispose();
                            Login.create();
                            JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                        }
                        //Otherwise, generic error.
                        JOptionPane.showMessageDialog(null, "Error! Please Contact IT Support and Quote the Following: \n Rename billboard|" + response.status());
                    }
                    else {
                        //If Response is ok (no errors)
                        JOptionPane.showMessageDialog(null, "Billboard Successfully Renamed");
                        dispose();
                        ListBillboards.create(UserData);
                    }
                }
                catch (Exception f) {
                    JOptionPane.showMessageDialog(null, "Error! Please Try Again or Contact IT Support and Quote the Following: \n Fetch Billboard List |" + f.getMessage());
                }
            }
        });
    }

    /**
     * Create function. Creates instance of GUI
     *
     * @param userDataInput The session key and user ID for the user logged in.
     * @return void
     */
    protected static void create(String[] userDataInput, String billboardID, String currentName) {
        JFrame frame = new RenameBillboard("Billboard Client", userDataInput, billboardID, currentName);
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(10, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-5461075));
        buttonBack = new JButton();
        buttonBack.setText("Cancel");
        panel1.add(buttonBack, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setForeground(new Color(-12828863));
        labelTitle.setText("Rename Billboard");
        panel1.add(labelTitle, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCreate = new JButton();
        buttonCreate.setText("Rename");
        panel1.add(buttonCreate, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldName = new JTextField();
        panel1.add(fieldName, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        labelName = new JLabel();
        labelName.setForeground(new Color(-11578538));
        labelName.setHorizontalAlignment(0);
        labelName.setHorizontalTextPosition(0);
        labelName.setText("New Billboard Name");
        panel1.add(labelName, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(85, 30), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(18, 18), null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(10, 10), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(15, 15), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(15, 15), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(300, 0), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel1.add(spacer7, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
