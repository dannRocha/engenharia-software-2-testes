package test.emprestimos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.emprestimos.application.service.GerenciarLivrosImpl;
import test.emprestimos.domain.exceptions.LivroExisteException;
import test.emprestimos.domain.model.Livro;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;
import test.emprestimos.domain.ports.service.GerenciarLivrosServicePort;
import test.emprestimos.infra.adapter.database.inmemory.LivroRepositoryInMemory;

public class GerenciarLivrosServiceTest {
  private GerenciarLivrosServicePort gerenciarLivrosService;
  private LivroRepositoryPort livroRepository;

  @BeforeEach
  void setup() {
    livroRepository = LivroRepositoryInMemory.obterInstancia();
    gerenciarLivrosService = new GerenciarLivrosImpl(livroRepository);
  }

  @AfterEach
  void restore() {
    livroRepository.drop();
  }

  @Test
  void deveRegistrarNovoLivro() {
    var livro = Livro.builder()
      .id(UUID.randomUUID())
      .autor("Astrogildo")
      .titulo("Master Sword - Bla bla")
      .build();

    gerenciarLivrosService.cadastrarLivro(livro);

    var livroSalvoOptional = gerenciarLivrosService.buscarLivroPorId(livro.getId());

    assertTrue(livroSalvoOptional.isPresent());

    var livroSalvo = livroSalvoOptional.get();
    assertEquals(livro, livroSalvo);
  }

  @Test
  void naoDeveRegistrarLivroDuplicado() {
    var livro = Livro.builder()
      .id(UUID.randomUUID())
      .autor("TurtleMe")
      .titulo("The Beginning After The End")
      .build();

    gerenciarLivrosService.cadastrarLivro(livro);

    assertThrows(LivroExisteException.class, () -> {
      gerenciarLivrosService.cadastrarLivro(livro);
    });
  }
}
