package br.edu.fiap.marketplace.service;

import br.edu.fiap.marketplace.dto.AtualizarQuantidadeRequest;
import br.edu.fiap.marketplace.dto.CarrinhoRequest;
import br.edu.fiap.marketplace.dto.CarrinhoResponse;
import br.edu.fiap.marketplace.entity.Carrinho;
import br.edu.fiap.marketplace.entity.CatalogoProduto;
import br.edu.fiap.marketplace.entity.Usuario;
import br.edu.fiap.marketplace.exception.RegraNegocioException;
import br.edu.fiap.marketplace.exception.RecursoNaoEncontradoException;
import br.edu.fiap.marketplace.repository.CarrinhoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/** Coordena a criação e a evolução de carrinhos ainda abertos. */
@Service
public class CarrinhoService {

    private final CarrinhoRepository repository;
    private final UsuarioService usuarioService;
    private final CatalogoProdutoService produtoService;

    public CarrinhoService(CarrinhoRepository repository, UsuarioService usuarioService,
                           CatalogoProdutoService produtoService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
    }

    public CarrinhoResponse criar(CarrinhoRequest request) {
        Usuario usuario = usuarioService.buscarEntidade(request.usuarioId());
        CatalogoProduto produto = produtoService.buscarEntidade(request.produtoId());
        validarCompra(usuario, produto, request.quantidade());

        Carrinho carrinho = new Carrinho(usuario, produto, request.quantidade());
        return CarrinhoResponse.de(repository.save(carrinho));
    }

    public CarrinhoResponse buscar(Long id) {
        return CarrinhoResponse.de(buscarEntidade(id));
    }

    public List<CarrinhoResponse> listarPorUsuario(Long usuarioId) {
        usuarioService.buscarEntidade(usuarioId);
        return repository.findByUsuarioId(usuarioId).stream().map(CarrinhoResponse::de).toList();
    }

    public CarrinhoResponse alterarQuantidade(Long id, AtualizarQuantidadeRequest request) {
        Carrinho carrinho = buscarEntidade(id);
        validarCompra(carrinho.getUsuario(), carrinho.getProduto(), request.quantidade());
        carrinho.alterarQuantidade(request.quantidade());
        return CarrinhoResponse.de(repository.save(carrinho));
    }

    public void cancelar(Long id) {
        Carrinho carrinho = buscarEntidade(id);
        carrinho.cancelar();
        repository.save(carrinho);
    }

    public Carrinho buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho não encontrado."));
    }

    private void validarCompra(Usuario usuario, CatalogoProduto produto, int quantidade) {
        if (!usuario.isAtivo()) {
            throw new RegraNegocioException("Usuário inativo não pode criar carrinho.");
        }
        if (!produto.isAtivo()) {
            throw new RegraNegocioException("Produto inativo não pode entrar no carrinho.");
        }
        if (quantidade > produto.getEstoque()) {
            throw new RegraNegocioException("Estoque insuficiente para a quantidade solicitada.");
        }
    }
}
