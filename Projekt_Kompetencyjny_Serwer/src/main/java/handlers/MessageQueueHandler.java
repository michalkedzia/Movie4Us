package handlers;

import handlers.ClientHandler;
import log.MyLOG;
import model.Message;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class MessageQueueHandler implements Runnable {
  private ConcurrentMap<String,ClientHandler> clientsMap;
  private BlockingQueue<Message> messageQueue;

    public MessageQueueHandler(ConcurrentMap<String, ClientHandler> clientsMap, BlockingQueue<Message> messageQueue) {
        this.clientsMap = clientsMap;
        this.messageQueue = messageQueue;
    }

    @Override
  public void run() {
    while (true) {
        Message peek = null;
        try {
            peek = messageQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

      switch (peek.getAction()) {
        case "connect":
            connectTwoUser(peek.getUsername(),peek.getConnectedUser());
          break;

      }

      MyLOG.myLOG(peek.toString());
    }
  }

  // TODO nowy wyjatek do obsługi
  private void connectTwoUser(String from, String to) {
      ClientHandler clientHandlerFrom = clientsMap.get(from);
      ClientHandler clientHandlerTo = clientsMap.get(to);
      clientHandlerFrom.setConnectedUser(to);
      clientHandlerTo.setConnectedUser(from);

      clientHandlerFrom.getOut().write(from + " " + to + "\n");
      clientHandlerFrom.getOut().flush();

      clientHandlerTo.getOut().write(from + " " + to + "\n");
      clientHandlerTo.getOut().flush();


  }
}
