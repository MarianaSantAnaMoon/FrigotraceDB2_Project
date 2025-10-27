package frigotraceDB2.dao;

import frigotraceDB2.modelo.Fornecedor;
import conexaomysql.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    // C - CREATE (Inserir)

    public void inserir(Fornecedor fornecedor) {
        
        String sql = "INSERT INTO fornecedor (nome_razao_social, cnpj_cpf, id_endereco) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            stmt.setString(1, fornecedor.getNomeRazaoSocial()); 
            stmt.setString(2, fornecedor.getCnpjCpf());         
            
            // O idEndereco é um Integer, então usamos setInt, mas se for null, o setNull() é melhor.
            // Para simplificar, faremos a verificação:
            if (fornecedor.getIdEndereco() != null) {
                stmt.setInt(3, fornecedor.getIdEndereco());
            } else {
                // Se for nulo, dizemos explicitamente ao JDBC
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate(); 
            
            System.out.println("CRIAR FORN: Fornecedor " + fornecedor.getNomeRazaoSocial() + " inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("CRIAR FORN: Erro ao inserir fornecedor: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }


    // R - READ (Consultar Todos)
    public List<Fornecedor> buscarTodos() {
        
        String sql = "SELECT id_fornecedor, nome_razao_social, cnpj_cpf, id_endereco FROM fornecedor";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        List<Fornecedor> listaFornecedores = new ArrayList<>(); 

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            rs = stmt.executeQuery(); 

            while (rs.next()) {
                
                Integer id = rs.getInt("id_fornecedor");
                String nome = rs.getString("nome_razao_social");
                String cnpj = rs.getString("cnpj_cpf");
                
                // Trata o campo id_endereco que pode ser NULL no banco
                Integer idEnd = rs.getObject("id_endereco") != null ? rs.getInt("id_endereco") : null;
                
                // Mapeia para objeto Java
                Fornecedor fornecedor = new Fornecedor(id, nome, cnpj, idEnd); 

                listaFornecedores.add(fornecedor);
            }

        } catch (SQLException e) {
            System.err.println("CONSULTAR FORN: Erro ao buscar fornecedores: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con); 
        }
        
        return listaFornecedores;
    }
    
    // U - UPDATE (Atualizar)
    public void atualizar(Fornecedor fornecedor) {
        
        String sql = "UPDATE fornecedor SET nome_razao_social = ?, cnpj_cpf = ?, id_endereco = ? WHERE id_fornecedor = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            // 1. Mapeia os NOVOS valores
            stmt.setString(1, fornecedor.getNomeRazaoSocial()); 
            stmt.setString(2, fornecedor.getCnpjCpf());         
            
            // Tratamento para o campo que aceita NULL
            if (fornecedor.getIdEndereco() != null) {
                stmt.setInt(3, fornecedor.getIdEndereco());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            // 2. Mapeia o ID para o WHERE
            stmt.setInt(4, fornecedor.getIdFornecedor()); 
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                 System.out.println("ATUALIZAR FORN: Fornecedor ID " + fornecedor.getIdFornecedor() + " atualizado para " + fornecedor.getNomeRazaoSocial() + " com sucesso!");
            } else {
                 System.out.println("ATUALIZAR FORN: Fornecedor ID " + fornecedor.getIdFornecedor() + " não encontrado para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("ATUALIZAR FORN: Erro ao atualizar fornecedor: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
    
    // D - DELETE (Excluir)
    public void excluir(int idFornecedor) {
        
        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            stmt.setInt(1, idFornecedor); // O '?' será o ID

            int linhasAfetadas = stmt.executeUpdate(); 
            
            if (linhasAfetadas > 0) {
                System.out.println("EXCLUIR FORN: Fornecedor de ID " + idFornecedor + " excluído com sucesso!");
            } else {
                System.out.println("EXCLUIR FORN: Fornecedor de ID " + idFornecedor + " não encontrado para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("EXCLUIR FORN: Erro ao excluir fornecedor: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
}