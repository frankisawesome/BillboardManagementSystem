package BillboardAssignment.BillboardServer.Services.Billboard;

import BillboardAssignment.BillboardServer.Services.Authentication.*;
import BillboardAssignment.BillboardServer.Services.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.Services.User.User;
import BillboardAssignment.BillboardServer.Services.User.UserManager;
import BillboardAssignment.BillboardServer.Services.User.UserPrivilege;
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

    public boolean validateName(UserSessionKey key, String name) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException {
        checkSessionKeyPrivileges(key, UserPrivilege.EditAllBillboards);

        try {
            ArrayList<Billboard> billboard =  billboardDb.getWhere("name", name, new Billboard("", "", 0));
            if (billboard.size() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
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

    public void delete(int billboardId, UserSessionKey key) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException {
        checkSessionKeyPrivileges(key, UserPrivilege.EditAllBillboards);
        billboardDb.removeObject(billboardId);
    }

    public Billboard get(String name) throws DatabaseObjectNotFoundException, NoSuchFieldException, DatabaseNotAccessibleException {
        return billboardDb.getWhere("name", name, new Billboard("", "", 0)).get(0);
    }

    public void createFirstBillboard() {
        Billboard billboard = new Billboard(0, "first", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<billboard>\n" +
                "    <message colour=\"#000000\">Second Billboard</message>\n" +
                "    <picture \n" +
                "\tdata=\"iVBORw0KGgoAAAANSUhEUgAAACAAAAAQCAIAAAD4YuoOAAAAKXRFWHRDcmVhdGlvbiBUaW1lAJCFIDI1IDMgMjAyMCAwOTowMjoxNyArMDkwMHlQ1XMAAAAHdElNRQfkAxkAAyQ8nibjAAAACXBIWXMAAAsSAAALEgHS3X78AAAABGdBTUEAALGPC/xhBQAAAS5JREFUeNq1kb9KxEAQxmcgcGhhJ4cnFwP6CIIiPoZwD+ALXGFxj6BgYeU7BO4tToSDFHYWZxFipeksbMf5s26WnAkJki2+/c03OzPZDRJNYcgVwfsU42cmKi5YjS1s4p4DCrkBPc0wTlkdX6bsG4hZQOj3HRDLHqh08U4Adb/zgEMtq5RuH3Axd45PbftdB2wO5OsWc7pOYaOeOk63wYfdFtL5qldB34W094ZfJ+4RlFldTrmW/ZNbn2g0of1vLHdZq77qSDCaSAsLf9kXh9w44PNoR/YSPHycEmbIOs5QzBJsmDHrWLPeF24ZkCe6ZxDCOqHcmxmsr+hsicahss+n8vYb8NHZPTJxi/RGC5IqbRwqH6uxVTX+5LvHtvT/V/R6PGh/iF4GHoBAwz7RD26spwq6Amh/AAAAAElFTkSuQmCC\"/>\n" +
                "    <information colour=\"#000000\">This billboard has a message tag, a picture tag (linking to a URL with a GIF image) and an information tag. The picture is drawn in the centre and the message and information text are centred in the space between the top of the image and the top of the page, and the space between the bottom of the image and the bottom of the page, respectively.</information>\n" +
                "</billboard>", 69420);
        try {
            billboardDb.addObject(billboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
