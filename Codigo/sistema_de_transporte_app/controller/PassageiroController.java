package sistema_de_transporte_app.controller;

import repository.impl.PassageiroRepository;
import sistema_de_transporte_app.model.Passageiro;

import java.util.List;

public class PassageiroController {
    private final PassageiroRepository passageiroRepository;

    public PassageiroController(PassageiroRepository passageiroRepository) {
        this.passageiroRepository = passageiroRepository;
    }

    // NOVO: agora cadastra usuário e senha também
    public Passageiro cadastrar(String nome, String telefone, String cpf, String usuario, String senha) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF é obrigatório.");
        }
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("Usuário é obrigatório.");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        Passageiro existente = passageiroRepository.buscarPorCpf(cpf);
        if (existente != null) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Passageiro p = new Passageiro(nome, telefone, cpf, usuario, senha);
        return passageiroRepository.salvar(p);
    }

    public Passageiro buscarPorCpf(String cpf) {
        return passageiroRepository.buscarPorCpf(cpf);
    }

    public List<Passageiro> listarTodos() {
        return passageiroRepository.listarTodos();
    }

    // NOVO: método para o login usar
    public boolean autenticarPorUsuario(String usuario, String senha) {
        if (usuario == null || usuario.isBlank() || senha == null) return false;

        for (Passageiro p : passageiroRepository.listarTodos()) {
            if (p.getUsuario() != null && p.getUsuario().equals(usuario)) {
                return p.getSenha() != null && p.getSenha().equals(senha);
            }
        }
        return false;
    }

    // Alternativa: login por CPF (se você preferir)
    public boolean autenticarPorCpf(String cpf, String senha) {
        Passageiro p = passageiroRepository.buscarPorCpf(cpf);
        return p != null && p.getSenha() != null && p.getSenha().equals(senha);
    }
}