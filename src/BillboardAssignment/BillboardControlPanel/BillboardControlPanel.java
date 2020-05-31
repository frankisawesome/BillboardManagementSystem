package BillboardAssignment.BillboardControlPanel;

import javax.swing.*;

public class BillboardControlPanel {
    public static void main (String[] args) {
        try {
            // Launch Login Screen
            Login.create();
        }
        catch(Exception e){
            System.out.print("Control Panel be broke bro :( A unexpected exception was thrown.");
        }
    }
}

