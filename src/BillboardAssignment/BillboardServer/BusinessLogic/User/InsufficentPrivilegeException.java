package BillboardAssignment.BillboardServer.BusinessLogic.User;

public class InsufficentPrivilegeException extends Throwable {
    public InsufficentPrivilegeException(UserPrivilege privilege) {
        super(privilege.toString());
    }
}
