package View;

import Controller.DataBase;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class TelaRemoverAluno extends JFrame {
    private JTextField nomeField;
    private JComboBox<String> cursoComboBox;
    private JButton removerBtn;
    private JPanel panel;
    //private JButton confirmarBtn;

    public TelaRemoverAluno() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            UIManager.put("ComboBox.padding", new Insets(4, 4, 4, 4));
            UIManager.put("ComboBox.minimumWidth", 25);
            UIManager.put("ComboBox.minimumHeight", 15);
            UIManager.put("ComboBox.selectionInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ComboBox.buttonSeparatorWidth", 1);
            UIManager.put("ComboBox.editorColumns", 1);
            UIManager.put("ComboBox.font", new Font("Comic Sans MS", Font.BOLD, 13));


            UIManager.put("Button.margin", new Insets(6, 2, 6, 2));
            UIManager.put("Button.minimumWidth", 10);
            UIManager.put("Button.minimumHeight", 35);
            UIManager.put("Button.iconTextGap", 5);
            UIManager.put("Button.font", new Font("Comic Sans MS", Font.BOLD, 13));

        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Remover Dados - Alunos");
        setSize(400, 550);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configuração do painel com gradiente
        panel = new GradientPanel("diagonal");
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Comic Sans MS", Font.BOLD, 13);

        // Linha 1: Nome
        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel nomeLabel = new JLabel("RA:");
        nomeLabel.setFont(labelFont);
        panel.add(nomeLabel, constraints);

        constraints.gridx = 1;
        nomeField = new JTextField(15);
        nomeField.putClientProperty("JComponent.roundRect", true);
        panel.add(nomeField, constraints);

        // Linha 2: Botão Remover
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        removerBtn = new JButton("Remover Aluno");
        removerBtn.setFont(labelFont);
        removerBtn.addActionListener(new RemoverListener());
        removerBtn.putClientProperty("Button.minimumHeight", 38);
        panel.add(removerBtn, constraints);

        add(panel);
        setVisible(true);
    }

    // Listener para o botao remover
    private class RemoverListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ra = nomeField.getText(); // Aqui você pode substituir por CPF, caso queira buscar por CPF

            if (ra.isEmpty()) {
                JOptionPane.showMessageDialog(null, "RA não pode estar vazio.");
                return;
            }

            try (Connection conexao = DataBase.conectar()) {
                // Verificar se o RA (ou CPF) existe no banco de dados
                try (PreparedStatement verificarAluno = conexao.prepareStatement("SELECT Ra FROM alunos WHERE Ra = ?")) {
                    verificarAluno.setString(1, ra);
                    ResultSet resultado = verificarAluno.executeQuery();

                    if (!resultado.next()) { // Se não encontrar o RA, exibe uma mensagem ao usuário
                        JOptionPane.showMessageDialog(null, "RA não encontrado no banco de dados.");
                        return;
                    }
                }

                // Desativar verificações de chave estrangeira
                try (PreparedStatement disableFKChecks = conexao.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;")) {
                    disableFKChecks.executeUpdate();
                }

                // Excluir o aluno
                try (PreparedStatement deleteAluno = conexao.prepareStatement("DELETE FROM alunos WHERE Ra = ?;")) {
                    deleteAluno.setString(1, ra);
                    deleteAluno.executeUpdate();
                }

                // Excluir registros de disciplinas vinculados ao aluno
                try (PreparedStatement deleteDisciplinas = conexao.prepareStatement("DELETE FROM disciplinas_alunos WHERE Alunos_Ra = ?;")) {
                    deleteDisciplinas.setString(1, ra);
                    deleteDisciplinas.executeUpdate();
                }

                // Reativar verificações de chave estrangeira
                try (PreparedStatement enableFKChecks = conexao.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;")) {
                    enableFKChecks.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Aluno removido com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao remover usuario: " + ex.getMessage());
            }
        }
    }

    private class GradientPanel extends JPanel {
        private String tipoGradiente;

        public GradientPanel(String tipoGradiente) {
            this.tipoGradiente = tipoGradiente;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            GradientPaint diagonalGradient = new GradientPaint(
                    0, 0, new Color(50, 58, 198),
                    width, height, new Color(236, 236, 236)
            );
            g2d.setPaint(diagonalGradient);
            g2d.fillRect(0, 0, width, height);
        }
    }
}