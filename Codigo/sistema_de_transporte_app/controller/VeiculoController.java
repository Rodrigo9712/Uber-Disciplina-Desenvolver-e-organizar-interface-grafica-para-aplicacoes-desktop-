
package sistema_de_transporte_app.controller;

import repository.impl.VeiculoRepository;
import sistema_de_transporte_app.model.Veiculo;

import java.util.List;

public class VeiculoController {
    private final VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public Veiculo cadastrar(String categoria, String modelo, String placa, String cor) {
        Veiculo v = veiculoRepository.buscarPorPlaca(placa);
        if (v == null) v = new Veiculo(categoria, modelo, placa, cor);
        else { v.setCategoria(categoria); v.setModelo(modelo); v.setCor(cor); }
        return veiculoRepository.salvar(v);
    }

    public List<Veiculo> listarTodos() { return veiculoRepository.listarTodos(); }
}