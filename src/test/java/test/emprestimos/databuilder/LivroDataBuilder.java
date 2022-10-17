package test.emprestimos.databuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import test.emprestimos.domain.model.Livro;

public class LivroDataBuilder {

  private Set<Livro> livros;

  public LivroDataBuilder(Set<Livro> livros) {
    this.livros = livros;
  }

  public static LivroDataBuilder aGroup() {
    return new LivroDataBuilder(gerarLivros());
  }

  public LivroDataBuilder limit(long lim) {
    livros = livros
      .stream()
      .limit(lim)
      .collect(Collectors.toSet());

    return this;
  } 

  public LivroDataBuilder reservados() {
    livros = livros.parallelStream()
     .map(livro -> {
       livro.setReservado(Boolean.TRUE);
       return livro;
     })
     .collect(Collectors.toSet());

   return this;
  }

  public LivroDataBuilder emprestados() {
     livros = livros.parallelStream()
     .map(livro -> {
       livro.setEmprestado(Boolean.TRUE);
       return livro;
     })
     .collect(Collectors.toSet());

    return this;
  }

  public Set<Livro> buildGroup() {
    return livros;
  }

  
  private static Set<Livro> gerarLivros() {
    var livros = new HashSet<Livro>();
    for(int i = 0; i < 5; i++) {
      var livro = Livro.builder()
        .id(UUID.randomUUID())
        .autor("TurtleMe")
        .titulo("The Beginning After The End. vol " + i + 1)
        .build();

      livros.add(livro);
    }

    return livros;
  }
}
