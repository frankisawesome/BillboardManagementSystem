package BillboardAssignment.BillboardServer.BillboardServer;

import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;

import java.io.Serializable;

/**
 * Data that will be sent back to the front end for display purposes
 */
public class UserData implements Serializable {
    public int id;
    public String userName;
    public UserPrivilege[] privileges;
    public UserData(int id, String name, UserPrivilege[] privileges) {
        this.id = id;
        userName = name;
        this.privileges = privileges;
    }
}
