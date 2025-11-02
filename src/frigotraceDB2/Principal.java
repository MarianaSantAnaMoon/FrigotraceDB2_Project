package frigotraceDB2;

import conexaomysql.Conexao;


public class Principal {

    public static void main(String[] args) {
        
        System.out.println("----------------------------------------");
        System.out.println("Iniciando o Sistema FrigoTraceDB2...");
        
        // 1. Verifica a Conexão
        System.out.println("\n--- Verificando conexão com o banco... ---");
        new Conexao().getConexao(); 

       

        System.out.println("\n----------------------------------------");
        System.out.println("Sistema Principal finalizado.");
        
        // Coloque aqui o código principal do seu sistema (ex: Menu, iniciar interface gráfica, etc.)
    }
}

