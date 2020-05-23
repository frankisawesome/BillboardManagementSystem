package BillboardAssignment.BillboardServer.BillboardServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

public class BillboardServer {
    ServerSocket server;
    public BillboardServer() {
        try {
            init(getPortFromProps());
        }
        //catch file not found before IO as there's only one possible cause
        catch (FileNotFoundException e) {
            System.err.println("Server props file not found, include server.props under the project folder");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private int getPortFromProps() throws FileNotFoundException, IOException{
        Properties props = new Properties();
        FileInputStream in = null;
        in = new FileInputStream("./server.props");
        props.load(in);
        in.close();
        return Integer.parseInt(props.getProperty("port"));
    }

    private void init(int port) throws IOException{
        server = new ServerSocket(port);
    }

    public void run() throws IOException {
        while (true) {
            System.out.println("Server running");
            Socket socket = server.accept();
            String responseBody;
            System.out.println("connection from" + socket);
            // get the input stream from the connected socket
            InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            try {
                ServerRequest request = (ServerRequest) objectInputStream.readObject();
                responseBody = "Your request type is: " + request.requestType();
            } catch (ClassNotFoundException e) {
                responseBody = "Server only accepts a ServerRequest object";
            }

            ServerResponse<String> response = new ServerResponse<String>(responseBody, "ok");

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject(response);

            objectInputStream.close();
        }
    }
}
