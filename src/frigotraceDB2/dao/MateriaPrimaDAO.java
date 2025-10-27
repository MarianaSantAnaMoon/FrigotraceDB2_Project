package frigotraceDB2.dao;

import frigotraceDB2.modelo.MateriaPrima;
import conexaomysql.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// Importa��o necess�ria para a manipula��o de datas no JDBC
import java.sql.Date; 
import java.util.Calendar;
import java.util.TimeZone;

public class MateriaPrimaDAO {


    // C - CREATE (Inserir)
    public void inserir(MateriaPrima mp) {
        
        String sql = "INSERT INTO materia_prima (sisb_sif, data_producao, data_validade, temperatura, id_fornecedor) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            stmt.setString(1, mp.getSisbSif()); 
            
            // Tratamento de Datas: Converte java.util.Date para java.sql.Date
            // Verifica se a data � nula, j� que o campo aceita NULL
            if (mp.getDataProducao() != null) {
                stmt.setDate(2, new java.sql.Date(mp.getDataProducao().getTime()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }
            if (mp.getDataValidade() != null) {
                stmt.setDate(3, new java.sql.Date(mp.getDataValidade().getTime()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            
            // Tratamento da Temperatura (FLOAT)
            if (mp.getTemperatura() != null) {
                stmt.setFloat(4, mp.getTemperatura());
            } else {
                stmt.setNull(4, java.sql.Types.FLOAT);
            }

            // Tratamento do FK (id_fornecedor)
            if (mp.getIdFornecedor() != null) {
                stmt.setInt(5, mp.getIdFornecedor());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate(); 
            
            System.out.println("CRIAR MP: Mat�ria-Prima SISB/SIF " + mp.getSisbSif() + " inserida com sucesso!");

        } catch (SQLException e) {
            System.err.println("CRIAR MP: Erro ao inserir mat�ria-prima: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }

    // R - READ (Consultar Todos)
    public List<MateriaPrima> buscarTodos() {
        
        String sql = "SELECT id_materia_prima, sisb_sif, data_producao, data_validade, temperatura, id_fornecedor FROM materia_prima";

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        List<MateriaPrima> listaMateriaPrima = new ArrayList<>(); 

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            rs = stmt.executeQuery(); 

            while (rs.next()) {
                
                Integer id = rs.getInt("id_materia_prima");
                String sisb = rs.getString("sisb_sif");
                
                // Convers�o de java.sql.Date para java.util.Date
                java.util.Date dataProd = rs.getDate("data_producao");
                java.util.Date dataVal = rs.getDate("data_validade");

                // Leitura de Float (pode ser null)
                Float temp = rs.getObject("temperatura") != null ? rs.getFloat("temperatura") : null;
                
                // Leitura do FK (id_fornecedor, pode ser null)
                Integer idForn = rs.getObject("id_fornecedor") != null ? rs.getInt("id_fornecedor") : null;
                
                // Mapeia para objeto Java
                MateriaPrima mp = new MateriaPrima(id, sisb, dataProd, dataVal, temp, idForn); 

                listaMateriaPrima.add(mp);
            }

        } catch (SQLException e) {
            System.err.println("CONSULTAR MP: Erro ao buscar mat�ria-prima: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con); 
        }
        
        return listaMateriaPrima;
    }
    
    // U - UPDATE (Atualizar)
    public void atualizar(MateriaPrima mp) {
        
        String sql = "UPDATE materia_prima SET sisb_sif = ?, data_producao = ?, data_validade = ?, temperatura = ?, id_fornecedor = ? WHERE id_materia_prima = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            // 1. Mapeia os NOVOS valores
            stmt.setString(1, mp.getSisbSif()); 
            
            // Datas
            if (mp.getDataProducao() != null) {
                stmt.setDate(2, new java.sql.Date(mp.getDataProducao().getTime()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }
            if (mp.getDataValidade() != null) {
                stmt.setDate(3, new java.sql.Date(mp.getDataValidade().getTime()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            
            // Temperatura
            if (mp.getTemperatura() != null) {
                stmt.setFloat(4, mp.getTemperatura());
            } else {
                stmt.setNull(4, java.sql.Types.FLOAT);
            }
            
            // FK
            if (mp.getIdFornecedor() != null) {
                stmt.setInt(5, mp.getIdFornecedor());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            // 2. Mapeia o ID para o WHERE
            stmt.setInt(6, mp.getIdMateriaPrima()); 
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                 System.out.println("ATUALIZAR MP: Mat�ria-Prima ID " + mp.getIdMateriaPrima() + " atualizada com sucesso!");
            } else {
                 System.out.println("ATUALIZAR MP: Mat�ria-Prima ID " + mp.getIdMateriaPrima() + " n�o encontrada para atualiza��o.");
            }

        } catch (SQLException e) {
            System.err.println("ATUALIZAR MP: Erro ao atualizar mat�ria-prima: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
    

    // D - DELETE (Excluir)
    public void excluir(int idMateriaPrima) {
        
        String sql = "DELETE FROM materia_prima WHERE id_materia_prima = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = new Conexao().getConexao(); 
            stmt = con.prepareStatement(sql); 

            stmt.setInt(1, idMateriaPrima); // O '?' ser� o ID

            int linhasAfetadas = stmt.executeUpdate(); 
            
            if (linhasAfetadas > 0) {
                System.out.println("EXCLUIR MP: Mat�ria-Prima de ID " + idMateriaPrima + " exclu�da com sucesso!");
            } else {
                System.out.println("EXCLUIR MP: Mat�ria-Prima de ID " + idMateriaPrima + " n�o encontrada para exclus�o.");
            }

        } catch (SQLException e) {
            System.err.println("EXCLUIR MP: Erro ao excluir mat�ria-prima: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Ignorar */ }
            }
            Conexao.fecharConexao(con);
        }
    }
}