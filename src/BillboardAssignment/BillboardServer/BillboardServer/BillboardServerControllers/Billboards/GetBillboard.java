package BillboardAssignment.BillboardServer.BillboardServer.BillboardServerControllers.Billboards;

import BillboardAssignment.BillboardServer.BillboardServer.BillboardServerControllers.BillboardController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetBillboard implements HttpHandler, BillboardController {
    String path = "/billboard";

    public  GetBillboard() {

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "This is a fake billboard";
        exchange.sendResponseHeaders(200, 0);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    public String path() {
        return path;
    }
}
