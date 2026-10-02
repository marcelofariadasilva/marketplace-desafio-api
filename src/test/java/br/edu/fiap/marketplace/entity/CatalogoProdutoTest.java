package br.edu.fiap.marketplace.entity;
import static org.junit.jupiter.api.Assertions.*; import java.math.BigDecimal; import org.junit.jupiter.api.Test;
class CatalogoProdutoTest {
 @Test void naoBaixaEstoqueAcimaDoSaldo(){CatalogoProduto p=new CatalogoProduto("Mouse","Ergonômico",new BigDecimal("100.00"),2,true);assertThrows(IllegalStateException.class,()->p.baixarEstoque(3));assertEquals(2,p.getEstoque());}
}
