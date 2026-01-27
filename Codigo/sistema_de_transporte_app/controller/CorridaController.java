
package sistema_de_transporte_app.controller;

import repository.impl.CorridaRepository;
import repository.impl.MotoristaRepository;
import repository.impl.PassageiroRepository;
import sistema_de_transporte_app.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CorridaController {
    private final CorridaRepository corridaRepository;
    private final PassageiroRepository passageiroRepository;
    private final MotoristaRepository motoristaRepository;

    public CorridaController(CorridaRepository corridaRepository,
                             PassageiroRepository passageiroRepository,
                             MotoristaRepository motoristaRepository) {
        this.corridaRepository = corridaRepository;
        this.passageiroRepository = passageiroRepository;
        this.motoristaRepository = motoristaRepository;
    }

    public double calcularValor(String categoria, double distanciaKm) {
        double base, porKm;
        switch (categoria.toUpperCase()) {
            case "LUXO": base = 8.0; porKm = 3.8; break;
            case "SUV":  base = 6.0; porKm = 3.0; break;
            default:     base = 4.0; porKm = 2.2; // ECONOMICO
        }
        double taxaPlataforma = 0.10;
        double subtotal = base + (porKm * distanciaKm);
        return Math.round((subtotal * (1 + taxaPlataforma)) * 100.0) / 100.0;
    }

    public Corrida solicitarCorrida(String cpfPassageiro, String origem, String destino,
                                    double distanciaKm, String categoria) {
        Passageiro passageiro = passageiroRepository.buscarPorCpf(cpfPassageiro);
        if (passageiro == null) throw new IllegalArgumentException("Passageiro não encontrado");

        Motorista motorista = escolherMotoristaDisponivel(categoria)
                .orElseThrow(() -> new IllegalStateException("Sem motorista disponível para " + categoria));

        double valor = calcularValor(categoria, distanciaKm);
        int tempoEstimadoMin = (int) Math.max(5, Math.round(distanciaKm * 3));

        Corrida c = new Corrida(0, passageiro, motorista, origem, destino, distanciaKm, tempoEstimadoMin, valor);
        c.iniciarCorrida();
        corridaRepository.salvar(c);
        passageiro.adicionarAoHistorico(c);
        motorista.alterarStatusDisponibilidade(false);

        return c;
    }

    public void finalizarCorrida(long id) {
        Corrida c = corridaRepository.buscarPorId(id);
        if (c != null) {
            c.finalizarCorrida();
            Motorista m = c.getMotorista();
            if (m != null) {
                m.aceitarPagamentoDinheiro(c.getValor());
                m.alterarStatusDisponibilidade(true);
            }
            corridaRepository.salvar(c);
        }
    }

    public void cancelarCorrida(long id) {
        Corrida c = corridaRepository.buscarPorId(id);
        if (c != null) {
            c.cancelarCorrida();
            Motorista m = c.getMotorista();
            if (m != null) m.alterarStatusDisponibilidade(true);
            corridaRepository.salvar(c);
        }
    }

    public List<Corrida> listarCorridas() { return corridaRepository.listarTodas(); }

    private Optional<Motorista> escolherMotoristaDisponivel(String categoria) {
        List<Motorista> todos = motoristaRepository.listarTodos();
        return todos.stream()
                .filter(Motorista::isDisponivel)
                .filter(m -> m.getVeiculo() != null && m.getVeiculo().getCategoria().equalsIgnoreCase(categoria))
                .sorted(Comparator.comparingDouble(Motorista::getAvaliacao).reversed())
                .findFirst();
    }
}