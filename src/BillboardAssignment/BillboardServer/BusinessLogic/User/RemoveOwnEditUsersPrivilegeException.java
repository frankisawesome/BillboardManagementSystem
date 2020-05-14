package BillboardAssignment.BillboardServer.BusinessLogic.User;

public class RemoveOwnEditUsersPrivilegeException extends Exception {

    /**
     * Constructor of the Exception
     *
     */
    public RemoveOwnEditUsersPrivilegeException() {
        super(String.format("Error! Users cannot remove their own edit users permission!"));
    }
}
