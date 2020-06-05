package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.Services.Billboard.ScheduleManager;
import BillboardAssignment.BillboardServer.Server.ServerResponse;

import java.util.HashMap;

public class ScheduleController extends Controller {
    private ScheduleManager scheduleManager;
    private ServerResponse response;

    private ScheduleController (String message, HashMap<String, String> body, ScheduleManager scheduleManager) {
        super(message, body);
        this.scheduleManager = scheduleManager;
    }
    @Override
    void handle() {
        switch (message) {
            case "set":
                response = set();
                break;
            case "remove":
                response = remove();
                break;
            case "get schedule":
                response = getSchedule();
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    private ServerResponse set() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            scheduleManager.addToSchedule(body.get("billboardName"), body.get("day"), body.get("startTime"), body.get("endTime"), key);
            return new ServerResponse("Schedule set", "ok");
        });
    }

    private ServerResponse remove() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            scheduleManager.removeFromSchedule(body.get("billboardName"), key);
            return new ServerResponse("Schedule removed", "ok");
        });
    }

    private ServerResponse getSchedule() {
        return useDbTryCatch(() -> {
            String schedule = scheduleManager.scheduledBillboard();
            return new ServerResponse(schedule, "ok");
        });
    }

    public static ServerResponse use(String message, HashMap<String, String> body, ScheduleManager scheduleManager) {
        ScheduleController controller = new ScheduleController(message, body, scheduleManager);
        controller.handle();
        return controller.response;
    }
}
