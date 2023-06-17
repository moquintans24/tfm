package es.udc.lbd.tfm.restservice.model.exception;

public class UserLoginExistsException extends ModelException {
  public UserLoginExistsException(String msg) {
    super(msg);
  }
}
