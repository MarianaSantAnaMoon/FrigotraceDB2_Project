package testes;

import frigotraceDB2.modelo.Fornecedor;
import frigotraceDB2.dao.FornecedorDAO;
import java.util.List;

/**
 * Classe respons�vel por executar e validar todas as opera��es do CRUD do FornecedorDAO.
 */
public class FornecedorDAOTeste {

    private FornecedorDAO dao = new FornecedorDAO();
    // Ajuste para um ID de endere�o que exista na sua tabela 'endereco'
    private Integer idEnderecoExistente = 1; 

    public static void main(String[] args) {
        FornecedorDAOTeste teste = new FornecedorDAOTeste();
        
        System.out.println("\n=======================================================");
        System.out.println("====== INICIANDO TESTES CRUD: FORNECEDOR DAO ======");
        System.out.println("=======================================================");
        
        // Sequ�ncia de testes:
        teste.testarInsercao();
        teste.testarConsulta();
        teste.testarAtualizacao();
        teste.testarExclusao();
        
        System.out.println("\n--- TODOS OS TESTES CRUD DO FORNECEDOR DAO FORAM EXECUTADOS ---");
    }
    
    // M�todo auxiliar para imprimir fornecedores
    private void imprimirFornecedores(String titulo, List<Fornecedor> fornecedores) {
        System.out.println("\n" + titulo);
        if (fornecedores.isEmpty()) {
            System.out.println("  (Lista vazia)");
        } else {
            for (Fornecedor f : fornecedores) {
                System.out.printf("  [ID: %d] Nome: %s | CNPJ: %s | FK Endereco: %s\n", 
                    f.getIdFornecedor(), 
                    f.getNomeRazaoSocial(), 
                    f.getCnpjCpf(),
                    f.getIdEndereco() != null ? f.getIdEndereco().toString() : "NULO"); // Trata o NULO
            }
        }
    }

    // C - Teste de Inser��o (Create)
    public void testarInsercao() {
        System.out.println("\n--- Teste C: INSER��O ---");
        
        // Fornecedor para ser atualizado (Com endere�o)
        Fornecedor f1 = new Fornecedor("FORNECEDOR UPDATE LTDA", "33.333.333/0001-33", idEnderecoExistente);
        
        // Fornecedor para ser deletado (Sem endere�o, testando o NULL)
        Fornecedor f2 = new Fornecedor("FORNECEDOR DELETE S.A.", "44.444.444/0001-44", null); 
        
        dao.inserir(f1);
        dao.inserir(f2);
    }
    
    // R - Teste de Consulta (Read)
    public void testarConsulta() {
        List<Fornecedor> fornecedores = dao.buscarTodos();
        imprimirFornecedores("--- Teste R: CONSULTA ATUAL ---", fornecedores);
    }
    
    // U - Teste de Atualiza��o (Update)
    public void testarAtualizacao() {
        System.out.println("\n--- Teste U: ATUALIZA��O ---");
        
        // 1. Precisamos buscar o ID do fornecedor que queremos atualizar
        List<Fornecedor> fornecedores = dao.buscarTodos();
        
        Fornecedor fornecedorParaAtualizar = fornecedores.stream()
            .filter(f -> f.getNomeRazaoSocial().equals("FORNECEDOR UPDATE LTDA"))
            .findFirst()
            .orElse(null);
            
        if (fornecedorParaAtualizar != null) {
            
            // 2. Define os novos dados: Muda o nome e remove o FK do endere�o (deixa NULO)
            fornecedorParaAtualizar.setNomeRazaoSocial("NOME FORN ATUALIZADO OK");
            fornecedorParaAtualizar.setCnpjCpf("88.888.888/0001-88");
            fornecedorParaAtualizar.setIdEndereco(null); // Testa a atualiza��o para NULL
            
            // 3. Executa a atualiza��o
            dao.atualizar(fornecedorParaAtualizar);
            
            // 4. Consulta novamente para verificar
            imprimirFornecedores("--- Consulta P�s-Atualiza��o (Verificar o NULL) ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Fornecedor para UPDATE n�o encontrado. A execu��o pode ter falhado.");
        }
    }
    
    // D - Teste de Exclus�o (Delete)
    public void testarExclusao() {
        System.out.println("\n--- Teste D: EXCLUS�O ---");
        
        // 1. Busca novamente para garantir que pega o ID correto
        List<Fornecedor> fornecedores = dao.buscarTodos();
        
        Fornecedor fornecedorParaDeletar = fornecedores.stream()
            // Procura pelo nome original do Fornecedor para DELETE
            .filter(f -> f.getNomeRazaoSocial().equals("FORNECEDOR DELETE S.A."))
            .findFirst()
            .orElse(null);

        if (fornecedorParaDeletar != null) {
            
            // 2. Executa a exclus�o
            dao.excluir(fornecedorParaDeletar.getIdFornecedor());
            
            // 3. Consulta final para verificar
            imprimirFornecedores("--- Consulta Final P�s-Exclus�o ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Fornecedor para DELETE n�o encontrado. A execu��o pode ter falhado.");
        }
    }
}