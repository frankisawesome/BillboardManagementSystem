package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUser extends JFrame {
    private JLabel labelTitle;
    private JLabel labelName;
    private JPasswordField passwordNew1;
    private JPasswordField passwordNew2;
    private JButton buttonConfirm;
    private JButton buttonCancel;
    private JTextField fieldUsername;
    private JPanel Background;
    private String[] UserData;

    /**
     * Change user window object constructor. Sets up GUI and also contains listeners
     *
     * @param titles        - Window Title
     * @param userDataInput - Array containing session key and user ID for user performing the request
     * @return N/A
     */
    private CreateUser(String titles, String[] userDataInput) {
        super(titles);
        //Setup GUI
        $$$setupUI$$$();
        this.UserData = userDataInput;
        this.setContentPane(Background);
        this.pack();

        //Listener for the back button, simply disposes and creates main menu
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserManage.create(UserData);
            }
        });
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Fetch user inputs.
                String userName = fieldUsername.getText();
                char[] pwdChar = passwordNew1.getPassword();
                String pwd1 = new String(pwdChar);
                char[] pwdChar2 = passwordNew2.getPassword();
                String pwd2 = new String(pwdChar2);

                //Check validity of inputs
                if (!userName.equals("")) {
                    if (pwd1.equals(pwd2)) {
                        if (!pwd1.equals("")) {
                            //Send request to server, if an error occurs it will be handled in the request function.
                            //1 returned - successful, 0 returned - fail
                            if (CreateUserRequest(userName, pwd1) == 1) {
                                JOptionPane.showMessageDialog(null, "User successfully created!\n" +
                                        "Please give user permissions via Edit User in the User Management Tool");
                                dispose();
                                UserManage.create(UserData);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Error! Password cannot be blank.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Error! Entered passwords do not match.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error! Username cannot be blank.");
                }
            }
        });
    }

    /**
     * Sends a request to create a new user to the server. All exceptions occuring as a result are handled in the method.
     *
     * @param id  - User ID for the new user
     * @param pwd - Unhashed password for the new user.
     * @return int 1 - Successful 2 - Fail
     */
    private int CreateUserRequest(String id, String pwd) {
        try {
            //Hash the new password according to a generic salt
            String saltString = "mahna mahna";
            pwd = hashPassword(pwd, saltString);

            //Setup Server Request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("keyId", UserData[1]);
            requestBody.put("newUserName", id);
            requestBody.put("password", pwd);
            requestBody.put("key", UserData[0]);

            //Send Request
            ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "create", requestBody);
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
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Create User | " + e.getMessage());
            return (0);
        }
    }

    /**
     * Create function. Creates instance of GUI
     *
     * @param userDataInput The session key and user ID for the user logged in.
     * @return void
     */
    //Method to create GUI
    protected static void create(String[] userDataInput) {
        JFrame frame = new CreateUser("Billboard Client", userDataInput);
        frame.setVisible(true);
    }

    /**
     * Hash a given password using the SHA-512 algorithm
     *
     * @param password
     * @param salt
     * @return A byte array of the hashed password
     */
    private String hashPassword(String password, String salt) {

        /* Initialise the algorithm data or the IDE will get mad */
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) { /* This will never happen as SHA-512 is definitely an algorithm */
            e.printStackTrace();
        }

        /* Set the hashing algo to use our salt */
        md.update(salt.getBytes());
        String hashedPassword = null;
        try {
            hashedPassword = new String(md.digest(password.getBytes()), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); /* Will never happen */
        }
        return hashedPassword;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Background = new JPanel();
        Background.setLayout(new GridLayoutManager(13, 4, new Insets(0, 0, 0, 0), -1, -1));
        Background.setBackground(new Color(-5461075));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setForeground(new Color(-12828863));
        labelTitle.setText("Create User");
        Background.add(labelTitle, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        Background.add(spacer1, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 30), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        Background.add(spacer2, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 15), null, null, 0, false));
        labelName = new JLabel();
        labelName.setForeground(new Color(-11578538));
        labelName.setHorizontalAlignment(0);
        labelName.setHorizontalTextPosition(0);
        labelName.setText("Password");
        Background.add(labelName, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-11578538));
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("User Name");
        Background.add(label1, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordNew1 = new JPasswordField();
        Background.add(passwordNew1, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        Background.add(spacer3, new GridConstraints(7, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 8), null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setForeground(new Color(-11578538));
        label2.setHorizontalAlignment(0);
        label2.setHorizontalTextPosition(0);
        label2.setText("Re-Enter Password");
        Background.add(label2, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordNew2 = new JPasswordField();
        Background.add(passwordNew2, new GridConstraints(9, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        Background.add(spacer4, new GridConstraints(10, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
        buttonConfirm = new JButton();
        buttonConfirm.setText("Confirm");
        Background.add(buttonConfirm, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        Background.add(buttonCancel, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        Background.add(spacer5, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(15, -1), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        Background.add(spacer6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(15, -1), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        Background.add(spacer7, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        fieldUsername = new JTextField();
        Background.add(fieldUsername, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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
        return Background;
    }

}
