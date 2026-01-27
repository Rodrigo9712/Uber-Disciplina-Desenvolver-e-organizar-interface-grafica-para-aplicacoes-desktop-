
package sistema_de_transporte_app.view;

import sistema_de_transporte_app.controller.MotoristaController;
import sistema_de_transporte_app.model.Motorista;

import javax.swing.*;
import java.awt.*;

public class MotoristaView extends JPanel {
    private final MotoristaController motoristaController;

    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtDocumento = new JTextField(15);
    private final JComboBox<String> cbCategoria = new JComboBox<>(new String[]{"ECONOMICO", "SUV", "LUXO"});
    private final JTextField txtModelo = new JTextField(15);
    private final JTextField txtPlaca = new JTextField(10);
    private final JTextField txtCor = new JTextField(10);
    private final JTextArea output = new JTextArea(8, 40);

    public MotoristaView(MotoristaController motoristaController) {
        this.motoristaController = motoristaController;
        setLayout(new BorderLayout(8,8));

        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        form.add(new JLabel("Nome:")); form.add(txtNome);
        form.add(new JLabel("Documento:")); form.add(txtDocumento);
        form.add(new JLabel("Categoria:")); form.add(cbCategoria);
        form.add(new JLabel("Modelo:")); form.add(txtModelo);
        form.add(new JLabel("Placa:")); form.add(txtPlaca);
        form.add(new JLabel("Cor:")); form.add(txtCor);

        JButton btnCadastrar = new JButton("Cadastrar motorista + veículo");
        btnCadastrar.addActionListener(e -> cadastrar());

        output.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(btnCadastrar, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);
    }

    private void cadastrar() {
        try {
            Motorista m = motoristaController.cadastrarMotoristaComVeiculo(
                    txtNome.getText().trim(),
                    txtDocumento.getText().trim(),
                    cbCategoria.getSelectedItem().toString(),
                    txtModelo.getText().trim(),
                    txtPlaca.getText().trim(),
                    txtCor.getText().trim()
            );
            output.append("OK: " + m + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}