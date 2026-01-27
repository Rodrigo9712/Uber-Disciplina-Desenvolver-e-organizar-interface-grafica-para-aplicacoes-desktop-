
package repository.impl;

import sistema_de_transporte_app.model.Corrida;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class CorridaRepository {
    private final Map<Long, Corrida> map = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    public Corrida salvar(Corrida c) {
        if (c.getId() == 0) {
            c.setId(seq.getAndIncrement());
        }
        map.put(c.getId(), c);
        return c;
    }

    public Corrida buscarPorId(long id) { return map.get(id); }

    public List<Corrida> listarTodas() { return new ArrayList<>(map.values()); }

    public void removerPorId(long id) { map.remove(id); }
}