package test.emprestimos.infra.adapter.database.jpa.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


import test.emprestimos.domain.model.Livro;
import test.emprestimos.domain.model.Usuario;

// @Entity
public class EmprestimoEntityJPAAdapter {
  
  // @Id
  // @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  private Usuario usuario;
  private LocalDate dataEmprestimo;
  private LocalDate dataPrevista;
  private LocalDate dataDevolucao;
  private List<Livro> livrosEmprestados;
}
