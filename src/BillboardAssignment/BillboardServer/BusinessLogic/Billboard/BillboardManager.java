package BillboardAssignment.BillboardServer.BusinessLogic.Billboard;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.*;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserManager;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;
import BillboardAssignment.BillboardServer.Database.*;

import java.util.ArrayList;

public class BillboardManager extends UserManager {
    public Queryable<Billboard> billboardDb;
    private SessionKeyManager sessionKeyManager;


    public BillboardManager(Queryable<Billboard> billboardDb, Queryable<UserSessionKey> sessionKeyDb, Queryable<User> userDb) throws DatabaseNotAccessibleException{
        super(new PasswordManager(userDb), new SessionKeyManager(sessionKeyDb), userDb);
        this.billboardDb = billboardDb;
    }

    public void create(Billboard newBillboard, UserSessionKey key) throws InsufficentPrivilegeException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, OutOfDateSessionKeyException, IncorrectSessionKeyException, DatabaseLogicException {
       checkSessionKeyPrivileges(key, UserPrivilege.CreateBillboards);

       int id = billboardDb.getMaxID();

       Billboard billboard = new Billboard(id + 1, newBillboard.name, newBillboard.xml, newBillboard.creatorId);

       billboardDb.addObject(billboard);
    }

    public ArrayList<Billboard> list(UserSessionKey key) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException {
        checkSessionKeyPrivileges(key, UserPrivilege.EditAllBillboards);

        return billboardDb.getAllObjects();
    }

    public void rename(int billboardId, String newName, UserSessionKey key) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, DatabaseLogicException {
        checkSessionKeyPrivileges(key, UserPrivilege.EditAllBillboards);

        Billboard old = billboardDb.getObject(billboardId);
        billboardDb.removeObject(billboardId);
        billboardDb.addObject(new Billboard(billboardId, newName, old.xml, old.creatorId));
    }

    public void edit(int billboardId, String newContent, UserSessionKey key) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, DatabaseLogicException {
        checkSessionKeyPrivileges(key, UserPrivilege.EditAllBillboards);

        Billboard old = billboardDb.getObject(billboardId);
        billboardDb.removeObject(billboardId);
        billboardDb.addObject(new Billboard(billboardId, old.name, newContent, old.creatorId));
    }

    public void createFirstBillboard() {
        Billboard billboard = new Billboard(0, "first", "somexml", 69420);
        try {
            billboardDb.addObject(billboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
