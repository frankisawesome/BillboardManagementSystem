package BillboardAssignment.BillboardServer.BusinessLogic.Billboard;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.*;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserManager;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;
import BillboardAssignment.BillboardServer.Database.*;

public class BillboardManager extends UserManager {
    public Queryable<Billboard> billboardDb;
    private SessionKeyManager sessionKeyManager;


    public BillboardManager(Queryable<Billboard> billboardDb, Queryable<UserSessionKey> sessionKeyDb, Queryable<User> userDb) throws DatabaseNotAccessibleException{
        super(new PasswordManager(userDb), new SessionKeyManager(sessionKeyDb), userDb);
        this.billboardDb = billboardDb;
    }

    public void create(Billboard newBillboard, UserSessionKey key) throws InsufficentPrivilegeException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, OutOfDateSessionKeyException, IncorrectSessionKeyException, DatabaseLogicException {
       checkSessionKeyPrivileges(key, UserPrivilege.CreateBillboards);

       billboardDb.addObject(newBillboard);
    }
}
