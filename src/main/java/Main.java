import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
    public static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        Server server = new Server(64);

        server.addHandler("GET", "/classic.html", (request, out) -> {
            try{
                final var filePath = Path.of(".", "public", "classic.html");
                final var mimeType = Files.probeContentType(filePath);
                final var template = Files.readString(filePath);
                    final var content = template.replace(
                            "{time}",
                            LocalDateTime.now().toString()
                    ).getBytes();
                    out.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + content.length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.write(content);
                    out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.listen(SERVER_PORT);
    }
}

