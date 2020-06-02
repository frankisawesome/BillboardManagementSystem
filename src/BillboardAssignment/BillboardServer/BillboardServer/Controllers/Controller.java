package BillboardAssignment.BillboardServer.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.RemoveOwnEditUsersPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.RemoveOwnUserException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;

import java.util.HashMap;

/**
 * An abstract class, with the main purpose being abstracting common logic existing between all controller classes
 */
public abstract class Controller {
    /**
     * The core fields given from a server request
     */
    protected String message;
    protected HashMap<String, String> body;
    //The request to be sent, undefined by default
    protected ServerResponse response;

    /**
     * Override this contructor if a child class has additional fields to be set up
     * @param message request message
     * @param body request body
     */
    public Controller(String message, HashMap<String, String> body) {
        this.message = message;
        this.body = body;
    }

    /**
     * All child classes should impelment a handle method, which does approriate business logic based on the request and assign approriate information into the response fiel
     */
    abstract void handle();

    /**
     * A shorthand method to generate a response with only a error message and an empty body string
     * @param error the error message
     * @return Server response containing the error
     */
    protected ServerResponse<String> errorResponse(String error) {
        return new ServerResponse<>("", error);
    }

    /**
     * Reconstruct a session key object from the id and key string
     * @return a UserSession key
     */
    protected UserSessionKey reconstructKey() {
        return new UserSessionKey(Integer.parseInt(body.get("keyId")), body.get("key"));
    }

    /**
     * Parse privilege string to UserPrivilege
     * @param privilege Privilege in string
     * @return privilege in UserPrivilege
     * @throws PrivilegeStringInvalidException When the string given has no match
     */
    protected UserPrivilege parsePrivilege(String privilege) throws PrivilegeStringInvalidException{
        switch (privilege) {
            case "EditUsers":
                return UserPrivilege.EditUsers;
            case "EditAllBillboards":
                return UserPrivilege.EditAllBillboards;
            case "ScheduleBillboards":
                return UserPrivilege.ScheduleBillboards;
            case "CreateBillboards":
                return UserPrivilege.CreateBillboards;
            default:
                throw new PrivilegeStringInvalidException();
        }
    }

    /**
     * Handles all db and session key related exceptions
     * @param operation Some db operation, normally method bodies of a controller class, should always pass a lambda function here to avoid boilerplate
     * @return The response from server
     */
    protected ServerResponse useDbTryCatch(DbOperation operation) {
        try {
            return operation.execution();
        }
        catch (InsufficentPrivilegeException e) {
            return errorResponse("User doesn't have the required privilege to perform operation");
        } catch (IncorrectSessionKeyException e) {
            return errorResponse("Session key invalid");
        } catch (OutOfDateSessionKeyException e) {
            return errorResponse("Session key has expired");
        } catch (DatabaseObjectNotFoundException e) {
            return errorResponse("Object not found in database, step through this error.");
        } catch (RemoveOwnUserException e) {
            return errorResponse("You appear to be trying to delete yourself as a user, that's a no-op");
        } catch (RemoveOwnEditUsersPrivilegeException e) {
            return errorResponse("You appear to be trying to remove your own EditUser privilege, that's a no-op");
        }
        catch (Exception e) {
            return errorResponse(e.getMessage());
        }
    }
}
