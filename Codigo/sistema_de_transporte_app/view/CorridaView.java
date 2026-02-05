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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;

/**
 * CorridaView sem pedir tempo ou distância.
 * A distância é estimada automaticamente e de modo determinístico a partir de Origem + Destino.
 */
public class CorridaView extends JPanel {
    private final CorridaController corridaController;
    private final PassageiroController passageiroController;

    private final JTextField txtCpfPassageiro = new JTextField(14);
    private final JTextField txtOrigem = new JTextField(20);
    private final JTextField txtDestino = new JTextField(20);
    private final JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"ECONOMICO", "SUV", "LUXO"});

    private final JLabel lblEstimado = new JLabel("R$ 0,00");
    private final JLabel lblDistanciaCalc = new JLabel("Distância (estimada): 0,0 km");

    private final JTextArea output = new JTextArea(18, 72);

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

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Categoria:"), c);
        c.gridx = 1; c.gridy = row++; form.add(cbCategoria, c);

        // ---------- ESTIMATIVAS ----------
        JPanel estimativas = new JPanel(new GridBagLayout());
        GridBagConstraints ce = new GridBagConstraints();
        ce.insets = new Insets(2, 4, 2, 4);
        ce.anchor = GridBagConstraints.WEST;

        JLabel lbl1 = new JLabel("Estimativa:");
        lbl1.setFont(lbl1.getFont().deriveFont(Font.BOLD));
        lblEstimado.setFont(lblEstimado.getFont().deriveFont(Font.BOLD));

        ce.gridx = 0; ce.gridy = 0; estimativas.add(lbl1, ce);
        ce.gridx = 1; ce.gridy = 0; estimativas.add(lblEstimado, ce);

        ce.gridx = 0; ce.gridy = 1; ce.gridwidth = 2; estimativas.add(lblDistanciaCalc, ce);

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
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        output.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                new EmptyBorder(8,8,8,8)
        ));

        // ---------- LAYOUT PRINCIPAL ----------
        JPanel top = new JPanel(new BorderLayout(8,8));
        top.add(form, BorderLayout.NORTH);
        top.add(estimativas, BorderLayout.CENTER);
        top.add(botoes, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    /** Preenche o CPF após login pela TelaPrincipal. */
    public void setCpfPassageiro(String cpf) { txtCpfPassageiro.setText(cpf); }

    // ==================== AÇÕES ====================
    private void calcular() {
        try {
            String cat = categoria();
            String origem = normalizar(txtOrigem.getText());
            String destino = normalizar(txtDestino.getText());
            if (origem.isEmpty() || destino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha Origem e Destino.", "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double distKm = estimarDistanciaKmDeterministica(origem, destino, cat);
            lblDistanciaCalc.setText(String.format("Distância (estimada): %.1f km", distKm));

            double valor = corridaController.calcularValor(cat, distKm);
            lblEstimado.setText(String.format("R$ %.2f", valor));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao calcular estimativa.", "Erro", JOptionPane.ERROR_MESSAGE);
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

            String cat = categoria();
            String origem = normalizar(txtOrigem.getText());
            String destino = normalizar(txtDestino.getText());
            if (origem.isEmpty() || destino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha Origem e Destino.", "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double distKm = estimarDistanciaKmDeterministica(origem, destino, cat);
            lblDistanciaCalc.setText(String.format("Distância (estimada): %.1f km", distKm));

            Corrida corrida = corridaController.solicitarCorrida(
                    cpf, origem, destino, distKm, cat
            );
            corridaAtualId = corrida.getId();
            lblEstimado.setText(String.format("R$ %.2f", corrida.getValor()));

            imprimirCabecalho("CORRIDA SOLICITADA");
            output.append(formatarCorrida(corrida, distKm));
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

    // ==================== LÓGICA DE DISTÂNCIA SEM INPUT ====================
    private String categoria() {
        return cbCategoria.getSelectedItem().toString();
    }

    private String normalizar(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Distância estimada de forma determinística a partir de origem+destino.
     * 1) Gera um hash SHA-256 da string "origem->destino".
     * 2) Converte parte do hash em número para obter base entre 5 e 35 km.
     * 3) Aplica multiplicador leve pela categoria.
     */
    private double estimarDistanciaKmDeterministica(String origem, String destino, String categoria) {
        double baseKm = 5.0;      // mínimo
        double faixaKm = 30.0;    // varia até 35 (5 + 30)
        double numero = mapHashToUnit(origem + "->" + destino); // 0..1
        double km = baseKm + faixaKm * numero;

        double multCat;
        switch (categoria.toUpperCase()) {
            case "SUV":  multCat = 1.08; break;
            case "LUXO": multCat = 1.12; break;
            default:     multCat = 1.00; // ECONOMICO
        }
        km *= multCat;

        // arredonda para uma casa e limita entre 5.0 e 40.0 por segurança visual
        km = Math.max(5.0, Math.min(40.0, Math.round(km * 10.0) / 10.0));
        return km;
    }

    /**
     * Mapeia um hash SHA-256 para um número contínuo entre 0 e 1.
     */
    private double mapHashToUnit(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            // Usa os 8 primeiros bytes como inteiro sem sinal
            long acc = 0;
            for (int i = 0; i < 8; i++) {
                acc = (acc << 8) | (hash[i] & 0xFF);
            }
            // normaliza para 0..1 usando divisão por 2^64-1
            return (acc & 0xFFFFFFFFFFFFFFFFL) / (double) (0x1p64 - 1); // 2^64 - 1
        } catch (Exception e) {
            // fallback simples
            int h = Math.abs(input.hashCode());
            return (h % 10000) / 9999.0;
        }
    }

    // ==================== FORMATAÇÃO ====================
    private void imprimirCabecalho(String titulo) {
        output.append("\n");
        output.append("========================================\n");
        output.append(String.format("= %-38s =%n", titulo));
        output.append("========================================\n");
    }

    private void imprimirRodape() {
        output.append("========================================\n");
    }

    private String formatarCorrida(Corrida c, double distKm) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String solicitada = c.getSolicitadaEm() != null ? c.getSolicitadaEm().format(dtf) : "-";
        String finalizada = c.getFinalizadaEm() != null ? c.getFinalizadaEm().format(dtf) : "-";
        String statusBonito = c.getStatus() == null ? "-" : c.getStatus().replace('_', ' ').toUpperCase();

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
        sb.append(String.format("Distância (estimada): %.1f km%n", distKm));
        sb.append(String.format("Valor...............: R$ %.2f%n", c.getValor()));
        sb.append(String.format("Status..............: %s%n", statusBonito));
        sb.append(String.format("Solicitada em.......: %s%n", solicitada));
        sb.append(String.format("Finalizada em.......: %s%n", finalizada));
        return sb.toString();
    }

    private String valueOr(String s, String def) { return (s == null || s.isEmpty()) ? def : s; }
    private String safeName(Passageiro p) { return p == null ? "-" : valueOr(p.getNome(), "-"); }
}