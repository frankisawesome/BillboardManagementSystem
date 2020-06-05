package BillboardAssignment.BillboardServer.Tests;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static BillboardAssignment.BillboardServer.Tests.TestUserControllers.requestBodyWithKey;
import static org.junit.jupiter.api.Assertions.*;

public class TestScheduleControllers {
    @Test
    void testSetSchedule() throws Exception {
        HashMap<String, String> requestBody = requestBodyWithKey();
        requestBody.put("billboardName", "first");
        requestBody.put("day", "Thursday");
        requestBody.put("startTime", "00:00");
        requestBody.put("endTime", "23:59");
        ServerRequest request = new ServerRequest(RequestType.SCHEDUELE, "set", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    void getCurrentSchedule() throws Exception {
        HashMap<String, String> requestBody = requestBodyWithKey();
        ServerRequest request = new ServerRequest(RequestType.SCHEDUELE, "get schedule", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    void testScheduleList() throws Exception {
        HashMap<String, String> requestBody = requestBodyWithKey();
        ServerRequest request = new ServerRequest(RequestType.SCHEDUELE, "schedule list", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    void testRemoveSchedule() throws Exception {
        HashMap<String, String> requestBody = requestBodyWithKey();
        requestBody.put("billboardName", "first");
        ServerRequest request = new ServerRequest(RequestType.SCHEDUELE, "remove", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

}
