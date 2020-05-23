package BillboardAssignment.BillboardServer.MockClient;

import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class Main {
    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 3005)) {
            ServerRequest request = new ServerRequest("My request type");
            // get the output stream from the socket.
            OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(request);

            InputStream input = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(input);

            ServerResponse<String> response = (ServerResponse<String>) objectInputStream.readObject();
            System.out.println("Reponse stats: " + response.status());
            System.out.println(response.body());


        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("This should never happen because my server code is rock solid");
        }
    }
}
