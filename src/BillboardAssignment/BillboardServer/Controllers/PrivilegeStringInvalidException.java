package BillboardAssignment.BillboardServer.Controllers;

public class PrivilegeStringInvalidException extends Exception {
    @Override
    public String getMessage() {
        return "Cannot parse string to a UserPrivilege";
    }
}
