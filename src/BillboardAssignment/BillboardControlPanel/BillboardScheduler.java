
package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;

import static BillboardAssignment.BillboardServer.Tests.TestUserControllers.requestBodyWithKey;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.HashMap;

/**
 * A custom renderer to allow multiple lines in a table cell
 */
class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

    /**
     * The method to create a new custom table cell renderer
     * @return N/A
     */
    public MultiLineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    /**
     * A method which adjusts the height of a table cell based on the contents
     * @param table The JTable object whose contents are being edited
     * @param value The information currently in the table cell
     * @param isSelected A boolean of whether the table cell is currently selected
     * @param hasFocus A boolean of whether the table cell currently has focus
     * @param row An integer of the cell's row
     * @param column An integer of the cell's column
     * @return The table cell component
     */
    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                             boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        int height = getPreferredSize().height;
        if (table.getRowHeight(row) < height)
            table.setRowHeight(row, height);
        return this;
    }
}

/**
 * The GUI for the calendar table and billboard scheduling inputs
 */
public class BillboardScheduler {

    JFrame frame;
    JTable table;
    JScrollPane scrollPane;

    /**
     * A method which locates the index of a string within an array
     * @param arr The string array which is being searched to find the desired index
     * @param t The string of which the index is being found
     * @return An integer representing the index of the desired string within the array
     */
    public static int findIndex(String arr[], String t)
    {
        // find length of array
        int len = arr.length;
        int i = 0;

        // traverse in the array
        while (i < len) {
            // if the i-th element is t, return the index
            if (arr[i].equals(t)) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    /**
     * A method which converts afternoon times to and from 24 hour time
     * @param time An integer of the hour as it is currently being shown
     * @return An integer of the new hour which has been converted
     */
    public static int changeTime(int time) {
        if (time >= 12) {
            if (time == 13) {
                time = 1;
            }
            if (time == 14) {
                time = 2;
            }
            if (time == 15) {
                time = 3;
            }
            if (time == 16) {
                time = 4;
            }
            if (time == 17) {
                time = 5;
            }
        }
        else {
            if (time == 1) {
                time = 13;
            }
            if (time == 2) {
                time = 14;
            }
            if (time == 3) {
                time = 15;
            }
            if (time == 4) {
                time = 16;
            }
            if (time == 5) {
                time = 17;
            }
        }
        return time;
    }

    /**
     * A method which formats and adds a new string to the calendar table
     * @param hours_int An integer of the starting time of the billboard
     * @param start_hour An integer copy of hours_int to check whether the time is am or pm when converted to 24 hour
     * @param minutes_int An integer of the starting minute of the billboard in the hour
     * @param add_minutes An integer of the inputted number of minutes to be added to the starting time
     * @param name A string of the name of the billboard to be added
     * @param day A string of the day in which the billboard is to be scheduled
     * @param columnNames A string array of the column names ('Time' + the Days) in the table
     * @param rowNames A string array of the row names (9am - 4pm times) in the table
     * @return void
     */
    public void addToCalendar(int hours_int, int start_hour, int minutes_int, int add_minutes,
                              String name, String day, String[] columnNames, String[] rowNames) throws Exception {
        String am_or_pm;

        start_hour = changeTime(start_hour);

        if (start_hour == hours_int) {
            am_or_pm = "am";
        } else {
            am_or_pm = "pm";
        }

        String desired_hour = start_hour + " " + am_or_pm;

        int columnnum = findIndex(columnNames, day);
        int rownum = findIndex(rowNames, desired_hour);

        int total_minutes = minutes_int + add_minutes;

        // Calculate how many hours have passed
        while (total_minutes >= 60) {
            hours_int++;
            total_minutes = total_minutes - 60;
        }

        // Check if the billboard has reached the end of the day
        if (hours_int >= 17) {
            hours_int = 17;
            total_minutes = 0;
        }

        String am_or_pm2;
        int hold_time = hours_int;

        hours_int = changeTime(hours_int);
        if (hours_int == hold_time) {
            if (hours_int == 12) {
                am_or_pm2 = "pm";
            }
            else {
                am_or_pm2 = "am";
            }
        } else {
            am_or_pm2 = "pm";
        }

        String schedule;

        int start_24 = changeTime(start_hour);
        int end_24 = changeTime(hours_int);

        // Convert to 24 hour time and convert to a string
        String start_string = Integer.toString(start_24);
        String end_string = Integer.toString(end_24);
        String start_string_mins;
        String end_string_mins;

        if (start_24 < 10) {
            start_string = "0" + start_string;
        }

        if (end_24 < 10) {
            end_string = "0" + end_string;
        }

        // Create the starting time string to be added to the table.
        // Add a 0 before the minutes if it's a single digit
        if (minutes_int >= 10) {
            start_string_mins = ":" + minutes_int;
        } else {
            start_string_mins = ":0" + minutes_int;
        }

        schedule = name + " " + start_string + start_string_mins + am_or_pm;

        // Add the ending time string to the starting time, separated by -
        // Add a 0 before the minutes if it's a single digit
        if (total_minutes >= 10) {
            end_string_mins = ":" + total_minutes;
        } else {
            end_string_mins = ":0" + total_minutes;

        }

        schedule = schedule + " - " + end_string + end_string_mins + am_or_pm2;
        schedule += " ";

        // If the day and time are valid, add the billboard to the table
        if (columnnum != -1 && rownum != -1){
            try {
                // Send to schedule database
                HashMap<String, String> requestBody = requestBodyWithKey();
                requestBody.put("billboardName", name);
                requestBody.put("day", day);
                requestBody.put("startTime", start_string + start_string_mins);
                requestBody.put("endTime", end_string + end_string_mins);
                ServerRequest request = new ServerRequest(RequestType.SCHEDUELE, "set", requestBody);
                ServerResponse response = request.getResponse();

                //Check that response is ok, if so add the billboard to the table
                if (response.status().equals("ok")) {
                    String current = (String) table.getModel().getValueAt(rownum, columnnum);
                    table.setValueAt(current+schedule, rownum, columnnum);
                } else {
                    System.out.println(response.status());
                    JOptionPane.showMessageDialog(null, "Error! Something went wrong scheduling" +
                            " the billboard in the server.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * A method to check whether the duration and repeat values inputted by the user are numeric
     * @param num The string entered by the user
     * @return A boolean representing whether or not the string is able to be parsed as an integer
     */
    public static boolean isNumeric(String num) {
        // If the string is empty, return null
        if (num == null) {
            return false;
        }
        // Return true if the string can be represented as an integer, or false if it cannot
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * The scheduler object constructor. Creates the GUI with a number of event listeners
     * @param UserData Array containing session key and user ID for user performing the request
     * @return N/A
     */
    BillboardScheduler(String[] UserData)
    {
        // Frame initialisation
        frame = new JFrame();

        // Frame Title
        frame.setTitle("BillboardScheduler");

        // Initial data to be displayed in the JTable
        String[][] data = {
                { "8am", "", "", "", "", "" },
                { "9am", "", "", "", "", "" },
                { "10am", "", "", "", "", "" },
                { "11am", "", "", "", "", "" },
                { "12pm", "", "", "", "", "" },
                { "1pm", "", "", "", "", "" },
                { "2pm", "", "", "", "", "" },
                { "3pm", "", "", "", "", "" },
                { "4pm", "", "", "", "", "" },
        };

        // Column names and row names of the table
        String[] columnNames = { "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        String[] rowNames = {"8 am", "9 am", "10 am", "11 am", "12 pm", "1 pm", "2 pm", "3 pm", "4 pm"};

        // Initializing the JTable
        table = new JTable(data, columnNames);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);

        // Add the table to a JScrollPane
        scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // The text options for the dropdown menu
        String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday"};
        String placeholder_names[]={"Billboard 1","Billboard 2","Billboard 3","Billboard 4","Billboard 5"};

        // The JLabels and their corresponding text entries and dropdown menus
        JLabel name = new JLabel("Billboard Name");
        JComboBox dropdown = new JComboBox(placeholder_names);
        JLabel day = new JLabel("Scheduled Day");
        JComboBox dropdown2 = new JComboBox(days);
        JLabel start_hour = new JLabel("Desired Start Hour");
        JComboBox hour_field = new JComboBox(rowNames);
        JLabel start_minute = new JLabel("Desired Start Minute (0-59)");
        JTextField minute_field = new JTextField(5);
        JLabel run_time = new JLabel("Duration (minutes)");
        JTextField time_field = new JTextField(5);

        // The checkboxes for repeating
        JCheckBox Checkbox1 = new JCheckBox("Repeat Daily");
        JCheckBox Checkbox2 = new JCheckBox("Repeat Hourly");
        JCheckBox Checkbox3 = new JCheckBox("Repeat every [ ] minutes");

        // A disappearing text box for the repeating minutes option
        JTextField repeat_minutes = new JTextField(5);
        repeat_minutes.setVisible(false);

        // The button to add a new billboard with the entered specifications
        JButton button = new JButton("   Schedule   ");

        // Add all the elements to a JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        panel.add(name, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(dropdown, c);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(day, c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(dropdown2, c);
        c.gridx = 0;
        c.gridy = 2;
        panel.add(start_hour, c);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(hour_field, c);
        c.gridx = 0;
        c.gridy = 3;
        panel.add(start_minute, c);
        c.gridx = 1;
        c.gridy = 3;
        panel.add(minute_field, c);
        c.gridx = 0;
        c.gridy = 4;
        panel.add(run_time, c);
        c.gridx = 1;
        c.gridy = 4;
        panel.add(time_field, c);
        c.gridx = 0;
        c.gridy = 5;
        panel.add(Checkbox1, c);
        c.gridx = 0;
        c.gridy = 6;
        panel.add(Checkbox2, c);
        c.gridx = 0;
        c.gridy = 7;
        panel.add(Checkbox3, c);
        c.gridx = 1;
        c.gridy = 7;
        panel.add(repeat_minutes, c);
        c.gridx = 1;
        c.gridy = 8;
        panel.add(button, c);

        final String[] repeat = {""};

        // Add a header with a title to the GUI
        JLabel title = new JLabel("Billboard Schedule");
        title.setFont(new Font("", Font.PLAIN, 20));
        JPanel header = new JPanel();
        header.add(title);
        frame.add(header, BorderLayout.NORTH);

        // Add a footer containing a button to return to the main menu
        JButton exit = new JButton("Return to Main Menu");
        JPanel bottom = new JPanel();
        bottom.add(exit);
        frame.add(bottom, BorderLayout.SOUTH);

        // An event listener for the exit button to return to the menu upon being pressed
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainMenu.create(UserData);
            }
        });

        // An event listener for the schedule button to add a billboard to the table using the unputted values
        // and reset all the entries afterwards
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) dropdown.getItemAt(dropdown.getSelectedIndex());
                String day = (String) dropdown2.getItemAt(dropdown2.getSelectedIndex());
                String hours = (String) hour_field.getItemAt(hour_field.getSelectedIndex());
                hours = hours.split(" ")[0];
                String minutes = minute_field.getText();
                String duration = time_field.getText();
                String repeat_time = repeat_minutes.getText();

                int hours_int = Integer.parseInt(hours);
                hours_int = changeTime(hours_int);
                int start_hour = hours_int;
                int repeat_int;

                if (isNumeric(minutes) && isNumeric(duration)) {
                    int minutes_int = Integer.parseInt(minutes);
                    int add_minutes = Integer.parseInt(duration);

                    if (0 <= minutes_int && minutes_int <= 59) {
                        if (add_minutes > 0) {
                            if (repeat[0].equals("")) {
                                try {
                                    addToCalendar(hours_int, start_hour, minutes_int, add_minutes,
                                            name, day, columnNames, rowNames);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            else if (repeat[0].equals("Daily")) {
                                int day_index = findIndex(columnNames, day);
                                while (day_index < 6) {
                                    try {
                                        addToCalendar(hours_int, start_hour, minutes_int, add_minutes,
                                                name, columnNames[day_index], columnNames, rowNames);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    day_index++;
                                }
                            }
                            else if (repeat[0].equals("Hourly") || repeat[0].equals("Minutely")) {
                                if (repeat[0].equals("Minutely")) {
                                    repeat_int = Integer.parseInt(repeat_time);
                                } else {
                                    repeat_int = 60;
                                }

                                if (repeat_int >= add_minutes) {
                                    int end_time = hours_int*60 + minutes_int;

                                    while (end_time < 1020) {
                                        try {
                                            addToCalendar(hours_int, start_hour, minutes_int, add_minutes,
                                                    name, day, columnNames, rowNames);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        end_time += repeat_int;

                                        minutes_int += repeat_int;

                                        while (minutes_int >= 60) {
                                            hours_int++;
                                            minutes_int = minutes_int - 60;
                                        }

                                        start_hour = hours_int;
                                    }
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Error! Duration must be greater than 0.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error! Please enter a starting minute " +
                                "between 0 and 59.");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Error! Please enter a numeric value " +
                            "for both start minute and duration.");
                }

                minute_field.setText("");
                time_field.setText("");
                repeat_minutes.setText("");
                Checkbox1.setSelected(false);
                Checkbox2.setSelected(false);
                Checkbox3.setSelected(false);
                repeat_minutes.setVisible(false);
                repeat[0] = "";
            }
        });

        // Add an event listener to check and uncheck the repeat daily box
        Checkbox1.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    Checkbox2.setSelected(false);
                    Checkbox3.setSelected(false);

                    repeat[0] = "Daily";
                } else {
                    repeat[0] = "";
                }
            }
        });

        // Add an event listener to check and uncheck the repeat hourly box
        Checkbox2.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    Checkbox1.setSelected(false);
                    Checkbox3.setSelected(false);

                    repeat[0] = "Hourly";
                } else {
                    repeat[0] = "";
                }
            }
        });

        // Add an event listener to check and uncheck the repeat minutely box and show or hide
        // the corresponding text entry field
        Checkbox3.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    repeat_minutes.setVisible(true);
                    Checkbox1.setSelected(false);
                    Checkbox2.setSelected(false);
                    panel.invalidate();
                    panel.validate();
                    repeat[0] = "Minutely";
                } else {
                    repeat_minutes.setVisible(false);
                    repeat[0] = "";
                }
            }
        });

        frame.add(panel, BorderLayout.WEST);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        // Frame Size
        frame.setSize(1400, 400);
        // Frame Visible = true
        frame.setVisible(true);
    }

    /**
     * The method to create a new instance of the schedule GUI
     * @param UserData Array containing session key and user ID for user performing the request
     * @return void
     */
    public static void create(String[] UserData)
    {
        new BillboardScheduler(UserData);
    }
}
