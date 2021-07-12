import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final List<String> listQueryParam;
    private final List<String> listQueryValue;
    private final InputStream in;

    private Request(String method, String path, Map<String, String> headers, InputStream in, List<String> listQueryParam, List<String> listQueryValue) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.listQueryParam = listQueryParam;
        this.listQueryValue = listQueryValue;
        this.in = in;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getListQueryParam() {
        return listQueryParam;
    }

    public List<String> getListQueryValue() {
        return listQueryValue;
    }

    public InputStream getIn() {
        return in;
    }

    public static Request fronInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // read only request line for simplicity
        // must be in form GET /path HTTP/1.1
        final var requestLine = reader.readLine();
        final var parts = requestLine.split(" ");

        if (parts.length != 3) {
            throw new IOException("Invalid request");
        }
        String method = parts[0];
        String path = parts[1];
        String line;

        //считываем headers
        Map<String, String> headers = new HashMap<>();
        while(!(line = reader.readLine()).equals("")){
            int i = line.indexOf(":");
            String headersName = line.substring(0, i);
            String headersValue = line.substring(i + 2);
            headers.put(headersName, headersValue);
        }

        //считываем query параметры со значениями
        String[] arr = path.split("\\?");
        String pathNotQuery = arr[0];
        String[] queryParams = arr[1].split("&");
        List<String> listQueryParam = new ArrayList<>(queryParams.length);
        List<String> listQueryValue = new ArrayList<>(queryParams.length);
        for (int i = 0; i < queryParams.length; i++) {
            int y = queryParams[i].indexOf("=");
            if (queryParams[i].substring(y + 1).isEmpty()) {
                queryParams[i] += "null";
            }
            String[] queryParamValue = queryParams[i].split("=");

            listQueryParam.add(queryParamValue[0]);
            listQueryValue.add(queryParamValue[1]);
        }

        return new Request(method, path, headers, inputStream, listQueryParam, listQueryValue);
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", headers=" + headers +
                '}';
    }
}