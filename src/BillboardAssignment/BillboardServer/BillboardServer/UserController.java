package BillboardAssignment.BillboardServer.BillboardServer;


import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectPasswordException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserDataInput;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserManager;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;

import java.util.HashMap;

public class UserController {
    private String message;
    private HashMap<String, String> body;
    private UserManager userManager;
    private ServerResponse response;

    private UserController(String message, UserManager userManager, HashMap<String, String> body) {
        this.message = message;
        this.userManager = userManager;
        this.body = body;
    }

    private void Handle() {
        switch (message) {
            case "login":
                try {
                    UserSessionKey sessionKey = userManager.login(new UserDataInput(Integer.parseInt(body.get("id")), body.get("password")));
                    response = new ServerResponse<UserSessionKey>(sessionKey, "ok");
                } catch (IncorrectPasswordException | DatabaseObjectNotFoundException e) {
                    response = new ServerResponse("", "Incorrect id or password");
                } catch (Exception e) {
                    response = new ServerResponse("", e.getMessage());
                }
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    public static ServerResponse use(String message, UserManager manager, HashMap<String, String> body) {
        UserController controller = new UserController(message, manager, body);
        controller.Handle();
        return controller.response;
    }
}
