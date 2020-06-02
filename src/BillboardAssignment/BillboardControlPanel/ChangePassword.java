package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
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

public class ChangePassword extends JFrame {
    private JLabel labelTitle;
    private JPasswordField passwordExisting;
    private JLabel labelName;
    private JButton buttonConfirm;
    private JButton buttonCancel;
    private JPasswordField passwordNew1;
    private JPasswordField passwordNew2;
    private JPanel Background;
    private JLabel labelUsername;
    private JLabel labelExisting;
    private String[] UserData;
    private int requestType; //Type = 0, change own password, Type = 1, Change another user's password
    private String changeTargetID;

    public ChangePassword(String titles, String[] userDataInput, int type, String changeTarget) {
        super(titles);
        //Setup GUI
        $$$setupUI$$$();
        this.UserData = userDataInput;
        this.requestType = type;
        this.changeTargetID = changeTarget;
        PersonaliseGUI();
        this.setContentPane(Background);
        this.pack();

        //Listener for confirm button
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pwdE = "";
                //Fetch existing password
                if (requestType == 0) {
                    char[] pwdCharE = passwordExisting.getPassword();
                    pwdE = new String(pwdCharE);
                }
                //Fetch new password 1
                char[] pwdChar1 = passwordNew1.getPassword();
                String pwd1 = new String(pwdChar1);
                //Fetch new password 2
                char[] pwdChar2 = passwordNew2.getPassword();
                String pwd2 = new String(pwdChar2);

                //Checks validity of password change, first that a password has been entered, then that new passwords match, then that existing password is correct.
                if (pwd1.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please Enter a New Password, Field Cannot Be Blank");
                } else {
                    if (pwd1.equals(pwd2)) {
                        int checkResult;
                        //If changing own password check validity of existing password
                        if (requestType == 0) {
                            checkResult = checkPassword(pwdE);
                            // checkResult 1 - Success, 0 - Incorrect, 2 - Exception Thrown (Note exception is thrown in the check function.
                        }
                        //If changing another user's password as admin then this check is not required so return a result of 1.
                        else {
                            checkResult = 1;
                        }
                        if (checkResult == 1) {
                            changePassword(pwd1);
                        } else {
                            if (checkResult == 0) {
                                JOptionPane.showMessageDialog(null, "Existing password is incorrect, please try again!");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Error, new password entries do not match!");
                    }
                }
            }
        });

