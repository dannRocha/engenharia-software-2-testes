package test.emprestimos.domain.exceptions;

public class UsuarioExisteException extends BaseException{
  public UsuarioExisteException(String mensagem) {
    super(mensagem);
  }
}
