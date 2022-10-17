package test.emprestimos.application.service.emprestimo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import test.emprestimos.domain.exceptions.EmprestimoNotFoundException;
import test.emprestimos.domain.model.Emprestimo;
import test.emprestimos.domain.model.OrdemDePagemento;
import test.emprestimos.domain.ports.repository.EmprestimoRepositoryPort;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;
import test.emprestimos.domain.ports.service.emprestimo.DevolucaoEmprestimoServicePort;

public class DevolucaoEmprestimoImpl implements DevolucaoEmprestimoServicePort {

  protected final EmprestimoRepositoryPort emprestimoRepository;
  protected final LivroRepositoryPort livroRepository;

  public DevolucaoEmprestimoImpl(EmprestimoRepositoryPort emprestimoRepository, LivroRepositoryPort livroRepository) {
    this.emprestimoRepository = emprestimoRepository;
    this.livroRepository = livroRepository;
  }

  private final BigDecimal TARIFA_POR_ATRASO = new BigDecimal(0.40D);
  private final BigDecimal VALOR_POR_LIVRO_FIXO = new BigDecimal(5.00D);
  private final BigDecimal PORCENTAGEM_MAXIMA_TARIFA = new BigDecimal(0.6D);

  @Override
  public Emprestimo finalizarEmprestimo(UUID emprestimoID) {

    var emprestimoSalvo = emprestimoRepository.buscarPorId(emprestimoID);

    if(emprestimoSalvo.isEmpty()) {
      throw new EmprestimoNotFoundException("emprestimo não encontrado");
    }

    var emprestimo = emprestimoSalvo.get();

    if(Objects.isNull(emprestimo.getDataDevolucao()))
      emprestimo.setDataDevolucao(LocalDate.now());

    var livros = emprestimo.getLivrosEmprestados().parallelStream().map(livro -> {
      livro.setReservado(Boolean.FALSE);
      livro.setEmprestado(Boolean.FALSE);
      return livro;
    })
    .collect(Collectors.toSet());

    livroRepository.salvarTodos(livros);
    emprestimoRepository.salvar(emprestimo);

    return emprestimo;
  }

  @Override
  public OrdemDePagemento gerarOrdemDePagamento(Emprestimo emprestimo) {
    var dataPrevista = emprestimo.getDataPrevista();
    var dataDevolucao = emprestimo.getDataDevolucao();

    var atrasoEmDias = dataPrevista.until(dataDevolucao, ChronoUnit.DAYS);
    var tarifaEmprestimo = VALOR_POR_LIVRO_FIXO.multiply(new BigDecimal(emprestimo.getLivrosEmprestados().size()));

    var tarifaAtraso = TARIFA_POR_ATRASO
      .multiply(new BigDecimal(atrasoEmDias).setScale(2, RoundingMode.HALF_EVEN))
      .min(tarifaEmprestimo.multiply(PORCENTAGEM_MAXIMA_TARIFA)).setScale(2, RoundingMode.HALF_EVEN);
    
    return new OrdemDePagemento() {{
      setId(UUID.randomUUID());
      setValor(tarifaEmprestimo);
      setMulta(tarifaAtraso);
    }};
  }
}
