package BillboardAssignment.BillboardControlPanel;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;

import javax.swing.*;

public class BillboardControlPanel {

    /**
     * Control panel main function. Launches control panel and calls function that generates first window, Login.
     * @param args
     * @return Void
     */
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