        //Listener for cancel button
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                //If request type is 0 - change own password, load main menu.
                if (requestType == 0) {
                    MainMenu.create(UserData);
                }
            }
        });
    }

    protected int checkPassword(String password) {
        String[] loginReturn;
        loginReturn = LoginRequest(UserData[1], password);
        //If Dummy Login Successful
        if (loginReturn[0] == "1") {
            //Change to new Session Key
            String newSessionKey = loginReturn[1];
            UserData[0] = newSessionKey;
            return (1);
        }
        //If Dummy Login Unsuccessful
        else {
            if (loginReturn[0] == "2") {
                //If fail was due to exception thrown
                JOptionPane.showMessageDialog(null, loginReturn[1]);
                return (3);
            } else {
                //If fail was due to wrong password
                return (0);
            }
        }
    }

    //Personalises GUI to Specific Request
    private void PersonaliseGUI() {
        if (requestType == 0) {
            this.labelUsername.setText(("User - " + UserData[1]));
        } else {
            this.labelUsername.setText(("User - " + changeTargetID));
            labelExisting.setVisible(false);
            passwordExisting.setVisible(false);
        }
    }

    protected void changePassword(String Password) {
        try {
            int requestTarget;

            //Set up Request
            HashMap<String, String> requestBody = new HashMap<>();

            //Set up target of password change
            if (requestType == 0) {
                requestBody.put("idToFind", UserData[1]);
            } else {
                requestBody.put("idToFind", changeTargetID);
            }

            //Hash the new password according to a generic salt
            String saltString = "mahna mahna";
            Password = hashPassword(Password, saltString);

            requestBody.put("newPassword", Password);
            requestBody.put("key", UserData[0]);
            requestBody.put("keyId", UserData[1]);

            //Send Request
            ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "change password", requestBody);
            ServerResponse<String> response = request.getResponse();
            if (response.status().equals("ok")) {
                JOptionPane.showMessageDialog(null, "Password successfully changed!");
                dispose();
                //If request type is 0 (changing own password) create main menu, and pass back new session key, else simply dispose.
                if (requestType == 0) {
                    MainMenu.create(UserData);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error Please Contact IT Support and Quote the Following: \n Change Server request rejected unexpectedly " + response.status());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Please Contact IT Support and Quote the Following: \n" + e.getMessage());
        }
    }

    //Type = 0, change own password, Type = 1, Change another user's password.
    // Overload - Change target only required if type = 1, otherwise blank.
    protected static void create(String[] userDataInput, int type) {
        JFrame frame = new ChangePassword("Billboard Client", userDataInput, type, "");
        frame.setVisible(true);
    }

    protected static void create(String[] userDataInput, int type, String changeTarget) {
        JFrame frame = new ChangePassword("Billboard Client", userDataInput, type, changeTarget);
        frame.setVisible(true);
    }

    //Login Function for creating a dummy login request to check correctness of existing password.
    private String[] LoginRequest(String id, String pwd) {
        // Set Up Request
        HashMap<String, String> requestBody = new HashMap<String, String>();

        //Hash the new password according to a generic salt
        String saltString = "mahna mahna";
        pwd = hashPassword(pwd, saltString);

        requestBody.put("id", id);
        requestBody.put("password", pwd);
        //Send Request
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        try {
            ServerResponse<UserSessionKey> response = request.getResponse();
            if (response.status().equals("ok")) {
                //If response ok, return session key and code 1 - successful

                String[] returnVal = {"1", response.body().sessionKey};
                return (returnVal);
            } else {
                // If response fail, return code 2 and error message.
                String errorMsg = ("Error: " + response.status());
                String[] returnVal = {"3", errorMsg};
                return (returnVal);
            }
        } catch (Exception e) {
            // If exception thrown, return code 2 and error message prompting user to seek IT support.
            String[] returnVal = {"2", "Please Contact IT Support and Quote the Following: \n" + "Error in dummy login for existing check \n" + e.getMessage()};
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
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Background = new JPanel();
        Background.setLayout(new GridLayoutManager(14, 4, new Insets(0, 0, 0, 0), -1, -1));
        Background.setBackground(new Color(-5461075));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setForeground(new Color(-12828863));
        labelTitle.setText("Change Password");
        Background.add(labelTitle, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        Background.add(spacer1, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 30), null, null, 0, false));
        passwordExisting = new JPasswordField();
        Background.add(passwordExisting, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        Background.add(spacer2, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 15), null, null, 0, false));
        labelName = new JLabel();
        labelName.setForeground(new Color(-11578538));
        labelName.setHorizontalAlignment(0);
        labelName.setHorizontalTextPosition(0);
        labelName.setText("New Password");
        Background.add(labelName, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelExisting = new JLabel();
        labelExisting.setForeground(new Color(-11578538));
        labelExisting.setHorizontalAlignment(0);
        labelExisting.setHorizontalTextPosition(0);
        labelExisting.setText("Existing Password");
        Background.add(labelExisting, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordNew1 = new JPasswordField();
        Background.add(passwordNew1, new GridConstraints(7, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        Background.add(spacer3, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 8), null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-11578538));
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Re-Enter New Password");
        Background.add(label1, new GridConstraints(9, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordNew2 = new JPasswordField();
        Background.add(passwordNew2, new GridConstraints(10, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        Background.add(spacer4, new GridConstraints(11, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
        buttonConfirm = new JButton();
        buttonConfirm.setText("Confirm");
        Background.add(buttonConfirm, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        Background.add(buttonCancel, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelUsername = new JLabel();
        Font labelUsernameFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 14, labelUsername.getFont());
        if (labelUsernameFont != null) labelUsername.setFont(labelUsernameFont);
        labelUsername.setForeground(new Color(-12828863));
        labelUsername.setText("Error");
        Background.add(labelUsername, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        Background.add(spacer5, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        Background.add(spacer6, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(15, -1), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        Background.add(spacer7, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(15, -1), null, null, 0, false));
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
