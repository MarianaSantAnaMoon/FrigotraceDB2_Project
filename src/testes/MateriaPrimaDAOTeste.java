package testes;

import frigotraceDB2.modelo.MateriaPrima;
import frigotraceDB2.dao.MateriaPrimaDAO;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * Classe responsável por executar e validar todas as operações do CRUD da MateriaPrimaDAO.
 */
public class MateriaPrimaDAOTeste {

    private MateriaPrimaDAO dao = new MateriaPrimaDAO();
    // ATENÇÃO: ESTE ID DE FORNECEDOR DEVE EXISTIR na sua tabela 'fornecedor' para que o teste de FK funcione.
    private Integer idFornecedorExistente = 1; 

    public static void main(String[] args) {
        MateriaPrimaDAOTeste teste = new MateriaPrimaDAOTeste();
        
        System.out.println("\n============================================================");
        System.out.println("====== INICIANDO TESTES CRUD: MATÉRIA-PRIMA DAO ======");
        System.out.println("============================================================");
        
        // Sequência de testes:
        teste.testarInsercao();
        teste.testarConsulta();
        teste.testarAtualizacao();
        teste.testarExclusao();
        
        System.out.println("\n--- TODOS OS TESTES CRUD DA MATÉRIA-PRIMA DAO FORAM EXECUTADOS ---");
    }
    
    // Método auxiliar para imprimir matéria-prima
    private void imprimirMateriasPrimas(String titulo, List<MateriaPrima> lista) {
        System.out.println("\n" + titulo);
        if (lista.isEmpty()) {
            System.out.println("  (Lista vazia)");
        } else {
            for (MateriaPrima mp : lista) {
                System.out.printf("  [ID: %d] SISB/SIF: %s | Temp: %s | Validade: %s | FK Forn: %s\n", 
                    mp.getIdMateriaPrima(), 
                    mp.getSisbSif(), 
                    mp.getTemperatura() != null ? mp.getTemperatura().toString() : "NULO",
                    mp.getDataValidade() != null ? mp.getDataValidade().toString() : "NULO",
                    mp.getIdFornecedor() != null ? mp.getIdFornecedor().toString() : "NULO"
                ); 
            }
        }
    }
    
    // Método auxiliar para gerar data simples
    private Date criarData(int ano, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        // Os meses em Calendar são base 0 (Janeiro = 0)
        cal.set(ano, mes - 1, dia, 0, 0, 0); 
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    // C - Teste de Inserção (Create)
    public void testarInsercao() {
        System.out.println("\n--- Teste C: INSERÇÃO ---");
        
        Date dataProd = criarData(2025, 10, 20);
        Date dataVal = criarData(2025, 12, 31);
        
        // MP 1 para ser atualizada (Com todos os campos preenchidos)
        MateriaPrima mp1 = new MateriaPrima(
            "SIF-UPDATE-2025", 
            dataProd, 
            dataVal, 
            3.5f, // Temperatura
            idFornecedorExistente
        );
        
        // MP 2 para ser deletada (Com alguns campos NULOS, testando a robustez)
        MateriaPrima mp2 = new MateriaPrima(
            "SIF-DELETE-2025", 
            null, // Data Produção NULA
            criarData(2026, 1, 15), 
            null, // Temperatura NULA
            idFornecedorExistente
        );
        
        dao.inserir(mp1);
        dao.inserir(mp2);
    }
    
    // R - Teste de Consulta (Read)
    public void testarConsulta() {
        List<MateriaPrima> lista = dao.buscarTodos();
        imprimirMateriasPrimas("--- Teste R: CONSULTA ATUAL ---", lista);
    }
    
    // U - Teste de Atualização (Update)
    public void testarAtualizacao() {
        System.out.println("\n--- Teste U: ATUALIZAÇÃO ---");
        
        // 1. Busca o ID do item que queremos atualizar
        List<MateriaPrima> lista = dao.buscarTodos();
        
        MateriaPrima mpParaAtualizar = lista.stream()
            .filter(mp -> "SIF-UPDATE-2025".equals(mp.getSisbSif()))
            .findFirst()
            .orElse(null);
            
        if (mpParaAtualizar != null) {
            
            // 2. Define os novos dados:
            mpParaAtualizar.setSisbSif("SIF-ATUALIZADO-OK");
            mpParaAtualizar.setTemperatura(2.0f); // Nova Temperatura
            mpParaAtualizar.setDataValidade(criarData(2026, 6, 30)); // Nova Validade
            mpParaAtualizar.setIdFornecedor(null); // Testa alteração de FK para NULL
            
            // 3. Executa a atualização
            dao.atualizar(mpParaAtualizar);
            
            // 4. Consulta novamente para verificar
            imprimirMateriasPrimas("--- Consulta Pós-Atualização (Verificar a nova temperatura e data) ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Matéria-Prima para UPDATE não encontrada.");
        }
    }
    
    // D - Teste de Exclusão (Delete)
    public void testarExclusao() {
        System.out.println("\n--- Teste D: EXCLUSÃO ---");
        
        // 1. Busca novamente para garantir que pega o ID correto
        List<MateriaPrima> lista = dao.buscarTodos();
        
        MateriaPrima mpParaDeletar = lista.stream()
            .filter(mp -> "SIF-DELETE-2025".equals(mp.getSisbSif()))
            .findFirst()
            .orElse(null);

        if (mpParaDeletar != null) {
            
            // 2. Executa a exclusão
            dao.excluir(mpParaDeletar.getIdMateriaPrima());
            
            // 3. Consulta final para verificar
            imprimirMateriasPrimas("--- Consulta Final Pós-Exclusão ---", dao.buscarTodos());
        } else {
            System.out.println("  Aviso: Matéria-Prima para DELETE não encontrada.");
        }
    }
}
