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
    private JButton confirmarBtn;

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

        } catch (Exception e){
            e.printStackTrace();
        }

        setTitle("Inserção de Dados - Alunos");
        setSize(400, 400);
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
        removerBtn.addActionListener(new InserirDadosListener());
        removerBtn.putClientProperty("Button.minimumHeight", 38);
        panel.add(removerBtn, constraints);

        add(panel);
        setVisible(true);
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

    private class InserirDadosListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ra = nomeField.getText();

            if (ra.isEmpty()) {
                JOptionPane.showMessageDialog(null, "RA não pode estar vazio.");
                return;
            }

            try (Connection conexao = DataBase.conectar()) {
                String sql = "DELETE FROM alunos WHERE ?;";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, ra);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Aluno removido com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao remover usuario: " + ex.getMessage());
            }
        }
    }
}