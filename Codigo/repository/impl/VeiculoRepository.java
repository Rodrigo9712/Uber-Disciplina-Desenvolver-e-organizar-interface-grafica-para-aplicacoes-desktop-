
package repository.impl;

import sistema_de_transporte_app.model.Veiculo;
import java.util.*;

public class VeiculoRepository {
    private final Map<String, Veiculo> map = new HashMap<>();

    public Veiculo salvar(Veiculo v) {
        map.put(v.getPlaca(), v);
        return v;
    }

    public Veiculo buscarPorPlaca(String placa) { return map.get(placa); }

    public List<Veiculo> listarTodos() { return new ArrayList<>(map.values()); }

    public void removerPorPlaca(String placa) { map.remove(placa); }
}