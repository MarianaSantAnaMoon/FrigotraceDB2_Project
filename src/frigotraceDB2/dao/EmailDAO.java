package frigotraceDB2.dao;

import conexaomysql.Conexao;
import frigotraceDB2.modelo.Email;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmailDAO {

    public void inserir(Email email) {
        String sql = "INSERT INTO email (endereco_email) VALUES (?)";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.getEnderecoEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir email: " + e.getMessage());
        }
    }

    public List<Email> listar() {
        List<Email> lista = new ArrayList<>();
        String sql = "SELECT * FROM email";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Email email = new Email();
                email.setIdEmail(rs.getInt("id_email"));
                email.setEnderecoEmail(rs.getString("endereco_email"));
                lista.add(email);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar emails: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Email email) {
        String sql = "UPDATE email SET endereco_email = ? WHERE id_email = ?";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.getEnderecoEmail());
            stmt.setInt(2, email.getIdEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar email: " + e.getMessage());
        }
    }

    public void excluir(int idEmail) {
        String sql = "DELETE FROM email WHERE id_email = ?";

        try (Connection conn = new Conexao().getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmail);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir email: " + e.getMessage());
        }
    }
}
