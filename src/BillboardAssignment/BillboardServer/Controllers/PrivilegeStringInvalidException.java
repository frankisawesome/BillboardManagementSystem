package BillboardAssignment.BillboardServer.Controllers;

/**
 * Exception used when a string cannot be cast to a UserPrivilege
 */
public class PrivilegeStringInvalidException extends Exception {
    @Override
    public String getMessage() {
        return "Cannot parse string to a UserPrivilege";
    }
}
