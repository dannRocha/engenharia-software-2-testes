package test.emprestimos.infra.adapter.database.inmemory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import test.emprestimos.domain.model.Usuario;
import test.emprestimos.domain.ports.repository.UsuarioRepositoryPort;

public class UsuarioRepositoryInMemory implements UsuarioRepositoryPort{
  private Set<Usuario> usuarios = new HashSet<>();
  private static UsuarioRepositoryPort instance;

  private UsuarioRepositoryInMemory() {}

  public static UsuarioRepositoryPort obterInstancia() {
    if(Objects.isNull(instance)) {
      instance = new UsuarioRepositoryInMemory();
    }

    return instance;
  }

  @Override
  public Optional<Usuario> buscarPorId(UUID id) {
    return usuarios.parallelStream()
      .filter(usuario -> usuario.getId().equals(id))
      .findFirst();
  }

  @Override
  public void salvar(Usuario usuario) {
    
    if(Objects.isNull(usuario.getId())) {
      usuario.setId(UUID.randomUUID());
    }

    if(usuarios.contains(usuario)) {
      usuarios.remove(usuario);
    }
    
    usuarios.add(usuario);
  }

  @Override
  public void drop() {
    usuarios.clear();    
  }
}
