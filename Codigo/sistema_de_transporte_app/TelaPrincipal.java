
package sistema_de_transporte_app;

import repository.impl.CorridaRepository;
import repository.impl.MotoristaRepository;
import repository.impl.PassageiroRepository;
import repository.impl.VeiculoRepository;

import sistema_de_transporte_app.controller.*;
import sistema_de_transporte_app.model.Passageiro;
import sistema_de_transporte_app.view.*;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    // Repositórios (no pacote repository.impl)
    private final PassageiroRepository passageiroRepo = new PassageiroRepository();
    private final MotoristaRepository motoristaRepo = new MotoristaRepository();
    private final VeiculoRepository veiculoRepo = new VeiculoRepository();
    private final CorridaRepository corridaRepo = new CorridaRepository();

    // Controllers
    private final PassageiroController passageiroController = new PassageiroController(passageiroRepo);
    private final MotoristaController motoristaController = new MotoristaController(motoristaRepo, veiculoRepo);
    private final VeiculoController veiculoController = new VeiculoController(veiculoRepo);
    private final CorridaController corridaController = new CorridaController(corridaRepo, passageiroRepo, motoristaRepo);

    // Views
    private CorridaView corridaView;
    private PassageiroView passageiroView;
    private MotoristaView motoristaView;
    private VeiculoView veiculoView;

    // UI Login
    private final JTabbedPane tabs = new JTabbedPane();
    private final JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JTextField txtCpfLogin = new JTextField(14);
    private final JLabel lblUsuario = new JLabel("Não logado");

    public TelaPrincipal() {
        super("Sistema de Transporte - Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 640);
        setLocationRelativeTo(null);

        montarLogin();
        montarTabs();

        tabs.addTab("Login", loginPanel);
        tabs.addTab("Corrida", corridaView);
        tabs.addTab("Passageiro", passageiroView);
        tabs.addTab("Motorista/Veículo", motoristaView);
        tabs.addTab("Veículos (lista)", veiculoView);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);

        habilitarAbaCorrida(false);

        seed(); // dados de exemplo
    }

    private void montarLogin() {
        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(e -> realizarLogin());

        loginPanel.add(new JLabel("CPF do Passageiro:"));
        loginPanel.add(txtCpfLogin);
        loginPanel.add(btnLogin);
        loginPanel.add(new JLabel(" | Usuário: "));
        loginPanel.add(lblUsuario);
    }

    private void montarTabs() {
        corridaView = new CorridaView(corridaController, passageiroController);
        passageiroView = new PassageiroView(passageiroController);
        motoristaView = new MotoristaView(motoristaController);
        veiculoView = new VeiculoView(veiculoController);
    }

    private void realizarLogin() {
        String cpf = txtCpfLogin.getText().trim();
        Passageiro p = passageiroController.buscarPorCpf(cpf);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "CPF não encontrado. Cadastre o passageiro na aba 'Passageiro'.", "Login", JOptionPane.WARNING_MESSAGE);
            lblUsuario.setText("Não logado");
            habilitarAbaCorrida(false);
            return;
        }
        lblUsuario.setText(p.getNome() + " (" + p.getCpf() + ")");
        corridaView.setCpfPassageiro(cpf);
        habilitarAbaCorrida(true);
        tabs.setSelectedIndex(1); // ir para Corrida
    }

    private void habilitarAbaCorrida(boolean habilitar) {
        tabs.setEnabledAt(1, habilitar);
    }

    private void seed() {
        // Passageiro de teste
        passageiroController.cadastrar("Cliente Teste", "51999990000", "11122233344");
        // Motoristas de teste (uma de cada categoria)
        motoristaController.cadastrarMotoristaComVeiculo("João Motorista", "DOC-001", "ECONOMICO", "Onix", "ABC1A23", "Prata");
        motoristaController.cadastrarMotoristaComVeiculo("Maria Driver", "DOC-002", "SUV", "Duster", "DEF4B56", "Preto");
        motoristaController.cadastrarMotoristaComVeiculo("Carlos Lux", "DOC-003", "LUXO", "BMW 320i", "GHI7C89", "Branco");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}