
package sistema_de_transporte_app.view;

import sistema_de_transporte_app.controller.PassageiroController;
import sistema_de_transporte_app.model.Passageiro;

import javax.swing.*;
import java.awt.*;

public class PassageiroView extends JPanel {
    private final PassageiroController passageiroController;

    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtTelefone = new JTextField(15);
    private final JTextField txtCpf = new JTextField(14);
    private final JTextArea output = new JTextArea(7, 40);

    public PassageiroView(PassageiroController passageiroController) {
        this.passageiroController = passageiroController;
        setLayout(new BorderLayout(8,8));

        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        form.add(new JLabel("Nome:")); form.add(txtNome);
        form.add(new JLabel("Telefone:")); form.add(txtTelefone);
        form.add(new JLabel("CPF:")); form.add(txtCpf);

        JButton btnCadastrar = new JButton("Cadastrar passageiro");
        btnCadastrar.addActionListener(e -> cadastrar());

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(btnCadastrar, BorderLayout.SOUTH);

        output.setEditable(false);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);

        JButton btnListar = new JButton("Listar passageiros");
        btnListar.addActionListener(e -> listar());
        add(btnListar, BorderLayout.SOUTH);
    }

    private void cadastrar() {
        try {
            Passageiro p = passageiroController.cadastrar(
                    txtNome.getText().trim(),
                    txtTelefone.getText().trim(),
                    txtCpf.getText().trim()
            );
            output.append("OK: " + p + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listar() {
        output.append("=== Passageiros ===\n");
        for (Passageiro p : passageiroController.listarTodos()) {
            output.append(p.toString() + "\n");
        }
    }
}