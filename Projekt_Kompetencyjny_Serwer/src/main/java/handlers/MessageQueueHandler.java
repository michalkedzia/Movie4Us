package handlers;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import log.*;
import model.*;

public class MessageQueueHandler implements Runnable {
  private ConcurrentMap<String, ClientHandler> clientsMap;
  private BlockingQueue<Message> messageQueue;

  public MessageQueueHandler(
      ConcurrentMap<String, ClientHandler> clientsMap, BlockingQueue<Message> messageQueue) {
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
          {
            connectTwoUser(peek.getUsername(), peek.getConnectedUser());
            break;
          }
        case "category":
          {
            categorySelection(peek);
            break;
          }
      }

      MyLOG.myLOG(peek.toString());
    }
  }

  private void categorySelection(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    if (clientHandler.getCommonList().stream()
        .map(Message::getAction)
        .collect(Collectors.toList())
        .contains("category")) {
      Iterator<Message> iterator = clientHandler.getCommonList().iterator();
      while (iterator.hasNext()) {
        Message next = iterator.next();
        if (next.getAction().equals("category")) {
          System.out.println(next.getSelectedCategory() + " " + message.getSelectedCategory());
//          TODO filmy sa wyierane na pdostwe jednej kategti, trzba dodać żeby byly po kilku

          iterator.remove();
          break;
        }
      }
    } else {
      clientHandler.getCommonList().add(message);
    }
  }






  // TODO nowy wyjatek do obsługi albo nie
  private void connectTwoUser(String from, String to) {
    ClientHandler clientHandlerFrom = clientsMap.get(from);
    ClientHandler clientHandlerTo = clientsMap.get(to);
    clientHandlerFrom.setConnectedUser(to);
    clientHandlerTo.setConnectedUser(from);

    clientHandlerFrom.getOut().write(from + " " + to + "\n");
    clientHandlerFrom.getOut().flush();

    clientHandlerTo.getOut().write(from + " " + to + "\n");
    clientHandlerTo.getOut().flush();

    Vector<Message> list = new Vector<>();
    clientHandlerFrom.setCommonList(list);
    clientHandlerTo.setCommonList(list);
  }




}
