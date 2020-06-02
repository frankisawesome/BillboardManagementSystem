package BillboardAssignment.BillboardServer.BillboardServer.Tests;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static BillboardAssignment.BillboardServer.BillboardServer.Tests.TestUserControllers.requestBodyWithKey;
import static org.junit.jupiter.api.Assertions.*;

public class TestBillboardControllers {
    @Test
    void getBillboard() throws Exception{
        ServerRequest<String> request = new ServerRequest<String>(RequestType.BILLBOARD, "get one");
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    void createBillboard() throws Exception{
        HashMap<String, String> requestBody = requestBodyWithKey();
        requestBody.put("billboardName", "mybb");
        requestBody.put("content", "<billboard><title>My Billboard!</title></billboard>");
        ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "create", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    void listAllBillboards() throws Exception {
        HashMap<String, String> requestBody = requestBodyWithKey();
        ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "list billboards", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
        //Also check if the body is an array of billboards
    }

    @Test
    void renameBillboard() throws Exception {
        HashMap<String, String> requestBody = requestBodyWithKey();
        requestBody.put("billboardId", "0");
        requestBody.put("newName", "mynewbb");
        ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "rename billboard", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    void checkIfSchedueled() throws Exception {
        HashMap<String, String> requestBody = requestBodyWithKey();
        requestBody.put("billboardName", "mybb");
        ServerRequest request = new ServerRequest(RequestType.BILLBOARD, "is schedueled", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
        assertEquals(true, response.body());
    }

}
