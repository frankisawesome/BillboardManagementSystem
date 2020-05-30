
package BillboardAssignment.BillboardControlPanel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class BillboardScheduler {

    public static int findIndex(String arr[], String t)
    {
        // find length of array
        int len = arr.length;
        int i = 0;

        // traverse in the array
        while (i < len) {
            // if the i-th element is t
            // then return the index
            if (arr[i].equals(t)) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

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
        return time;
    }

    BillboardScheduler()
    {
        // Frame initialisation
        JFrame frame = new JFrame();

        // Frame Title
        frame.setTitle("BillboardScheduler");

        // Data to be displayed in the JTable
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
                { "5pm", "", "", "", "", "" },
        };

        // Column Names
        String[] columnNames = { "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        String[] rowNames = {"8 am", "9 am", "10 am", "11 am", "12 pm", "1 pm", "2 pm", "3 pm", "4 pm", "5 pm"};

        // Initializing the JTable
        JTable table = new JTable(data, columnNames);
        table.setBounds(30, 40, 300, 200);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(50);

        // adding it to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday"};
        String placeholder_names[]={"Billboard 1","Billboard 2","Billboard 3","Billboard 4","Billboard 5"};

        JLabel name = new JLabel("Billboard Name");
        JComboBox dropdown =new JComboBox(placeholder_names);
        JLabel day = new JLabel("Scheduled Day");
        JComboBox dropdown2 =new JComboBox(days);
        JLabel start_hour = new JLabel("Desired Start Hour (8-17)");
        JTextField hour_field = new JTextField(5);
        JLabel start_minute = new JLabel("Desired Start Minute (0-59)");
        JTextField minute_field = new JTextField(5);
        JLabel run_time = new JLabel("Duration (minutes)");
        JTextField time_field = new JTextField(5);

        JCheckBox Checkbox1 = new JCheckBox("Repeat Daily");
        JCheckBox Checkbox2 = new JCheckBox("Repeat Hourly");
        JCheckBox Checkbox3 = new JCheckBox("Repeat every [ ] minutes");

        JTextField repeat = new JTextField(5);
        repeat.setVisible(false);

        JButton button = new JButton("   Schedule   ");

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
        panel.add(repeat, c);
        c.gridx = 1;
        c.gridy = 8;
        panel.add(button, c);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) dropdown.getItemAt(dropdown.getSelectedIndex());
                String day = (String) dropdown2.getItemAt(dropdown2.getSelectedIndex());
                String hours = hour_field.getText();
                String minutes = minute_field.getText();
                String duration = time_field.getText();

                int hours_int = Integer.parseInt(hours);
                int start_hour = hours_int;
                int minutes_int = Integer.parseInt(minutes);
                int add_minutes = Integer.parseInt(duration);

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

                if (8 <= hours_int && hours_int <= 17) {
                    if (0 <= minutes_int && minutes_int <= 59) {
                        if (add_minutes >= 0) {
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
                                am_or_pm2 = "am";
                            } else {
                                am_or_pm2 = "pm";
                            }

                            String schedule;

                            // Print a 0 before the minutes if single digits
                            if (minutes_int >= 10) {
                                schedule = name + " " + start_hour + ":" + minutes_int + am_or_pm;
                            } else {
                                schedule = name + " " + start_hour + ":0" + minutes_int + am_or_pm;
                            }

                            if (total_minutes >= 10) {
                                schedule = schedule + " - " + hours_int + ":" + total_minutes + am_or_pm2;
                            } else {
                                schedule = schedule + " - " + hours_int + ":0" + total_minutes + am_or_pm2;
                            }

                            if (columnnum != -1 && rownum != -1){
                                table.setValueAt(schedule, rownum, columnnum);
                            }
                        }
                    }
                }

                hour_field.setText("");
                minute_field.setText("");
                time_field.setText("");
            }
        });

        Checkbox1.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    Checkbox2.setSelected(false);
                    Checkbox3.setSelected(false);
                }
            }
        });

        Checkbox2.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    Checkbox1.setSelected(false);
                    Checkbox3.setSelected(false);
                }
            }
        });

        Checkbox3.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    repeat.setVisible(true);
                    Checkbox1.setSelected(false);
                    Checkbox2.setSelected(false);
                    panel.invalidate();
                    panel.validate();
                } else {
                    repeat.setVisible(false);
                }
            }
        });

        frame.add(panel, BorderLayout.WEST);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();

        // Frame Size
        frame.setSize(1200, 300);
        // Frame Visible = true
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        new BillboardScheduler();
    }
}
