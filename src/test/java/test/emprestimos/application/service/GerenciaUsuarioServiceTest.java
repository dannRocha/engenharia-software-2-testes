package test.emprestimos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.emprestimos.application.service.GerenciaUsuarioImpl;
import test.emprestimos.databuilder.UsuarioDataBuilder;
import test.emprestimos.domain.exceptions.UsuarioExisteException;
import test.emprestimos.domain.model.Usuario;
import test.emprestimos.domain.ports.repository.UsuarioRepositoryPort;
import test.emprestimos.domain.ports.service.GerenciaUsuarioServicePort;
import test.emprestimos.infra.adapter.database.inmemory.UsuarioRepositoryInMemory;

public class GerenciaUsuarioServiceTest {

  private GerenciaUsuarioServicePort gerenciaUsuario;
  private UsuarioRepositoryPort usuarioRepository;

  @BeforeEach
  void setup() {
    usuarioRepository = UsuarioRepositoryInMemory.obterInstancia();
    gerenciaUsuario = new GerenciaUsuarioImpl(usuarioRepository);
  }

  @AfterEach
  void restore() {
    usuarioRepository.drop();
  }

  @Test
  void deveAdicionarNovoUsuario() {
    var id = UUID.randomUUID();
    var usuario = new Usuario() {{
      setId(id);
      setNome("Astrogildo");
    }};

    gerenciaUsuario.cadastrarUsuario(usuario);
    var usuarioOptional = gerenciaUsuario.buscarUsuarioPorId(id);
    assertTrue(usuarioOptional.isPresent());

    var usuarioSalvo = usuarioOptional.get();
    assertEquals(usuario, usuarioSalvo);
  }

  @Test
  void naoDeveCadastrarNovoComMesmoID() {
    var id = UUID.randomUUID();
    var usuario = new Usuario() {{
      setId(id);
      setNome("Astrogildo");
    }};

    gerenciaUsuario.cadastrarUsuario(usuario);
    

    assertThrows(UsuarioExisteException.class, () -> {
      gerenciaUsuario.cadastrarUsuario(usuario);
    });
  }

  @Test
  void deveBuscarUsuarioSalvoPorID() {
    var usuarios = UsuarioDataBuilder.aGroup().buildGroup();

    var id = usuarios.stream().findFirst().get().getId();

    usuarios.stream().forEach(gerenciaUsuario::cadastrarUsuario);

    var usuarioSalvo = gerenciaUsuario.buscarUsuarioPorId(id);
    assertTrue(usuarioSalvo.isPresent());

  }

}
