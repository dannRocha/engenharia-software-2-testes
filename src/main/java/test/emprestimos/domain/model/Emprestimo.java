package test.emprestimos.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Emprestimo {
  @EqualsAndHashCode.Include
  private UUID id;
  private Usuario usuario;
  private LocalDate dataEmprestimo;
  private LocalDate dataPrevista;
  private LocalDate dataDevolucao;
  private List<Livro> livrosEmprestados;
}
