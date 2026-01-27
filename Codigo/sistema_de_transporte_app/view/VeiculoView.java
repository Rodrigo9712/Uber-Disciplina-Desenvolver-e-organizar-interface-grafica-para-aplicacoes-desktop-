
package sistema_de_transporte_app.view;

import sistema_de_transporte_app.controller.VeiculoController;
import sistema_de_transporte_app.model.Veiculo;

import javax.swing.*;
import java.awt.*;

public class VeiculoView extends JPanel {
    private final VeiculoController veiculoController;
    private final JTextArea output = new JTextArea(10, 40);

    public VeiculoView(VeiculoController veiculoController) {
        this.veiculoController = veiculoController;
        setLayout(new BorderLayout(8,8));
        output.setEditable(false);

        JButton btnListar = new JButton("Listar veículos");
        btnListar.addActionListener(e -> listar());

        add(btnListar, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    private void listar() {
        output.setText("=== Veículos ===\n");
        for (Veiculo v : veiculoController.listarTodos()) {
            output.append(v.toString() + "\n");
        }
    }
}