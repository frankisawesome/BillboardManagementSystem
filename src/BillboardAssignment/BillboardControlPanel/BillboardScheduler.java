
package BillboardAssignment.BillboardControlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class BillboardScheduler {

    BillboardScheduler()
    {
        // Frame initialisation
        JFrame frame = new JFrame();

        // Frame Title
        frame.setTitle("BillboardScheduler");

        // Data to be displayed in the JTable
        String[][] data = {
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

        // Initializing the JTable
        JTable table = new JTable(data, columnNames);
        table.setBounds(30, 40, 300, 200);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        // adding it to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JLabel name = new JLabel("Billboard Name");
        JTextField name_field = new JTextField(15);
        JLabel day = new JLabel("Scheduled Day");
        JTextField day_field = new JTextField(10);
        JLabel start_time = new JLabel("Desired Start Time");
        JTextField start_field = new JTextField(10);
        JLabel run_time = new JLabel("Duration");
        JTextField time_field = new JTextField(10);

        JCheckBox Checkbox1 = new JCheckBox("Repeat Daily");
        JCheckBox Checkbox2 = new JCheckBox("Repeat Hourly");
        JCheckBox Checkbox3 = new JCheckBox("Repeat every [ ] minutes");


        JTextField repeat = new JTextField(10);
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
        panel.add(name_field, c);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(day, c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(day_field, c);
        c.gridx = 0;
        c.gridy = 2;
        panel.add(start_time, c);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(start_field, c);
        c.gridx = 0;
        c.gridy = 3;
        panel.add(run_time, c);
        c.gridx = 1;
        c.gridy = 3;
        panel.add(time_field, c);
        c.gridx = 0;
        c.gridy = 4;
        panel.add(Checkbox1, c);
        c.gridx = 0;
        c.gridy = 5;
        panel.add(Checkbox2, c);
        c.gridx = 0;
        c.gridy = 6;
        panel.add(Checkbox3, c);
        c.gridx = 1;
        c.gridy = 6;
        panel.add(repeat, c);
        c.gridx = 1;
        c.gridy = 7;
        panel.add(button, c);

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
        frame.setSize(700, 300);
        // Frame Visible = true
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        new BillboardScheduler();
    }
}
