package conexaomysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; // É bom usar a exceção específica

public class Conexao {

    public Connection getConexao() {
        // 1. Inicializa a variável como null
        Connection conectar = null;

        try {

            String endereco = "jdbc:mysql://localhost:3306/FrigoTraceDB";
            String usuario = "mariana";
            String senha = "1234";
          
            // 2. Tenta carregar o driver (necessário para versões mais antigas do JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 3. Tenta estabelecer a conexão
            conectar = DriverManager.getConnection(endereco, usuario, senha);
            System.out.println("Conexão estabelecida com sucesso!"); // Adicionado para feedback
        }

        catch (ClassNotFoundException e) { // Trata erro do Driver

            System.out.println("Erro: Driver JDBC não encontrado: " + e.getMessage());
        }
          catch (SQLException e) { // Trata erro de Conexão
               System.out.println("Erro ao tentar conectar-se ao banco de dados: " + e.getMessage());

        }       
        // 4. Retorna a conexão (null se houve erro, ou o objeto Connection se funcionou)

        return conectar; 
    }
    
    // MÉTODO NOVO ADICIONADO: Essencial para o DAO fechar a conexão
    // Precisa ser 'public static' para ser chamado diretamente da classe Conexao
    public static void fecharConexao(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}