package frigotraceDB2.dao;

import conexaomysql.Conexao; 
import frigotraceDB2.modelo.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {
    
    private final Conexao conexao = new Conexao(); 

    private Venda extrairVenda(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setIdVenda(rs.getInt("id_venda"));
        venda.setIdProduto(rs.getInt("id_produto"));
        venda.setDataVenda(rs.getDate("data_venda"));
        venda.setValorTotal(rs.getBigDecimal("valor_total"));
        venda.setDataEntrega(rs.getDate("data_entrega"));
        venda.setIdCliente(rs.getInt("id_cliente"));
        return venda;
    }

    // CRUD - C (Create/Criar)
    public Venda salvar(Venda venda) throws SQLException {
        String sql = "INSERT INTO venda (id_produto, data_venda, valor_total, data_entrega, id_cliente) VALUES (?, ?, ?, ?, ?)";
        
        Connection connection = null;
        try {
            connection = conexao.getConexao(); 
             
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                ps.setInt(1, venda.getIdProduto());
                ps.setDate(2, venda.getDataVenda());
                ps.setBigDecimal(3, venda.getValorTotal());
                ps.setDate(4, venda.getDataEntrega());
                ps.setInt(5, venda.getIdCliente());
                
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        venda.setIdVenda(rs.getInt(1));
                    }
                }
            }
            return venda;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar Venda: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection); 
        }
    }

    // CRUD - R (Read/Ler - por ID)
    public Venda buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_venda, id_produto, data_venda, valor_total, data_entrega, id_cliente FROM venda WHERE id_venda = ?";
        Venda venda = null;
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        venda = extrairVenda(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Venda por ID: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
        return venda;
    }

    // CRUD - R (Read/Ler - todos)
    public List<Venda> listarTodos() throws SQLException {
        String sql = "SELECT id_venda, id_produto, data_venda, valor_total, data_entrega, id_cliente FROM venda";
        List<Venda> vendas = new ArrayList<>();
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    vendas.add(extrairVenda(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as Vendas: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
        return vendas;
    }

    // CRUD - U (Update/Atualizar)
    public boolean atualizar(Venda venda) throws SQLException {
        String sql = "UPDATE venda SET id_produto = ?, data_venda = ?, valor_total = ?, data_entrega = ?, id_cliente = ? WHERE id_venda = ?";
        int linhasAfetadas = 0;
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                
                ps.setInt(1, venda.getIdProduto());
                ps.setDate(2, venda.getDataVenda());
                ps.setBigDecimal(3, venda.getValorTotal());
                ps.setDate(4, venda.getDataEntrega());
                ps.setInt(5, venda.getIdCliente());
                ps.setInt(6, venda.getIdVenda());
                
                linhasAfetadas = ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Venda: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
        return linhasAfetadas > 0;
    }

    // CRUD - D (Delete/Deletar)
    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM venda WHERE id_venda = ?";
        int linhasAfetadas = 0;
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                
                ps.setInt(1, id);
                linhasAfetadas = ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar Venda: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
        return linhasAfetadas > 0;
    }
}