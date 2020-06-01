package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditUser extends JFrame {
    private JLabel labelTitle;
    private JButton buttonAddAdmin;
    private JLabel labelUsername;
    private JButton buttonChangePassword;
    private JButton buttonDeleteUser;
    private JButton buttonBack;
    private JPanel panel1;
    private JButton buttonAddCreate;
    private JButton buttonAddEdit;
    private JButton buttonAddSchedule;
    private JButton buttonRemoveCreate;
    private JButton buttonRemoveEdit;
    private JButton buttonRemoveSchedule;
    private JButton buttonRemoveAdmin;
    private String[] adminUserData;
    private String[] editingUserData;

    /**
     * Edit user window object constructor. Sets up GUI and also contains listeners
     *
     * @param titles               - Window Title
     * @param userDataInput        - Array containing session key and user ID for user performing the request
     * @param editingUserDataInput - Array of info for the user being edited {id,CreatePerm,EditPerm,SchedulePerm,AdminPerm} Where Perms are 1 - true, 0 - false
     * @return N/A
     */
    public EditUser(String titles, String[] userDataInput, String[] editingUserDataInput) {
        super(titles);
        //Setup GUI
        $$$setupUI$$$();
        this.adminUserData = userDataInput;
        this.editingUserData = editingUserDataInput;
        PersonaliseWindow();
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        //Listener for the back button, simply disposes
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserManage.create(adminUserData);
            }
        });

        //Listener for change password button, passes to change password window.
        buttonChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (adminUserData[1].equals(editingUserData[0])) {
                    JOptionPane.showMessageDialog(null, "Please change your own password from the button on the main menu.\nThanks!");
                } else {
                    ChangePassword.create(adminUserData, 1, editingUserData[0]);
                }
            }
        });

        //Listener for the remove user button
        buttonDeleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Contingency check in case button is displayed when it shouldnt be to prevent deleting your own account.
                if (adminUserData[1].equals(editingUserData[0])) {
                    JOptionPane.showMessageDialog(null, "You cannot delete your own account.");
                } else {
                    //Dialog for user confirmation that they really want to delete the account.
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to " +
                            "PERMANENTLY delete this account?\n User - " + editingUserData[0], "Warning", dialogButton);

                    //If user confirms, call function to delete User
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        DeleteUser();
                    }
                }
            }
        });

        buttonAddCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Send request and if successful
                if (AddPermission("CreateBillboards") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[2] = "1";
                        editingUserData[1] = "1";
                    } else {
                        editingUserData[1] = "1";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });

        buttonAddEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (AddPermission("EditAllBillboards") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[3] = "1";
                        editingUserData[2] = "1";
                    } else {
                        editingUserData[2] = "1";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });

        buttonAddSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (AddPermission("ScheduleBillboards") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[4] = "1";
                        editingUserData[3] = "1";
                    } else {
                        editingUserData[3] = "1";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });

        buttonAddAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (AddPermission("EditUsers") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[5] = "1";
                        editingUserData[4] = "1";
                    } else {
                        editingUserData[4] = "1";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });

        buttonRemoveCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Send request and if successful
                if (RemovePermission("CreateBillboards") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[2] = "0";
                        editingUserData[1] = "0";
                    } else {
                        editingUserData[1] = "0";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });

        buttonRemoveEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Send request and if successful
                if (RemovePermission("EditAllBillboards") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[3] = "0";
                        editingUserData[2] = "0";
                    } else {
                        editingUserData[2] = "0";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });

        buttonRemoveSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Send request and if successful
                if (RemovePermission("ScheduleBillboards") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[4] = "0";
                        editingUserData[3] = "0";
                    } else {
                        editingUserData[3] = "0";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });

        buttonRemoveAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Send request and if successful
                if (RemovePermission("EditUsers") == 1) {
                    //If Admin update admin data, otherwise if other user, update other user data
                    if (adminUserData[1].equals(editingUserData[0])) {
                        adminUserData[5] = "0";
                        editingUserData[4] = "0";
                    } else {
                        editingUserData[4] = "0";
                    }
                    //Update GUI according to changes
                    PersonaliseWindow();
                    pack();
                }
            }
        });
    }

    /**
     * Method to add a user permission
     *
     * @param permissionName - Name of the permission to Add
     * @return Int   = 0 if request failed, = 1 if successful
     */
    private int AddPermission(String permissionName) {
        //Set Up Server Request
        try {
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("newPrivilege", permissionName);
            requestBody.put("idToFind", editingUserData[0]);
            requestBody.put("key", adminUserData[0]);
            requestBody.put("keyId", adminUserData[1]);

            //Perform Request
            ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "add privilege", requestBody);
            ServerResponse<String> response = request.getResponse();

            //Check that response is ok, if not display error message.
            if (!response.status().equals("ok")) {
                //If error is an invalid session key, dispose and return to login screen
                if (response.status().equals("Session key invalid")) {
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    dispose();
                    Login.create();
                    return (0);
                } else {
                    System.out.println(response.status());
                    JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Add Permission |" + response.status());
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Add Permission | " + e.getMessage());
            return (0);
        }

        return (1);
    }

    /**
     * Method to remove a user permission
     *
     * @param permissionName - Name of the permission to remove
     * @return Int   = 0 if request failed, = 1 if successful
     */
    private int RemovePermission(String permissionName) {
        //Set Up Server Request
        try {
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("privilegeToRemove", permissionName);
            requestBody.put("idToFind", editingUserData[0]);
            requestBody.put("key", adminUserData[0]);
            requestBody.put("keyId", adminUserData[1]);

            //Perform Request
            ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "remove privilege", requestBody);
            ServerResponse<String> response = request.getResponse();

            //Check that response is ok, if not display error message.
            if (!response.status().equals("ok")) {
                //If error is an invalid session key, dispose and return to login screen
                if (response.status().equals("Session key invalid")) {
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    dispose();
                    Login.create();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n  Remove Permission |" + response.status());
                    return (0);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Remove Permission | " + e.getMessage());
            return (0);
        }

        return (1);
    }

    /**
     * Create function. Creates instance of GUI
     *
     * @param AdminDataInput       The session key and user ID for the user logged in.
     * @param editingUserDataInput Array of info for the user being edited {id,CreatePerm,EditPerm,SchedulePerm,AdminPerm} Where Perms are 1 - true, 0 - false
     * @return void
     */
    protected static void create(String[] AdminDataInput, String[] editingUserDataInput) {
        JFrame frame = new EditUser("Billboard Client", AdminDataInput, editingUserDataInput);
        frame.setVisible(true);
    }

    /**
     * Method to delete a user. Note user ID is not passed but rather fetched from variables in the EditUser object
     *
     * @return void
     */
    private void DeleteUser() {
        //If Successful, inform user, dispose and return to user management menu.
        try {
            //Set up Request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("idToFind", editingUserData[0]);
            requestBody.put("key", adminUserData[0]);
            requestBody.put("keyId", adminUserData[1]);

            //Send Request
            ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "delete user", requestBody);
            ServerResponse<String> response = request.getResponse();

            //If successful, inform user, dispose, return to user management menu
            if (response.status().equals("ok")) {
                JOptionPane.showMessageDialog(null, "User has been deleted!");
                dispose();
                UserManage.create(adminUserData);
            }
            //If unsuccessful display error
            else {
                //If error is an invalid session key, dispose and return to login screen
                if (response.status().equals("Session key invalid")) {
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    dispose();
                    Login.create();
                } else {
                    JOptionPane.showMessageDialog(null, "Error! Please Contact IT Support and Quote the Following: \n Delete User |" + response.status());
                }
            }

        }
        //If exception thrown, catch and display for user
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Delete User | " + e.getMessage());
        }

    }

    /**
     * Changes the GUI according to the data of the user being edited.
     */
    private void PersonaliseWindow() {
        labelUsername.setText(("User - " + editingUserData[0]));
        buttonAddCreate.setVisible(editingUserData[1].equals("0"));
        buttonAddEdit.setVisible(editingUserData[2].equals("0"));
        buttonAddSchedule.setVisible(editingUserData[3].equals("0"));
        buttonAddAdmin.setVisible(editingUserData[4].equals("0"));
        buttonRemoveCreate.setVisible(editingUserData[1].equals("1"));
        buttonRemoveEdit.setVisible(editingUserData[2].equals("1"));
        buttonRemoveSchedule.setVisible(editingUserData[3].equals("1"));
        if (adminUserData[1].equals(editingUserData[0])) {
            buttonRemoveAdmin.setVisible(false);
            buttonDeleteUser.setVisible(false);
        } else {
            buttonRemoveAdmin.setVisible(editingUserData[4].equals("1"));
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
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(15, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-5461075));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setForeground(new Color(-12828863));
        labelTitle.setText("Edit User");
        panel1.add(labelTitle, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Create");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Edit All");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Schedule");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("User Admin");
        panel5.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAddAdmin = new JButton();
        buttonAddAdmin.setText("Grant");
        panel1.add(buttonAddAdmin, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAddSchedule = new JButton();
        buttonAddSchedule.setText("Grant");
        panel1.add(buttonAddSchedule, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAddEdit = new JButton();
        buttonAddEdit.setText("Grant");
        panel1.add(buttonAddEdit, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAddCreate = new JButton();
        buttonAddCreate.setText("Grant");
        panel1.add(buttonAddCreate, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemoveSchedule = new JButton();
        buttonRemoveSchedule.setText("Revoke");
        panel1.add(buttonRemoveSchedule, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemoveAdmin = new JButton();
        buttonRemoveAdmin.setText("Revoke");
        panel1.add(buttonRemoveAdmin, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemoveEdit = new JButton();
        buttonRemoveEdit.setText("Revoke");
        panel1.add(buttonRemoveEdit, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 5), null, null, 0, false));
        labelUsername = new JLabel();
        Font labelUsernameFont = this.$$$getFont$$$("Droid Sans Mono", Font.PLAIN, 14, labelUsername.getFont());
        if (labelUsernameFont != null) labelUsername.setFont(labelUsernameFont);
        labelUsername.setForeground(new Color(-12828863));
        labelUsername.setText("Error");
        panel1.add(labelUsername, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonChangePassword = new JButton();
        buttonChangePassword.setText("Change Password");
        panel1.add(buttonChangePassword, new GridConstraints(9, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 5), null, null, 0, false));
        buttonDeleteUser = new JButton();
        buttonDeleteUser.setForeground(new Color(-4517878));
        buttonDeleteUser.setText("Delete User");
        panel1.add(buttonDeleteUser, new GridConstraints(11, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(12, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 15), null, null, 0, false));
        buttonBack = new JButton();
        buttonBack.setText("Go Back");
        panel1.add(buttonBack, new GridConstraints(13, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(14, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel1.add(spacer7, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel1.add(spacer8, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(5, -1), null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel1.add(spacer9, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(5, -1), null, null, 0, false));
        buttonRemoveCreate = new JButton();
        buttonRemoveCreate.setText("Revoke");
        panel1.add(buttonRemoveCreate, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
