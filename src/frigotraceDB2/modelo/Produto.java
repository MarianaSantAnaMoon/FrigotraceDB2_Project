package frigotraceDB2.modelo;

import java.math.BigDecimal;
import java.util.Date;

public class Produto {

    private Integer idProduto;
    private String tipoDeProduto;
    private String lote;
    private BigDecimal preco;
    private Float peso;
    private Date dataProducao;
    private Date dataValidade;

    // Construtor completo
    public Produto(Integer idProduto, String tipoDeProduto, String lote, BigDecimal preco,
                   Float peso, Date dataProducao, Date dataValidade) {
        this.idProduto = idProduto;
        this.tipoDeProduto = tipoDeProduto;
        this.lote = lote;
        this.preco = preco;
        this.peso = peso;
        this.dataProducao = dataProducao;
        this.dataValidade = dataValidade;
    }

    // Construtor sem ID (para inserir)
    public Produto(String tipoDeProduto, String lote, BigDecimal preco,
                   Float peso, Date dataProducao, Date dataValidade) {
        this.tipoDeProduto = tipoDeProduto;
        this.lote = lote;
        this.preco = preco;
        this.peso = peso;
        this.dataProducao = dataProducao;
        this.dataValidade = dataValidade;
    }

    // ? CONSTRUTOR VAZIO (RESOLVE O ERRO)
    public Produto() {
    }

    // Getters e Setters
    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getTipoDeProduto() {
        return tipoDeProduto;
    }

    public void setTipoDeProduto(String tipoDeProduto) {
        this.tipoDeProduto = tipoDeProduto;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Date getDataProducao() {
        return dataProducao;
    }

    public void setDataProducao(Date dataProducao) {
        this.dataProducao = dataProducao;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }
}
