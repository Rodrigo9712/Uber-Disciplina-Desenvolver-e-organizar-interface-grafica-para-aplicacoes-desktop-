
package sistema_de_transporte_app.model;

import java.util.ArrayList;
import java.util.List;

public class Passageiro {
    private String nome;
    private String telefone;
    private double avaliacao;
    private String cpf;
    private List<Corrida> historico = new ArrayList<>();

    public Passageiro() { }

    public Passageiro(String nome, String telefone, String cpf) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.avaliacao = 5.0;
    }

    public void adicionarAoHistorico(Corrida c) { this.historico.add(c); }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public double getAvaliacao() { return avaliacao; }
    public void setAvaliacao(double avaliacao) { this.avaliacao = avaliacao; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public List<Corrida> getHistorico() { return new ArrayList<>(historico); }
    public void setHistorico(List<Corrida> historico) { this.historico = historico; }

    @Override
    public String toString() { return nome + " (" + cpf + ")"; }
}