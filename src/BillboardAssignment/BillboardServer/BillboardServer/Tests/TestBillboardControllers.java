package BillboardAssignment.BillboardServer.BillboardServer.Tests;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestBillboardControllers {
    @Test
    void checkGetBillboard() throws Exception{
        ServerRequest<String> request = new ServerRequest<String>(RequestType.BILLBOARD, "get one");
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }
}
