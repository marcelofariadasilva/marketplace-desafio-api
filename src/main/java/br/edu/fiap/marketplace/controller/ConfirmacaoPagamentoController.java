package br.edu.fiap.marketplace.controller;

import br.edu.fiap.marketplace.dto.ConfirmacaoPagamentoRequest;
import br.edu.fiap.marketplace.dto.ConfirmacaoPagamentoResponse;
import br.edu.fiap.marketplace.service.ConfirmacaoPagamentoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Expõe criação, consulta e decisão de confirmações de pagamento. */
@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos")
@SecurityRequirement(name = "bearerAuth")
public class ConfirmacaoPagamentoController {
    private final ConfirmacaoPagamentoService service;
    public ConfirmacaoPagamentoController(ConfirmacaoPagamentoService service) { this.service = service; }
    @PostMapping
    public ResponseEntity<ConfirmacaoPagamentoResponse> criar(@Valid @RequestBody ConfirmacaoPagamentoRequest request) {
        ConfirmacaoPagamentoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/api/pagamentos/" + response.id())).body(response);
    }
    @GetMapping("/{id}")
    public ConfirmacaoPagamentoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PatchMapping("/{id}/aprovar")
    public ConfirmacaoPagamentoResponse aprovar(@PathVariable Long id) { return service.aprovar(id); }
    @PatchMapping("/{id}/recusar")
    public ConfirmacaoPagamentoResponse recusar(@PathVariable Long id) { return service.recusar(id); }
}
