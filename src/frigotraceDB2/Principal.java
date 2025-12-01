package frigotraceDB2;

import conexaomysql.Conexao;
import java.sql.Connection;

public class Principal {

    public static void main(String[] args) {

        System.out.println("----------------------------------------");
        System.out.println("Iniciando o Sistema FrigoTraceDB2...");

        // 1. Verifica Conexão
        System.out.println("\n--- Verificando conexão com o banco... ---");
        Connection con = null;

        try {
            con = new Conexao().getConexao();

            if (con != null) {
                System.out.println("Conexão estabelecida com sucesso!");

                // 2. Abre o MENU PRINCIPAL (FrigoTraceDB / Equipe A)
                System.out.println("\n--- Carregando Menu Principal... ---");
                java.awt.EventQueue.invokeLater(() -> {
                    new MenuPrincipal().setVisible(true);
                });

            } else {
                System.err.println("Falha ao estabelecer conexão com o banco de dados.");
            }

        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO DE CONEXÃO: " + e.getMessage());

        } finally {
            if (con != null) {
              
            }
        }
    }
}
