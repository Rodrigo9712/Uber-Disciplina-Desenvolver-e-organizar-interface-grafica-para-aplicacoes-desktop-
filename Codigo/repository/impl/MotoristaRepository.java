
package repository.impl;

import sistema_de_transporte_app.model.Motorista;
import java.util.*;

public class MotoristaRepository {
    private final Map<String, Motorista> map = new HashMap<>();

    public Motorista salvar(Motorista m) {
        map.put(m.getDocumentoVeiculo(), m);
        return m;
    }

    public Motorista buscarPorDocumento(String documento) { return map.get(documento); }

    public List<Motorista> listarTodos() { return new ArrayList<>(map.values()); }

    public void removerPorDocumento(String documento) { map.remove(documento); }
}