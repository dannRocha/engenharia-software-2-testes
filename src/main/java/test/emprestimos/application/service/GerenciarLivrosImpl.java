package test.emprestimos.application.service;

import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import test.emprestimos.domain.exceptions.LivroExisteException;
import test.emprestimos.domain.model.Livro;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;
import test.emprestimos.domain.ports.service.GerenciarLivrosServicePort;

// @Service
@AllArgsConstructor
public class GerenciarLivrosImpl implements GerenciarLivrosServicePort {
  protected final LivroRepositoryPort livroRepository;

  public void cadastrarLivro(Livro livro) {

    if(livroRepository.buscarPorId(livro.getId()).isPresent()) {
      throw new LivroExisteException("Livro já registrado");
    }

    livroRepository.salvar(livro);
  }

  public Optional<Livro> buscarLivroPorId(UUID id) {
    return livroRepository.buscarPorId(id);
  }
}
