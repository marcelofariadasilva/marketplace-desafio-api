package br.edu.fiap.marketplace.service;

import br.edu.fiap.marketplace.dto.ConfirmacaoPagamentoRequest;
import br.edu.fiap.marketplace.dto.ConfirmacaoPagamentoResponse;
import br.edu.fiap.marketplace.entity.Carrinho;
import br.edu.fiap.marketplace.entity.ConfirmacaoPagamento;
import br.edu.fiap.marketplace.entity.Usuario;
import br.edu.fiap.marketplace.exception.ConflitoNegocioException;
import br.edu.fiap.marketplace.exception.RegraNegocioException;
import br.edu.fiap.marketplace.exception.RecursoNaoEncontradoException;
import br.edu.fiap.marketplace.repository.CarrinhoRepository;
import br.edu.fiap.marketplace.repository.CatalogoProdutoRepository;
import br.edu.fiap.marketplace.repository.ConfirmacaoPagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Coordena o pagamento; a aprovação altera estoque, carrinho e pagamento juntos. */
@Service
public class ConfirmacaoPagamentoService {
    private final ConfirmacaoPagamentoRepository pagamentoRepository;
    private final CarrinhoService carrinhoService;
    private final UsuarioService usuarioService;
    private final CatalogoProdutoRepository produtoRepository;
    private final CarrinhoRepository carrinhoRepository;

    public ConfirmacaoPagamentoService(ConfirmacaoPagamentoRepository pagamentoRepository,
            CarrinhoService carrinhoService, UsuarioService usuarioService,
            CatalogoProdutoRepository produtoRepository, CarrinhoRepository carrinhoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.carrinhoService = carrinhoService;
        this.usuarioService = usuarioService;
        this.produtoRepository = produtoRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    public ConfirmacaoPagamentoResponse criar(ConfirmacaoPagamentoRequest request) {
        validarDuplicidade(request);
        Carrinho carrinho = carrinhoService.buscarEntidade(request.carrinhoId());
        Usuario usuario = usuarioService.buscarEntidade(request.usuarioId());
        if (!carrinho.getUsuario().getId().equals(usuario.getId())) {
            throw new RegraNegocioException("Usuário informado não é o dono do carrinho.");
        }
        ConfirmacaoPagamento pagamento = new ConfirmacaoPagamento(
                carrinho, usuario, request.idPagamento(), carrinho.calcularTotal());
        return ConfirmacaoPagamentoResponse.de(pagamentoRepository.save(pagamento));
    }

    public ConfirmacaoPagamentoResponse buscar(Long id) { return ConfirmacaoPagamentoResponse.de(buscarEntidade(id)); }

    /** A anotação garante rollback se uma das três persistências falhar. */
    @Transactional
    public ConfirmacaoPagamentoResponse aprovar(Long id) {
        ConfirmacaoPagamento pagamento = buscarEntidade(id);
        Carrinho carrinho = pagamento.getCarrinho();
        if (carrinho.getQuantidade() > carrinho.getProduto().getEstoque()) {
            throw new RegraNegocioException("Estoque insuficiente para concluir o pagamento.");
        }
        carrinho.getProduto().baixarEstoque(carrinho.getQuantidade());
        pagamento.aprovar();
        produtoRepository.save(carrinho.getProduto());
        carrinhoRepository.save(carrinho);
        return ConfirmacaoPagamentoResponse.de(pagamentoRepository.save(pagamento));
    }

    public ConfirmacaoPagamentoResponse recusar(Long id) { ConfirmacaoPagamento pagamento = buscarEntidade(id); pagamento.recusar(); return ConfirmacaoPagamentoResponse.de(pagamentoRepository.save(pagamento)); }
    private ConfirmacaoPagamento buscarEntidade(Long id) { return pagamentoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado.")); }
    private void validarDuplicidade(ConfirmacaoPagamentoRequest request) { if (pagamentoRepository.findByCarrinhoId(request.carrinhoId()).isPresent() || pagamentoRepository.existsByIdPagamento(request.idPagamento())) { throw new ConflitoNegocioException("Carrinho ou identificador de pagamento já possui confirmação."); } }
}
