package BillboardAssignment.BillboardServer.Server;

import BillboardAssignment.BillboardServer.Services.User.UserPrivilege;

import java.io.Serializable;

/**
 * Data that will be sent back to the front end for display purposes
 */
public class UserData implements Serializable {
    public int id;
    public String userName;
    public UserPrivilege[] privileges;

    /**
     * Constructs user data with necessary information
     * @param id
     * @param name
     * @param privileges
     */
    public UserData(int id, String name, UserPrivilege[] privileges) {
        this.id = id;
        userName = name;
        this.privileges = privileges;
    }
}
