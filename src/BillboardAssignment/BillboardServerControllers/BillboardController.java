package BillboardAssignment.BillboardServerControllers;

import com.sun.net.httpserver.HttpHandler;

public interface BillboardController extends HttpHandler {
    public String path();
}
