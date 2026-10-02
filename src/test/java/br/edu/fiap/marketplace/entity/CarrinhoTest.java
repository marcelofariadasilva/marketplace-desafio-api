package br.edu.fiap.marketplace.entity;
import static org.junit.jupiter.api.Assertions.*; import java.math.BigDecimal; import org.junit.jupiter.api.Test;
class CarrinhoTest {
 @Test void carrinhoFinalizadoNaoPermiteAlterarQuantidade(){Usuario u=new Usuario("Ana","ana@example.com","hash");CatalogoProduto p=new CatalogoProduto("Teclado","ABNT",new BigDecimal("200.00"),4,true);Carrinho c=new Carrinho(u,p,2);assertEquals(new BigDecimal("400.00"),c.calcularTotal());c.finalizar();assertThrows(IllegalStateException.class,()->c.alterarQuantidade(1));}
}
