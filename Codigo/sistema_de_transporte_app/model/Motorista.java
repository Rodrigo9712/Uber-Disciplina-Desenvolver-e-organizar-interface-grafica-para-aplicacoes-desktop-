
package sistema_de_transporte_app.model;

public class Motorista {
    private String nome;
    private int numeroCorridas;
    private double avaliacao;
    private double saldoGanhos;
    private String documentoVeiculo; // ID único do motorista
    private Veiculo veiculo;
    private boolean disponivel;

    public Motorista() { }

    public Motorista(String nome, String documentoVeiculo) {
        this.nome = nome;
        this.documentoVeiculo = documentoVeiculo;
        this.avaliacao = 5.0;
        this.saldoGanhos = 0.0;
        this.numeroCorridas = 0;
        this.disponivel = true;
    }

    // Regras
    public void alterarStatusDisponibilidade(boolean disponivel) { this.disponivel = disponivel; }
    public void aceitarPagamentoDinheiro(double valor) {
        this.saldoGanhos += valor;
        this.numeroCorridas++;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getNumeroCorridas() { return numeroCorridas; }
    public void setNumeroCorridas(int numeroCorridas) { this.numeroCorridas = numeroCorridas; }
    public double getAvaliacao() { return avaliacao; }
    public void setAvaliacao(double avaliacao) { this.avaliacao = avaliacao; }
    public double getSaldoGanhos() { return saldoGanhos; }
    public void setSaldoGanhos(double saldoGanhos) { this.saldoGanhos = saldoGanhos; }
    public String getDocumentoVeiculo() { return documentoVeiculo; }
    public void setDocumentoVeiculo(String documentoVeiculo) { this.documentoVeiculo = documentoVeiculo; }
    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public String toString() {
        return nome + " (" + (veiculo != null ? veiculo.getCategoria() + " - " + veiculo.getPlaca() : "sem veículo") + ")";
    }
}