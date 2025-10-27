package conexaomysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; // � bom usar a exce��o espec�fica

public class Conexao {

    public Connection getConexao() {
        // 1. Inicializa a vari�vel como null
        Connection conectar = null;

        try {

            String endereco = "jdbc:mysql://localhost:3306/FrigoTraceDB";
            String usuario = "mariana";
            String senha = "1234";
          
            // 2. Tenta carregar o driver (necess�rio para vers�es mais antigas do JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // 3. Tenta estabelecer a conex�o
            conectar = DriverManager.getConnection(endereco, usuario, senha);
            System.out.println("Conex�o estabelecida com sucesso!"); // Adicionado para feedback
        }

        catch (ClassNotFoundException e) { // Trata erro do Driver

            System.out.println("Erro: Driver JDBC n�o encontrado: " + e.getMessage());
        }
          catch (SQLException e) { // Trata erro de Conex�o
               System.out.println("Erro ao tentar conectar-se ao banco de dados: " + e.getMessage());

        }       
        // 4. Retorna a conex�o (null se houve erro, ou o objeto Connection se funcionou)

        return conectar; 
    }
    
    // M�TODO NOVO ADICIONADO: Essencial para o DAO fechar a conex�o
    // Precisa ser 'public static' para ser chamado diretamente da classe Conexao
    public static void fecharConexao(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conex�o: " + e.getMessage());
            }
        }
    }
}