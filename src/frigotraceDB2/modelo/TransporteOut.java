package frigotraceDB2.modelo;

import java.sql.Date;

public class TransporteOut {

    private int idTransporteOut;
    private String veiculoPlaca;
    private float temperaturaTransporte;
    private Date dataSaida; 
    private int idVenda;

    public TransporteOut() {}

    public TransporteOut(int idTransporteOut, String veiculoPlaca, float temperaturaTransporte,
                         Date dataSaida, int idVenda) {
        this.idTransporteOut = idTransporteOut;
        this.veiculoPlaca = veiculoPlaca;
        this.temperaturaTransporte = temperaturaTransporte;
        this.dataSaida = dataSaida;
        this.idVenda = idVenda;
    }

    public int getIdTransporteOut() { return idTransporteOut; }
    public void setIdTransporteOut(int idTransporteOut) { this.idTransporteOut = idTransporteOut; }

    public String getVeiculoPlaca() { return veiculoPlaca; }
    public void setVeiculoPlaca(String veiculoPlaca) { this.veiculoPlaca = veiculoPlaca; }

    public float getTemperaturaTransporte() { return temperaturaTransporte; }
    public void setTemperaturaTransporte(float temperaturaTransporte) { this.temperaturaTransporte = temperaturaTransporte; }

    public Date getDataSaida() { return dataSaida; }
    public void setDataSaida(Date dataSaida) { this.dataSaida = dataSaida; }

    public int getIdVenda() { return idVenda; }
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }
}
