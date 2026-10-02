package br.edu.fiap.marketplace.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import br.edu.fiap.marketplace.dto.CarrinhoResponse;
import br.edu.fiap.marketplace.entity.StatusCarrinho;
import br.edu.fiap.marketplace.service.CarrinhoService;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CarrinhoController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarrinhoControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @MockitoBean CarrinhoService service;
 @Test void criaCarrinhoValido() throws Exception {when(service.criar(any())).thenReturn(new CarrinhoResponse(10L,1L,"Ana",2L,"Mouse",new BigDecimal("50.00"),2,new BigDecimal("100.00"),StatusCarrinho.ABERTO,Instant.now()));mvc.perform(post("/api/carrinhos").contentType(MediaType.APPLICATION_JSON).content("{\"usuarioId\":1,\"produtoId\":2,\"quantidade\":2}" )).andExpect(status().isCreated()).andExpect(header().string("Location","/api/carrinhos/10")).andExpect(jsonPath("$.total").value(100.00));}
 @Test void rejeitaQuantidadeInvalida() throws Exception {mvc.perform(post("/api/carrinhos").contentType(MediaType.APPLICATION_JSON).content("{\"usuarioId\":1,\"produtoId\":2,\"quantidade\":0}")).andExpect(status().isBadRequest());}
}
