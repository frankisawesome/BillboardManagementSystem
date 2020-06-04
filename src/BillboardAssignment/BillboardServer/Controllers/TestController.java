package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.Server.ServerResponse;

/**
 * Singleton class that handles test requests - only the static method .use should be called
 */
public class TestController {
    String message;

    private TestController(String message) {
        this.message = message;
    }

    public static ServerResponse<String> use(String message) {
        TestController controller = new TestController(message);
        return new ServerResponse<>("Your request message was: " + message, "ok");
    }
}
