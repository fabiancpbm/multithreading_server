import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(6666);
    }

    private ServerSocket socket;

    public void start(int port) throws IOException {
        System.out.println("[INFO] - Starting server");
        this.socket = new ServerSocket(port);
        System.out.println("[INFO] - Waiting clients...");
        while (true) {
            final Socket clientSocket = socket.accept();
            final ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
        }
    }

    public void stop() throws IOException {
        this.socket.close();
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                String clientRequest = "";
                while ((clientRequest = in.readLine()) != null) {
                    System.out.println("[INFO] - Thread '" + this.getName() + "' received the request '" +
                            clientRequest + "'");
                    out.println("Service provided by Thread '" + this.getName() + "'");
                }

                in.close();
                out.close();
                this.clientSocket.close();
            } catch (IOException e) {
                System.err.println("[ERROR] - " + e.getMessage());
            }
        }
    }
}
