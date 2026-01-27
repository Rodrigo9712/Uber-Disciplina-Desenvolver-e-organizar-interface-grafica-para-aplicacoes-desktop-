
package repository.impl;

import sistema_de_transporte_app.model.Passageiro;
import java.util.*;

public class PassageiroRepository {
    private final Map<String, Passageiro> map = new HashMap<>();

    public Passageiro salvar(Passageiro p) {
        map.put(p.getCpf(), p);
        return p;
    }

    public Passageiro buscarPorCpf(String cpf) { return map.get(cpf); }

    public List<Passageiro> listarTodos() { return new ArrayList<>(map.values()); }

    public void removerPorCpf(String cpf) { map.remove(cpf); }
}