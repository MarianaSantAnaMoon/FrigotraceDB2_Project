package frigotraceDB2.modelo;

public class Fornecedor {

    private Integer idFornecedor;
    private String nomeRazaoSocial;
    private String cnpjCpf;
    private Integer idEndereco; // Agora totalmente opcional (pode ser null)

    // Construtor para BUSCA (com ID)
    public Fornecedor(Integer idFornecedor, String nomeRazaoSocial, String cnpjCpf, Integer idEndereco) {
        this.idFornecedor = idFornecedor;
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = idEndereco;
    }

    // Construtor para INSERÇÃO com endereço
    public Fornecedor(String nomeRazaoSocial, String cnpjCpf, Integer idEndereco) {
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = idEndereco;
    }

    // Construtor para INSERÇÃO sem endereço
    public Fornecedor(String nomeRazaoSocial, String cnpjCpf) {
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = null;
    }

    public Fornecedor() {}

    // Getters e setters
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

    @Override
    public String toString() {
        return "Fornecedor{" + "id=" + idFornecedor + ", nome=" + nomeRazaoSocial +
                ", CNPJ/CPF=" + cnpjCpf + ", idEndereco=" + idEndereco + '}';
    }
}
