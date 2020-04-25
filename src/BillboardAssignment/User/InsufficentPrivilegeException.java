package BillboardAssignment.User;

public class InsufficentPrivilegeException extends Throwable {
    public InsufficentPrivilegeException(UserPrivilege privilege) {
        super(privilege.toString());
    }
}
