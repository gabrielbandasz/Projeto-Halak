package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int idPedido; // ID do pedido
    private int idCliente; // ID do cliente associado ao pedido
    private Date dataPedido; // Data do pedido
    private List<Produto> produtos; // Lista de produtos associados ao pedido
    private int quantidade; // Quantidade total de produtos
    private double valorTotal; // Valor total do pedido

    // Construtor para criar um novo pedido
    public Pedido(int idCliente, Date dataPedido) {
        this.idCliente = idCliente;
        this.dataPedido = dataPedido;
        this.produtos = new ArrayList<>();
        atualizarTotais(); // Calcula a quantidade e o valor total
    }

    // Construtor para pedidos existentes
    public Pedido(int idPedido, int idCliente, Date dataPedido, List<Produto> produtos) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.dataPedido = dataPedido;
        this.produtos = produtos != null ? produtos : new ArrayList<>();
        atualizarTotais(); // Calcula a quantidade e o valor total
    }

    public Pedido(int idPedido, int idCliente, Date dataPedido, int quantidade, double valorTotal) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.dataPedido = dataPedido;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    // Atualiza os campos quantidade e valorTotal com base na lista de produtos
    private void atualizarTotais() {
        this.quantidade = produtos.size();
        this.valorTotal = 0.0;
        for (Produto produto : produtos) {
            this.valorTotal += produto.getValorPalletizado();
        }
    }

    // Método para adicionar um produto ao pedido
    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
        atualizarTotais();
    }

    // Método para remover um produto do pedido
    public void removerProduto(Produto produto) {
        this.produtos.remove(produto);
        atualizarTotais();
    }

    // Getters e Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos != null ? produtos : new ArrayList<>();
        atualizarTotais();
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    
    

    // Métodos utilitários (mantidos para compatibilidade)
    public int getQuantidadeTotal() {
        return quantidade;
    }

    public double calcularValorTotal() {
        return valorTotal;
    }
}
