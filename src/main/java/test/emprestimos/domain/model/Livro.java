package test.emprestimos.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Livro {
  @EqualsAndHashCode.Include
  private UUID id;
  private String autor;
  private String titulo;
  private Boolean emprestado;
  private Boolean reservado;
  private List<Emprestimo> historico;

  public Boolean emprestar() {
    if(!this.getEmprestado()) {
      setEmprestado(Boolean.TRUE);     
    }
    return getEmprestado();
  }

  public List<Emprestimo> consulatarEmprestimosPorUsuario(Usuario usuario) {
    var emprestimosPorUsuario = new ArrayList<Emprestimo>();
    for(var emprestimo : emprestimosPorUsuario) {
      if(!emprestimo.getUsuario().equals(usuario)) {
        continue;
      }
      emprestimosPorUsuario.add(emprestimo);
    }

    return emprestimosPorUsuario;
  }
}
