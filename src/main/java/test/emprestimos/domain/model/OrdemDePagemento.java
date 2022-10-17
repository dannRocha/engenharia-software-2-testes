package test.emprestimos.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrdemDePagemento {
  @EqualsAndHashCode.Include
  private UUID id;
  private BigDecimal valor;
  private BigDecimal multa;

  public BigDecimal total() {
    return valor.add(multa);
  }
}
