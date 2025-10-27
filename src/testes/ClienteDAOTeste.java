package testes; // CORRIGIDO: Agora aponta para o seu novo pacote 'testes'

import frigotraceDB2.modelo.Cliente;
import frigotraceDB2.dao.ClienteDAO;
import java.util.List;
import java.util.stream.Collectors; // Opcional, mas pode ser �til no futuro.

/**
 * Classe respons�vel por executar e validar todas as opera��es do CRUD do ClienteDAO.
 */
public class ClienteDAOTeste {

    private ClienteDAO dao = new ClienteDAO();
    private int idEnderecoExistente = 1; // Ajuste para um ID v�lido em 'endereco'

    public static void main(String[] args) {
        ClienteDAOTeste teste = new ClienteDAOTeste();
        
        // Sequ�ncia de testes:
        teste.testarInsercao();
        teste.testarConsulta();
        teste.testarAtualizacao();
        teste.testarExclusao();
        
        System.out.println("\n--- TODOS OS TESTES CRUD DO CLIENTE DAO FORAM EXECUTADOS ---");
    }
    
    // M�todo auxiliar para imprimir clientes
    private void imprimirClientes(String titulo, List<Cliente> clientes) {
        System.out.println("\n" + titulo);
        if (clientes.isEmpty()) {
            System.out.println("  (Lista vazia)");
        } else {
            for (Cliente c : clientes) {
                System.out.printf("  [ID: %d] Nome: %s | CNPJ: %s\n", 
                    c.getIdCliente(), 
                    c.getNomeRazaoSocial(), 
                    c.getCnpjCpf());
            }
        }
    }

  
    // C - Teste de Inser��o (Create)
    public void testarInsercao() {
        System.out.println("\n--- Teste C: INSER��O ---");
        
        // Tentaremos deletar clientes anteriores para garantir que os IDs n�o conflitem
        // NOTA: Em um teste profissional, voc� faria um TRUNCATE TABLE aqui.
        
        Cliente c1 = new Cliente("CLIENTE TESTE UPDATE", "11.111.111/0001-11", idEnderecoExistente);
        Cliente c2 = new Cliente("CLIENTE TESTE DELETE", "22.222.222/0001-22", idEnderecoExistente);
        
        dao.inserir(c1);
        dao.inserir(c2);
    }
    
    // R - Teste de Consulta (Read)
    public void testarConsulta() {
        List<Cliente> clientes = dao.buscarTodos();
        imprimirClientes("--- Teste R: CONSULTA ATUAL ---", clientes);
    }
    
    // U - Teste de Atualiza��o (Update)
    public void testarAtualizacao() {
        System.out.println("\n--- Teste U: ATUALIZA��O ---");
        
        // Busca o ID do cliente que queremos atualizar, verificando o nome
        List<Cliente> clientes = dao.buscarTodos();
        
        // Usando streams para encontrar o cliente pelo nome
        Cliente clienteParaAtualizar = clientes.stream()
            .filter(c -> c.getNomeRazaoSocial().equals("CLIENTE TESTE UPDATE"))
            .findFirst()
            .orElse(null);
            
        if (clienteParaAtualizar != null) {
            
            // Define os novos dados, mantendo o ID
            clienteParaAtualizar.setNomeRazaoSocial("NOME ATUALIZADO OK");
            clienteParaAtualizar.setCnpjCpf("99.999.999/0001-99");
            
            // Executa a atualiza��o
            dao.atualizar(clienteParaAtualizar);
            
            // Consulta novamente para verificar
            imprimirClientes("--- Consulta P�s-Atualiza��o ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Cliente para UPDATE n�o encontrado. A execu��o pode ter falhado.");
        }
    }
    
    // D - Teste de Exclus�o (Delete)
    public void testarExclusao() {
        System.out.println("\n--- Teste D: EXCLUS�O ---");
        
        // Busca o cliente que queremos deletar, verificando o nome
        List<Cliente> clientes = dao.buscarTodos();
        
        Cliente clienteParaDeletar = clientes.stream()
            .filter(c -> c.getNomeRazaoSocial().equals("CLIENTE TESTE DELETE") || c.getNomeRazaoSocial().equals("NOME ATUALIZADO OK"))
            .findFirst()
            .orElse(null);

        if (clienteParaDeletar != null) {
            
            // Executa a exclus�o
            dao.excluir(clienteParaDeletar.getIdCliente());
            
            // Consulta final para verificar
            imprimirClientes("--- Consulta Final P�s-Exclus�o ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Cliente para DELETE n�o encontrado. A execu��o pode ter falhado.");
        }
    }
}