package testes;

import frigotraceDB2.dao.VendaDAO;
import frigotraceDB2.modelo.Venda;
import java.math.BigDecimal;
import java.sql.Date; 
import java.sql.SQLException;
import java.util.List;

public class VendaDaoTeste {

    // Método principal para executar o teste
    public static void main(String[] args) {
        
        // 1. Configuração Inicial
        VendaDAO vendaDAO = new VendaDAO();
        
        // ATENÇÃO: Substitua estes IDs por valores que REALMENTE EXISTEM
        // nas suas tabelas 'produto' e 'cliente', senão a FK falhará!
        int idProdutoExistente = 1; 
        int idClienteExistente = 1; 

        // 2. Criação do Objeto de Teste
        // Este objeto ainda não tem o ID da Venda (ele será gerado pelo banco)
        Venda novaVenda = new Venda(
            idProdutoExistente,
            // new Date(System.currentTimeMillis()) cria a data de hoje
            new Date(System.currentTimeMillis()), 
            new BigDecimal("250.99"), // Valor da venda
            Date.valueOf("2025-12-01"), // Data de entrega
            idClienteExistente
        );

        int idDaVendaCriada = 0;

        try {
            System.out.println("--- 1. Teste: Inserção (CREATE) ---");
            
            // Salva a nova venda no banco de dados
            Venda vendaSalva = vendaDAO.salvar(novaVenda);
            
            // O ID gerado pelo banco é retornado e armazenado no objeto
            idDaVendaCriada = vendaSalva.getIdVenda();
            
            if (idDaVendaCriada > 0) {
                System.out.println("? SUCESSO: Venda Salva. ID Gerado: " + idDaVendaCriada);
            } else {
                 System.out.println("? ERRO: Venda não foi salva corretamente.");
            }

            // --- TESTE DE LEITURA POR ID ---
            System.out.println("\n--- 2. Teste: Busca por ID (READ) ---");
            Venda vendaBuscada = vendaDAO.buscarPorId(idDaVendaCriada);
            
            if (vendaBuscada != null) {
                System.out.println("? SUCESSO: Venda Encontrada: " + vendaBuscada);
            } else {
                System.out.println("? ERRO: Venda com ID " + idDaVendaCriada + " não foi encontrada.");
            }

            // --- TESTE DE ATUALIZAÇÃO ---
            System.out.println("\n--- 3. Teste: Atualização (UPDATE) ---");
            // Mudando o valor total da venda
            vendaBuscada.setValorTotal(new BigDecimal("300.50")); 
            boolean atualizado = vendaDAO.atualizar(vendaBuscada);
            
            if (atualizado) {
                System.out.println("? SUCESSO: Venda Atualizada. Novo valor: R$300.50");
            } else {
                System.out.println("? ERRO: Venda não foi atualizada.");
            }
            
            // --- TESTE DE LISTAGEM ---
            System.out.println("\n--- 4. Teste: Listar Todos (READ ALL) ---");
            List<Venda> todasVendas = vendaDAO.listarTodos();
            System.out.println("Total de Vendas no Banco: " + todasVendas.size());
            todasVendas.forEach(v -> System.out.println(" - " + v));

            
            // --- TESTE DE DELEÇÃO (OPCIONAL, CUIDADO AO USAR!) ---
            /*
            System.out.println("\n--- 5. Teste: Deleção (DELETE) ---");
            boolean deletado = vendaDAO.deletar(idDaVendaCriada);
            if (deletado) {
                System.out.println("? SUCESSO: Venda com ID " + idDaVendaCriada + " Deletada.");
            } else {
                 System.out.println("? ERRO: Venda não foi deletada.");
            }
            */

        } catch (SQLException e) {
            System.err.println("\n? ERRO NO BANCO DE DADOS DURANTE O TESTE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}