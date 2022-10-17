package test.emprestimos.domain.ports.service.emprestimo;

import java.util.UUID;

import test.emprestimos.domain.model.Emprestimo;
import test.emprestimos.domain.model.OrdemDePagemento;

public interface DevolucaoEmprestimoServicePort {
  Emprestimo finalizarEmprestimo(UUID emprestimoID);
  OrdemDePagemento gerarOrdemDePagamento(Emprestimo emprestimo);
}
