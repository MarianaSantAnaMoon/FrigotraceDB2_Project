package frigotraceDB2.dao;

import frigotraceDB2.modelo.Produto;
import conexaomysql.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date; // java.sql.Date para o JDBC
import java.math.BigDecimal; // BigDecimal para precis�o do pre�o

public class ProdutoDAO {
 
    // C - CREATE (Inserir)

    public void inserir(Produto produto) {
        
        String sql = "INSERT INTO produto (tipo_de_produto, lote, preco, peso, data_producao, data_validade) VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            stmt.setString(1, produto.getTipoDeProduto()); 
            stmt.setString(2, produto.getLote());         
            
            // Pre�o (BigDecimal)
            if (produto.getPreco() != null) {
                stmt.setBigDecimal(3, produto.getPreco());
            } else {
                stmt.setNull(3, java.sql.Types.DECIMAL);
            }
            
            // Peso (Float)
            if (produto.getPeso() != null) {
                stmt.setFloat(4, produto.getPeso());
            } else {
                stmt.setNull(4, java.sql.Types.FLOAT);
            }

            // Data Produ��o
            if (produto.getDataProducao() != null) {
                stmt.setDate(5, new java.sql.Date(produto.getDataProducao().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            
            // Data Validade
            if (produto.getDataValidade() != null) {
                stmt.setDate(6, new java.sql.Date(produto.getDataValidade().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            
            stmt.executeUpdate(); 
            
            System.out.println("CRIAR PROD: Produto Lote " + produto.getLote() + " inserido com sucesso!");

        } catch (SQLException e) {
            System.err.println("CRIAR PROD: Erro ao inserir produto: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }

    // R - READ (Consultar Todos)
    public List<Produto> buscarTodos() {
        
        String sql = "SELECT id_produto, tipo_de_produto, lote, preco, peso, data_producao, data_validade FROM produto";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        List<Produto> listaProdutos = new ArrayList<>(); 

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            rs = stmt.executeQuery(); 

            while (rs.next()) {
                
                Integer id = rs.getInt("id_produto");
                String tipo = rs.getString("tipo_de_produto");
                String lote = rs.getString("lote");
                
                // Pre�o (BigDecimal)
                BigDecimal preco = rs.getBigDecimal("preco");
                
                // Peso (Float)
                Float peso = rs.getObject("peso") != null ? rs.getFloat("peso") : null;

                // Datas (java.util.Date)
                java.util.Date dataProd = rs.getDate("data_producao");
                java.util.Date dataVal = rs.getDate("data_validade");
                
                // Mapeia para objeto Java
                Produto produto = new Produto(id, tipo, lote, preco, peso, dataProd, dataVal); 

                listaProdutos.add(produto);
            }

        } catch (SQLException e) {
            System.err.println("CONSULTAR PROD: Erro ao buscar produtos: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con); 
        }
        
        return listaProdutos;
    }
    
    // U - UPDATE (Atualizar)
    public void atualizar(Produto produto) {
        
        String sql = "UPDATE produto SET tipo_de_produto = ?, lote = ?, preco = ?, peso = ?, data_producao = ?, data_validade = ? WHERE id_produto = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            // 1. Mapeia os NOVOS valores
            stmt.setString(1, produto.getTipoDeProduto()); 
            stmt.setString(2, produto.getLote());         
            
            // Pre�o
            if (produto.getPreco() != null) {
                stmt.setBigDecimal(3, produto.getPreco());
            } else {
                stmt.setNull(3, java.sql.Types.DECIMAL);
            }
            
            // Peso
            if (produto.getPeso() != null) {
                stmt.setFloat(4, produto.getPeso());
            } else {
                stmt.setNull(4, java.sql.Types.FLOAT);
            }

            // Data Produ��o
            if (produto.getDataProducao() != null) {
                stmt.setDate(5, new java.sql.Date(produto.getDataProducao().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            
            // Data Validade
            if (produto.getDataValidade() != null) {
                stmt.setDate(6, new java.sql.Date(produto.getDataValidade().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            
            // 2. Mapeia o ID para o WHERE
            stmt.setInt(7, produto.getIdProduto()); 
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                 System.out.println("ATUALIZAR PROD: Produto ID " + produto.getIdProduto() + " atualizado com sucesso!");
            } else {
                 System.out.println("ATUALIZAR PROD: Produto ID " + produto.getIdProduto() + " n�o encontrado para atualiza��o.");
            }

        } catch (SQLException e) {
            System.err.println("ATUALIZAR PROD: Erro ao atualizar produto: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
    

    // D - DELETE (Excluir)
    public void excluir(int idProduto) {
        
        String sql = "DELETE FROM produto WHERE id_produto = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            stmt.setInt(1, idProduto); // O '?' ser� o ID

            int linhasAfetadas = stmt.executeUpdate(); 
            
            if (linhasAfetadas > 0) {
                System.out.println("EXCLUIR PROD: Produto de ID " + idProduto + " exclu�do com sucesso!");
            } else {
                System.out.println("EXCLUIR PROD: Produto de ID " + idProduto + " n�o encontrado para exclus�o.");
            }

        } catch (SQLException e) {
            System.err.println("EXCLUIR PROD: Erro ao excluir produto: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
}