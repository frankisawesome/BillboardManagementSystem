package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.Billboard;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static BillboardAssignment.BillboardServer.BillboardServer.Tests.TestUserControllers.requestBodyWithKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListBillboards extends JFrame {
    private JLabel labelTitle;
    private JLabel labelB1;
    private JLabel labelB2;
    private JLabel labelB4;
    private JLabel labelB5;
    private JLabel labelB6;
    private JLabel labelB7;
    private JLabel labelB8;
    private JLabel labelB9;
    private JLabel labelB10;
    private JLabel labelB3;
    private JLabel labelPage;
    private JButton buttonPrevious;
    private JButton buttonNext;
    private JButton buttonExit;
    private JPanel panel1;
    private JButton buttonSelect1;
    private JButton buttonPreview;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private JLabel labelSelectedName;
    private JButton buttonSelect2;
    private JButton buttonSelect3;
    private JButton buttonSelect4;
    private JButton buttonSelect5;
    private JButton buttonSelect6;
    private JButton buttonSelect7;
    private JButton buttonSelect8;
    private JButton buttonSelect9;
    private JButton buttonSelect10;
    private JPanel boxLabel1;
    private JPanel boxLabel2;
    private JPanel boxLabel3;
    private JPanel boxLabel4;
    private JPanel boxLabel5;
    private JPanel boxLabel6;
    private JPanel boxLabel7;
    private JPanel boxLabel8;
    private JPanel boxLabel9;
    private JPanel boxLabel10;
    private JButton buttonRename;
    private String[] UserData;
    private String[][] billboardList;
    private int page;
    private int selection;

    /**
     * List Billboards window object constructor. Sets up GUI and also contains listeners
     *
     * @param titles        - Window Title
     * @param userDataInput - Array containing session key and user ID for user performing the request
     * @return N/A
     */
    private ListBillboards(String titles, String[] userDataInput) {
        super(titles);
        //Setup GUI
        $$$setupUI$$$();
        this.UserData = userDataInput;
        this.billboardList = FetchBillboards();
        DisplayBillboardsMain(0);
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

        //--
        //Listeners for Presses of the Select Buttons
        buttonSelect1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(0);
            }
        });
        buttonSelect2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(1);
            }
        });
        buttonSelect3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(2);
            }
        });
        buttonSelect4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(3);
            }
        });
        buttonSelect5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(4);
            }
        });
        buttonSelect6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(5);
            }
        });
        buttonSelect7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(6);
            }
        });
        buttonSelect8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(7);
            }
        });
        buttonSelect9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(8);
            }
        });
        buttonSelect10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectBillboard(9);
            }
        });
        //-------------------

        //Listener for next button, if clicksed display next page
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayBillboardsMain(page + 1);
            }
        });

        //Listener for previous button, if clicked display previous page
        buttonPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayBillboardsMain(page - 1);
            }
        });

        //Listener for delete button. Will ask for confirmation, then send request to server to delete billboard
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Dialog for user confirmation that they really want to delete the billboard.
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to " +
                                "PERMANENTLY delete this billboard?\n Billboard- " + billboardList[selection][2], "Warning",
                        dialogButton);

                //If user confirms, call function to delete User
                if (dialogResult == JOptionPane.YES_OPTION) {
                    DeleteBillboard();
                }
            }
        });

        //Listener for rename button
        buttonRename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RenameBillboard.create(UserData, billboardList[selection][0], billboardList[selection][2]);
            }
        });

        //Listener for edit button
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "PLEASE LINK TO BILLBOARD XML EDITOR BILLY");
            }
        });

        //Listener for previwer button
        buttonPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "PLEASE LINK TO BILLBOARD XML VIEWER BILLY");
            }
        });
    }

    /**
     * Sends a request to delete the currently selected billboard to server.
     *
     * @return void
     */
    private void DeleteBillboard() {
        JOptionPane.showMessageDialog(null, "Billboard successfully deleted! NOT REALLY, NOT LINKED TO SERVER");
    }

    /**
     * Queries database and returns list of all billboards (id, author, name)
     *
     * @return String[][] list of all billboards (id, author, name)
     */
    private String[][] FetchBillboards() {
        try {
            //Set Up Request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("keyId", UserData[1]);
            requestBody.put("key", UserData[0]);

            //Send request
            ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "list billboards", requestBody);
            ServerResponse response = request.getResponse();

            //Catch any error messages returned by server
            if (!response.status().equals("ok")) {
                String[][] Error = {{"E"}};

                //If error is an invalid session key, dispose and return to login screen
                if (response.status().equals("Session key invalid")) {
                    String[][] ErrorI = {{"I"}}; //Code for invalid session key.
                    dispose();
                    Login.create();
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    return (ErrorI);
                }
                JOptionPane.showMessageDialog(null, "Error! Please Contact IT Support and Quote the Following: \n Fetch Billboard List|" + response.status());
                return (Error);
            }
            /* Change User to some other object */
            ArrayList<Billboard> serverReturnList = (ArrayList<Billboard>) response.body();

            //Turn array list into an array to provide correct return format.
            String[][] returnVal = new String[serverReturnList.size()][4];
            for (int i = 0; i < serverReturnList.size(); i++) {
                returnVal[i][0] = String.valueOf(serverReturnList.get(i).getID());
                returnVal[i][1] = String.valueOf(serverReturnList.get(i).creatorId);
                returnVal[i][2] = serverReturnList.get(i).name;
                returnVal[i][3] = serverReturnList.get(i).xml;
            }

            //String[][] returnVal = {{"1", "69421", "cat1"}, {"1", "69420", "cat2"}, {"1", "69420", "cat3"}, {"1", "69420", "cat4"}, {"1", "69420", "cat5"}, {"1", "69420", "cat6"}, {"1", "69420", "cat7"}, {"1", "69420", "cat8"}, {"1", "69420", "cat9"}, {"1", "69420", "cat10"}, {"1", "69420", "cat11"}};
            return (returnVal);
        }
        //Catch Exception
        catch (Exception e) {
            String[][] Error = new String[1][1];
            Error[0][0] = "E";
            JOptionPane.showMessageDialog(null, "Error! Please Try Again or Contact IT Support and Quote the Following: \n Fetch Billboard List |" + e.getMessage());
            return (Error);
        }
    }

    /**
     * Selects billboard according to user selection.
     *
     * @param row The row of the billboard in list in GUI which was selected.
     * @return void
     */
    private void SelectBillboard(int row) {
        //Store Selection
        selection = (page * 10) + row;

        //Unhighlight all rows and enable all select buttons to erase previous selection
        boxLabel1.setBackground(new Color(238, 238, 238));
        boxLabel2.setBackground(new Color(238, 238, 238));
        boxLabel3.setBackground(new Color(238, 238, 238));
        boxLabel4.setBackground(new Color(238, 238, 238));
        boxLabel5.setBackground(new Color(238, 238, 238));
        boxLabel6.setBackground(new Color(238, 238, 238));
        boxLabel7.setBackground(new Color(238, 238, 238));
        boxLabel8.setBackground(new Color(238, 238, 238));
        boxLabel9.setBackground(new Color(238, 238, 238));
        boxLabel10.setBackground(new Color(238, 238, 238));
        buttonSelect1.setEnabled(true);
        buttonSelect2.setEnabled(true);
        buttonSelect3.setEnabled(true);
        buttonSelect4.setEnabled(true);
        buttonSelect5.setEnabled(true);
        buttonSelect6.setEnabled(true);
        buttonSelect7.setEnabled(true);
        buttonSelect8.setEnabled(true);
        buttonSelect9.setEnabled(true);
        buttonSelect10.setEnabled(true);


        //Highlight row selected.
        switch (row) {
            case 0:
                boxLabel1.setBackground(new Color(125, 125, 125));
                buttonSelect1.setEnabled(false);
                break;
            case 1:
                boxLabel2.setBackground(new Color(125, 125, 125));
                buttonSelect2.setEnabled(false);
                break;
            case 2:
                boxLabel3.setBackground(new Color(125, 125, 125));
                buttonSelect3.setEnabled(false);
                break;
            case 3:
                boxLabel4.setBackground(new Color(125, 125, 125));
                buttonSelect4.setEnabled(false);
                break;
            case 4:
                boxLabel5.setBackground(new Color(125, 125, 125));
                buttonSelect5.setEnabled(false);
                break;
            case 5:
                boxLabel6.setBackground(new Color(125, 125, 125));
                buttonSelect6.setEnabled(false);
                break;
            case 6:
                boxLabel7.setBackground(new Color(125, 125, 125));
                buttonSelect7.setEnabled(false);
                break;
            case 7:
                boxLabel8.setBackground(new Color(125, 125, 125));
                buttonSelect8.setEnabled(false);
                break;
            case 8:
                boxLabel9.setBackground(new Color(125, 125, 125));
                buttonSelect9.setEnabled(false);
                break;
            case 9:
                boxLabel10.setBackground(new Color(125, 125, 125));
                buttonSelect10.setEnabled(false);
                break;
        }

        //Sets view preview button visible in case it is hidden (which it is when there is no previous selection)
        buttonPreview.setVisible(true);


        //Check which buttons to enable.
        if (UserData[3].equals("1")) {
            buttonDelete.setVisible(true);
            buttonEdit.setVisible(true);
            buttonRename.setVisible(true);
        } else {
            if (billboardList[selection][1].equals(UserData[1]) && UserData[2].equals("1")) {
                if (!CheckScheduled()) {
                    buttonDelete.setVisible(true);
                    buttonEdit.setVisible(true);
                    buttonRename.setVisible(true);
                } else {
                    buttonDelete.setVisible(false);
                    buttonEdit.setVisible(false);
                    buttonRename.setVisible(false);
                }
            } else {
                buttonDelete.setVisible(false);
                buttonEdit.setVisible(false);
                buttonRename.setVisible(false);
            }
        }
        pack();
    }

    /**
     * Queries server and checks if billboard already scheduled at any time
     *
     * @return boolean - True if scheduled, false if not.
     */
    private boolean CheckScheduled() {
        try {
            //Set up request
            HashMap<String, String> requestBody = new HashMap<>();
            requestBody.put("keyId", UserData[1]);
            requestBody.put("key", UserData[0]);
            requestBody.put("billboardName", billboardList[selection][2]);

            //Send Request
            ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "is schedueled", requestBody);
            ServerResponse response = request.getResponse();

            //Check if response is ok, else handle errors
            if (response.status().equals("ok")) {
                //Return Appropiate response.
                if (response.body().equals(true)) {
                    return (true);
                } else {
                    return (false);
                }
            } else {
                //If error is an invalid session key, dispose and return to login screen
                if (response.status().equals("Session key invalid")) {
                    JOptionPane.showMessageDialog(null, "Your session has expired, please log in again!");
                    dispose();
                    return (true);
                }
                JOptionPane.showMessageDialog(null, "Error! Please Try Again or Contact IT Support and Quote the Following: \n Check if Scheduled |" + response.status());
                //Assume true as a safe outcome and return. (A return of true in context of selecting a billboard will not allow
                // a user to delete or edit the billboard unless they have EditAllBillboards permission.
                return (true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error! Please Try Again or Contact IT Support and Quote the Following: \n Check if Scheduled |" + e.getMessage());
            //Assume true as a safe outcome and return. (A return of true in context of selecting a billboard will not allow
            // a user to delete or edit the billboard unless they have EditAllBillboards permission.
            return (true);
        }
    }

    /**
     * Populates GUI with data from the list of billboards.
     *
     * @param pageInput - Page Number of the GUI Displayed
     */
    private void DisplayBillboardsMain(int pageInput) {
        this.page = pageInput;

        //Unhighlight all rows and enable all select buttons, as well as action buttons to erase previous selection
        boxLabel1.setBackground(new Color(238, 238, 238));
        boxLabel2.setBackground(new Color(238, 238, 238));
        boxLabel3.setBackground(new Color(238, 238, 238));
        boxLabel4.setBackground(new Color(238, 238, 238));
        boxLabel5.setBackground(new Color(238, 238, 238));
        boxLabel6.setBackground(new Color(238, 238, 238));
        boxLabel7.setBackground(new Color(238, 238, 238));
        boxLabel8.setBackground(new Color(238, 238, 238));
        boxLabel9.setBackground(new Color(238, 238, 238));
        boxLabel10.setBackground(new Color(238, 238, 238));
        buttonSelect1.setEnabled(true);
        buttonSelect2.setEnabled(true);
        buttonSelect3.setEnabled(true);
        buttonSelect4.setEnabled(true);
        buttonSelect5.setEnabled(true);
        buttonSelect6.setEnabled(true);
        buttonSelect7.setEnabled(true);
        buttonSelect8.setEnabled(true);
        buttonSelect9.setEnabled(true);
        buttonSelect10.setEnabled(true);
        buttonEdit.setVisible(false);
        buttonDelete.setVisible(false);
        buttonPreview.setVisible(false);
        buttonRename.setVisible(false);


        //Check if billboard exists to be displayed, if so display it, else display blank field.
        //Billboard 1
        if (billboardList.length >= (page * 10 + 1)) {
            labelB1.setText(billboardList[page * 10][2]);
            buttonSelect1.setVisible(true);
        } else {
            labelB1.setText("");
            buttonSelect1.setVisible(false);
        }
        //Billboard 2
        if (billboardList.length >= (page * 10 + 2)) {
            labelB2.setText(billboardList[(page * 10 + 1)][2]);
            buttonSelect2.setVisible(true);
        } else {
            labelB2.setText("");
            buttonSelect2.setVisible(false);
        }
        //Billboard 3
        if (billboardList.length >= (page * 10 + 3)) {
            labelB3.setText(billboardList[(page * 10 + 2)][2]);
            buttonSelect3.setVisible(true);
        } else {
            labelB3.setText("");
            buttonSelect3.setVisible(false);
        }
        //Billboard 4
        if (billboardList.length >= (page * 10 + 4)) {
            labelB4.setText(billboardList[(page * 10 + 3)][2]);
            buttonSelect4.setVisible(true);
        } else {
            labelB4.setText("");
            buttonSelect4.setVisible(false);
        }
        //Billboard 5
        if (billboardList.length >= (page * 10 + 5)) {
            labelB5.setText(billboardList[(page * 10 + 4)][2]);
            buttonSelect5.setVisible(true);
        } else {
            labelB5.setText("");
            buttonSelect5.setVisible(false);
        }
        //Billboard 6
        if (billboardList.length >= (page * 10 + 6)) {
            labelB6.setText(billboardList[(page * 10 + 5)][2]);
            buttonSelect6.setVisible(true);
        } else {
            labelB6.setText("");
            buttonSelect6.setVisible(false);
        }
        //Billboard 7
        if (billboardList.length >= (page * 10 + 7)) {
            labelB7.setText(billboardList[(page * 10 + 6)][2]);
            buttonSelect7.setVisible(true);
        } else {
            labelB7.setText("");
            buttonSelect7.setVisible(false);
        }
        //Billboard 8
        if (billboardList.length >= (page * 10 + 8)) {
            labelB8.setText(billboardList[(page * 10 + 7)][2]);
            buttonSelect8.setVisible(true);
        } else {
            labelB8.setText("");
            buttonSelect8.setVisible(false);
        }
        //Billboard 9
        if (billboardList.length >= (page * 10 + 9)) {
            labelB9.setText(billboardList[(page * 10 + 8)][2]);
            buttonSelect9.setVisible(true);
        } else {
            labelB9.setText("");
            buttonSelect9.setVisible(false);
        }
        //Billboard 10
        if (billboardList.length >= (page * 10 + 10)) {
            labelB10.setText(billboardList[(page * 10 + 9)][2]);
            buttonSelect10.setVisible(true);
        } else {
            labelB10.setText("");
            buttonSelect10.setVisible(false);
        }

        //Check if the next or previous page buttons should be activated.
        buttonNext.setEnabled((billboardList.length > (page * 10 + 10)));
        buttonPrevious.setEnabled(page > 0);

        //Set page number to display
        int numPages = (int) Math.ceil((double) billboardList.length / 10);
        labelPage.setText("(Page " + (page + 1) + " of " + numPages + ")");
    }

    /**
     * Create function. Creates instance of GUI
     *
     * @param userDataInput The session key and user ID for the user logged in.
     * @return void
     */
    protected static void create(String[] userDataInput) {
        JFrame frame = new ListBillboards("Billboard Client", userDataInput);
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
        panel1.setLayout(new GridLayoutManager(27, 8, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-5461075));
        panel1.setEnabled(true);
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$("Droid Sans Mono", Font.BOLD, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setForeground(new Color(-12828863));
        labelTitle.setText("List Billboards");
        panel1.add(labelTitle, new GridConstraints(0, 1, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
        boxLabel1 = new JPanel();
        boxLabel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        boxLabel1.setEnabled(true);
        panel1.add(boxLabel1, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB1 = new JLabel();
        labelB1.setText("Label");
        boxLabel1.add(labelB1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        boxLabel2 = new JPanel();
        boxLabel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel2, new GridConstraints(7, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB2 = new JLabel();
        labelB2.setText("Label");
        boxLabel2.add(labelB2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel4 = new JPanel();
        boxLabel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel4, new GridConstraints(11, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB4 = new JLabel();
        labelB4.setText("Label");
        boxLabel4.add(labelB4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel5 = new JPanel();
        boxLabel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel5, new GridConstraints(13, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB5 = new JLabel();
        labelB5.setText("Label");
        boxLabel5.add(labelB5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel6 = new JPanel();
        boxLabel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel6, new GridConstraints(15, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB6 = new JLabel();
        labelB6.setText("Label");
        boxLabel6.add(labelB6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel7 = new JPanel();
        boxLabel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel7, new GridConstraints(17, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB7 = new JLabel();
        labelB7.setText("Label");
        boxLabel7.add(labelB7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel8 = new JPanel();
        boxLabel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel8, new GridConstraints(19, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB8 = new JLabel();
        labelB8.setText("Label");
        boxLabel8.add(labelB8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel9 = new JPanel();
        boxLabel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel9, new GridConstraints(21, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB9 = new JLabel();
        labelB9.setText("Label");
        boxLabel9.add(labelB9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel10 = new JPanel();
        boxLabel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel10, new GridConstraints(23, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB10 = new JLabel();
        labelB10.setText("Label");
        boxLabel10.add(labelB10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxLabel3 = new JPanel();
        boxLabel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(boxLabel3, new GridConstraints(9, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelB3 = new JLabel();
        labelB3.setText("Label");
        boxLabel3.add(labelB3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelPage = new JLabel();
        labelPage.setText("Page (x of x)");
        panel2.add(labelPage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel1.add(spacer7, new GridConstraints(16, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel1.add(spacer8, new GridConstraints(18, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel1.add(spacer9, new GridConstraints(20, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel1.add(spacer10, new GridConstraints(22, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 2), null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel1.add(spacer11, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(3, -1), null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel1.add(spacer12, new GridConstraints(24, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        buttonPrevious = new JButton();
        buttonPrevious.setEnabled(false);
        buttonPrevious.setLabel("Previous Page");
        buttonPrevious.setText("Previous Page");
        panel1.add(buttonPrevious, new GridConstraints(25, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonNext = new JButton();
        buttonNext.setEnabled(false);
        buttonNext.setLabel("Next Page");
        buttonNext.setText("Next Page");
        panel1.add(buttonNext, new GridConstraints(25, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        panel1.add(spacer13, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(5, -1), null, null, 0, false));
        buttonExit = new JButton();
        buttonExit.setText("Exit");
        panel1.add(buttonExit, new GridConstraints(25, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect1 = new JButton();
        buttonSelect1.setText("Select");
        panel1.add(buttonSelect1, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect2 = new JButton();
        buttonSelect2.setText("Select");
        panel1.add(buttonSelect2, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect3 = new JButton();
        buttonSelect3.setText("Select");
        panel1.add(buttonSelect3, new GridConstraints(9, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect4 = new JButton();
        buttonSelect4.setText("Select");
        panel1.add(buttonSelect4, new GridConstraints(11, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect5 = new JButton();
        buttonSelect5.setText("Select");
        panel1.add(buttonSelect5, new GridConstraints(13, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect6 = new JButton();
        buttonSelect6.setText("Select");
        panel1.add(buttonSelect6, new GridConstraints(15, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect7 = new JButton();
        buttonSelect7.setText("Select");
        panel1.add(buttonSelect7, new GridConstraints(17, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect8 = new JButton();
        buttonSelect8.setText("Select");
        panel1.add(buttonSelect8, new GridConstraints(19, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect9 = new JButton();
        buttonSelect9.setText("Select");
        panel1.add(buttonSelect9, new GridConstraints(21, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelect10 = new JButton();
        buttonSelect10.setText("Select");
        panel1.add(buttonSelect10, new GridConstraints(23, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer14 = new Spacer();
        panel1.add(spacer14, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, -1), null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(11, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(5, 6, 15, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Selected Billboard");
        panel3.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer15 = new Spacer();
        panel3.add(spacer15, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 8), null, null, 0, false));
        buttonPreview = new JButton();
        buttonPreview.setText("Preview");
        panel3.add(buttonPreview, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer16 = new Spacer();
        panel3.add(spacer16, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        buttonEdit = new JButton();
        buttonEdit.setText("Edit");
        panel3.add(buttonEdit, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer17 = new Spacer();
        panel3.add(spacer17, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        buttonDelete = new JButton();
        buttonDelete.setForeground(new Color(-4517878));
        buttonDelete.setText("Delete");
        panel3.add(buttonDelete, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelSelectedName = new JLabel();
        labelSelectedName.setText("Please Select a Billboard");
        panel3.add(labelSelectedName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer18 = new Spacer();
        panel3.add(spacer18, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(3, 3), null, null, 0, false));
        final Spacer spacer19 = new Spacer();
        panel3.add(spacer19, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(3, 3), null, null, 0, false));
        final Spacer spacer20 = new Spacer();
        panel3.add(spacer20, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        buttonRename = new JButton();
        buttonRename.setText("Rename");
        panel3.add(buttonRename, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer21 = new Spacer();
        panel3.add(spacer21, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        final Spacer spacer22 = new Spacer();
        panel1.add(spacer22, new GridConstraints(5, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(3, -1), null, null, 0, false));
        final Spacer spacer23 = new Spacer();
        panel1.add(spacer23, new GridConstraints(26, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 3), null, null, 0, false));
        final Spacer spacer24 = new Spacer();
        panel1.add(spacer24, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 7), null, null, 0, false));
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
