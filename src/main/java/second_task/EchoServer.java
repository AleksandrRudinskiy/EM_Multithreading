package second_task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    private static final int PORT = 7; // Порт 7 для Echo протокола

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo Server запущен на порту " + PORT);
            while (true) {
                // Ждем подключения клиента
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключение установлено: " + clientSocket.getInetAddress());

                // Создаем новый поток для обработки клиента
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }
}

// Обработчик клиента
class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();
        ) {
            String inputLine;
            // Читаем данные от клиента и отправляем их обратно
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Получено от клиента: " + inputLine);
                // Отправляем обратно данные клиенту
                out.write((inputLine + "\n").getBytes()); // Добавляем перенос строки
                out.flush(); // Сбрасываем буфер
            }
        } catch (IOException e) {
            System.err.println("Ошибка обработки клиента: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Закрываем соединение после завершения
                System.out.println("Соединение закрыто: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Ошибка закрытия соединения: " + e.getMessage());
            }
        }
    }
}