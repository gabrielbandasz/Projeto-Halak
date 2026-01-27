package dao;

import model.Usuario;
import util.Conexao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {


    // CREATE - Inserir novo cliente
    public boolean inserir(Usuario cliente) {
        String sql = "INSERT INTO cliente (razao_social, nome_fantasia, cnpj, inscricao_estadual, " +
                     "endereco, bairro_cidade_estado, cep, fone, fax, contato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getRazaoSocial());
            stmt.setString(2, cliente.getNomeFantasia());
            stmt.setString(3, cliente.getCnpj());
            stmt.setString(4, cliente.getInscricaoEstadual());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getBairroCidadeEstado());
            stmt.setString(7, cliente.getCep());
            stmt.setString(8, cliente.getFone());
            stmt.setString(9, cliente.getFax());
            stmt.setString(10, cliente.getContato());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
            return false;
        }
    }

    // IMPORTAÇÃO EM MASSA DE UM CSV
    public void importarClientesDeCSV(String caminhoArquivoCSV) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivoCSV))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(","); // altere para ";" se necessário

                if (dados.length >= 10) {
                    Usuario cliente = new Usuario();
                    cliente.setRazaoSocial(dados[0].trim());
                    cliente.setNomeFantasia(dados[1].trim());
                    cliente.setCnpj(dados[2].trim());
                    cliente.setInscricaoEstadual(dados[3].trim());
                    cliente.setEndereco(dados[4].trim());
                    cliente.setBairroCidadeEstado(dados[5].trim());
                    cliente.setCep(dados[6].trim());
                    cliente.setFone(dados[7].trim());
                    cliente.setFax(dados[8].trim());
                    cliente.setContato(dados[9].trim());

                    inserir(cliente);
                }
            }

            System.out.println("Importação de clientes concluída com sucesso.");

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }
    }

    // READ - Buscar todos os clientes
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario c = new Usuario();
                c.setId(rs.getInt("id"));
                c.setRazaoSocial(rs.getString("razao_social"));
                c.setNomeFantasia(rs.getString("nome_fantasia"));
                c.setCnpj(rs.getString("cnpj"));
                c.setInscricaoEstadual(rs.getString("inscricao_estadual"));
                c.setEndereco(rs.getString("endereco"));
                c.setBairroCidadeEstado(rs.getString("bairro_cidade_estado"));
                c.setCep(rs.getString("cep"));
                c.setFone(rs.getString("fone"));
                c.setFax(rs.getString("fax"));
                c.setContato(rs.getString("contato"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        return lista;
    }

    // UPDATE - Atualizar cliente
    public boolean atualizar(Usuario cliente) {
        String sql = "UPDATE cliente SET razao_social=?, nome_fantasia=?, cnpj=?, inscricao_estadual=?, " +
                     "endereco=?, bairro_cidade_estado=?, cep=?, fone=?, fax=?, contato=? WHERE id=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getRazaoSocial());
            stmt.setString(2, cliente.getNomeFantasia());
            stmt.setString(3, cliente.getCnpj());
            stmt.setString(4, cliente.getInscricaoEstadual());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getBairroCidadeEstado());
            stmt.setString(7, cliente.getCep());
            stmt.setString(8, cliente.getFone());
            stmt.setString(9, cliente.getFax());
            stmt.setString(10, cliente.getContato());
            stmt.setInt(11, cliente.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Excluir cliente por ID
    public boolean excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
            return false;
        }
    }

    // READ - Buscar cliente por ID
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        Usuario c = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                c = new Usuario();
                c.setId(rs.getInt("id"));
                c.setRazaoSocial(rs.getString("razao_social"));
                c.setNomeFantasia(rs.getString("nome_fantasia"));
                c.setCnpj(rs.getString("cnpj"));
                c.setInscricaoEstadual(rs.getString("inscricao_estadual"));
                c.setEndereco(rs.getString("endereco"));
                c.setBairroCidadeEstado(rs.getString("bairro_cidade_estado"));
                c.setCep(rs.getString("cep"));
                c.setFone(rs.getString("fone"));
                c.setFax(rs.getString("fax"));
                c.setContato(rs.getString("contato"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }

        return c;
    }
 

public void importarClientesDeTXT(String caminhoArquivo) {
    try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
        String linha;
        Usuario cliente = null;
        int campoPreenchido = 0;

        while ((linha = br.readLine()) != null) {
            linha = linha.trim();
            if (linha.isEmpty()) continue;

            if (linha.startsWith("Razão Social")) {
                cliente = new Usuario(); // Novo cliente
                cliente.setRazaoSocial(extrairValor(linha));
                campoPreenchido = 1;
            } else if (cliente != null && linha.startsWith("Nome Fantasia")) {
                cliente.setNomeFantasia(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("CNPJ")) {
                cliente.setCnpj(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("Inscrição Estadual")) {
                cliente.setInscricaoEstadual(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("Endereço")) {
                cliente.setEndereco(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("Bairro / Cidade/ Estado")) {
                cliente.setBairroCidadeEstado(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("Cep")) {
                cliente.setCep(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("Fone")) {
                cliente.setFone(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("Fax")) {
                cliente.setFax(extrairValor(linha));
                campoPreenchido++;
            } else if (cliente != null && linha.startsWith("Contato")) {
                cliente.setContato(extrairValor(linha));
                campoPreenchido++;

                // Após preencher todos os campos, tenta inserir
                if (campoPreenchido == 10) {
                    if (!cnpjExiste(cliente.getCnpj())) {
                        inserir(cliente);
                    }
                    campoPreenchido = 0;
                    cliente = null;
                }
            }
        }

        System.out.println("Importação concluída com sucesso!");

    } catch (IOException e) {
        System.err.println("Erro ao ler arquivo TXT: " + e.getMessage());
    }
}

// Método auxiliar para extrair valores após o nome do campo
private String extrairValor(String linha) {
    int idx = linha.indexOf("\t");
    return idx >= 0 ? linha.substring(idx).trim() : linha.replaceAll(".*?:", "").trim();
}

// Verifica se o CNPJ já existe
public boolean cnpjExiste(String cnpj) {
    String sql = "SELECT COUNT(*) FROM cliente WHERE cnpj = ?";
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, cnpj);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (SQLException e) {
        System.out.println("Erro ao verificar CNPJ: " + e.getMessage());
    }
    return false;
}


}

    