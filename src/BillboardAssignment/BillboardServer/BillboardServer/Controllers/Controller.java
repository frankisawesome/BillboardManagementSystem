package BillboardAssignment.BillboardServer.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;

import java.util.HashMap;

public abstract class Controller {
    protected String message;
    protected HashMap<String, String> body;
    protected ServerResponse response;

    public Controller(String message, HashMap<String, String> body) {
        this.message = message;
        this.body = body;
    }

    //Should generate the reponse, based on the message property
    abstract void handle();

    protected ServerResponse<String> errorResponse(String error) {
        return new ServerResponse<>("", error);
    }

    protected UserSessionKey reconstructKey() {
        return new UserSessionKey(Integer.parseInt(body.get("keyId")), body.get("key"));
    }

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
        } catch (Exception e) {
            return errorResponse(e.getMessage());
        }
    }
}
