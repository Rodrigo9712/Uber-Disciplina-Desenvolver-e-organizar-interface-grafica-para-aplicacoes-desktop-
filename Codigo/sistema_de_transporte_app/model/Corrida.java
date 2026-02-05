
package sistema_de_transporte_app.model;

import java.time.LocalDateTime;

public class Corrida {
    private long id;
    private int tempoEstimado;       // minutos
    private double valor;
    private double distanciaKm;
    private String status;           // PENDENTE, EM_ANDAMENTO, FINALIZADA, CANCELADA
    private Passageiro passageiro;
    private Motorista motorista;
    private String origem;
    private String destino;
    private LocalDateTime solicitadaEm;
    private LocalDateTime finalizadaEm;

    public Corrida() { }

    public Corrida(long id, Passageiro passageiro, Motorista motorista, String origem, String destino,
                   double distanciaKm, int tempoEstimado, double valor) {
        this.id = id;
        this.passageiro = passageiro;
        this.motorista = motorista;
        this.origem = origem;
        this.destino = destino;
        this.distanciaKm = distanciaKm;
        this.tempoEstimado = tempoEstimado;
        this.valor = valor;
        this.status = "PENDENTE";
        this.solicitadaEm = LocalDateTime.now();
    }

    public void iniciarCorrida() {
        if ("PENDENTE".equals(status)) this.status = "EM_ANDAMENTO";
    }

    public void finalizarCorrida() {
        if (!"CANCELADA".equals(status)) {
            this.status = "FINALIZADA";
            this.finalizadaEm = LocalDateTime.now();
        }
    }

    public void cancelarCorrida() {
        if (!"FINALIZADA".equals(status)) this.status = "CANCELADA";
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getTempoEstimado() { return tempoEstimado; }
    public void setTempoEstimado(int tempoEstimado) { this.tempoEstimado = tempoEstimado; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Passageiro getPassageiro() { return passageiro; }
    public void setPassageiro(Passageiro passageiro) { this.passageiro = passageiro; }
    public Motorista getMotorista() { return motorista; }
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public LocalDateTime getSolicitadaEm() { return solicitadaEm; }
    public void setSolicitadaEm(LocalDateTime solicitadaEm) { this.solicitadaEm = solicitadaEm; }
    public LocalDateTime getFinalizadaEm() { return finalizadaEm; }
    public void setFinalizadaEm(LocalDateTime finalizadaEm) { this.finalizadaEm = finalizadaEm; }

    @Override
    public String toString() {
        return "Corrida{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", valor=" + valor +
                ", distanciaKm=" + distanciaKm +
                ", passageiro=" + (passageiro != null ? passageiro.getNome() : "-") +
                ", motorista=" + (motorista != null ? motorista.getNome() : "-") +
                ", origem='" + origem + '\'' +
                ", destino='" + destino + '\'' +
                '}';
    }
}