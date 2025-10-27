package testes; // CORRIGIDO: Agora aponta para o seu novo pacote 'testes'

import frigotraceDB2.modelo.Cliente;
import frigotraceDB2.dao.ClienteDAO;
import java.util.List;
import java.util.stream.Collectors; // Opcional, mas pode ser útil no futuro.

/**
 * Classe responsável por executar e validar todas as operações do CRUD do ClienteDAO.
 */
public class ClienteDAOTeste {

    private ClienteDAO dao = new ClienteDAO();
    private int idEnderecoExistente = 1; // Ajuste para um ID válido em 'endereco'

    public static void main(String[] args) {
        ClienteDAOTeste teste = new ClienteDAOTeste();
        
        // Sequência de testes:
        teste.testarInsercao();
        teste.testarConsulta();
        teste.testarAtualizacao();
        teste.testarExclusao();
        
        System.out.println("\n--- TODOS OS TESTES CRUD DO CLIENTE DAO FORAM EXECUTADOS ---");
    }
    
    // Método auxiliar para imprimir clientes
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

  
    // C - Teste de Inserção (Create)
    public void testarInsercao() {
        System.out.println("\n--- Teste C: INSERÇÃO ---");
        
        // Tentaremos deletar clientes anteriores para garantir que os IDs não conflitem
        // NOTA: Em um teste profissional, você faria um TRUNCATE TABLE aqui.
        
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
    
    // U - Teste de Atualização (Update)
    public void testarAtualizacao() {
        System.out.println("\n--- Teste U: ATUALIZAÇÃO ---");
        
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
            
            // Executa a atualização
            dao.atualizar(clienteParaAtualizar);
            
            // Consulta novamente para verificar
            imprimirClientes("--- Consulta Pós-Atualização ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Cliente para UPDATE não encontrado. A execução pode ter falhado.");
        }
    }
    
    // D - Teste de Exclusão (Delete)
    public void testarExclusao() {
        System.out.println("\n--- Teste D: EXCLUSÃO ---");
        
        // Busca o cliente que queremos deletar, verificando o nome
        List<Cliente> clientes = dao.buscarTodos();
        
        Cliente clienteParaDeletar = clientes.stream()
            .filter(c -> c.getNomeRazaoSocial().equals("CLIENTE TESTE DELETE") || c.getNomeRazaoSocial().equals("NOME ATUALIZADO OK"))
            .findFirst()
            .orElse(null);

        if (clienteParaDeletar != null) {
            
            // Executa a exclusão
            dao.excluir(clienteParaDeletar.getIdCliente());
            
            // Consulta final para verificar
            imprimirClientes("--- Consulta Final Pós-Exclusão ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Cliente para DELETE não encontrado. A execução pode ter falhado.");
        }
    }
}