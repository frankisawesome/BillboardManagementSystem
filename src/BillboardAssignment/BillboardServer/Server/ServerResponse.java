package BillboardAssignment.BillboardServer.Server;

import java.io.Serializable;

/**
 * Contains a body and a status, mimicking http responses
 * @param <T>
 */
public class ServerResponse<T> implements Serializable {
    private T responseBody;
    private String responseStatus;

    /**
     * Construtor with a generic body tpye and a string status
     * @param body
     * @param status
     */
    public ServerResponse (T body, String status) {
        responseBody = body;
        responseStatus = status;
    }

    /**
     * Getter method for status
     * @return status string
     */
    public String status() {
        return responseStatus;
    }

    /**
     * Getter method for body
     * @return body
     */
    public T body() {
        return responseBody;
    }
}
