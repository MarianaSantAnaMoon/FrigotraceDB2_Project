package frigotraceDB2.modelo;

import java.math.BigDecimal;
import java.sql.Date; // Usando java.sql.Date para mapeamento direto com o JDBC

public class Venda {

    private int idVenda;
    private int idProduto; // Chave estrangeira para a tabela 'produto'
    private Date dataVenda;
    private BigDecimal valorTotal;
    private Date dataEntrega;
    private int idCliente; // Chave estrangeira para a tabela 'cliente'

    // Construtor Completo
    public Venda(int idVenda, int idProduto, Date dataVenda, BigDecimal valorTotal, Date dataEntrega, int idCliente) {
        this.idVenda = idVenda;
        this.idProduto = idProduto;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.dataEntrega = dataEntrega;
        this.idCliente = idCliente;
    }

    // Construtor sem o idVenda (útil para INSERT)
    public Venda(int idProduto, Date dataVenda, BigDecimal valorTotal, Date dataEntrega, int idCliente) {
        this.idProduto = idProduto;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.dataEntrega = dataEntrega;
        this.idCliente = idCliente;
    }

    // Construtor Vazio
    public Venda() {
    }

    // Getters e Setters
    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Venda{" +
               "idVenda=" + idVenda +
               ", idProduto=" + idProduto +
               ", dataVenda=" + dataVenda +
               ", valorTotal=" + valorTotal +
               ", dataEntrega=" + dataEntrega +
               ", idCliente=" + idCliente +
               '}';
    }
}