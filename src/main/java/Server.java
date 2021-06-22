import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int SERVER_PORT = 11111;
    static ExecutorService executor = Executors.newFixedThreadPool(64);

    public static void main(String[] args) {
        final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

        final ServerSocket serverSocket = createServer();

        workServer(serverSocket, validPaths);
    }

    public static ServerSocket createServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }

    private static void workServer(ServerSocket serverSocket, List<String> validPaths) {
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                executor.execute(new MonoThread(socket, validPaths));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
