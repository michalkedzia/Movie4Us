package exceptions;

public class APIException {
  public static class InvalidFilterValueException extends BaseException {
    public InvalidFilterValueException(String value) {
      super(value);
    }
  }

  public static class WrongCallTypeException extends BaseException {
    public WrongCallTypeException(String value) {
      super(value);
    }
  }

  public static class WrongJsonObjectException extends BaseException {
    public WrongJsonObjectException(String msg) {
      super(msg);
    }
  }
}
