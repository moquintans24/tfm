package es.udc.lbd.tfm.restservice.web.exception;

public class RequestBodyNotValidException extends ResourceException {
  public RequestBodyNotValidException(String errorMsg) {
    super(errorMsg);
  }
}
