
package sistema_de_transporte_app.model;

public class Veiculo {
    private String categoria; // ECONOMICO, SUV, LUXO
    private String modelo;
    private String placa;
    private String cor;
    private Motorista motorista;

    public Veiculo() { }

    public Veiculo(String categoria, String modelo, String placa, String cor) {
        this.categoria = categoria;
        this.modelo = modelo;
        this.placa = placa;
        this.cor = cor;
    }

    public void atualizarLocalizacao() { /* stub */ }

    // Getters e Setters
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public Motorista getMotorista() { return motorista; }
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }

    @Override
    public String toString() { return categoria + " - " + modelo + " (" + placa + ")"; }
}