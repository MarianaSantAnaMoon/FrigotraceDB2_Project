package frigotraceDB2.dao;

import conexaomysql.Conexao;
import frigotraceDB2.modelo.TransporteIn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransporteInDAO {

    public void inserir(TransporteIn t) {
        String sql = "INSERT INTO transporte_in (veiculo_placa, temperatura_transporte, data_recebida, id_materia_prima) VALUES (?, ?, ?, ?)";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getVeiculoPlaca());
            stmt.setFloat(2, t.getTemperaturaTransporte());
            stmt.setDate(3, t.getDataRecebida());
            stmt.setInt(4, t.getIdMateriaPrima());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir transporte_in: " + e.getMessage());
        }
    }

    public List<TransporteIn> listar() {
        List<TransporteIn> lista = new ArrayList<>();
        String sql = "SELECT * FROM transporte_in";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TransporteIn t = new TransporteIn();
                t.setIdTransporteIn(rs.getInt("id_transporte_in"));
                t.setVeiculoPlaca(rs.getString("veiculo_placa"));
                t.setTemperaturaTransporte(rs.getFloat("temperatura_transporte"));
                t.setDataRecebida(rs.getDate("data_recebida"));
                t.setIdMateriaPrima(rs.getInt("id_materia_prima"));
                lista.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar transporte_in: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(TransporteIn t) {
        String sql = "UPDATE transporte_in SET veiculo_placa = ?, temperatura_transporte = ?, data_recebida = ?, id_materia_prima = ? WHERE id_transporte_in = ?";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getVeiculoPlaca());
            stmt.setFloat(2, t.getTemperaturaTransporte());
            stmt.setDate(3, t.getDataRecebida());
            stmt.setInt(4, t.getIdMateriaPrima());
            stmt.setInt(5, t.getIdTransporteIn());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar transporte_in: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM transporte_in WHERE id_transporte_in = ?";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir transporte_in: " + e.getMessage());
        }
    }
}
