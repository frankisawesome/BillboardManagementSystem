package BillboardAssignment.BillboardServer;

import BillboardAssignment.BillboardServerControllers.AdminControllers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BillboardServer {
    HttpServer server;

    HttpHandler adminControllers = new AdminControllers();

    /**
     * Spin up the server on given port
     * @param port the port the the server will be listening on
     * @throws IOException throws exception when port is occupied
     */
    public BillboardServer(int port) throws IOException  {
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        server.createContext("/test", adminControllers);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();
    }
}
