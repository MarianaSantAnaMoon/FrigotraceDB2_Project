package frigotraceDB2.dao;

import frigotraceDB2.modelo.Fornecedor;
import conexaomysql.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    // C - CREATE
    public void inserir(Fornecedor fornecedor) {

        String sql = "INSERT INTO fornecedor (nome_razao_social, cnpj_cpf, id_endereco) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao();
            stmt = con.prepareStatement(sql);

            stmt.setString(1, fornecedor.getNomeRazaoSocial());
            stmt.setString(2, fornecedor.getCnpjCpf());

            Integer idEndereco = fornecedor.getIdEndereco();
            if (idEndereco == null || idEndereco == 0) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, idEndereco);
            }

            stmt.executeUpdate();

            System.out.println("CRIAR FORN: Fornecedor " + fornecedor.getNomeRazaoSocial() + " inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("CRIAR FORN: Erro ao inserir fornecedor: " + e.getMessage());
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
            Conexao.fecharConexao(con);
        }
    }

    // R - READ
    public List<Fornecedor> buscarTodos() {

        String sql = "SELECT id_fornecedor, nome_razao_social, cnpj_cpf, id_endereco FROM fornecedor";

        List<Fornecedor> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = new Conexao().getConexao();
            stmt = con.prepareStatement(sql);

            rs = stmt.executeQuery();

            while (rs.next()) {

                Integer id = rs.getInt("id_fornecedor");
                String nome = rs.getString("nome_razao_social");
                String cnpj = rs.getString("cnpj_cpf");

                // CORRETO: getObject retorna null quando o campo SQL é NULL
                Integer idEndereco = (Integer) rs.getObject("id_endereco");

                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setIdFornecedor(id);
                fornecedor.setNomeRazaoSocial(nome);
                fornecedor.setCnpjCpf(cnpj);
                fornecedor.setIdEndereco(idEndereco);

                lista.add(fornecedor);
            }

        } catch (SQLException e) {
            System.err.println("CONSULTAR FORN: Erro ao buscar fornecedores: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
            Conexao.fecharConexao(con);
        }

        return lista;
    }

    // U - UPDATE
    public void atualizar(Fornecedor fornecedor) {

        String sql = "UPDATE fornecedor SET nome_razao_social = ?, cnpj_cpf = ?, id_endereco = ? WHERE id_fornecedor = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao();
            stmt = con.prepareStatement(sql);

            stmt.setString(1, fornecedor.getNomeRazaoSocial());
            stmt.setString(2, fornecedor.getCnpjCpf());

            Integer idEndereco = fornecedor.getIdEndereco();
            if (idEndereco == null || idEndereco == 0) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, idEndereco);
            }

            stmt.setInt(4, fornecedor.getIdFornecedor());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("ATUALIZAR FORN: Fornecedor ID " + fornecedor.getIdFornecedor()
                        + " atualizado com sucesso!");
            } else {
                System.out.println("ATUALIZAR FORN: Fornecedor ID " + fornecedor.getIdFornecedor()
                        + " não encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("ATUALIZAR FORN: Erro ao atualizar fornecedor: " + e.getMessage());
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
            Conexao.fecharConexao(con);
        }
    }

    // D - DELETE
    public void excluir(int idFornecedor) {

        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao();
            stmt = con.prepareStatement(sql);

            stmt.setInt(1, idFornecedor);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("EXCLUIR FORN: Fornecedor ID " + idFornecedor + " excluído com sucesso!");
            } else {
                System.out.println("EXCLUIR FORN: Fornecedor ID " + idFornecedor + " não encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("EXCLUIR FORN: Erro ao excluir fornecedor: " + e.getMessage());
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
            Conexao.fecharConexao(con);
        }
    }
}
