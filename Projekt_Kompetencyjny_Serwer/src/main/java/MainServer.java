import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import model.*;
import handlers.*;
import log.*;


public class MainServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(5000);
        Gson gson = new Gson();
        Message message;


        ConcurrentMap<String,ClientHandler> clientsMap = new ConcurrentHashMap<>();

//        List<ClientHandler> clientHandlerList = new CopyOnWriteArrayList<>();
        BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>();
        executorService.execute(new MessageQueueHandler(clientsMap,messageQueue));


        while (true){
            System.out.println("server start");
            Socket socket = serverSocket.accept();
            System.out.println(socket.toString());
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ClientHandler clientHandler;
            String json = bufferedReader.readLine();
            message = gson.fromJson(json,Message.class);
            if(message.getAction().equals("login")){
                clientHandler = new ClientHandler(socket,printWriter,bufferedReader,messageQueue);
                clientHandler.setUsername(message.getUsername());
                clientsMap.put(message.getUsername(),clientHandler);
                executorService.execute(clientHandler);
            }else {
                MyLOG.myLOG("login error " + socket);
            }


//            ClientHandler clientHandler = new ClientHandler(socket, printWriter, bufferedReader,messageQueue);
//            clientHandlerList.add(clientHandler);
//            executorService.execute(clientHandler);
        }
    }






}
