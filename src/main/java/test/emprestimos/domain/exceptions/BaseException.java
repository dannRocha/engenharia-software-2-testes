package test.emprestimos.domain.exceptions;

public class BaseException extends RuntimeException {
  public BaseException(String mensagem) {
    super(mensagem);
  }
}
