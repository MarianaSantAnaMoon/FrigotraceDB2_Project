package frigotraceDB2.dao;

import conexaomysql.Conexao;
import frigotraceDB2.modelo.Telefone;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefoneDAO {

    public void inserir(Telefone telefone) {
        String sql = "INSERT INTO telefone (numero) VALUES (?)";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, telefone.getNumero());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir telefone: " + e.getMessage());
        }
    }

    public List<Telefone> listar() {
        List<Telefone> lista = new ArrayList<>();
        String sql = "SELECT * FROM telefone";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Telefone telefone = new Telefone();
                telefone.setIdTelefone(rs.getInt("id_telefone"));
                telefone.setNumero(rs.getString("numero"));
                lista.add(telefone);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar telefones: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Telefone telefone) {
        String sql = "UPDATE telefone SET numero = ? WHERE id_telefone = ?";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, telefone.getNumero());
            stmt.setInt(2, telefone.getIdTelefone());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar telefone: " + e.getMessage());
        }
    }

    public void excluir(int idTelefone) {
        String sql = "DELETE FROM telefone WHERE id_telefone = ?";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTelefone);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir telefone: " + e.getMessage());
        }
    }
}
