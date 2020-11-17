package handlers;

import api.data.PageMovieData;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import log.*;

import model.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BlockingQueue<Message> messageQueue;
    private Vector<Message> commonList;
    private String username;
    private String connectedUser;

    public ClientHandler(Socket socket, PrintWriter out, BufferedReader in, BlockingQueue<Message> messageQueue) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.messageQueue = messageQueue;
    }

    public Vector<Message> getCommonList() {
        return commonList;
    }

    public void setCommonList(Vector<Message> commonList) {
        this.commonList = commonList;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(BlockingQueue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(String connectedUser) {
        this.connectedUser = connectedUser;
    }

    @Override
    public void run() {
        String json = null;
        Gson gson = new Gson();
        Message message;


        while (true){
            try {
                json= in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MyLOG.myLOG(json);
            message = gson.fromJson(json, Message.class);
            messageQueue.add(message);
        }
    }


}
