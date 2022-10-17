package test.emprestimos.domain.ports.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import test.emprestimos.domain.model.Emprestimo;

public interface EmprestimoRepositoryPort extends Repository<Emprestimo, UUID>  {
  Set<Emprestimo> buscarEmprestimosPorUsuario(UUID usuarioID);
  Optional<Emprestimo> buscarUltimoEmprestimoPorUsuario(UUID usuarioID);
  Set<Emprestimo> buscarEmprestimosPendentesPorUsuario(UUID usuarioID);
}
