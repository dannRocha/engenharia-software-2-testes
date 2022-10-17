package test.emprestimos.domain.ports.service;

import java.util.Optional;
import java.util.UUID;

import test.emprestimos.domain.exceptions.LivroExisteException;
import test.emprestimos.domain.model.Livro;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;

public interface GerenciarLivrosServicePort {
  void cadastrarLivro(Livro livro);
  Optional<Livro> buscarLivroPorId(UUID id);
}
