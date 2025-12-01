package frigotraceDB2.modelo;

public class Endereco {

    // 1. Atributos privados (Baseados na sua tabela 'endereco')
    private Integer idEndereco; // PK - Integer para tratar o auto-incremento/null
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    // 2. Construtores

    // Construtor completo (Para ler do banco de dados)
    public Endereco(Integer idEndereco, String rua, String bairro, String cidade, String estado, String cep) {
        this.idEndereco = idEndereco;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    // Construtor para NOVO Endereço (Para inserir no banco, sem o ID)
    public Endereco(String rua, String bairro, String cidade, String estado, String cep) {
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.idEndereco = null; // O ID será gerado pelo banco
    }
    
    // Construtor vazio (Opcional, mas útil)
    public Endereco() {
    }

    // 3. Getters e Setters
    
    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
    
    @Override
    public String toString() {
        return "Endereco{" + "id=" + idEndereco + ", rua=" + rua + ", cidade=" + cidade + ", cep=" + cep + '}';
    }
}