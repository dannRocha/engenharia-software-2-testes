package test.emprestimos.databuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import test.emprestimos.domain.model.Emprestimo;

public class EmprestimoDataBuilder {

  private Set<Emprestimo> emprestimos;

  private EmprestimoDataBuilder(Set<Emprestimo> emprestimos) {
    this.emprestimos = emprestimos;
  }

  public static EmprestimoDataBuilder aGroup() {
    return new EmprestimoDataBuilder(gerarEmprestimos());
  }

  public static Emprestimo.EmprestimoBuilder aEmprestimo() {
    return Emprestimo.builder();
  }

  public EmprestimoDataBuilder limit(int lim) {
    emprestimos = emprestimos
      .stream()
      .limit(lim)
      .collect(Collectors.toSet()); 
      
    return this;
  }

  public EmprestimoDataBuilder comQuantidadeDeLivros(int lim) {
    emprestimos = emprestimos.stream()
      .map(emprestimo -> {

        var livros = emprestimo
            .getLivrosEmprestados()
            .stream()
            .limit(lim)
            .collect(Collectors.toList());
            
        emprestimo.setLivrosEmprestados(livros);

        return emprestimo;
     })
     .collect(Collectors.toSet());

     return this;
  }

  public EmprestimoDataBuilder semDataDevolucao() {
    emprestimos = emprestimos.stream()
    .map(emprestimo -> {
      emprestimo.setDataDevolucao(null);  
      return emprestimo;
   })
   .collect(Collectors.toSet());

   return this;
  }
  
  public Set<Emprestimo> buildGroup() {
    return emprestimos;
  }

  private static Set<Emprestimo> gerarEmprestimos() {
    var emprestimos = new HashSet<Emprestimo>();
    
    var usuario = UsuarioDataBuilder
      .aUsuario()
      .id(UUID.randomUUID())
      .nome("Astrogildo")
      .build();


    for(int i = 0; i < 10; i++) {
      var livros = LivroDataBuilder
        .aGroup()
        .buildGroup();

      var id = UUID.randomUUID();
      
      var emprestimo = Emprestimo.builder()
        .id(id)
        .usuario(usuario)
        .dataEmprestimo(LocalDate.now())
        .dataPrevista(LocalDate.now().plusDays(7))
        .dataDevolucao(LocalDate.now().plusDays(7))
        .livrosEmprestados(livros.parallelStream().collect(Collectors.toList()))
        .build();

      emprestimos.add(emprestimo);
    }

    return emprestimos;
  }
}
