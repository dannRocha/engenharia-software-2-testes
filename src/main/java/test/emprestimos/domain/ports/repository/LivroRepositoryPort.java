package test.emprestimos.domain.ports.repository;

import java.util.Set;
import java.util.UUID;

import test.emprestimos.domain.model.Livro;

public interface LivroRepositoryPort extends Repository<Livro, UUID>  {
  Set<Livro> buscarColecaoDeLivros(Set<UUID> IDs);
  void salvarTodos(Set<Livro> livros);
}
