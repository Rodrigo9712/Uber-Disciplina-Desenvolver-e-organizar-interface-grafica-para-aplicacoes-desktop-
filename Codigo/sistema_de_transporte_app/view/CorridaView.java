
package sistema_de_transporte_app.view;

import sistema_de_transporte_app.controller.CorridaController;
import sistema_de_transporte_app.controller.PassageiroController;
import sistema_de_transporte_app.model.Corrida;
import sistema_de_transporte_app.model.Motorista;
import sistema_de_transporte_app.model.Veiculo;
import sistema_de_transporte_app.model.Passageiro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class CorridaView extends JPanel {
    private final CorridaController corridaController;
    private final PassageiroController passageiroController;

    private final JTextField txtCpfPassageiro = new JTextField(14);
    private final JTextField txtOrigem = new JTextField(20);
    private final JTextField txtDestino = new JTextField(20);
    private final JTextField txtDistancia = new JTextField(6);
    private final JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"ECONOMICO", "SUV", "LUXO"});

    private final JLabel lblEstimado = new JLabel("R$ 0,00");
    private final JTextArea output = new JTextArea(16, 72);

    private Long corridaAtualId = null;

    public CorridaView(CorridaController corridaController, PassageiroController passageiroController) {
        this.corridaController = corridaController;
        this.passageiroController = passageiroController;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // ---------- FORM ----------
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        c.gridx = 0; c.gridy = row; form.add(new JLabel("CPF Passageiro (login):"), c);
        c.gridx = 1; c.gridy = row++; form.add(txtCpfPassageiro, c);

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Origem:"), c);
        c.gridx = 1; c.gridy = row++; form.add(txtOrigem, c);

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Destino:"), c);
        c.gridx = 1; c.gridy = row++; form.add(txtDestino, c);

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Distância (km):"), c);
        c.gridx = 1; c.gridy = row++; form.add(txtDistancia, c);

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Categoria:"), c);
        c.gridx = 1; c.gridy = row++; form.add(cbCategoria, c);

        // ---------- ESTIMATIVA ----------
        JPanel estimativa = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel lbl = new JLabel("Estimativa:");
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        lblEstimado.setFont(lblEstimado.getFont().deriveFont(Font.BOLD));
        estimativa.add(lbl);
        estimativa.add(lblEstimado);

        // ---------- BOTÕES ----------
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnCalcular = new JButton("Calcular valor");
        btnCalcular.addActionListener(e -> calcular());
        JButton btnSolicitar = new JButton("Solicitar corrida");
        btnSolicitar.addActionListener(e -> solicitar());
        JButton btnFinalizar = new JButton("Finalizar");
        btnFinalizar.addActionListener(e -> finalizar());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cancelar());
        botoes.add(btnCalcular); botoes.add(btnSolicitar); botoes.add(btnFinalizar); botoes.add(btnCancelar);

        // ---------- OUTPUT ----------
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13)); // monoespaçado para alinhar
        output.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(8,8,8,8)
        ));

        // ---------- LAYOUT PRINCIPAL ----------
        JPanel top = new JPanel(new BorderLayout(8,8));
        top.add(form, BorderLayout.NORTH);
        top.add(estimativa, BorderLayout.CENTER);
        top.add(botoes, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    // Permite que a TelaPrincipal preencha o CPF após o login
    public void setCpfPassageiro(String cpf) { txtCpfPassageiro.setText(cpf); }

    // ==================== AÇÕES ====================
    private void calcular() {
        try {
            double dist = Double.parseDouble(txtDistancia.getText().trim());
            String cat = cbCategoria.getSelectedItem().toString();
            double valor = corridaController.calcularValor(cat, dist);
            lblEstimado.setText(String.format("R$ %.2f", valor));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Distância inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void solicitar() {
        try {
            String cpf = txtCpfPassageiro.getText().trim();
            Passageiro p = passageiroController.buscarPorCpf(cpf);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "CPF não encontrado. Cadastre o passageiro.", "Login", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double dist = Double.parseDouble(txtDistancia.getText().trim());
            String cat = cbCategoria.getSelectedItem().toString();

            Corrida corrida = corridaController.solicitarCorrida(
                    cpf, txtOrigem.getText().trim(), txtDestino.getText().trim(), dist, cat
            );
            corridaAtualId = corrida.getId();
            lblEstimado.setText(String.format("R$ %.2f", corrida.getValor()));
            imprimirCabecalho("CORRIDA SOLICITADA");
            output.append(formatarCorrida(corrida));
            imprimirRodape();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao solicitar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizar() {
        if (corridaAtualId == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma corrida ativa.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        corridaController.finalizarCorrida(corridaAtualId);
        imprimirCabecalho("CORRIDA FINALIZADA");
        output.append(String.format("ID..................: %d%n", corridaAtualId));
        output.append("Status..............: FINALIZADA\n");
        imprimirRodape();
        corridaAtualId = null;
    }

    private void cancelar() {
        if (corridaAtualId == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma corrida ativa.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        corridaController.cancelarCorrida(corridaAtualId);
        imprimirCabecalho("CORRIDA CANCELADA");
        output.append(String.format("ID..................: %d%n", corridaAtualId));
        output.append("Status..............: CANCELADA\n");
        imprimirRodape();
        corridaAtualId = null;
    }

    // ==================== FORMATAÇÕES ====================
    private void imprimirCabecalho(String titulo) {
        output.append("\n");
        output.append("========================================\n");
        output.append(String.format("= %-38s =%n", titulo));
        output.append("========================================\n");
    }

    private void imprimirRodape() {
        output.append("========================================\n");
    }

    private String formatarCorrida(Corrida c) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String solicitada = c.getSolicitadaEm() != null ? c.getSolicitadaEm().format(dtf) : "-";
        String finalizada = c.getFinalizadaEm() != null ? c.getFinalizadaEm().format(dtf) : "-";

        String statusBonito = c.getStatus() == null ? "-"
                : c.getStatus().replace('_', ' ').toUpperCase();

        // Dados do motorista/veículo (com segurança contra null)
        Motorista mot = c.getMotorista();
        String nomeMot = mot != null ? valueOr(mot.getNome(), "-") : "-";
        String docMot = mot != null ? valueOr(mot.getDocumentoVeiculo(), "-") : "-";
        String avaliacao = mot != null ? String.format("%.1f ★", mot.getAvaliacao()) : "-";

        Veiculo v = mot != null ? mot.getVeiculo() : null;
        String categoria = v != null ? valueOr(v.getCategoria(), "-") : "-";
        String modelo    = v != null ? valueOr(v.getModelo(), "-") : "-";
        String placa     = v != null ? valueOr(v.getPlaca(), "-") : "-";
        String cor       = v != null ? valueOr(v.getCor(), "-") : "-";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("ID..................: %d%n", c.getId()));
        sb.append(String.format("Passageiro..........: %s%n", safeName(c.getPassageiro())));
        sb.append(String.format("Motorista...........: %s%n", nomeMot));
        sb.append(String.format("Documento...........: %s%n", docMot));
        sb.append(String.format("Avaliação...........: %s%n", avaliacao));
        sb.append(String.format("Categoria (carro)...: %s%n", categoria));
        sb.append(String.format("Modelo do carro.....: %s%n", modelo));
        sb.append(String.format("Placa...............: %s%n", placa));
        sb.append(String.format("Cor.................: %s%n", cor));
        sb.append(String.format("Origem..............: %s%n", valueOr(c.getOrigem(), "-")));
        sb.append(String.format("Destino.............: %s%n", valueOr(c.getDestino(), "-")));
        sb.append(String.format("Distância...........: %.1f km%n", c.getDistanciaKm()));
        sb.append(String.format("Tempo estimado......: %d min%n", c.getTempoEstimado()));
        sb.append(String.format("Valor...............: R$ %.2f%n", c.getValor()));
        sb.append(String.format("Status..............: %s%n", statusBonito));
        sb.append(String.format("Solicitada em.......: %s%n", solicitada));
        sb.append(String.format("Finalizada em.......: %s%n", finalizada));
        return sb.toString();
    }

    // Helpers
    private String valueOr(String s, String def) { return (s == null || s.isEmpty()) ? def : s; }
    private String safeName(Passageiro p) { return p == null ? "-" : valueOr(p.getNome(), "-"); }
}