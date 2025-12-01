package frigotraceDB2.dao;
 
import conexaomysql.Conexao;
import frigotraceDB2.modelo.TransporteOut;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class TransporteOutDAO {
 
    private final Conexao conexao = new Conexao();
 
    // Inserir (retorna o próprio objeto com id gerado opcionalmente)
    public TransporteOut inserir(TransporteOut t) throws SQLException {
        String sql = "INSERT INTO transporte_out (veiculo_placa, temperatura_transporte, data_saida, id_venda) VALUES (?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, t.getVeiculoPlaca());
                stmt.setFloat(2, t.getTemperaturaTransporte());
                stmt.setDate(3, t.getDataSaida());
                stmt.setInt(4, t.getIdVenda());
                stmt.executeUpdate();
 
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        t.setIdTransporteOut(rs.getInt(1));
                    }
                }
            }
            return t;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir transporte_out: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
    }
 
    // Listar todos
    public List<TransporteOut> listar() throws SQLException {
        List<TransporteOut> lista = new ArrayList<>();
        String sql = "SELECT id_transporte_out, veiculo_placa, temperatura_transporte, data_saida, id_venda FROM transporte_out";
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TransporteOut t = new TransporteOut();
                    t.setIdTransporteOut(rs.getInt("id_transporte_out"));
                    t.setVeiculoPlaca(rs.getString("veiculo_placa"));
                    t.setTemperaturaTransporte(rs.getFloat("temperatura_transporte"));
                    t.setDataSaida(rs.getDate("data_saida"));
                    t.setIdVenda(rs.getInt("id_venda"));
                    lista.add(t);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Erro ao listar transporte_out: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
    }
 
    // Atualizar
    public boolean atualizar(TransporteOut t) throws SQLException {
        String sql = "UPDATE transporte_out SET veiculo_placa = ?, temperatura_transporte = ?, data_saida = ?, id_venda = ? WHERE id_transporte_out = ?";
        int linhas = 0;
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, t.getVeiculoPlaca());
                stmt.setFloat(2, t.getTemperaturaTransporte());
                stmt.setDate(3, t.getDataSaida());
                stmt.setInt(4, t.getIdVenda());
                stmt.setInt(5, t.getIdTransporteOut());
                linhas = stmt.executeUpdate();
            }
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar transporte_out: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
    }
 
    // Excluir
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM transporte_out WHERE id_transporte_out = ?";
        int linhas = 0;
        Connection connection = null;
        try {
            connection = conexao.getConexao();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                linhas = stmt.executeUpdate();
            }
            return linhas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir transporte_out: " + e.getMessage());
            throw e;
        } finally {
            Conexao.fecharConexao(connection);
        }
    }
}