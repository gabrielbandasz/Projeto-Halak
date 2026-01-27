package dao;

import model.Produto;
import util.Conexao;

import java.io.*;
import java.sql.*;
import java.util.*;

public class ProdutoDAO {

    private static final String CAMINHO_ARQUIVO = "produtos.txt";

    // ================================
    // MÉTODOS PARA ARQUIVO .TXT
    // ================================

    public List<Produto> carregarProdutos(String caminhoArquivo) {
        List<Produto> produtos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split("\t");

                if (partes.length != 11) {
                    System.err.println("Linha ignorada (esperado 11 colunas): " + linha);
                    continue;
                }

                try {
                    String referencia = partes[0].trim();
                    String nome = partes[1].trim();
                    String superficie = partes[2].trim();
                    String colecao = partes[3].trim();
                    String formato = partes[4].trim();
                    String localUso = partes[5].trim();
                    double m2Pallet = parseDouble(partes[6]);
                    double kgM2 = parseDouble(partes[7]);
                    double m2Caixa = parseDouble(partes[8]);
                    int pcsCaixa = Integer.parseInt(partes[9].trim());
                    double valorPalletizado = parseDouble(partes[10]);

                    Produto produto = new Produto(
                        referencia, nome, superficie, colecao, formato,
                        localUso, m2Pallet, kgM2, m2Caixa, pcsCaixa, valorPalletizado
                    );

                    produtos.add(produto);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter valores numéricos: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }

        return produtos;
    }

    public List<Produto> carregarProdutos() {
        return carregarProdutos(CAMINHO_ARQUIVO);
    }

    public void salvarProdutos(List<Produto> produtos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Produto p : produtos) {
                String linha = String.join("\t",
                    p.getReferencia(),
                    p.getProduto(),
                    p.getSuperficie(),
                    p.getColecao(),
                    p.getFormato(),
                    p.getLocalUso(),
                    String.valueOf(p.getM2Pallet()),
                    String.valueOf(p.getKgM2()),
                    String.valueOf(p.getM2Caixa()),
                    String.valueOf(p.getPcsCaixa()),
                    String.valueOf(p.getValorPalletizado())
                );
                bw.write(linha);
                bw.newLine();
            }
        }
    }

    private double parseDouble(String valor) {
        try {
            valor = valor.replace("R$", "").replace(" ", "").replace(",", ".");
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter valor para double: " + valor);
            return 0.0;
        }
    }

    public Produto buscarPorReferencia(String referencia) {
        List<Produto> produtos = carregarProdutos();

        for (Produto p : produtos) {
            if (p.getReferencia().equalsIgnoreCase(referencia.trim())) {
                return p;
            }
        }
        return null;
    }

    // ================================
    // MÉTODOS PARA BANCO DE DADOS
    // ================================

    public void salvarProduto(Produto produto, int idPedido) throws SQLException {
        String sql = "INSERT INTO compras (referencia, produto, superficie, colecao, formato, local_uso, m2_pallet, kg_m2, m2_caixa, pcs_caixa, valor_paletizado, id_pedido) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getReferencia());
            stmt.setString(2, produto.getProduto());
            stmt.setString(3, produto.getSuperficie());
            stmt.setString(4, produto.getColecao());
            stmt.setString(5, produto.getFormato());
            stmt.setString(6, produto.getLocalUso());
            stmt.setDouble(7, produto.getM2Pallet());
            stmt.setDouble(8, produto.getKgM2());
            stmt.setDouble(9, produto.getM2Caixa());
            stmt.setInt(10, produto.getPcsCaixa());
            stmt.setDouble(11, produto.getValorPalletizado());
            stmt.setInt(12, idPedido);

            stmt.executeUpdate();
        }
    }

    public List<Produto> carregarProdutosDoBanco(int idPedido) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM compras WHERE id_pedido = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto(
                        rs.getString("referencia"),
                        rs.getString("produto"),
                        rs.getString("superficie"),
                        rs.getString("colecao"),
                        rs.getString("formato"),
                        rs.getString("local_uso"),
                        rs.getDouble("m2_pallet"),
                        rs.getDouble("kg_m2"),
                        rs.getDouble("m2_caixa"),
                        rs.getInt("pcs_caixa"),
                        rs.getDouble("valor_paletizado")
                    );
                    produtos.add(produto);
                }
            }
        }

        return produtos;
    }

    public void excluirProduto(String referencia, int idPedido) throws SQLException {
    String sql = "DELETE FROM compras WHERE referencia = ? AND id_pedido = ?";

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, referencia);
        stmt.setInt(2, idPedido); // Garante que só exclui do pedido certo

        int linhasAfetadas = stmt.executeUpdate();

        if (linhasAfetadas == 0) {
            throw new SQLException("Nenhum produto encontrado com essa referência para o pedido informado.");
        }
    }
}

    }

