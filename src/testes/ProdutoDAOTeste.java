package testes;

import frigotraceDB2.modelo.Produto;
import frigotraceDB2.dao.ProdutoDAO;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.math.BigDecimal;

/**
 * Classe responsável por executar e validar todas as operações do CRUD do ProdutoDAO.
 */
public class ProdutoDAOTeste {

    private ProdutoDAO dao = new ProdutoDAO();

    public static void main(String[] args) {
        ProdutoDAOTeste teste = new ProdutoDAOTeste();
        
        System.out.println("\n============================================================");
        System.out.println("====== INICIANDO TESTES CRUD: PRODUTO DAO ======");
        System.out.println("============================================================");
        
        // Sequência de testes:
        teste.testarInsercao();
        teste.testarConsulta();
        teste.testarAtualizacao();
        teste.testarExclusao();
        
        System.out.println("\n--- TODOS OS TESTES CRUD DO PRODUTO DAO FORAM EXECUTADOS ---");
    }
    
    // Método auxiliar para imprimir produtos
    private void imprimirProdutos(String titulo, List<Produto> lista) {
        System.out.println("\n" + titulo);
        if (lista.isEmpty()) {
            System.out.println("  (Lista vazia)");
        } else {
            for (Produto p : lista) {
                System.out.printf("  [ID: %d] Tipo: %s | Lote: %s | Preço: R$%s | Peso: %s kg\n", 
                    p.getIdProduto(), 
                    p.getTipoDeProduto(), 
                    p.getLote(),
                    p.getPreco() != null ? p.getPreco().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "NULO",
                    p.getPeso() != null ? p.getPeso().toString() : "NULO"
                ); 
            }
        }
    }
    
    // Método auxiliar para gerar data simples
    private Date criarData(int ano, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes - 1, dia, 0, 0, 0); 
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    // C - Teste de Inserção (Create)
    public void testarInsercao() {
        System.out.println("\n--- Teste C: INSERÇÃO ---");
        
        // Datas comuns de produção e validade para os testes
        Date dataProd = criarData(2025, 9, 1);
        Date dataVal = criarData(2026, 9, 1);
        
        // Produto 1: JERKED BEEF (Para ser atualizado)
        Produto p1 = new Produto(
            "Jerked Beef", 
            "LOTE-JB-456", 
            new BigDecimal("25.50"), 
            1.5f, // 1.5 kg
            dataProd, 
            dataVal
        );
        
        // Produto 2: CHARQUE (Para ser deletado, com campos NULOS)
        Produto p2 = new Produto(
            "Charque", 
            "LOTE-CH-999", 
            new BigDecimal("30.00"), 
            null, // Peso NULO
            dataProd, 
            null  // Data Validade NULA
        );
        
        dao.inserir(p1);
        dao.inserir(p2);
    }
    
    // R - Teste de Consulta (Read)
    public void testarConsulta() {
        List<Produto> lista = dao.buscarTodos();
        imprimirProdutos("--- Teste R: CONSULTA ATUAL ---", lista);
    }
    
    // U - Teste de Atualização (Update)
    public void testarAtualizacao() {
        System.out.println("\n--- Teste U: ATUALIZAÇÃO ---");
        
        // 1. Busca o ID do item "Jerked Beef" que queremos atualizar
        List<Produto> lista = dao.buscarTodos();
        
        Produto produtoParaAtualizar = lista.stream()
            .filter(p -> "Jerked Beef".equals(p.getTipoDeProduto()))
            .findFirst()
            .orElse(null);
            
        if (produtoParaAtualizar != null) {
            
            // 2. Define os novos dados:
            produtoParaAtualizar.setTipoDeProduto("Jerked Beef Especial");
            produtoParaAtualizar.setPreco(new BigDecimal("28.99")); // Novo Preço
            produtoParaAtualizar.setPeso(2.0f); // Novo Peso
            
            // 3. Executa a atualização
            dao.atualizar(produtoParaAtualizar);
            
            // 4. Consulta novamente para verificar
            imprimirProdutos("--- Consulta Pós-Atualização (Verificar o novo preço e nome) ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Produto para UPDATE (Jerked Beef) não encontrado.");
        }
    }
    
    // D - Teste de Exclusão (Delete)
    public void testarExclusao() {
        System.out.println("\n--- Teste D: EXCLUSÃO ---");
        
        // 1. Busca novamente para garantir que pega o ID correto
        List<Produto> lista = dao.buscarTodos();
        
        Produto produtoParaDeletar = lista.stream()
            .filter(p -> "Charque".equals(p.getTipoDeProduto()))
            .findFirst()
            .orElse(null);

        if (produtoParaDeletar != null) {
            
            // 2. Executa a exclusão
            dao.excluir(produtoParaDeletar.getIdProduto());
            
            // 3. Consulta final para verificar
            imprimirProdutos("--- Consulta Final Pós-Exclusão ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Produto para DELETE (Charque) não encontrado.");
        }
    }
}
