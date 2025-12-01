package frigotraceDB2.modelo;

// Classe que representa a tabela 'cliente'
public class Cliente {

    // 1. Atributos da Tabela
    private Integer idCliente; // Chave primária (PK)
    private String nomeRazaoSocial;
    private String cnpjCpf;
    
    // [CORREÇÃO] O tipo foi mudado para Integer para permitir o valor 'null'
    private Integer idEndereco; // Chave Estrangeira (FK) - Agora pode ser null

    // 2. Construtores

    // Construtor para NOVO cliente COM endereço (opcional)
    public Cliente(String nomeRazaoSocial, String cnpjCpf, Integer idEndereco) {
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = idEndereco;
    }
    
    // [NOVO] Construtor para NOVO cliente SEM endereço (apenas campos obrigatórios)
    public Cliente(String nomeRazaoSocial, String cnpjCpf) {
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = null; // Seta explicitamente como null
    }

    // Opcional: Construtor vazio
    public Cliente() {
    }

    // 3. Getters e Setters
    
    // Getter e Setter para o ID Cliente (PK)
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
    
    // Getters e Setters para Nome e CNPJ/CPF
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

    // [CORREÇÃO] Getter e Setter para o ID Endereço (Agora Integer)
    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }
    
    // Opcional: toString()
    @Override
    public String toString() {
        return "Cliente{" + "id=" + idCliente + ", nome=" + nomeRazaoSocial + ", CNPJ/CPF=" + cnpjCpf + ", idEndereco=" + idEndereco + '}';
    }
}