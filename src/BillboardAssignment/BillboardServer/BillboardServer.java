package BillboardAssignment.BillboardServer;

import BillboardAssignment.BillboardServerControllers.BillboardController;
import BillboardAssignment.BillboardServerControllers.Billboards.GetBillboard;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BillboardServer {
    private HttpServer server;

    private BillboardController[] controllers;

    /**
     * Spin up the server on given port
     * @param port the port the the server will be listening on
     * @throws IOException throws exception when port is occupied
     */
    public BillboardServer(int port) throws IOException  {
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        //Instantiate all controllers
        controllers = new BillboardController[] { new GetBillboard() };
    }

    public void start() {
        for (BillboardController controller: controllers) {
            server.createContext(controller.path(), controller);
        }
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();
    }
}
