package frigotraceDB2.modelo;

public class Telefone {
    private int idTelefone;
    private String numero;

    public Telefone() {}

    public Telefone(int idTelefone, String numero) {
        this.idTelefone = idTelefone;
        this.numero = numero;
    }

    public int getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(int idTelefone) {
        this.idTelefone = idTelefone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
