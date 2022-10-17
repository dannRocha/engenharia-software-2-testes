package test.emprestimos.infra.adapter.database.inmemory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import test.emprestimos.domain.model.Emprestimo;
import test.emprestimos.domain.ports.repository.EmprestimoRepositoryPort;

public class EmprestimoRepositoryInMemory implements EmprestimoRepositoryPort{
  private Set<Emprestimo> emprestimos = new HashSet<>();
  private static EmprestimoRepositoryPort instance;

  private EmprestimoRepositoryInMemory() {}

  public static EmprestimoRepositoryPort obterInstancia() {
    if(Objects.isNull(instance)) {
      instance = new EmprestimoRepositoryInMemory();
    }

    return instance;
  }

  @Override
  public Optional<Emprestimo> buscarPorId(UUID id) {
    return emprestimos.parallelStream()
      .filter(emprestimo -> emprestimo.getId().equals(id))
      .findFirst();
  }

  @Override
  public void salvar(Emprestimo model) {
     
    if(Objects.isNull(model.getId())) {
      model.setId(UUID.randomUUID());
    }

    if(emprestimos.contains(model)) {
      emprestimos.remove(model);
    }
    
    emprestimos.add(model);
  }

  @Override
  public void drop() {
    emprestimos.clear();
  }

  @Override
  public Set<Emprestimo> buscarEmprestimosPorUsuario(UUID id) {
    return emprestimos.parallelStream().filter(emprestimo -> 
      emprestimo.getUsuario().getId().equals(id)
    )
    .collect(Collectors.toSet());
  }

  @Override
  public Optional<Emprestimo> buscarUltimoEmprestimoPorUsuario(UUID usuarioID) {
    var emprestimosSalvos = buscarEmprestimosPorUsuario(usuarioID);

    if(emprestimosSalvos.isEmpty())
      return Optional.empty();
    
    var emprestimosList = emprestimosSalvos
      .parallelStream()
      .filter(emprestimo -> Objects.isNull(emprestimo.getDataDevolucao()))
      .collect(Collectors.toList());

    if(emprestimosList.isEmpty())
      return Optional.empty();

    emprestimosList.sort((o1, o2) -> o2.getDataPrevista().compareTo(o1.getDataPrevista()));


    return Optional.ofNullable(emprestimosList.get(0));
  }

  @Override
  public Set<Emprestimo> buscarEmprestimosPendentesPorUsuario(UUID usuarioID) {
    var emprestimosSalvos = buscarEmprestimosPorUsuario(usuarioID);

    if(emprestimosSalvos.isEmpty())
      return Set.of();
    
    return emprestimosSalvos
      .parallelStream()
      .filter(emprestimo -> Objects.isNull(emprestimo.getDataDevolucao()))
      .collect(Collectors.toSet());
  }
}
