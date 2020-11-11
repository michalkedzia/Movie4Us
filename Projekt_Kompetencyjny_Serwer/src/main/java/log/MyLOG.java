package log;

public class MyLOG {

  public static synchronized void myLOG(String log) {
    System.out.println("*** SERVER LOG ---> " + log);
  }
}
