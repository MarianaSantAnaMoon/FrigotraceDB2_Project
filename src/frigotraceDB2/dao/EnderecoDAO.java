package frigotraceDB2.dao;

import frigotraceDB2.modelo.Endereco;
import conexaomysql.Conexao; // Sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importante para pegar o ID gerado
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    // C - CREATE (Inserir Endereço)
    // Retorna o ID gerado no banco e atualiza o objeto Endereco
    public void inserir(Endereco endereco) {
        
        String sql = "INSERT INTO endereco (rua, bairro, cidade, estado, cep) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 

        try {
            con = new Conexao().getConexao(); 
            // Informa ao JDBC que queremos as chaves geradas (o id_endereco)
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 

            stmt.setString(1, endereco.getRua());
            stmt.setString(2, endereco.getBairro());
            stmt.setString(3, endereco.getCidade());
            stmt.setString(4, endereco.getEstado());
            stmt.setString(5, endereco.getCep());
            
            stmt.executeUpdate(); 
            
            // Pega o ID gerado
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // Seta o ID gerado no objeto Java
                endereco.setIdEndereco(rs.getInt(1)); 
                System.out.println("CRIAR END: Endereço inserido com sucesso! ID: " + endereco.getIdEndereco());
            }

        } catch (SQLException e) {
            System.err.println("CRIAR END: Erro ao inserir endereço: " + e.getMessage());
        } finally {
            // Fechamento na ordem: ResultSet, PreparedStatement, Connection
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* Ignorar */ }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            Conexao.fecharConexao(con);
        }
    }


    // R - READ (Consultar Todos)
    public List<Endereco> buscarTodos() {
        
        String sql = "SELECT id_endereco, rua, bairro, cidade, estado, cep FROM endereco";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        List<Endereco> listaEnderecos = new ArrayList<>(); 

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            rs = stmt.executeQuery(); 

            while (rs.next()) {
                
                Integer id = rs.getInt("id_endereco");
                String rua = rs.getString("rua");
                String bairro = rs.getString("bairro");
                String cidade = rs.getString("cidade");
                String estado = rs.getString("estado");
                String cep = rs.getString("cep");
                
                // Mapeia para objeto Java usando o construtor completo
                Endereco endereco = new Endereco(id, rua, bairro, cidade, estado, cep); 

                listaEnderecos.add(endereco);
            }

        } catch (SQLException e) {
            System.err.println("CONSULTAR END: Erro ao buscar endereços: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { /* Ignorar */ }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            Conexao.fecharConexao(con); 
        }
        
        return listaEnderecos;
    }
    
    // Você também pode adicionar os métodos U - UPDATE e D - DELETE aqui, se precisar.
}