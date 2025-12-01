package frigotraceDB2.modelo;

public class Email {
    private int idEmail;
    private String enderecoEmail;

    public Email() {}

    public Email(int idEmail, String enderecoEmail) {
        this.idEmail = idEmail;
        this.enderecoEmail = enderecoEmail;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public String getEnderecoEmail() {
        return enderecoEmail;
    }

    public void setEnderecoEmail(String enderecoEmail) {
        this.enderecoEmail = enderecoEmail;
    }
}
