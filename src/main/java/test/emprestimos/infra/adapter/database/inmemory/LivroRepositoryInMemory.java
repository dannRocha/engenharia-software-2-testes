package test.emprestimos.infra.adapter.database.inmemory;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import test.emprestimos.domain.model.Livro;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;

public class LivroRepositoryInMemory implements LivroRepositoryPort {
  private Set<Livro> livros = new HashSet<>();
  private static LivroRepositoryPort instance;

  private LivroRepositoryInMemory() {}

  public static LivroRepositoryPort obterInstancia() {
    if(Objects.isNull(instance)) {
      instance = new LivroRepositoryInMemory();
    }

    return instance;
  }

  @Override
  public Optional<Livro> buscarPorId(UUID id) {
    return livros.parallelStream()
      .filter(livro -> livro.getId().equals(id))
      .findFirst();
  }

  @Override
  public void salvar(Livro model) {
     
    if(Objects.isNull(model.getId())) {
      model.setId(UUID.randomUUID());
    }

    if(livros.contains(model)) {
      livros.remove(model);
    }
    
    livros.add(model);
    
  }

  @Override
  public void drop() {
    livros.clear();
  }

  @Override
  public Set<Livro> buscarColecaoDeLivros(Set<UUID> IDs) {
    var livrosBusca = new HashSet<Livro>();

    livros.forEach(livro -> {
      if(!IDs.contains(livro.getId()))
        return;

      livrosBusca.add(livro);
    });

    return livrosBusca;
  }

  @Override
  public void salvarTodos(Set<Livro> livros) {
    livros.stream().forEach(livro -> {
      salvar(livro);
    });
  }
  
}
