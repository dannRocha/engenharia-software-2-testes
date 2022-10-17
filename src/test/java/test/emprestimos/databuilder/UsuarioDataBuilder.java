package test.emprestimos.databuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import test.emprestimos.domain.model.Usuario;
import test.emprestimos.domain.model.Emprestimo.EmprestimoBuilder;

public class UsuarioDataBuilder {
  private Set<Usuario> usuarios = new HashSet<>();


  private UsuarioDataBuilder(Set<Usuario> usuarios) {
    this.usuarios = usuarios;
  }

  public static UsuarioDataBuilder aGroup() {
    return new UsuarioDataBuilder(gerarUsuarios());
  }

  public Set<Usuario> buildGroup() {
    return usuarios;
  }

  private static Set<Usuario> gerarUsuarios() {
    var usuarios = new HashSet<Usuario>();
    for(int i = 0; i < 10; i++) {
      var id = UUID.randomUUID();
      var usuario = Usuario.builder()
        .id(id)
        .nome("Astrogildo :: " + id )
        .build();

      usuarios.add(usuario);
    }

    return usuarios;
  }


  public static Usuario.UsuarioBuilder aUsuario() {
    return Usuario.builder();
  }

}
