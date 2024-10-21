
// DESATUALIZADO TALVEZ EXCLUIR//////////////////////////////////////////////////////////////////

package View;

import Controller.DataBase;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelaCadastroAlunos extends JFrame {
    private JTextField raField, nomeField, cursoField;
    private JButton cadastrarButton, limparButton;
    private JLabel tituloLabel;

    public TelaCadastroAlunos() {
        super("Cadastro de Alunos");
        criartela();
    }

    private void criartela() {
        // Aplicando o tema FlatLaf
        FlatLightLaf.setup();

        // Configuração da janela principal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Painel principal com fundo degradê
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(135, 206, 235), getWidth(), getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GroupLayout(panel));
        setContentPane(panel);

        // Título estilizado
        tituloLabel = new JLabel("Cadastro de Alunos");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 22));
        tituloLabel.setForeground(new Color(30, 144, 255));

        // Campos de entrada
        raField = new JTextField(10);
        nomeField = new JTextField(10);
        cursoField = new JTextField(10);

        // Botões com ícones e estilo
        //cadastrarButton = new JButton("Cadastrar", new ImageIcon("check_icon.png"));
        //limparButton = new JButton("Limpar", new ImageIcon("clear_icon.png"));

        cadastrarButton.setBackground(new Color(50, 205, 50));
        cadastrarButton.setForeground(Color.WHITE);
        cadastrarButton.setFocusPainted(false);
        cadastrarButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true));

        limparButton.setBackground(new Color(220, 20, 60));
        limparButton.setForeground(Color.WHITE);
        limparButton.setFocusPainted(false);
        limparButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2, true));

        // Layout dos componentes
        GroupLayout layout = (GroupLayout) panel.getLayout();
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(tituloLabel)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(new JLabel("RA:"))
                                        .addComponent(new JLabel("Nome:"))
                                        .addComponent(new JLabel("Curso:"))
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(raField)
                                        .addComponent(nomeField)
                                        .addComponent(cursoField)
                                )
                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(cadastrarButton)
                                .addComponent(limparButton)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(tituloLabel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(new JLabel("RA:"))
                                .addComponent(raField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(new JLabel("Nome:"))
                                .addComponent(nomeField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(new JLabel("Curso:"))
                                .addComponent(cursoField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(cadastrarButton)
                                .addComponent(limparButton)
                        )
        );

        // Ações dos botões
        cadastrarButton.addActionListener(e -> cadastrarAluno());
        limparButton.addActionListener(e -> limparCampos());

        setVisible(true);
    }

    private void cadastrarAluno() {
        String ra = raField.getText();
        String nome = nomeField.getText();
        String curso = cursoField.getText();

        // Conexão com o banco de dados
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cadastroDeAlunos", "root", "password")) {
            String query = "INSERT INTO alunos (ra, nome, curso, matriculas_id) VALUES (?, ?, ?, 1)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, ra);
            stmt.setString(2, nome);
            stmt.setString(3, curso);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar aluno: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        raField.setText("");
        nomeField.setText("");
        cursoField.setText("");
    }
}