package BillboardAssignment.BillboardServerControllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class AdminControllers implements HttpHandler {
    public  AdminControllers() {

    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(200, 1);
        outputStream.write('o');
        outputStream.flush();
        outputStream.close();
    }
}
