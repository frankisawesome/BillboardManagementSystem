package BillboardAssignment.BillboardServer.BillboardServer;

import java.io.*;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.HashMap;

/**
 * A server request for data of type T
 * @param <T> the type of data you are requesting, for example String or User
 */
public class ServerRequest<T> implements Serializable {
    public final RequestType requestType;
    public final String requestMessage;
    public final HashMap<String, String> requestBody;

    /**
     * Constructs a new request ready to be sent to the server
     * @param requestType One of the RequestType enum fields
     * @param requestMessage What the request is
     */
    public ServerRequest(RequestType requestType, String requestMessage) {
        this.requestType = requestType;
        this.requestMessage = requestMessage;
        this.requestBody = new HashMap<String, String>();
    }

    public ServerRequest(RequestType requestType, String requestMessage, HashMap<String, String>requestBody) {
        this.requestType = requestType;
        this.requestMessage = requestMessage;
        this.requestBody = requestBody;
    }

    /**
     * Tries to send the request to the server and get approriate resonse - you need to write a try catch for this, and possibly re-send the response on certain exceptions
     * @return A ServerResponse object with a body of type T and status string
     * @throws Exception Many things could go wrong here, see the exact error by using e.getMessage()
     */
    public ServerResponse<T> getResponse() throws Exception {
        try (Socket socket = new Socket("localhost", 3005)) {
            // get the output stream from the socket.
            OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);

            InputStream input = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(input);
            ServerResponse response = (ServerResponse) objectInputStream.readObject();
            return response;
        } catch (UnknownHostException ex) {
            throw new Exception("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            throw new Exception("I/O error: " + ex.getMessage() + " make sure the server is actually running! you might want to call this method again because this exception might also inidcate the server is busy handling some other requests");
        } catch (ClassNotFoundException e) {
            throw new Exception("Class not found exception - check you have assigned the approriate type when using this generic class");
        }
    }
}
