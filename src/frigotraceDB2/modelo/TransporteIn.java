package frigotraceDB2.modelo;

import java.sql.Date;

public class TransporteIn {
    private int idTransporteIn;
    private String veiculoPlaca;
    private float temperaturaTransporte;
    private Date dataRecebida;
    private int idMateriaPrima;

    public TransporteIn() {}

    public TransporteIn(int idTransporteIn, String veiculoPlaca, float temperaturaTransporte, 
                        Date dataRecebida, int idMateriaPrima) {
        this.idTransporteIn = idTransporteIn;
        this.veiculoPlaca = veiculoPlaca;
        this.temperaturaTransporte = temperaturaTransporte;
        this.dataRecebida = dataRecebida;
        this.idMateriaPrima = idMateriaPrima;
    }

    public int getIdTransporteIn() {
        return idTransporteIn;
    }

    public void setIdTransporteIn(int idTransporteIn) {
        this.idTransporteIn = idTransporteIn;
    }

    public String getVeiculoPlaca() {
        return veiculoPlaca;
    }

    public void setVeiculoPlaca(String veiculoPlaca) {
        this.veiculoPlaca = veiculoPlaca;
    }

    public float getTemperaturaTransporte() {
        return temperaturaTransporte;
    }

    public void setTemperaturaTransporte(float temperaturaTransporte) {
        this.temperaturaTransporte = temperaturaTransporte;
    }

    public Date getDataRecebida() {
        return dataRecebida;
    }

    public void setDataRecebida(Date dataRecebida) {
        this.dataRecebida = dataRecebida;
    }

    public int getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(int idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
    }
}

