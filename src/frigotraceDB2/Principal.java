package frigotraceDB2;

import conexaomysql.Conexao;
import testes.ClienteDAOTeste;
import testes.FornecedorDAOTeste; // Novo
import testes.MateriaPrimaDAOTeste; // Novo
import testes.ProdutoDAOTeste; // Novo


public class Principal {

    public static void main(String[] args) {
        
        System.out.println("----------------------------------------");
        System.out.println("Iniciando o Sistema FrigoTraceDB2...");
        
        // 1. Verifica a Conex�o
        System.out.println("\n--- Verificando conex�o com o banco... ---");
        new Conexao().getConexao(); 

        // 2. Executa TODOS os Testes de Funcionalidade
        System.out.println("\n--- Executando testes de persist�ncia (CRUD)... ---");
        
        // ORDEM DE EXECU��O DOS TESTES:
        ClienteDAOTeste.main(args); 
        
        
        
        System.out.println("\n----------------------------------------");
        System.out.println("Sistema Principal finalizado.");
    }
}


