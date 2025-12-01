package frigotraceDB2.dao;

import frigotraceDB2.modelo.Cliente; // Importa o modelo
import conexaomysql.Conexao; // Importa sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types; // IMPORTAÇÃO NECESSÁRIA

public class ClienteDAO {

    // C - CREATE (Inserir)
    public void inserir(Cliente cliente) {
        
        // O SQL deve incluir id_endereco, mas trataremos o valor como NULL se for 0 ou null.
        String sql = "INSERT INTO cliente (nome_razao_social, cnpj_cpf, id_endereco) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            stmt.setString(1, cliente.getNomeRazaoSocial()); 
            stmt.setString(2, cliente.getCnpjCpf()); 
            
            // =========================================================
            // CORREÇÃO: Tratamento para NULL/0 no id_endereco
            // =========================================================
            Integer idEndereco = cliente.getIdEndereco(); 
            
            // A prioridade é checar se é NULL para evitar a NullPointerException!
            if (idEndereco == null || idEndereco == 0) {
                // Se for null ou 0, usa setNull para garantir o NULL no banco
                stmt.setNull(3, Types.INTEGER); // Usa Types.INTEGER
            } else {
                // Caso contrário, insere o ID fornecido
                stmt.setInt(3, idEndereco); 
            }
            // =========================================================

            stmt.executeUpdate(); 
            
            System.out.println("CRIAR: Cliente " + cliente.getNomeRazaoSocial() + " inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("CRIAR: Erro ao inserir cliente: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }

    // R - READ (Consultar Todos)
    /*Busca todos os clientes cadastrados.
     * @return Uma lista de objetos Cliente.*/
    public List<Cliente> buscarTodos() {
        
        String sql = "SELECT id_cliente, nome_razao_social, cnpj_cpf, id_endereco FROM cliente";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        List<Cliente> listaClientes = new ArrayList<>(); 

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            rs = stmt.executeQuery(); 

            while (rs.next()) {
                // 1. Pega os dados do banco
                int id = rs.getInt("id_cliente");
                String nome = rs.getString("nome_razao_social");
                String cnpj = rs.getString("cnpj_cpf");
                
                // =========================================================
                // CORREÇÃO: Usar getObject para tratar o NULL do banco
                // =========================================================
                // rs.getInt() retorna 0 para NULL. getObject() retorna NULL, o que é o desejado.
                Integer idEnd = (Integer) rs.getObject("id_endereco");
                // =========================================================
                
                // 2. Mapeia para objeto Java
                Cliente cliente = new Cliente(nome, cnpj, idEnd); 
                cliente.setIdCliente(id); 

                // 3. Adiciona à lista
                listaClientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("CONSULTAR: Erro ao buscar clientes: " + e.getMessage());
        } finally {
            // Fechamento na ordem inversa: ResultSet, PreparedStatement, Connection
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con); 
        }
        
        return listaClientes;
    }
    
    // U - UPDATE (Atualizar)
    public void atualizar(Cliente cliente) {
        
        String sql = "UPDATE cliente SET nome_razao_social = ?, cnpj_cpf = ?, id_endereco = ? WHERE id_cliente = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            // 1. Mapeia os NOVOS valores
            stmt.setString(1, cliente.getNomeRazaoSocial()); 
            stmt.setString(2, cliente.getCnpjCpf()); 
            
            // =========================================================
            // CORREÇÃO: Tratamento para NULL/0 no id_endereco
            // =========================================================
            Integer idEndereco = cliente.getIdEndereco(); 
            
            // A prioridade é checar se é NULL para evitar a NullPointerException!
            if (idEndereco == null || idEndereco == 0) {
                // Se for null ou 0, usa setNull para garantir o NULL no banco
                stmt.setNull(3, Types.INTEGER); 
            } else {
                // Caso contrário, insere o ID fornecido
                stmt.setInt(3, idEndereco); 
            }
            // =========================================================
            
            // 2. Mapeia o ID que será usado no WHERE
            stmt.setInt(4, cliente.getIdCliente()); // 4º '?' é o id_cliente
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                 System.out.println("ATUALIZAR: Cliente ID " + cliente.getIdCliente() + " atualizado para " + cliente.getNomeRazaoSocial() + " com sucesso!");
            } else {
                 System.out.println("ATUALIZAR: Cliente ID " + cliente.getIdCliente() + " não encontrado para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("ATUALIZAR: Erro ao atualizar cliente: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
    
    // D - DELETE (Excluir)
    /* Exclui um cliente pelo seu ID.
     * @param idCliente O ID do cliente a ser excluído.*/
    public void excluir(int idCliente) {
        
        // SQL: Usamos o WHERE para garantir que excluímos apenas o registro correto
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            // Mapeia o ID que será usado no WHERE do SQL
            stmt.setInt(1, idCliente); // O '?' será o ID

            int linhasAfetadas = stmt.executeUpdate(); 
            
            if (linhasAfetadas > 0) {
                System.out.println("EXCLUIR: Cliente de ID " + idCliente + " excluído com sucesso!");
            } else {
                System.out.println("EXCLUIR: Cliente de ID " + idCliente + " não encontrado para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("EXCLUIR: Erro ao excluir cliente: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
}