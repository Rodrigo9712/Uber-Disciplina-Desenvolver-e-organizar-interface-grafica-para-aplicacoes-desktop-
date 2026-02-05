package sistema_de_transporte_app.model;

import java.util.ArrayList;
import java.util.List;

public class Passageiro {
    private String nome;
    private String telefone;
    private double avaliacao;
    private String cpf;

    // ===== NOVOS CAMPOS (LOGIN) =====
    private String usuario;
    private String senha;
    // ================================

    private List<Corrida> historico = new ArrayList<>();

    public Passageiro() { }

    public Passageiro(String nome, String telefone, String cpf) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.avaliacao = 5.0;
    }

    public Passageiro(String nome, String telefone, String cpf, String usuario, String senha) {
        this(nome, telefone, cpf);
        this.usuario = usuario;
        this.senha = senha;
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

    // ===== NOVOS GET/SET (LOGIN) =====
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    // =================================

    public List<Corrida> getHistorico() { return new ArrayList<>(historico); }
    public void setHistorico(List<Corrida> historico) { this.historico = historico; }

    @Override
    public String toString() { return nome + " (" + cpf + ")"; }
}