package br.edu.fiap.marketplace.service;

import br.edu.fiap.marketplace.dto.CatalogoProdutoRequest;
import br.edu.fiap.marketplace.dto.CatalogoProdutoResponse;
import br.edu.fiap.marketplace.entity.CatalogoProduto;
import br.edu.fiap.marketplace.exception.RecursoNaoEncontradoException;
import br.edu.fiap.marketplace.repository.CatalogoProdutoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/** Implementa os casos de uso administrativos e públicos do catálogo. */
@Service
public class CatalogoProdutoService {

    private final CatalogoProdutoRepository repository;

    public CatalogoProdutoService(CatalogoProdutoRepository repository) {
        this.repository = repository;
    }

    public List<CatalogoProdutoResponse> listar() {
        return repository.findAll().stream().map(CatalogoProdutoResponse::de).toList();
    }

    public CatalogoProdutoResponse buscar(Long id) {
        return CatalogoProdutoResponse.de(buscarEntidade(id));
    }

    public CatalogoProdutoResponse criar(CatalogoProdutoRequest request) {
        return CatalogoProdutoResponse.de(repository.save(novoProduto(request)));
    }

    public CatalogoProdutoResponse atualizar(Long id, CatalogoProdutoRequest request) {
        CatalogoProduto produto = buscarEntidade(id);
        produto.atualizarDados(
                request.nome(), request.descricao(), request.preco(),
                request.estoque(), request.ativo());
        return CatalogoProdutoResponse.de(repository.save(produto));
    }

    /** O contrato deste endpoint representa exclusivamente reposição positiva. */
    public CatalogoProdutoResponse reporEstoque(Long id, int quantidade) {
        CatalogoProduto produto = buscarEntidade(id);
        produto.reporEstoque(quantidade);
        return CatalogoProdutoResponse.de(repository.save(produto));
    }

    public CatalogoProduto buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado."));
    }

    private CatalogoProduto novoProduto(CatalogoProdutoRequest request) {
        return new CatalogoProduto(
                request.nome(), request.descricao(), request.preco(),
                request.estoque(), request.ativo());
    }
}
