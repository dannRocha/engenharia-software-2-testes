package test.emprestimos.domain.ports.service;

import java.util.Optional;
import java.util.UUID;

import test.emprestimos.domain.model.Usuario;

public interface GerenciaUsuarioServicePort {
  public void cadastrarUsuario(Usuario usuario);
  public Optional<Usuario> buscarUsuarioPorId(UUID id);
  
}
