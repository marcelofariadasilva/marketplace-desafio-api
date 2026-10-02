package br.edu.fiap.marketplace.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import br.edu.fiap.marketplace.dto.AtualizarQuantidadeRequest;
import br.edu.fiap.marketplace.dto.CarrinhoRequest;
import br.edu.fiap.marketplace.dto.CarrinhoResponse;
import br.edu.fiap.marketplace.service.CarrinhoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** Expõe as operações autenticadas de criação e manutenção de carrinhos. */
@RestController
@RequestMapping("/api/carrinhos")
@Tag(name = "Carrinhos")
@SecurityRequirement(name = "bearerAuth")
public class CarrinhoController {
    private final CarrinhoService service;
    public CarrinhoController(CarrinhoService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<CarrinhoResponse> criar(@Valid @RequestBody CarrinhoRequest request) {
        CarrinhoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/api/carrinhos/" + response.id())).body(response);
    }
    @GetMapping("/{id}")
    public CarrinhoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @GetMapping("/usuario/{usuarioId}")
    public List<CarrinhoResponse> listar(@PathVariable Long usuarioId) { return service.listarPorUsuario(usuarioId); }
    @PatchMapping("/{id}/quantidade")
    public CarrinhoResponse alterar(@PathVariable Long id, @Valid @RequestBody AtualizarQuantidadeRequest request) {
        return service.alterarQuantidade(id, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) { service.cancelar(id); return ResponseEntity.noContent().build(); }
}
