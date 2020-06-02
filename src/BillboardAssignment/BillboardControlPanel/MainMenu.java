package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MainMenu extends JFrame {
    private JButton buttonLogout;
    private JPanel Background;
    private JLabel labelTitle;
    private JButton buttonCreate;
    private JButton buttonView;
    private JButton buttonSchedule;
    private JButton buttonUser;
    private JLabel labelUsername;
    private JButton buttonChangePwd;
    private String[] userData;

    /**
     * Main Menu window object constructor. Sets up GUI and also contains listeners
     *
     * @param titles        - Window Title
     * @param userDataInput - Array containing session key and user ID for user performing the request
     * @return N/A
     */
    private MainMenu(String titles, String[] userDataInput) {
        super(titles);
        $$$setupUI$$$();
        // Finish setting up UI after all elements added to background
        this.userData = userDataInput;

        //Send request to fetch updated user permissions, in case they have been changed mid session by admin.
        String[] newPermissions = GetPermissionsRequest(userData);
        if (newPermissions[0].equals("E")) {
            this.dispose();
            Login.create();
        }
        else {
            for (int i = 0; i < 4; i++) {
                this.setContentPane(Background);
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                //Display Username at top of screen.
                this.labelUsername.setText(("Welcome User - " + userData[1]));
                this.pack();
                userData[i + 2] = newPermissions[i];
            }
        }

        //Listener for Logout Button
        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HashMap<String, String> requestBody = new HashMap<String, String>();
                    requestBody.put("key", userData[0]);
                    requestBody.put("keyId", userData[1]);
                    ServerRequest<Boolean> request = new ServerRequest<Boolean>(RequestType.USER, "logout", requestBody);
                    ServerResponse<Boolean> response = request.getResponse();
                    if (response.body() == true) {
                        Login.create();
                        dispose();
                        JOptionPane.showMessageDialog(null, "You have successfully logged out!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error Please Contact IT Support and Quote the Following: \nLog-Out Request Rejected at Serverside");
                    }
                } catch (Exception f) {
                    JOptionPane.showMessageDialog(null, "Error Please Contact IT Support and Quote the Following: \n" + f.getMessage());
                }


            }
        });

        //Listener for Create Button
        buttonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Check if user has required permissions
                if (userData[2].equals("1")) {
                    dispose();
                    CreateBillboard.create(userData);
                } else {
                    JOptionPane.showMessageDialog(null, "You do not have the required permission to access this feature. " +
                            "\n If this is an error please retry, or contact system administrator.");
                }
            }
        });

        //Listener for View Button
        buttonView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ListBillboards.create(userData);
            }
        });

        //Listener for Schedule Button
        buttonSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Permission Check
                if (userData[4].equals("1")) {
                    dispose();
                    BillboardScheduler.create(null, userData);
                } else {
                    JOptionPane.showMessageDialog(null, "You do not have the required permission to access this feature. " +
                            "\n If this is an error please retry, or contact system administrator.");
                }
            }
        });

        //Listener for user management button.
        buttonUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Permission Check
                if (userData[5].equals("1")) {
                    dispose();
                    UserManage.create(userData);
                } else {
                    JOptionPane.showMessageDialog(null, "You do not have the required permission to access this feature. " +
                            "\n If this is an error please retry, or contact system administrator.");
                }
            }
        });

        //Listener for change password button
        buttonChangePwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ChangePassword.create(userData, 0);
            }
        });
    }


    /**
     * Create function. Creates instance of GUI
     *
     * @param userDataInput The session key and user ID for the user logged in.
     * @return void
     */
    protected static void create(String[] userDataInput) {
        //Create Menu Frame & Pass User Data
        JFrame frame = new MainMenu("Billboard Client", userDataInput);
        frame.setVisible(true);
    }

    /**
     * Sets up and sends permissions fetch request to server
     *
     * @param userData - Session key and user ID
     * @return String[] Permissions in binary format 1 - has, 0 - doesnt have {create, edit, schedule, user admin}
     */
    private String[] GetPermissionsRequest(String[] userData) {
        try {
            //Set up Request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("idToFind", userData[1]);
            requestBody.put("key", userData[0]);
            requestBody.put("keyId", userData[1]);

            //Send Request
            ServerRequest<UserPrivilege[]> request = new ServerRequest<>(RequestType.USER, "get privileges", requestBody);
            ServerResponse<UserPrivilege[]> response = request.getResponse();

            //Check that response is ok, if not display error message.
            if (!response.status().equals("ok")) {
                if (response.status().equals("Session key invalid")) {
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    dispose();
                    Login.create();
                    String[] Error = {"E"};
                    return (Error);
                } else {
                    String[] Error = {"E"};
                    JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Get Permissions |" + response.status());
                    return (Error);
                }
            }
            //Fetch response and convert to string format
            UserPrivilege[] perms = response.body();
            String[] stringPerms = {"0", "0", "0", "0"};
            String tempPerm;


            //Set up array with a binary code for each permission, 1=true (has), 0 = false (doesnt have)
            for (int i = 0; i < perms.length; i++) {
                tempPerm = String.valueOf(perms[i]);
                switch (tempPerm) {
                    case "CreateBillboards":
                        stringPerms[0] = "1";
                        break;
                    case "EditAllBillboards":
                        stringPerms[1] = "1";
                        break;
                    case "ScheduleBillboards":
                        stringPerms[2] = "1";
                        break;
                    case "EditUsers":
                        stringPerms[3] = "1";
                        break;
                    default:
                        //Return an element E if error occurs as a flag to login, error is handled here however with a prompt.
                        String[] Error = {"E"};
                        JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Invalid Permission returned by server");
                        return (Error);
                }
            }
            return (stringPerms);
        } catch (Exception e) {
            //Return an element E if exception occurs as a flag to login, exception is handled here however with a prompt.
            String[] Error = {"E"};
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Get Permissions |" + e.getMessage());
            return (Error);
        }
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
        Background.setLayout(new GridLayoutManager(10, 5, new Insets(0, 0, 0, 0), -1, -1));
        Background.setBackground(new Color(-5461075));
        buttonLogout = new JButton();
        buttonLogout.setForeground(new Color(-16053493));
        buttonLogout.setText("Log Out");
        Background.add(buttonLogout, new GridConstraints(8, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, -1), null, null, 0, false));
        labelTitle = new JLabel();
        labelTitle.setBackground(new Color(-12828863));
        Font labelTitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 22, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setForeground(new Color(-13619152));
        labelTitle.setHorizontalAlignment(0);
        labelTitle.setHorizontalTextPosition(0);
        labelTitle.setText("Sick Azz Billboard Manager");
        Background.add(labelTitle, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCreate = new JButton();
        buttonCreate.setText("Create Billboard");
        Background.add(buttonCreate, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, 100), null, null, 0, false));
        buttonView = new JButton();
        buttonView.setText("View/Modify Billboards");
        Background.add(buttonView, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, 100), null, null, 0, false));
        buttonSchedule = new JButton();
        buttonSchedule.setText("Schedule Billboards");
        Background.add(buttonSchedule, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, 100), null, null, 0, false));
        buttonUser = new JButton();
        buttonUser.setText("User Management (Admin)");
        Background.add(buttonUser, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, 100), null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        Background.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        Background.add(spacer2, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(50, 50), null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        Background.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(50, 50), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        Background.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        Background.add(spacer5, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, 20), null, new Dimension(25, 25), 0, false));
        final Spacer spacer6 = new Spacer();
        Background.add(spacer6, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(50, 50), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        Background.add(spacer7, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        labelUsername = new JLabel();
        Font labelUsernameFont = this.$$$getFont$$$(null, -1, 16, labelUsername.getFont());
        if (labelUsernameFont != null) labelUsername.setFont(labelUsernameFont);
        labelUsername.setForeground(new Color(-12828863));
        labelUsername.setHorizontalAlignment(0);
        labelUsername.setHorizontalTextPosition(0);
        labelUsername.setText("Credentials Error, Please Sign In Again!");
        Background.add(labelUsername, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        Background.add(spacer8, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        buttonChangePwd = new JButton();
        buttonChangePwd.setText("Change Password");
        Background.add(buttonChangePwd, new GridConstraints(6, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        Background.add(spacer9, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
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
