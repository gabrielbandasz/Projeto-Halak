package dao;

import model.Pedido;
import model.Produto;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PedidoDAO {

    // Salvar o pedido e os produtos relacionados
    public void salvarPedido(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedidos (id_cliente, data_pedido, quantidade, valor_total) VALUES (?, ?, ?, ?)";
        String sqlProduto = "INSERT INTO compras (referencia, produto, superficie, colecao, formato, local_uso, m2_pallet, kg_m2, m2_caixa, pcs_caixa, valor_paletizado, id_cliente, id_pedido) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false); // Início da transação

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setInt(1, pedido.getIdCliente());
                stmtPedido.setDate(2, new java.sql.Date(pedido.getDataPedido().getTime()));
                stmtPedido.setInt(3, pedido.getQuantidade());
                stmtPedido.setDouble(4, pedido.getValorTotal());
                stmtPedido.executeUpdate();

                ResultSet rs = stmtPedido.getGeneratedKeys();
                if (rs.next()) {
                    int idPedidoGerado = rs.getInt(1);
                    pedido.setIdPedido(idPedidoGerado);
                }
            }

            try (PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)) {
                for (Produto p : pedido.getProdutos()) {
                    stmtProduto.setString(1, p.getReferencia());
                    stmtProduto.setString(2, p.getProduto());
                    stmtProduto.setString(3, p.getSuperficie());
                    stmtProduto.setString(4, p.getColecao());
                    stmtProduto.setString(5, p.getFormato());
                    stmtProduto.setString(6, p.getLocalUso());
                    stmtProduto.setDouble(7, p.getM2Pallet());
                    stmtProduto.setDouble(8, p.getKgM2());
                    stmtProduto.setDouble(9, p.getM2Caixa());
                    stmtProduto.setInt(10, p.getPcsCaixa());
                    stmtProduto.setDouble(11, p.getValorPalletizado());
                    stmtProduto.setInt(12, pedido.getIdCliente());
                    stmtProduto.setInt(13, pedido.getIdPedido());
                    stmtProduto.addBatch();
                }
                stmtProduto.executeBatch();
            }

            conn.commit(); // Finaliza a transação
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Buscar todos os pedidos de um cliente específico
    public List<Pedido> listarPedidosPorCliente(int idCliente) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sqlPedidos = "SELECT * FROM pedidos WHERE id_cliente = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmtPedidos = conn.prepareStatement(sqlPedidos)) {

            stmtPedidos.setInt(1, idCliente);
            ResultSet rs = stmtPedidos.executeQuery();

            while (rs.next()) {
                int idPedido = rs.getInt("id_pedido");
                Date dataPedido = rs.getDate("data_pedido");
                int quantidade = rs.getInt("quantidade");
                double valorTotal = rs.getDouble("valor_total");

                // Obtém os produtos do pedido
                List<Produto> produtos = listarProdutosDoPedido(idPedido);

                // Cria o pedido
                Pedido pedido = new Pedido(idPedido, idCliente, dataPedido, produtos);
                pedidos.add(pedido);
            }
        }

        return pedidos;
    }

    // Buscar os produtos de um pedido específico
    public List<Produto> listarProdutosDoPedido(int idPedido) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM compras WHERE id_pedido = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto(
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
                produtos.add(p);
            }
        }

        return produtos;
    }

    // Criar um pedido vazio e retornar o ID gerado (caso precise)
    public int criarPedido(int idCliente) throws SQLException {
        int idPedidoGerado = -1;
        String sql = "INSERT INTO pedidos (id_cliente, data_pedido, quantidade, valor_total) VALUES (?, ?, ?, ?)";

        Date dataAtual = new Date(System.currentTimeMillis());
        int quantidade = 0;
        double valorTotal = 0.0;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idCliente);
            stmt.setDate(2, dataAtual);
            stmt.setInt(3, quantidade);
            stmt.setDouble(4, valorTotal);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idPedidoGerado = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return idPedidoGerado;
    }public void excluirPedidoComProdutos(int idPedido) throws SQLException {
    Connection conn = Conexao.conectar();

    String sqlExcluirCompras = "DELETE FROM compras WHERE id_pedido = ?";
    String sqlExcluirPedido = "DELETE FROM pedidos WHERE id_pedido = ?";

    try (PreparedStatement stmtCompras = conn.prepareStatement(sqlExcluirCompras);
         PreparedStatement stmtPedido = conn.prepareStatement(sqlExcluirPedido)) {

        // Primeiro exclui os produtos do pedido (da tabela compras)
        stmtCompras.setInt(1, idPedido);
        stmtCompras.executeUpdate();

        // Depois exclui o pedido da tabela pedidos
        stmtPedido.setInt(1, idPedido);
        stmtPedido.executeUpdate();
    }
    }
}






