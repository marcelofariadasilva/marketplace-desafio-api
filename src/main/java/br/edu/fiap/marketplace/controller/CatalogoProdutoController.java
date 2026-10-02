package br.edu.fiap.marketplace.controller;

import br.edu.fiap.marketplace.dto.CatalogoProdutoRequest;
import br.edu.fiap.marketplace.dto.CatalogoProdutoResponse;
import br.edu.fiap.marketplace.service.CatalogoProdutoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Expõe leitura pública e manutenção autenticada do catálogo. */
@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Catálogo")
public class CatalogoProdutoController {
    private final CatalogoProdutoService service;

    public CatalogoProdutoController(CatalogoProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CatalogoProdutoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CatalogoProdutoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<CatalogoProdutoResponse> criar(
            @Valid @RequestBody CatalogoProdutoRequest request) {
        CatalogoProdutoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/api/produtos/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public CatalogoProdutoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CatalogoProdutoRequest request) {
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/estoque")
    public CatalogoProdutoResponse reporEstoque(
            @PathVariable Long id,
            @RequestParam int quantidade) {
        return service.reporEstoque(id, quantidade);
    }
}
