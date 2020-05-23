package BillboardAssignment.BillboardServer.BillboardServer;

import java.io.Serializable;
import java.util.HashMap;

public class ServerRequest implements Serializable {
    private HashMap<String, String> data = new HashMap<String, String>();

    public ServerRequest(String requestType) {
        data.put("request", requestType);
    }

    public String requestType() {
        return data.get("request");
    }
}
