package test.emprestimos.infra.adapter.database.jpa;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import test.emprestimos.domain.model.Emprestimo;
import test.emprestimos.domain.ports.repository.EmprestimoRepositoryPort;
import test.emprestimos.infra.adapter.database.jpa.config.EmprestimoRepositoryJPA;

// @Component
@AllArgsConstructor
public class EmprestimoRepositoryJPAImpl implements EmprestimoRepositoryPort{

  private final EmprestimoRepositoryJPA emprestimoRepository;

  @Override
  public Optional<Emprestimo> buscarPorId(UUID id) {
    return Optional.empty();
  }

  @Override
  public void salvar(Emprestimo model) {
  }

  @Override
  public void drop() {
  }

  @Override
  public Set<Emprestimo> buscarEmprestimosPorUsuario(UUID id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<Emprestimo> buscarUltimoEmprestimoPorUsuario(UUID id) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

  @Override
  public Set<Emprestimo> buscarEmprestimosPendentesPorUsuario(UUID usuarioID) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
