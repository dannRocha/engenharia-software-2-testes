package test.emprestimos.application.service.emprestimo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.emprestimos.databuilder.EmprestimoDataBuilder;
import test.emprestimos.databuilder.LivroDataBuilder;
import test.emprestimos.databuilder.UsuarioDataBuilder;
import test.emprestimos.domain.exceptions.EmprestimoInvalidoException;
import test.emprestimos.domain.ports.repository.EmprestimoRepositoryPort;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;
import test.emprestimos.domain.ports.service.emprestimo.RegistrarEmprestimoServicePort;
import test.emprestimos.infra.adapter.database.inmemory.EmprestimoRepositoryInMemory;
import test.emprestimos.infra.adapter.database.inmemory.LivroRepositoryInMemory;


public class RegistrarEmprestimoServiceTest {

  private RegistrarEmprestimoServicePort registroEmprestimo;
  private EmprestimoRepositoryPort emprestimoRepository;
  private LivroRepositoryPort livroRepository;

  @BeforeEach
  void setup() {
    emprestimoRepository = EmprestimoRepositoryInMemory.obterInstancia();
    livroRepository = LivroRepositoryInMemory.obterInstancia();

    registroEmprestimo = new RegistrarEmprestimoImpl(
      emprestimoRepository,
      livroRepository
    );
  }

  @AfterEach
  void restore() {
    emprestimoRepository.drop();
    livroRepository.drop();
  }

  @Test
  void deveRegistrarEBuscarEmprestimoPorId() {
    var livros = LivroDataBuilder.aGroup()
      .limit(3)
      .buildGroup();

    var usuario  = UsuarioDataBuilder.aUsuario()
      .id(UUID.randomUUID())
      .nome("Astrogildo")
      .build();

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .id(UUID.randomUUID())
      .usuario(usuario)
      .dataEmprestimo(LocalDate.now())
      .dataPrevista(LocalDate.now().plusDays(1))
      .livrosEmprestados(livros.parallelStream().collect(Collectors.toList()))
      .build();

    livroRepository.salvarTodos(livros);

    registroEmprestimo.registrarEmprestimo(emprestimo);

    var usuarioSalvo = registroEmprestimo.buscarEmprestimoPorId(emprestimo.getId());
    assertTrue(usuarioSalvo.isPresent());
  }

  @Test
  void naoDeveRegistrarEmprestimoComLivrosIndisponiveis() {
    var livros = LivroDataBuilder.aGroup()
      .reservados()
      .buildGroup();

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .id(UUID.randomUUID())
      .dataEmprestimo(LocalDate.now())
      .dataPrevista(LocalDate.now().plusDays(1))
      .livrosEmprestados(livros.parallelStream().collect(Collectors.toList()))
      .build();

     livroRepository.salvarTodos(livros);


    assertThrows(EmprestimoInvalidoException.class, () -> {
      registroEmprestimo.registrarEmprestimo(emprestimo);
    });
  }

  @Test
  void naoDeveRegistrarEmprestimoComDataPrevisaoAusente() {
    var livros = LivroDataBuilder.aGroup()
      .buildGroup();

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .id(UUID.randomUUID())
      .livrosEmprestados(livros.parallelStream().collect(Collectors.toList()))
      .build();

    livroRepository.salvarTodos(livros);

    assertThrows(EmprestimoInvalidoException.class, () -> {
      registroEmprestimo.registrarEmprestimo(emprestimo);
    });
  }

  @Test
  void naoDeveRegistrarEmprestimoComDataPrevisaoInvalida() {
    var livros = LivroDataBuilder.aGroup()
      .buildGroup();

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .id(UUID.randomUUID())
      .dataEmprestimo(LocalDate.now())
      .dataPrevista(LocalDate.now().minusDays(1))
      .livrosEmprestados(livros.parallelStream().collect(Collectors.toList()))
      .build();

    livroRepository.salvarTodos(livros);

    assertThrows(EmprestimoInvalidoException.class, () -> {
      registroEmprestimo.registrarEmprestimo(emprestimo);
    });
  }


  @Test
  void deveBuscarUmEmprestimoPorUsuario() {
    var livros = LivroDataBuilder.aGroup()
      .limit(3)
      .buildGroup();

    var usuario = UsuarioDataBuilder.aUsuario()
      .id(UUID.randomUUID())
      .nome("Astrogildo")
      .build();

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .id(UUID.randomUUID())
      .usuario(usuario)
      .dataEmprestimo(LocalDate.now())
      .dataPrevista(LocalDate.now().plusDays(1))
      .livrosEmprestados(livros.parallelStream().collect(Collectors.toList()))
      .build();

    livroRepository.salvarTodos(livros);

    registroEmprestimo.registrarEmprestimo(emprestimo);
   
    var emprestimos = registroEmprestimo.buscarEmprestimosPorUsuario(usuario.getId());
    var expected = 1;
    assertEquals(expected, emprestimos.size());
  }


  @Test
  void deveBuscarTresEmprestimosDoUsuario() {
    var livros = LivroDataBuilder.aGroup()
      .limit(3)
      .emprestados()
      .buildGroup();
    
    var expected = 3;

    var emprestimos = EmprestimoDataBuilder
      .aGroup()
      .limit(expected)
      .buildGroup();

    var usuario = emprestimos.stream().findFirst().get().getUsuario();


    livroRepository.salvarTodos(livros);

    emprestimos.forEach(emprestimoRepository::salvar);
  
 
    var emprestimosRegistrados = registroEmprestimo.buscarEmprestimosPorUsuario(usuario.getId());

    assertEquals(expected, emprestimosRegistrados.size());
  }

  @Test
  void deveRegistarOQuartoEmprestimoParaUmUsuario() {
    var livros = LivroDataBuilder.aGroup()
      .buildGroup();

    var emprestimos = EmprestimoDataBuilder
      .aGroup()
      .limit(3)
      .comQuantidadeDeLivros(1)
      .semDataDevolucao()
      .buildGroup();

    var usuario = emprestimos.stream().findFirst().get().getUsuario();

    livroRepository.salvarTodos(livros);
    emprestimos.forEach(registroEmprestimo::registrarEmprestimo);

    livros = LivroDataBuilder.aGroup()
      .buildGroup();

    livroRepository.salvarTodos(livros);

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .id(UUID.randomUUID())
      .usuario(usuario)
      .dataEmprestimo(LocalDate.now())
      .dataPrevista(LocalDate.now().plusDays(1))
      .livrosEmprestados(livros.parallelStream().limit(1).collect(Collectors.toList()))
      .build();

      
    assertThrows(EmprestimoInvalidoException.class, () -> {
      registroEmprestimo.registrarEmprestimo(emprestimo);
    });
  }

  @Test
  void naoDeveRegistrarEmprestimoComLivrosAcimaDeTres() {
    var livros = LivroDataBuilder.aGroup()
      .limit(4L)
      .buildGroup();

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .id(UUID.randomUUID())
      .livrosEmprestados(livros.parallelStream().collect(Collectors.toList()))
      .build();

    livroRepository.salvarTodos(livros);

    assertThrows(EmprestimoInvalidoException.class, () -> {
      registroEmprestimo.registrarEmprestimo(emprestimo);
    });
  }
}
