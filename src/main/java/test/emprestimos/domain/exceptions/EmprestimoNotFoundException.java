package test.emprestimos.domain.exceptions;

public class EmprestimoNotFoundException extends BaseException {
  public EmprestimoNotFoundException(String mensagem) {
    super(mensagem);
  }
}
