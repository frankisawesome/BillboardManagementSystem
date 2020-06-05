package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.Billboard;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.BillboardManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Child class of controller, handles billboard related requests using a billboardManager
 */
public class BillboardController extends Controller {
    /**
     * Manages all db logic required
     */
    private BillboardManager billboardManager;

    /**
     * Child constructor
     * @param message request message
     * @param body request body
     * @param billboardManager billboard manager
     */
    private BillboardController(String message, HashMap<String, String> body, BillboardManager billboardManager) {
        super(message, body);
        this.billboardManager = billboardManager;
    }

    /**
     * handles the requests by invoking appropriate private method based on message
     */
    @Override
    protected void handle() {
        switch (message) {
            case "current":
                response = getCurrent();
                break;
            case "create":
                response = create();
                break;
            case "list billboards":
                response = list();
                break;
            case "rename billboard":
                response = rename();
                break;
            case "edit billboard":
                response = edit();
                break;
            case "delete billboard":
                response = delete();
                break;
            case "validate name":
                response = validateName();
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    /**
     * Get the currently scheduled billboard
     * @return response with Billboard body or error
     */
    private ServerResponse getCurrent() {
        return useDbTryCatch(() -> new ServerResponse(billboardManager.get("first"), "ok"));
    }

    /**
     * Create new billboard in db
     * @return success or error message
     */
    private ServerResponse create() {
        return useDbTryCatch(() -> {
            Billboard billboard = new Billboard(body.get("billboardName"), body.get("content"),Integer.parseInt(body.get("keyId")));
            UserSessionKey key = reconstructKey();
            billboardManager.create(billboard, key);
            return new ServerResponse("Billboard created", "ok");
        });
    }

    /**
     * Validates a billboard name by checking for duplicates
     * @return response with boolean indicating if valid or not
     */
    private ServerResponse validateName() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            boolean valid = billboardManager.validateName(key, body.get("name"));
            if (valid) {
                return new ServerResponse(true, "ok");
            } else {
                return new ServerResponse(false, "ok");
            }
        });
    }

    /**
     * List all billboards
     * @return response with a ArrayList of all billboards
     */
    private ServerResponse list() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            ArrayList<Billboard> billboards = billboardManager.list(key);
            return new ServerResponse(billboards, "ok");
        });
    }

    /**
     * Delete a billboard based on id
     * @return success or error message
     */
    private ServerResponse delete() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            billboardManager.delete(Integer.parseInt(body.get("billboardId")), key);
            return new ServerResponse("Billboard deleted", "ok");
        });
    }

    /**
     * Rename a billboard
     * @return success or error message
     */
    private ServerResponse rename() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            billboardManager.rename(Integer.parseInt(body.get("billboardId")), body.get("newName"), key);
            return new ServerResponse("Rename success", "ok");
        });
    }

    /**
     * Edit a billboard's content
     * @return success or error message
     */
    private ServerResponse edit() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            billboardManager.edit(Integer.parseInt(body.get("billboardId")), body.get("newContent"), key);
            return new ServerResponse("Edit success", "ok");
        });
    }

    /**
     * The only method that should be invoked outside of this class - instantiates controller, handles request and returns response
     * @param message request message
     * @param body request body
     * @param billboardManager billboard manager
     * @return corresponding server response
     */
    public static ServerResponse use(String message, HashMap<String, String> body, BillboardManager billboardManager) {
        BillboardController controller = new BillboardController(message, body, billboardManager);
        controller.handle();
        return controller.response;
    }
}
