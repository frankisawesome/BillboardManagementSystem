package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.Server.UserData;
import BillboardAssignment.BillboardServer.Services.User.UserPrivilege;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class UserManage extends JFrame {
    private JPanel panel1;
    private JButton buttonEdit1;
    private JLabel labelTitle;
    private JButton buttonPrevious;
    private JButton buttonNext;
    private JButton buttonExit;
    private JButton buttonEdit2;
    private JButton buttonEdit3;
    private JButton buttonEdit4;
    private JButton buttonEdit5;
    private JButton buttonEdit6;
    private JButton buttonEdit7;
    private JButton buttonEdit8;
    private JButton buttonEdit9;
    private JButton buttonEdit10;
    private JLabel labelU2;
    private JLabel labelU3;
    private JLabel labelU1;
    private JLabel labelU6;
    private JLabel labelU7;
    private JLabel labelU8;
    private JLabel labelU9;
    private JLabel labelU4;
    private JLabel labelU10;
    private JLabel labelU5;
    private JCheckBox create1;
    private JCheckBox create2;
    private JCheckBox create3;
    private JCheckBox create4;
    private JCheckBox create5;
    private JCheckBox create6;
    private JCheckBox create7;
    private JCheckBox create8;
    private JCheckBox create9;
    private JCheckBox create10;
    private JCheckBox edit1;
    private JCheckBox edit2;
    private JCheckBox edit3;
    private JCheckBox edit4;
    private JCheckBox edit5;
    private JCheckBox edit6;
    private JCheckBox edit7;
    private JCheckBox edit8;
    private JCheckBox edit9;
    private JCheckBox edit10;
    private JCheckBox schedule1;
    private JCheckBox schedule2;
    private JCheckBox schedule3;
    private JCheckBox schedule4;
    private JCheckBox schedule5;
    private JCheckBox schedule6;
    private JCheckBox schedule7;
    private JCheckBox schedule8;
    private JCheckBox schedule9;
    private JCheckBox schedule10;
    private JCheckBox admin1;
    private JCheckBox admin2;
    private JCheckBox admin3;
    private JCheckBox admin4;
    private JCheckBox admin5;
    private JCheckBox admin6;
    private JCheckBox admin7;
    private JCheckBox admin8;
    private JCheckBox admin9;
    private JCheckBox admin10;
    private JButton buttonCreate;
    private JLabel labelPage;
    private String[] UserData;
    private String[][] userList;
    private int page;

    /**
     * User Manage window object constructor. Sets up GUI and also contains listeners
     *
     * @param titles        - Window Title
     * @param userDataInput - Array containing session key and user ID for user performing the request
     * @return N/A
     */
    private UserManage(String titles, String[] userDataInput, String[][] userList) {
        super(titles);
        //Setup GUI
        $$$setupUI$$$();
        this.UserData = userDataInput;
        this.userList = userList;
        DisplayUsersMain(0);
        this.setContentPane(panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        //Listener for the back button, simply disposes and creates main menu
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainMenu.create(UserData);
            }
        });
        buttonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateUser.create(UserData);
                dispose();
            }
        });

        //Listener for next button, if clicksed display next page
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayUsersMain(page + 1);
            }
        });

        //Listener for previous button, if clicked display previouspage
        buttonPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayUsersMain(page - 1);
            }
        });
        buttonEdit1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[page * 10]);
                dispose();
            }
        });
        buttonEdit2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 1]);
                dispose();
            }
        });
        buttonEdit3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 2]);
                dispose();
            }
        });
        buttonEdit4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 3]);
                dispose();
            }
        });
        buttonEdit5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 4]);
                dispose();
            }
        });
        buttonEdit6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 5]);
                dispose();
            }
        });
        buttonEdit7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 6]);
                dispose();
            }
        });
        buttonEdit8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 7]);
                dispose();
            }
        });
        buttonEdit9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 8]);
                dispose();
            }
        });
        buttonEdit10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUser.create(UserData, userList[(page * 10) + 9]);
                dispose();
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
        String[][] userList = ListUsers(userDataInput);
        if (userList[0][0].equals("E")) {
            MainMenu.create(userDataInput);
        } else if (userList[0][0].equals("I")) {
            Login.create();
        } else {
            JFrame frame = new UserManage("Billboard Client", userDataInput, userList);
            frame.setVisible(true);
        }
    }

    /**
     * Fetch a list of all user accounts, along with their permissions
     *
     * @param userDataInput The session key and user ID for the user logged in.
     * @return String[][] Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private static String[][] ListUsers(String[] userDataInput) {
        try {
            //Set up request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("key", userDataInput[0]);
            requestBody.put("keyId", userDataInput[1]);

            //Send Request
            ServerRequest<UserData[]> request = new ServerRequest<>(RequestType.USER, "list users", requestBody);
            ServerResponse<UserData[]> response = request.getResponse();

            //Check that response is ok, if not display error message and return error.
            if (!response.status().equals("ok")) {
                String[][] Error = {{"E"}};

                //If error is an invalid session key, dispose and return to login screen
                if (response.status().equals("Session key invalid")) {
                    String[][] ErrorI = {{"I"}}; //Code for invalid session key.
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    return (ErrorI);
                }

                JOptionPane.showMessageDialog(null, "Error! Please Contact IT Support and Quote the Following: \n Fetch Users |" + response.status());
                return (Error);
            }
            UserData[] returnedID = response.body();
            String[][] returnVal = new String[returnedID.length][6];
            returnVal[0][0] = "E";

            //Loop through returned ID's and fetch permissions for each
            for (int i = 0; i < returnedID.length; i++) {
                //Fetch Permission
                String[] permissionReturned = GetPermissionsRequest(userDataInput, String.valueOf(returnedID[i].id), returnedID[i].userName);

                //If error code returned, return error code for ListUsers
                if (permissionReturned[0].equals("E")) {
                    String[][] Error = new String[1][1];
                    Error[0][0] = "E";
                    return (Error);
                }

                //If invalid session key error returned, return error code for ListUsers
                if (permissionReturned[0].equals("I")) {
                    String[][] Error = new String[1][1];
                    Error[0][0] = "I";
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    return (Error);
                }

                //If no error returned, add to array.
                returnVal[i] = permissionReturned;
            }

            return (returnVal);

        }
        //Catch exception and return error message
        catch (Exception e) {
            String[][] Error = new String[1][1];
            Error[0][0] = "E";
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Fetch Users | " + e.getMessage());
            return (Error);
        }
    }

    /**
     * Fetch a list of permissions for a specific user
     *
     * @param userData The session key and user ID for the user logged in.
     * @param targetID The ID for which permissions should be fetched for
     * @return String[] Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private static String[] GetPermissionsRequest(String[] userData, String targetID, String userName) {
        try {
            //Handle error if a targetID of null is entered.
            if (targetID == null) {
                String[] ErrorR = {"E"};
                JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Get Permissions user of ID null passed to get permissions function.");
                return (ErrorR);
            }
            //Set up Request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("idToFind", targetID);
            requestBody.put("key", userData[0]);
            requestBody.put("keyId", userData[1]);

            //Send Request
            ServerRequest<UserPrivilege[]> request = new ServerRequest<>(RequestType.USER, "get privileges", requestBody);
            ServerResponse<UserPrivilege[]> response = request.getResponse();

            //Fetch response and convert to string format
            UserPrivilege[] perms = response.body();
            String[] stringPerms = {targetID, "0", "0", "0", "0", userName};
            String tempPerm;

            //Check that response is ok, if not display error message.
            if (!response.status().equals("ok")) {
                //If error is an invalid session key, dispose and return to login screen
                if (response.status().equals("Session key invalid")) {
                    String[] ErrorI = {"I"}; //Code for invalid session key.
                    return (ErrorI);
                }
                String[] ErrorR = {"E"};
                JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: Get Permissions\n Get Permissions |" + response.status());
                return (ErrorR);
            }
            System.out.println(targetID);
            //Set up array with a binary code for each permission, 1=true (has), 0 = false (doesnt have)
            for (int i = 0; i < perms.length; i++) {
                tempPerm = String.valueOf(perms[i]);
                switch (tempPerm) {
                    case "CreateBillboards":
                        stringPerms[1] = "1";
                        break;
                    case "EditAllBillboards":
                        stringPerms[2] = "1";
                        break;
                    case "ScheduleBillboards":
                        stringPerms[3] = "1";
                        break;
                    case "EditUsers":
                        stringPerms[4] = "1";
                        break;
                    default:
                        //Return an element E if error occurs as a flag to login, error is handled here however with a prompt.
                        String[] ErrorR = {"E"};
                        JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Invalid Permission returned by server");
                        return (ErrorR);
                }
            }
            return (stringPerms);
        } catch (Exception e) {
            //Return an element E if exception occurs as a flag to login, exception is handled here however with a prompt.
            String[] ErrorR = {"E"};
            JOptionPane.showMessageDialog(null, "Please Contact IT Support and Quote the Following: \n Get Permissions E|" + e.getMessage());
            //System.out.println(e);
            //e.printStackTrace();
            return (ErrorR);
        }
    }

    /**
     * Populate GUI with data from the list of users
     */
    private void DisplayUsersMain(int pageInput) {
        String[] blank = {"", "", "", "", "", ""};
        this.page = pageInput;

        //Check if user exists to be displayed, if so display it, else display blank field.
        //User 1
        if (userList.length >= (page * 10 + 1)) {
            DisplayUser1(userList[page * 10]);
        } else {
            DisplayUser1(blank);
        }
        //User 2
        if (userList.length >= (page * 10 + 2)) {
            DisplayUser2(userList[page * 10 + 1]);
        } else {
            DisplayUser2(blank);
        }
        //User 3
        if (userList.length >= (page * 10 + 3)) {
            DisplayUser3(userList[page * 10 + 2]);
        } else {
            DisplayUser3(blank);
        }
        //User 4
        if (userList.length >= (page * 10 + 4)) {
            DisplayUser4(userList[page * 10 + 3]);
        } else {
            DisplayUser4(blank);
        }
        //User 5
        if (userList.length >= (page * 10 + 5)) {
            DisplayUser5(userList[page * 10 + 4]);
        } else {
            DisplayUser5(blank);
        }
        //User 6
        if (userList.length >= (page * 10 + 6)) {
            DisplayUser6(userList[page * 10 + 5]);
        } else {
            DisplayUser6(blank);
        }
        //User 7
        if (userList.length >= (page * 10 + 7)) {
            DisplayUser7(userList[page * 10 + 6]);
        } else {
            DisplayUser7(blank);
        }
        //User 8
        if (userList.length >= (page * 10 + 8)) {
            DisplayUser8(userList[page * 10 + 7]);
        } else {
            DisplayUser8(blank);
        }
        //User 9
        if (userList.length >= (page * 10 + 9)) {
            DisplayUser9(userList[page * 10 + 8]);
        } else {
            DisplayUser9(blank);
        }
        //User 10
        if (userList.length >= (page * 10 + 10)) {
            DisplayUser10(userList[page * 10 + 9]);
        } else {
            DisplayUser10(blank);
        }

        //Check if the next or previous page buttons should be activated.
        buttonNext.setEnabled((userList.length > (page * 10 + 10)));
        buttonPrevious.setEnabled(page > 0);

        //Set page number to display
        int numPages = (int) Math.ceil((double) userList.length / 10);
        labelPage.setText("User ID    (Page " + (page + 1) + " of " + numPages + ")");
    }

    /**
     * Display user info on GUI for user to be displayed in position 1
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser1(String[] data) {
        labelU1.setText(data[5]);
        create1.setSelected(data[1].equals("1"));
        edit1.setSelected(data[2].equals("1"));
        schedule1.setSelected(data[3].equals("1"));
        admin1.setSelected(data[4].equals("1"));
        buttonEdit1.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 2
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser2(String[] data) {
        labelU2.setText(data[5]);
        create2.setSelected(data[1].equals("1"));
        edit2.setSelected(data[2].equals("1"));
        schedule2.setSelected(data[3].equals("1"));
        admin2.setSelected(data[4].equals("1"));
        buttonEdit2.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 3
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser3(String[] data) {
        labelU3.setText(data[5]);
        create3.setSelected(data[1].equals("1"));
        edit3.setSelected(data[2].equals("1"));
        schedule3.setSelected(data[3].equals("1"));
        admin3.setSelected(data[4].equals("1"));
        buttonEdit3.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 4
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser4(String[] data) {
        labelU4.setText(data[5]);
        create4.setSelected(data[1].equals("1"));
        edit4.setSelected(data[2].equals("1"));
        schedule4.setSelected(data[3].equals("1"));
        admin4.setSelected(data[4].equals("1"));
        buttonEdit4.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 5
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser5(String[] data) {
        labelU5.setText(data[5]);
        create5.setSelected(data[1].equals("1"));
        edit5.setSelected(data[2].equals("1"));
        schedule5.setSelected(data[3].equals("1"));
        admin5.setSelected(data[4].equals("1"));
        buttonEdit5.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 6
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser6(String[] data) {
        labelU6.setText(data[5]);
        create6.setSelected(data[1].equals("1"));
        edit6.setSelected(data[2].equals("1"));
        schedule6.setSelected(data[3].equals("1"));
        admin6.setSelected(data[4].equals("1"));
        buttonEdit6.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 7
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser7(String[] data) {
        labelU7.setText(data[5]);
        create7.setSelected(data[1].equals("1"));
        edit7.setSelected(data[2].equals("1"));
        schedule7.setSelected(data[3].equals("1"));
        admin7.setSelected(data[4].equals("1"));
        buttonEdit7.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 8
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser8(String[] data) {
        labelU8.setText(data[5]);
        create8.setSelected(data[1].equals("1"));
        edit8.setSelected(data[2].equals("1"));
        schedule8.setSelected(data[3].equals("1"));
        admin8.setSelected(data[4].equals("1"));
        buttonEdit8.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 9
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser9(String[] data) {
        labelU9.setText(data[5]);
        create9.setSelected(data[1].equals("1"));
        edit9.setSelected(data[2].equals("1"));
        schedule9.setSelected(data[3].equals("1"));
        admin9.setSelected(data[4].equals("1"));
        buttonEdit9.setVisible(!data[0].equals(""));
    }

    /**
     * Display user info on GUI for user to be displayed in position 10
     *
     * @param data Data to be Displayed Format {User ID, Create, Edit, Schedule, User Admin}  Note the latter 4 are permissions in binary format.
     */
    private void DisplayUser10(String[] data) {
        labelU10.setText(data[5]);
        create10.setSelected(data[1].equals("1"));
        edit10.setSelected(data[2].equals("1"));
        schedule10.setSelected(data[3].equals("1"));
        admin10.setSelected(data[4].equals("1"));
        buttonEdit10.setVisible(!data[0].equals(""));
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
        panel1.setLayout(new GridLayoutManager(29, 12, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-5461075));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU1 = new JLabel();
        labelU1.setText("Label");
        panel2.add(labelU1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(7, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU2 = new JLabel();
        labelU2.setText("Label");
        panel3.add(labelU2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(11, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU4 = new JLabel();
        labelU4.setText("Label");
        panel4.add(labelU4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(13, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU5 = new JLabel();
        labelU5.setText("Label");
        panel5.add(labelU5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel6, new GridConstraints(15, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU6 = new JLabel();
        labelU6.setText("Label");
        panel6.add(labelU6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(17, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU7 = new JLabel();
        labelU7.setText("Label");
        panel7.add(labelU7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel8, new GridConstraints(19, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU8 = new JLabel();
        labelU8.setText("Label");
        panel8.add(labelU8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel9, new GridConstraints(21, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU9 = new JLabel();
        labelU9.setText("Label");
        panel9.add(labelU9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel10, new GridConstraints(23, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU10 = new JLabel();
        labelU10.setText("Label");
        panel10.add(labelU10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(12, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(5, -1), null, null, 0, false));
        buttonEdit1 = new JButton();
        buttonEdit1.setActionCommand("Edit");
        buttonEdit1.setText("Edit");
        panel1.add(buttonEdit1, new GridConstraints(5, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setForeground(new Color(-12828863));
        labelTitle.setText("User Management");
        panel1.add(labelTitle, new GridConstraints(0, 1, 1, 10, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(24, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 15), null, null, 0, false));
        buttonPrevious = new JButton();
        buttonPrevious.setEnabled(false);
        buttonPrevious.setLabel("Previous Page");
        buttonPrevious.setText("Previous Page");
        panel1.add(buttonPrevious, new GridConstraints(27, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonNext = new JButton();
        buttonNext.setEnabled(false);
        buttonNext.setLabel("Next Page");
        buttonNext.setText("Next Page");
        panel1.add(buttonNext, new GridConstraints(27, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel11, new GridConstraints(9, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelU3 = new JLabel();
        labelU3.setText("Label");
        panel11.add(labelU3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create1 = new JCheckBox();
        create1.setActionCommand("");
        create1.setEnabled(false);
        create1.setHorizontalAlignment(0);
        create1.setHorizontalTextPosition(0);
        create1.setLabel("");
        create1.setSelected(false);
        create1.setText("");
        panel1.add(create1, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(11, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(7, -1), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 7), null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel12, new GridConstraints(2, 1, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelPage = new JLabel();
        labelPage.setText("User ID");
        panel12.add(labelPage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel13, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Create");
        panel13.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel14, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Edit All");
        panel14.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel15, new GridConstraints(3, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Schedule");
        panel15.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel16, new GridConstraints(3, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("User Admin");
        panel16.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonExit = new JButton();
        buttonExit.setLabel("Exit");
        buttonExit.setText("Exit");
        panel1.add(buttonExit, new GridConstraints(27, 7, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit2 = new JButton();
        buttonEdit2.setActionCommand("Edit");
        buttonEdit2.setText("Edit");
        panel1.add(buttonEdit2, new GridConstraints(7, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit3 = new JButton();
        buttonEdit3.setActionCommand("Edit");
        buttonEdit3.setText("Edit");
        panel1.add(buttonEdit3, new GridConstraints(9, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit4 = new JButton();
        buttonEdit4.setActionCommand("Edit");
        buttonEdit4.setText("Edit");
        panel1.add(buttonEdit4, new GridConstraints(11, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit5 = new JButton();
        buttonEdit5.setActionCommand("Edit");
        buttonEdit5.setText("Edit");
        panel1.add(buttonEdit5, new GridConstraints(13, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit6 = new JButton();
        buttonEdit6.setActionCommand("Edit");
        buttonEdit6.setText("Edit");
        panel1.add(buttonEdit6, new GridConstraints(15, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit7 = new JButton();
        buttonEdit7.setActionCommand("Edit");
        buttonEdit7.setText("Edit");
        panel1.add(buttonEdit7, new GridConstraints(17, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit8 = new JButton();
        buttonEdit8.setActionCommand("Edit");
        buttonEdit8.setText("Edit");
        panel1.add(buttonEdit8, new GridConstraints(19, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit9 = new JButton();
        buttonEdit9.setActionCommand("Edit");
        buttonEdit9.setText("Edit");
        panel1.add(buttonEdit9, new GridConstraints(21, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEdit10 = new JButton();
        buttonEdit10.setActionCommand("Edit");
        buttonEdit10.setText("Edit");
        panel1.add(buttonEdit10, new GridConstraints(23, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel17 = new JPanel();
        panel17.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel17, new GridConstraints(2, 5, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(0);
        label5.setHorizontalTextPosition(0);
        label5.setText("Permissions");
        panel17.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit1 = new JCheckBox();
        edit1.setActionCommand("");
        edit1.setEnabled(false);
        edit1.setHorizontalAlignment(0);
        edit1.setHorizontalTextPosition(0);
        edit1.setLabel("");
        edit1.setText("");
        panel1.add(edit1, new GridConstraints(5, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule1 = new JCheckBox();
        schedule1.setActionCommand("");
        schedule1.setEnabled(false);
        schedule1.setHorizontalAlignment(0);
        schedule1.setHorizontalTextPosition(0);
        schedule1.setLabel("");
        schedule1.setText("");
        panel1.add(schedule1, new GridConstraints(5, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin1 = new JCheckBox();
        admin1.setActionCommand("");
        admin1.setEnabled(false);
        admin1.setHorizontalAlignment(0);
        admin1.setHorizontalTextPosition(0);
        admin1.setLabel("");
        admin1.setText("");
        panel1.add(admin1, new GridConstraints(5, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create2 = new JCheckBox();
        create2.setActionCommand("");
        create2.setEnabled(false);
        create2.setHorizontalAlignment(0);
        create2.setHorizontalTextPosition(0);
        create2.setLabel("");
        create2.setText("");
        panel1.add(create2, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create3 = new JCheckBox();
        create3.setActionCommand("");
        create3.setEnabled(false);
        create3.setHorizontalAlignment(0);
        create3.setHorizontalTextPosition(0);
        create3.setLabel("");
        create3.setText("");
        panel1.add(create3, new GridConstraints(9, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create4 = new JCheckBox();
        create4.setActionCommand("");
        create4.setEnabled(false);
        create4.setHorizontalAlignment(0);
        create4.setHorizontalTextPosition(0);
        create4.setLabel("");
        create4.setText("");
        panel1.add(create4, new GridConstraints(11, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create5 = new JCheckBox();
        create5.setActionCommand("");
        create5.setEnabled(false);
        create5.setHorizontalAlignment(0);
        create5.setHorizontalTextPosition(0);
        create5.setLabel("");
        create5.setText("");
        panel1.add(create5, new GridConstraints(13, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create6 = new JCheckBox();
        create6.setActionCommand("");
        create6.setEnabled(false);
        create6.setHorizontalAlignment(0);
        create6.setHorizontalTextPosition(0);
        create6.setLabel("");
        create6.setText("");
        panel1.add(create6, new GridConstraints(15, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create7 = new JCheckBox();
        create7.setActionCommand("");
        create7.setEnabled(false);
        create7.setHorizontalAlignment(0);
        create7.setHorizontalTextPosition(0);
        create7.setLabel("");
        create7.setText("");
        panel1.add(create7, new GridConstraints(17, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create8 = new JCheckBox();
        create8.setActionCommand("");
        create8.setEnabled(false);
        create8.setHorizontalAlignment(0);
        create8.setHorizontalTextPosition(0);
        create8.setLabel("");
        create8.setText("");
        panel1.add(create8, new GridConstraints(19, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        create9 = new JCheckBox();
        create9.setActionCommand("");
        create9.setEnabled(false);
        create9.setHorizontalAlignment(0);
        create9.setHorizontalTextPosition(0);
        create9.setLabel("");
        create9.setText("");
        panel1.add(create9, new GridConstraints(21, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit9 = new JCheckBox();
        edit9.setActionCommand("");
        edit9.setEnabled(false);
        edit9.setHorizontalAlignment(0);
        edit9.setHorizontalTextPosition(0);
        edit9.setLabel("");
        edit9.setText("");
        panel1.add(edit9, new GridConstraints(21, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit8 = new JCheckBox();
        edit8.setActionCommand("");
        edit8.setEnabled(false);
        edit8.setHorizontalAlignment(0);
        edit8.setHorizontalTextPosition(0);
        edit8.setLabel("");
        edit8.setText("");
        panel1.add(edit8, new GridConstraints(19, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit7 = new JCheckBox();
        edit7.setActionCommand("");
        edit7.setEnabled(false);
        edit7.setHorizontalAlignment(0);
        edit7.setHorizontalTextPosition(0);
        edit7.setLabel("");
        edit7.setText("");
        panel1.add(edit7, new GridConstraints(17, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit6 = new JCheckBox();
        edit6.setActionCommand("");
        edit6.setEnabled(false);
        edit6.setHorizontalAlignment(0);
        edit6.setHorizontalTextPosition(0);
        edit6.setLabel("");
        edit6.setText("");
        panel1.add(edit6, new GridConstraints(15, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit5 = new JCheckBox();
        edit5.setActionCommand("");
        edit5.setEnabled(false);
        edit5.setHorizontalAlignment(0);
        edit5.setHorizontalTextPosition(0);
        edit5.setLabel("");
        edit5.setText("");
        panel1.add(edit5, new GridConstraints(13, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit4 = new JCheckBox();
        edit4.setActionCommand("");
        edit4.setEnabled(false);
        edit4.setHorizontalAlignment(0);
        edit4.setHorizontalTextPosition(0);
        edit4.setLabel("");
        edit4.setText("");
        panel1.add(edit4, new GridConstraints(11, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit3 = new JCheckBox();
        edit3.setActionCommand("");
        edit3.setEnabled(false);
        edit3.setHorizontalAlignment(0);
        edit3.setHorizontalTextPosition(0);
        edit3.setLabel("");
        edit3.setText("");
        panel1.add(edit3, new GridConstraints(9, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit2 = new JCheckBox();
        edit2.setActionCommand("");
        edit2.setEnabled(false);
        edit2.setHorizontalAlignment(0);
        edit2.setHorizontalTextPosition(0);
        edit2.setLabel("");
        edit2.setText("");
        panel1.add(edit2, new GridConstraints(7, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule2 = new JCheckBox();
        schedule2.setActionCommand("");
        schedule2.setEnabled(false);
        schedule2.setHorizontalAlignment(0);
        schedule2.setHorizontalTextPosition(0);
        schedule2.setLabel("");
        schedule2.setText("");
        panel1.add(schedule2, new GridConstraints(7, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule3 = new JCheckBox();
        schedule3.setActionCommand("");
        schedule3.setEnabled(false);
        schedule3.setHorizontalAlignment(0);
        schedule3.setHorizontalTextPosition(0);
        schedule3.setLabel("");
        schedule3.setText("");
        panel1.add(schedule3, new GridConstraints(9, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule4 = new JCheckBox();
        schedule4.setActionCommand("");
        schedule4.setEnabled(false);
        schedule4.setHorizontalAlignment(0);
        schedule4.setHorizontalTextPosition(0);
        schedule4.setLabel("");
        schedule4.setText("");
        panel1.add(schedule4, new GridConstraints(11, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule5 = new JCheckBox();
        schedule5.setActionCommand("");
        schedule5.setEnabled(false);
        schedule5.setHorizontalAlignment(0);
        schedule5.setHorizontalTextPosition(0);
        schedule5.setLabel("");
        schedule5.setText("");
        panel1.add(schedule5, new GridConstraints(13, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule6 = new JCheckBox();
        schedule6.setActionCommand("");
        schedule6.setEnabled(false);
        schedule6.setHorizontalAlignment(0);
        schedule6.setHorizontalTextPosition(0);
        schedule6.setLabel("");
        schedule6.setText("");
        panel1.add(schedule6, new GridConstraints(15, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule7 = new JCheckBox();
        schedule7.setActionCommand("");
        schedule7.setEnabled(false);
        schedule7.setHorizontalAlignment(0);
        schedule7.setHorizontalTextPosition(0);
        schedule7.setLabel("");
        schedule7.setText("");
        panel1.add(schedule7, new GridConstraints(17, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule8 = new JCheckBox();
        schedule8.setActionCommand("");
        schedule8.setEnabled(false);
        schedule8.setHorizontalAlignment(0);
        schedule8.setHorizontalTextPosition(0);
        schedule8.setLabel("");
        schedule8.setText("");
        panel1.add(schedule8, new GridConstraints(19, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule9 = new JCheckBox();
        schedule9.setActionCommand("");
        schedule9.setEnabled(false);
        schedule9.setHorizontalAlignment(0);
        schedule9.setHorizontalTextPosition(0);
        schedule9.setLabel("");
        schedule9.setText("");
        panel1.add(schedule9, new GridConstraints(21, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin9 = new JCheckBox();
        admin9.setActionCommand("");
        admin9.setEnabled(false);
        admin9.setHorizontalAlignment(0);
        admin9.setHorizontalTextPosition(0);
        admin9.setLabel("");
        admin9.setText("");
        panel1.add(admin9, new GridConstraints(21, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin8 = new JCheckBox();
        admin8.setActionCommand("");
        admin8.setEnabled(false);
        admin8.setHorizontalAlignment(0);
        admin8.setHorizontalTextPosition(0);
        admin8.setLabel("");
        admin8.setText("");
        panel1.add(admin8, new GridConstraints(19, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin7 = new JCheckBox();
        admin7.setActionCommand("");
        admin7.setEnabled(false);
        admin7.setHorizontalAlignment(0);
        admin7.setHorizontalTextPosition(0);
        admin7.setLabel("");
        admin7.setText("");
        panel1.add(admin7, new GridConstraints(17, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin6 = new JCheckBox();
        admin6.setActionCommand("");
        admin6.setEnabled(false);
        admin6.setHorizontalAlignment(0);
        admin6.setHorizontalTextPosition(0);
        admin6.setLabel("");
        admin6.setText("");
        panel1.add(admin6, new GridConstraints(15, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin5 = new JCheckBox();
        admin5.setActionCommand("");
        admin5.setEnabled(false);
        admin5.setHorizontalAlignment(0);
        admin5.setHorizontalTextPosition(0);
        admin5.setLabel("");
        admin5.setText("");
        panel1.add(admin5, new GridConstraints(13, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin4 = new JCheckBox();
        admin4.setActionCommand("");
        admin4.setEnabled(false);
        admin4.setHorizontalAlignment(0);
        admin4.setHorizontalTextPosition(0);
        admin4.setLabel("");
        admin4.setText("");
        panel1.add(admin4, new GridConstraints(11, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin3 = new JCheckBox();
        admin3.setActionCommand("");
        admin3.setEnabled(false);
        admin3.setHorizontalAlignment(0);
        admin3.setHorizontalTextPosition(0);
        admin3.setLabel("");
        admin3.setText("");
        panel1.add(admin3, new GridConstraints(9, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin2 = new JCheckBox();
        admin2.setActionCommand("");
        admin2.setEnabled(false);
        admin2.setHorizontalAlignment(0);
        admin2.setHorizontalTextPosition(0);
        admin2.setLabel("");
        admin2.setText("");
        panel1.add(admin2, new GridConstraints(7, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel1.add(spacer7, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel1.add(spacer8, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel1.add(spacer9, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel1.add(spacer10, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel1.add(spacer11, new GridConstraints(16, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel1.add(spacer12, new GridConstraints(18, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        panel1.add(spacer13, new GridConstraints(20, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer14 = new Spacer();
        panel1.add(spacer14, new GridConstraints(22, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer15 = new Spacer();
        panel1.add(spacer15, new GridConstraints(28, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        create10 = new JCheckBox();
        create10.setActionCommand("");
        create10.setEnabled(false);
        create10.setHorizontalAlignment(0);
        create10.setHorizontalTextPosition(0);
        create10.setLabel("");
        create10.setText("");
        panel1.add(create10, new GridConstraints(23, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        edit10 = new JCheckBox();
        edit10.setActionCommand("");
        edit10.setEnabled(false);
        edit10.setHorizontalAlignment(0);
        edit10.setHorizontalTextPosition(0);
        edit10.setLabel("");
        edit10.setText("");
        panel1.add(edit10, new GridConstraints(23, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        schedule10 = new JCheckBox();
        schedule10.setActionCommand("");
        schedule10.setEnabled(false);
        schedule10.setHorizontalAlignment(0);
        schedule10.setHorizontalTextPosition(0);
        schedule10.setLabel("");
        schedule10.setText("");
        panel1.add(schedule10, new GridConstraints(23, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        admin10 = new JCheckBox();
        admin10.setActionCommand("");
        admin10.setEnabled(false);
        admin10.setHorizontalAlignment(0);
        admin10.setHorizontalTextPosition(0);
        admin10.setLabel("");
        admin10.setText("");
        panel1.add(admin10, new GridConstraints(23, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer16 = new Spacer();
        panel1.add(spacer16, new GridConstraints(9, 11, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(3, -1), null, null, 0, false));
        final Spacer spacer17 = new Spacer();
        panel1.add(spacer17, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(3, -1), null, null, 0, false));
        buttonCreate = new JButton();
        buttonCreate.setText("Create New User");
        panel1.add(buttonCreate, new GridConstraints(25, 1, 1, 10, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer18 = new Spacer();
        panel1.add(spacer18, new GridConstraints(26, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 15), null, null, 0, false));
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
