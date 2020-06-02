package BillboardAssignment.BillboardServer.Server;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {
    private T responseBody;
    private String responseStatus;

    public ServerResponse (T body, String status) {
        responseBody = body;
        responseStatus = status;
    }

    public String status() {
        return responseStatus;
    }

    public T body() {
        return responseBody;
    }
}
