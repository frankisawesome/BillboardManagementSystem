package BillboardAssignment.BillboardServer.MockClient;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;

/**
 * A collection of mains that demonstrate the use of the server
 */
public class TestMockClient {
    /**
     * simple demo of sending a test request to the server, try this out, and take a look at the ServerRequest and ServerResponse classes
     * Make sure you have the server running - run the main method in BillboardServer
     * @param args
     */
    public static void main(String[] args) {
        ServerRequest<String> myRequest = new ServerRequest<String>(RequestType.TEST, "Test Message");
        try {
            ServerResponse<String> response = myRequest.getResponse();
            System.out.println("Response status: "+ response.status());
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
