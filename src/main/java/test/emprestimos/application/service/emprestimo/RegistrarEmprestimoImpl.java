package test.emprestimos.application.service.emprestimo;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.val;
import test.emprestimos.domain.exceptions.EmprestimoInvalidoException;
import test.emprestimos.domain.model.Emprestimo;
import test.emprestimos.domain.ports.repository.EmprestimoRepositoryPort;
import test.emprestimos.domain.ports.repository.LivroRepositoryPort;
import test.emprestimos.domain.ports.service.emprestimo.RegistrarEmprestimoServicePort;

@AllArgsConstructor
public class RegistrarEmprestimoImpl implements RegistrarEmprestimoServicePort {
  protected final EmprestimoRepositoryPort emprestimoRepository;
  protected final LivroRepositoryPort livroRepository;

  private final Integer LIMITE_MAXIMO_DE_LIVROS_EMPRESTADO_POR_USUARIO = 3;

  public void registrarEmprestimo(Emprestimo emprestimo) {
    if(haLivrosEmprestados(emprestimo) || isInvalida(emprestimo) || limiteMaximoEmprestimo(emprestimo)) {
      throw new EmprestimoInvalidoException("não pode foi possivel registar emprestimo");
    }

    var livros = emprestimo.getLivrosEmprestados().parallelStream().map(livro -> {
      livro.setReservado(Boolean.FALSE);
      livro.setEmprestado(Boolean.TRUE);
      return livro;
    })
    .collect(Collectors.toSet());

    livroRepository.salvarTodos(livros);
    emprestimoRepository.salvar(emprestimo);

  }

  private boolean limiteMaximoEmprestimo(Emprestimo emprestimo) {
    var emprestimosPendentes = emprestimoRepository.buscarEmprestimosPendentesPorUsuario(emprestimo.getUsuario().getId());

    var totalDeLivrosPendentesPraDevolucao = emprestimosPendentes
      .stream()
      .map(empre -> empre.getLivrosEmprestados().size())
      .reduce(0, (acc, value) -> acc + value);

    return totalDeLivrosPendentesPraDevolucao + emprestimo.getLivrosEmprestados().size() > LIMITE_MAXIMO_DE_LIVROS_EMPRESTADO_POR_USUARIO;
        
  }

  @Override
  public Set<Emprestimo> buscarEmprestimosPorUsuario(UUID id) {
    return emprestimoRepository.buscarEmprestimosPorUsuario(id);
  }


  private boolean isInvalida(Emprestimo emprestimo) {
    return isNull(emprestimo.getDataPrevista(), emprestimo.getDataEmprestimo(), emprestimo.getUsuario()) 
      || emprestimo.getDataEmprestimo().compareTo(emprestimo.getDataPrevista()) > 0;
  }

  public Optional<Emprestimo> buscarEmprestimoPorId(UUID id) {
    return emprestimoRepository.buscarPorId(id);
  }

  private Boolean haLivrosEmprestados(Emprestimo emprestimo) {
    var IDs = emprestimo.getLivrosEmprestados()
      .parallelStream()
      .map(livro -> livro.getId())
      .collect(Collectors.toSet());

    var livros = livroRepository.buscarColecaoDeLivros(IDs);

    return livros
      .parallelStream()
      .anyMatch(livro -> Boolean.TRUE.equals(livro.getEmprestado()));
  }

  private Boolean isNull(Object ...args) {
    return Arrays.asList(args).parallelStream()
      .anyMatch(Objects::isNull);
  }
  
}
