package test.emprestimos.domain.ports.service.emprestimo;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import test.emprestimos.domain.model.Emprestimo;

public interface RegistrarEmprestimoServicePort {
  void registrarEmprestimo(Emprestimo emprestimo);
  Optional<Emprestimo> buscarEmprestimoPorId(UUID id);
  Set<Emprestimo> buscarEmprestimosPorUsuario(UUID id);
}
