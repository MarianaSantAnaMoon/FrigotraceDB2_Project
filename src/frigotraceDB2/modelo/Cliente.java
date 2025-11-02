package frigotraceDB2.modelo;

// Classe que representa a tabela 'cliente'
public class Cliente {

    // 1. Atributos da Tabela
    private Integer idCliente; // Chave prim?ria (PK) - Integer para permitir valor null antes de salvar
    private String nomeRazaoSocial;
    private String cnpjCpf;
    private int idEndereco; // Chave Estrangeira (FK) para a tabela 'endereco'

    // 2. Construtor (Para criar um novo objeto antes de salvar no banco)
    public Cliente(String nomeRazaoSocial, String cnpjCpf, int idEndereco) {
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cnpjCpf = cnpjCpf;
        this.idEndereco = idEndereco;
    }
    
    // Opcional: Construtor vazio
    public Cliente() {
    }

    // 3. Getters e Setters (Para acessar e modificar os atributos)
    
    // Getter e Setter para o ID (Importante para o SELECT e UPDATE)
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
    
    // Getters e Setters para os demais atributos
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

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }
    
    // Opcional: toString() para facilitar a visualiza??o no console
    @Override
    public String toString() {
        return "Cliente{" + "id=" + idCliente + ", nome=" + nomeRazaoSocial + ", CNPJ/CPF=" + cnpjCpf + '}';
    }
}