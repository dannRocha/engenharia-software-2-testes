package test.emprestimos.domain.ports.repository;

import java.util.Optional;

public interface Repository<T, ID> {
  Optional<T> buscarPorId(ID id);
  void salvar(T model);
  void drop();
}
