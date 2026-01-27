
package sistema_de_transporte_app.controller;

import repository.impl.MotoristaRepository;
import repository.impl.VeiculoRepository;
import sistema_de_transporte_app.model.Motorista;
import sistema_de_transporte_app.model.Veiculo;

import java.util.List;

public class MotoristaController {
    private final MotoristaRepository motoristaRepository;
    private final VeiculoRepository veiculoRepository;

    public MotoristaController(MotoristaRepository motoristaRepository, VeiculoRepository veiculoRepository) {
        this.motoristaRepository = motoristaRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public Motorista cadastrarMotoristaComVeiculo(String nome, String documento,
                                                  String categoria, String modelo, String placa, String cor) {
        Motorista m = motoristaRepository.buscarPorDocumento(documento);
        if (m == null) {
            m = new Motorista(nome, documento);
            motoristaRepository.salvar(m);
        }
        Veiculo v = veiculoRepository.buscarPorPlaca(placa);
        if (v == null) v = new Veiculo(categoria, modelo, placa, cor);
        else { v.setCategoria(categoria); v.setModelo(modelo); v.setCor(cor); }
        v.setMotorista(m);
        m.setVeiculo(v);
        veiculoRepository.salvar(v);
        motoristaRepository.salvar(m);
        return m;
    }

    public List<Motorista> listarTodos() { return motoristaRepository.listarTodos(); }

    public void definirDisponibilidade(String documento, boolean disponivel) {
        Motorista m = motoristaRepository.buscarPorDocumento(documento);
        if (m != null) {
            m.alterarStatusDisponibilidade(disponivel);
            motoristaRepository.salvar(m);
        }
    }
}