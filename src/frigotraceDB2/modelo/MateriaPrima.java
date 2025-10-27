package frigotraceDB2.modelo;

import java.util.Date; // Necessário para usar o tipo DATE/java.util.Date

public class MateriaPrima {

    private Integer idMateriaPrima;
    private String sisbSif;
    private Date dataProducao;
    private Date dataValidade;
    private Float temperatura; // FLOAT no BD, usamos Float (ou Double) no Java
    private Integer idFornecedor;

    // Construtores

    // Construtor completo (usado ao BUSCAR do banco)
    public MateriaPrima(Integer idMateriaPrima, String sisbSif, Date dataProducao, Date dataValidade, Float temperatura, Integer idFornecedor) {
        this.idMateriaPrima = idMateriaPrima;
        this.sisbSif = sisbSif;
        this.dataProducao = dataProducao;
        this.dataValidade = dataValidade;
        this.temperatura = temperatura;
        this.idFornecedor = idFornecedor;
    }

    // Construtor sem ID (usado ao INSERIR no banco)
    public MateriaPrima(String sisbSif, Date dataProducao, Date dataValidade, Float temperatura, Integer idFornecedor) {
        this.sisbSif = sisbSif;
        this.dataProducao = dataProducao;
        this.dataValidade = dataValidade;
        this.temperatura = temperatura;
        this.idFornecedor = idFornecedor;
    }

    // Getters e Setters
 
    public Integer getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(Integer idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
    }

    public String getSisbSif() {
        return sisbSif;
    }

    public void setSisbSif(String sisbSif) {
        this.sisbSif = sisbSif;
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

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }
}
