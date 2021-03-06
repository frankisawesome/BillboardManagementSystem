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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Login extends JFrame {
    //Form Components
    private JPanel Background;
    private JLabel labeltitle;
    private JButton buttonlogin;
    private JTextField fieldUsername;
    private JLabel labelPassword;
    private JLabel labelUsername;
    private JPasswordField passwordField;
    private String SessionKey;

    /**
     * Login window object constructor. Sets up GUI and also contains listeners
     * @param title - Window Title
     * @return N/A
     */
    private Login(String title) {
        super(title);
        $$$setupUI$$$();
        //Completes GUI setup after all components added to background in $$$setupUI$$$
        this.setContentPane(Background);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        //Listener for button press.
        buttonlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch user input.
                String id = fieldUsername.getText();
                char[] pwdChar = passwordField.getPassword();
                String pwd = new String(pwdChar);

                //Hash the password according to a generic salt
                String saltString = "mahna mahna";
                pwd = hashPassword(pwd, saltString);

                String[] loginReturn;
                //Call sendrequest function to send info to server.
                //In loginReturn array 0 - Return Code from Server 1 - Any Message from Server
                loginReturn = SendLoginRequest(id, pwd);
                //If Login Successful.
                if (loginReturn[0] == "1") {
                    SessionKey = loginReturn[1];
                    dispose();
                    //Store ID and Session key in an array of user data
                    String[] userData = {"", "", "", "", "", "", ""};
                    userData[0] = SessionKey;
                    userData[1] = loginReturn[2]; //User ID Returned
                    userData[6] = id;
                    MainMenu.create(userData);
                }
                //If Login Unsuccessful
                else {
                    JOptionPane.showMessageDialog(null, loginReturn[1]);
                }
            }
        });
        //------------------------
         // Key Listeners for User Enter Press In Login Page = Pressing Login Button
        buttonlogin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonlogin.doClick();
                }
            }
        });
        fieldUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonlogin.doClick();
                }
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonlogin.doClick();
                }
            }
        });
    }

    /**
     * Sets up and sends login request to server
     * @param id - User ID to login
     * @param pwd - Password to login
     * @return String[] {Status(1 - successful, 2 - fail), sessionKey if pass or error message if fail}
     */
    private String[] SendLoginRequest(String id, String pwd) {
        // Set Up Request
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", id);
        requestBody.put("password", pwd);
        //Send Request
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        try {
            ServerResponse<UserSessionKey> response = request.getResponse();
            if (response.status().equals("ok")) {
                //If response ok, return session key and code 1 - successful

                String[] returnVal = {"1", response.body().sessionKey, String.valueOf(response.body().getID())};
                return (returnVal);
            } else {
                // If response fail, return code 2 and error message.
                String errorMsg = ("Error: " + response.status());
                String[] returnVal = {"2", errorMsg};
                return (returnVal);
            }
        } catch (Exception e) {
            // If exception thrown, return code 2 and error message prompting user to seek IT support.
            String[] returnVal = {"2", "Please Contact IT Support and Quote the Following: \n" + e.getMessage()};
            return (returnVal);
        }
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
     * Create function. Creates instance of GUI
     * @return void
     */
    protected static void create() {
        //Creates new frame for Login and sets Visible.
        JFrame frame = new Login("Billboard Client");
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
        Background = new JPanel();
        Background.setLayout(new GridLayoutManager(8, 4, new Insets(0, 0, 0, 0), -1, -1));
        Background.setBackground(new Color(-5461075));
        labeltitle = new JLabel();
        Font labeltitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 22, labeltitle.getFont());
        if (labeltitleFont != null) labeltitle.setFont(labeltitleFont);
        labeltitle.setForeground(new Color(-13619152));
        labeltitle.setText("Sick Azz Billboard Manager");
        Background.add(labeltitle, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        Background.add(spacer1, new GridConstraints(2, 2, 6, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        buttonlogin = new JButton();
        buttonlogin.setBackground(new Color(-10196627));
        buttonlogin.setEnabled(true);
        Font buttonloginFont = this.$$$getFont$$$(null, Font.BOLD, -1, buttonlogin.getFont());
        if (buttonloginFont != null) buttonlogin.setFont(buttonloginFont);
        buttonlogin.setForeground(new Color(-66049));
        buttonlogin.setHideActionText(true);
        buttonlogin.setLabel("Login");
        buttonlogin.setText("Login");
        Background.add(buttonlogin, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldUsername = new JTextField();
        fieldUsername.setBackground(new Color(-7763830));
        fieldUsername.setForeground(new Color(-66049));
        fieldUsername.setSelectedTextColor(new Color(-66049));
        Background.add(fieldUsername, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        labelPassword = new JLabel();
        Font labelPasswordFont = this.$$$getFont$$$("Consolas", -1, 16, labelPassword.getFont());
        if (labelPasswordFont != null) labelPassword.setFont(labelPasswordFont);
        labelPassword.setForeground(new Color(-13619152));
        labelPassword.setText("Password");
        Background.add(labelPassword, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelUsername = new JLabel();
        labelUsername.setFocusCycleRoot(false);
        Font labelUsernameFont = this.$$$getFont$$$("Consolas", -1, 16, labelUsername.getFont());
        if (labelUsernameFont != null) labelUsername.setFont(labelUsernameFont);
        labelUsername.setForeground(new Color(-13619152));
        labelUsername.setText("Username");
        Background.add(labelUsername, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        Background.add(spacer2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 5), null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        Background.add(spacer3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(-7763830));
        Background.add(passwordField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        Background.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(2, 2), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        Background.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(2, 2), null, new Dimension(5, 5), 0, false));
        labelUsername.setLabelFor(fieldUsername);
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
