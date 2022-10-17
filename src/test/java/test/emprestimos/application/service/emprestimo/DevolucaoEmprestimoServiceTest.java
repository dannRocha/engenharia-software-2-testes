package test.emprestimos.application.service.emprestimo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.emprestimos.databuilder.EmprestimoDataBuilder;
import test.emprestimos.databuilder.LivroDataBuilder;
import test.emprestimos.databuilder.UsuarioDataBuilder;
import test.emprestimos.domain.model.Usuario;
import test.emprestimos.domain.ports.repository.EmprestimoRepositoryPort;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;
import test.emprestimos.domain.ports.service.emprestimo.DevolucaoEmprestimoServicePort;
import test.emprestimos.domain.ports.service.emprestimo.RegistrarEmprestimoServicePort;
import test.emprestimos.infra.adapter.database.inmemory.EmprestimoRepositoryInMemory;
import test.emprestimos.infra.adapter.database.inmemory.LivroRepositoryInMemory;

public class DevolucaoEmprestimoServiceTest {
  private DevolucaoEmprestimoServicePort devolucaoEmprestimo;
  private RegistrarEmprestimoServicePort registrarEmprestimo;
  private EmprestimoRepositoryPort emprestimoRepository;
  private LivroRepositoryPort livroRepository;

  @BeforeEach
  void setup() {
    emprestimoRepository = EmprestimoRepositoryInMemory.obterInstancia();
    livroRepository = LivroRepositoryInMemory.obterInstancia();

    devolucaoEmprestimo = new DevolucaoEmprestimoImpl(
      emprestimoRepository,
      livroRepository
    );

    registrarEmprestimo = new RegistrarEmprestimoImpl(
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
  void deveGerarUmaOrdemDePagamentoSemMulta() {
    var livros = LivroDataBuilder
      .aGroup()
      .limit(3)
      .buildGroup();

    livroRepository.salvarTodos(livros);
    var usuario = UsuarioDataBuilder.aUsuario()
      .id(UUID.randomUUID())
      .nome("Astrogildo")
      .build();

    var today = LocalDate.now();

    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .usuario(usuario)
      .dataEmprestimo(LocalDate.now().minusDays(1))
      .livrosEmprestados(livros.stream().collect(Collectors.toList()))
      .dataPrevista(today)
      .dataDevolucao(today)
      .build();

    registrarEmprestimo.registrarEmprestimo(emprestimo);

    var emprestimoFinalizado = devolucaoEmprestimo.finalizarEmprestimo(emprestimo.getId());

    var ordem = devolucaoEmprestimo.gerarOrdemDePagamento(emprestimoFinalizado);

    assertTrue(ordem.getMulta().equals(new BigDecimal("0.00")));
    
  }

  @Test
  void deveGerarUmaOrdemDePagamentoComMultaMaxima() {
    var livros = LivroDataBuilder
      .aGroup()
      .limit(3)
      .buildGroup();

    livroRepository.salvarTodos(livros);
    var usuario = UsuarioDataBuilder.aUsuario()
      .id(UUID.randomUUID())
      .nome("Astrogildo")
      .build();
    
    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .usuario(usuario)
      .dataEmprestimo(LocalDate.now().minusDays(1))
      .livrosEmprestados(livros.stream().collect(Collectors.toList()))
      .dataPrevista(LocalDate.now())
      .dataDevolucao(LocalDate.now().plusDays(120))
      .build();
    
    registrarEmprestimo.registrarEmprestimo(emprestimo);

    var emprestimoFinalizado = devolucaoEmprestimo.finalizarEmprestimo(emprestimo.getId());

    var ordem = devolucaoEmprestimo.gerarOrdemDePagamento(emprestimoFinalizado);

    var expected = ordem.getValor()
      .multiply(new BigDecimal("0.6")).setScale(2, RoundingMode.HALF_EVEN);

    assertEquals(expected, ordem.getMulta());
  }

  @Test
  void deveGerarUmaOrdemDePagamentoComMultaDeUmDiaDeAtraso() {
    var livros = LivroDataBuilder
      .aGroup()
      .limit(3)
      .buildGroup();

    livroRepository.salvarTodos(livros);
    var usuario = UsuarioDataBuilder.aUsuario()
      .id(UUID.randomUUID())
      .nome("Astrogildo")
      .build();
    
    var emprestimo = EmprestimoDataBuilder
      .aEmprestimo()
      .usuario(usuario)
      .dataEmprestimo(LocalDate.now().minusDays(1))
      .livrosEmprestados(livros.stream().collect(Collectors.toList()))
      .dataPrevista(LocalDate.now())
      .dataDevolucao(LocalDate.now().plusDays(1))
      .build();

    
    registrarEmprestimo.registrarEmprestimo(emprestimo);

    var emprestimoFinalizado = devolucaoEmprestimo.finalizarEmprestimo(emprestimo.getId());

    var ordem = devolucaoEmprestimo.gerarOrdemDePagamento(emprestimoFinalizado);

    var expected = new BigDecimal(0.40D).setScale(2, RoundingMode.HALF_EVEN);

    assertEquals(expected, ordem.getMulta());
  }
}
