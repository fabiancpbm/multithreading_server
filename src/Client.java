import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Client client = new Client("CLIENT_" + finalI);
                    client.startConnection("localhost", 6666);
                    final String response = client.sendRequest("HELLO_SERVER_" + client.clientName);
                    System.out.println("HELLO_SERVER_" + client.clientName + " Response: " + response);
                    client.stopConnection();
                } catch (IOException e) {
                    System.err.println("[ERROR] - " + e.getMessage());
                }
            }).start();
        }
    }

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public Client(String clientName) {
        this.clientName = clientName;
    }

    public void startConnection(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public String sendRequest(String message) throws IOException {
        this.out.println(message);
        String response = this.in.readLine();
        return response;
    }

    public void stopConnection() throws IOException {
        this.in.close();
        this.out.close();
        this.socket.close();
    }
}
