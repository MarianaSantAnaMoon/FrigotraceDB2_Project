package frigotraceDB2.modelo;

public class Fornecedor {

    // Atributos privados (campos da tabela)
    private Integer idFornecedor; // O ID pode ser null antes de ser salvo
    private String nomeRazaoSocial;
    private String cnpjCpf;
    private Integer idEndereco; // Usamos Integer porque a coluna no BD permite NULL

    // Construtores

    // Construtor completo (usado ao BUSCAR do banco)
    public Fornecedor(Integer idFornecedor, String nomeRazaoSocial, String cnpjCpf, Integer idEndereco) {
        this.idFornecedor = idFornecedor;
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = idEndereco;
    }

    // Construtor sem ID (usado ao INSERIR no banco)
    public Fornecedor(String nomeRazaoSocial, String cnpjCpf, Integer idEndereco) {
        // O ID é deixado nulo (null), pois será gerado pelo banco.
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = idEndereco;
    }

    // Getters e Setters
    
    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    public void setNomeRazaoSocial(String nomeRazaoSocial) {
        this.nomeRazaoSocial = nomeRazaoSocial;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }
}
