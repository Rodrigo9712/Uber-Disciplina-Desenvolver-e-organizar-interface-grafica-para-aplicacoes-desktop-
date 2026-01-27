
package sistema_de_transporte_app.controller;

import repository.impl.PassageiroRepository;
import sistema_de_transporte_app.model.Passageiro;
import java.util.List;

public class PassageiroController {
    private final PassageiroRepository passageiroRepository;

    public PassageiroController(PassageiroRepository passageiroRepository) {
        this.passageiroRepository = passageiroRepository;
    }

    public Passageiro cadastrar(String nome, String telefone, String cpf) {
        Passageiro existente = passageiroRepository.buscarPorCpf(cpf);
        if (existente != null) return existente;
        Passageiro p = new Passageiro(nome, telefone, cpf);
        return passageiroRepository.salvar(p);
    }

    public Passageiro buscarPorCpf(String cpf) { return passageiroRepository.buscarPorCpf(cpf); }

    public List<Passageiro> listarTodos() { return passageiroRepository.listarTodos(); }
}