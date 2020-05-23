package BillboardAssignment.BillboardServer.BillboardServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * Containing server setup and running methods
 */
public class BillboardServer {
    ServerSocket server;

    /**
     * A main method for creating a server instance and running it
     * @param args
     */
    public static void main (String[] args) {
        try {
            BillboardServer server = new BillboardServer();
            server.run();
        }
        catch (Exception e) {
            //if you see booger my code broke :(( solly
            System.out.print("Booger");
        }
    }

    /**
     * Constructor for a server instance, it will try to initialise the server using server.props - this file should be placed under the project root
     */
    public BillboardServer() {
        try {
            //initialising the server with props read from file
            init(getPortFromProps());
        }
        //catch file not found before IO as there's only one possible cause
        catch (FileNotFoundException e) {
            System.err.println("Server props file not found, include server.props under the project folder");
        } catch (IOException e) {
            //IO exception could be caused by a number of factors so we print out the actual exception message
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method reads props from a file and returns the port
     * @return the port specified in server.props
     * @throws FileNotFoundException server.props missing
     * @throws IOException
     */
    private int getPortFromProps() throws FileNotFoundException, IOException{
        Properties props = new Properties();
        FileInputStream in = null;
        in = new FileInputStream("./server.props");
        props.load(in);
        in.close();
        return Integer.parseInt(props.getProperty("port"));
    }

    /**
     * Initialises the socket server on a specified port
     * @param port and integer
     * @throws IOException
     */
    private void init(int port) throws IOException{
        server = new ServerSocket(port);
    }

    private ServerResponse useControllers(InputStream input) {
        ServerResponse<String> response;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(input);
            ServerRequest request = (ServerRequest) objectInputStream.readObject();
            System.out.println("Request received, message: " + request.requestMessage);
            switch (request.requestType) {
                case TEST:
                    response = TestController.use(request.requestMessage);
                    break;
                default:
                    response = new ServerResponse<String>("", "Request type must be of requestType enum");
            }
        } catch (Exception e) {
            response = new ServerResponse<String>("", e.getMessage());
        }
        return response;
    }

    /**
     * Runs the socket server in a single threaded loop
     * @throws IOException
     */
    public void run() throws IOException {
        while (true) {
            //tells the user the server is up and running
            System.out.println("Server listening");
            //handles incoming socket connections
            Socket socket = server.accept();
            String responseBody;
            System.out.println("connection from" + socket);
            // get the input stream from the connected socket
            ServerResponse response = useControllers(socket.getInputStream());

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject(response);
        }
    }
}
