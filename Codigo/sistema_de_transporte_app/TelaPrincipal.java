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

    // Repositórios (in-memory)
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

    // UI (abas)
    private final JTabbedPane tabs = new JTabbedPane();

    // ===== Simples TELA DE LOGIN na aba "Login" =====
    private final JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JTextField txtUsuario = new JTextField(12);
    private final JPasswordField txtSenha = new JPasswordField(12);
    private final JLabel lblUsuarioLogado = new JLabel("Não logado");
    // =================================================

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

        // Desabilita a aba Corrida até logar
        habilitarAbaCorrida(false);

        // Dados de exemplo (inclui usuário/senha)
        seed();
    }

    // ----- Login simples (usuário/senha em memória) -----
    private void montarLogin() {
        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(e -> realizarLogin());

        loginPanel.add(new JLabel("Usuário:"));
        loginPanel.add(txtUsuario);
        loginPanel.add(new JLabel("Senha:"));
        loginPanel.add(txtSenha);
        loginPanel.add(btnLogin);
        loginPanel.add(new JLabel(" | Usuário: "));
        loginPanel.add(lblUsuarioLogado);
    }

    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        // Procura um passageiro cujo usuario/senha batam
        Passageiro p = passageiroController.listarTodos()
                .stream()
                .filter(x -> usuario.equals(x.getUsuario()) && senha.equals(x.getSenha()))
                .findFirst()
                .orElse(null);

        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuário ou senha incorretos!",
                    "Login inválido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Login OK → habilita Corrida e vai pra lá
        lblUsuarioLogado.setText(p.getNome());
        corridaView.setCpfPassageiro(p.getCpf()); // CPF já preenchido na tela de Corrida
        habilitarAbaCorrida(true);
        tabs.setSelectedIndex(1); // vai para Corrida
    }
    // -----------------------------------------------------

    private void montarTabs() {
        corridaView = new CorridaView(corridaController, passageiroController);
        passageiroView = new PassageiroView(passageiroController);
        motoristaView = new MotoristaView(motoristaController);
        veiculoView = new VeiculoView(veiculoController);
    }

    private void habilitarAbaCorrida(boolean habilitar) {
        tabs.setEnabledAt(1, habilitar);
    }

    private void seed() {
        // Passageiro com login/senha
        Passageiro p = new Passageiro("Rodrigo", "51999998888", "12345678900");
        p.setUsuario("Rodrigo");
        p.setSenha("123456");
        passageiroRepo.salvar(p);

        // Motoristas (uma de cada categoria)
        motoristaController.cadastrarMotoristaComVeiculo("Pedro Da Rocha", "DOC-121", "ECONOMICO", "Fiat Mobi", "BRA2E45", "Prata");
        motoristaController.cadastrarMotoristaComVeiculo("Ana Clara Oliveira", "DOC-232", "SUV", "Jeep Compass", "DEF4B56", "Preto");
        motoristaController.cadastrarMotoristaComVeiculo("André Souza", "DOC-143", "LUXO", "BMW 320i", "FMS7A02", "Branco");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}