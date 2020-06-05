package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.BillboardManager;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.Schedule;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.ScheduleManager;
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
            case "schedule list":
                response = scheduleList();
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    private ServerResponse set() {
        return useDbTryCatch(() -> {
            scheduleManager.addToSchedule(body.get("billboardName"), body.get("day"), body.get("startTime"), body.get("endTime"));
            return new ServerResponse("Schedule set", "ok");
        });
    }

    private ServerResponse scheduleList() {
        return useDbTryCatch(() -> new ServerResponse(scheduleManager.getAllSchedules(), "ok"));
    }

    private ServerResponse remove() {
        return useDbTryCatch(() -> {
            scheduleManager.removeFromSchedule(body.get("billboardName"));
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
