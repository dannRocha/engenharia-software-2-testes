package test.emprestimos.application.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import test.emprestimos.domain.exceptions.UsuarioExisteException;
import test.emprestimos.domain.model.Emprestimo;
import test.emprestimos.domain.model.Usuario;
import test.emprestimos.domain.ports.repository.UsuarioRepositoryPort;
import test.emprestimos.domain.ports.service.GerenciaUsuarioServicePort;


// @Service
@AllArgsConstructor
public class GerenciaUsuarioImpl implements GerenciaUsuarioServicePort {
  protected final UsuarioRepositoryPort usuarioRepository;

  public void cadastrarUsuario(Usuario usuario) {
    if(usuarioRepository.buscarPorId(usuario.getId()).isPresent()) {
      throw new UsuarioExisteException("Usuário já existe no sistema");
    }

    usuarioRepository.salvar(usuario);  
  }

  public Optional<Usuario> buscarUsuarioPorId(UUID id) {
    return usuarioRepository.buscarPorId(id);
  }

}
