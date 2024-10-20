package View;

import Controller.DataBase;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormularioAlunos extends JFrame {
    private JTextField raField;
    private JTextField nomeField;
    private JTextField cursoField;
    private JTextField matriculaField;
    private JButton inserirBtn;
    private JPanel panel;
    private JFrame frame;

    public FormularioAlunos(){

        setTitle("Inserção de Dados - Alunos");
        setSize(400, 400);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuração do painel com gradiente
        panel = new GradientPanel("diagonal");
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;


        // Fonte personalizada para as labels
        Font labelFont = new Font("Comic Sans MS", Font.BOLD, 13);

        // Linha 1: RA
        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel raLabel = new JLabel("RA:");
        raLabel.setFont(labelFont);  // Aplica fonte maior e negrito
        panel.add(raLabel, constraints);

        constraints.gridx = 1;
        raField = new JTextField(15);
        raField.putClientProperty("JComponent.roundRect", true);
        panel.add(raField, constraints);

        // Linha 2: Nome
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(labelFont);  // Fonte personalizada
        panel.add(nomeLabel, constraints);

        constraints.gridx = 1;
        nomeField = new JTextField(15);
        nomeField.putClientProperty("JComponent.roundRect", true);
        panel.add(nomeField, constraints);

        // Linha 3: Curso
        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel cursoLabel = new JLabel("Curso:");
        cursoLabel.setFont(labelFont);  // Fonte personalizada
        panel.add(cursoLabel, constraints);

        constraints.gridx = 1;
        cursoField = new JTextField(15);
        cursoField.putClientProperty("JComponent.roundRect", true);
        panel.add(cursoField, constraints);

        // Linha 4: Matrícula
        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel matriculaLabel = new JLabel("Matrícula:");
        matriculaLabel.setFont(labelFont);  // Fonte personalizada
        panel.add(matriculaLabel, constraints);

        constraints.gridx = 1;
        matriculaField = new JTextField(15);
        matriculaField.putClientProperty("JComponent.roundRect", true);
        panel.add(matriculaField, constraints);

        // Linha 5: Botão Inserir
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2; // O botão ocupa duas colunas
        inserirBtn = new JButton("Inserir");
        inserirBtn.setFont(labelFont);
        inserirBtn.addActionListener(new InserirDadosListener());
        panel.add(inserirBtn, constraints);

        // Adicionando o painel ao frame
        add(panel);
        setVisible(true);
    }


    private class InserirDadosListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String ra = raField.getText();
            String nome = nomeField.getText();
            String curso = cursoField.getText();
            int matriculaId;

            // Validação do campo ID Matrícula
            try {
                matriculaId = Integer.parseInt(matriculaField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID Matrícula deve ser um número inteiro.");
                return; // Sai da ação caso ocorra erro
            }

            try (Connection conexao = DataBase.conectar()) {
                String sql = "INSERT INTO alunos (ra, nome, curso, matriculas_id) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, ra);
                stmt.setString(2, nome);
                stmt.setString(3, curso);
                stmt.setInt(4, matriculaId);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Dados inseridos com sucesso!");
            } catch (SQLException ex) {
                // Tratamento de diferentes tipos de erros SQL
                if (ex.getErrorCode() == 1062) {
                    // Código de erro para chave duplicada (MySQL)
                    JOptionPane.showMessageDialog(null, "Erro: RA já cadastrado.");
                } else if (ex.getErrorCode() == 1048) {
                    // Código de erro para valor nulo em campo não nulo
                    JOptionPane.showMessageDialog(null, "Erro: Campos não podem ser nulos.");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao inserir dados: " + ex.getMessage());
                }
            } catch (Exception ex) {
                // Captura de outras exceções que não são SQL
                JOptionPane.showMessageDialog(null, "Erro inesperado: " + ex.getMessage());
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

            switch (tipoGradiente.toLowerCase()) {
                case "radial":
                    Point2D center = new Point2D.Float(width / 2f, height / 2f);
                    float radius = Math.min(width, height);
                    RadialGradientPaint radialGradient = new RadialGradientPaint(
                            center, radius,
                            new float[]{0f, 1f},
                            new Color[]{new Color(50, 58, 198), new Color(236, 236, 236)}
                    );
                    g2d.setPaint(radialGradient);
                    break;

                case "horizontal":
                    GradientPaint horizontalGradient = new GradientPaint(
                            0, height / 2f, new Color(109, 110, 125),
                            width, height / 2f, new Color(20, 122, 246, 97)
                    );
                    g2d.setPaint(horizontalGradient);
                    break;

                case "diagonal":
                    GradientPaint diagonalGradient = new GradientPaint(
                            0, 0, new Color(50, 58, 198),
                            width, height, new Color(236, 236, 236, 97)
                    );
                    g2d.setPaint(diagonalGradient);
                    break;

                default:
                    // Se nenhum tipo for especificado, aplica um gradiente simples
                    GradientPaint defaultGradient = new GradientPaint(
                            0, 0, Color.LIGHT_GRAY,
                            0, height, Color.DARK_GRAY
                    );
                    g2d.setPaint(defaultGradient);
                    break;
            }
            g2d.fillRect(0, 0, width, height);
        }
    }

    //    public void gravarDadosDB(String ra, String nome, int idade)  throws Exception {
//        try{
//            String SQL = "INSERT INTO alunos (ra, nome, idade) " + "VALUES ('"+ ra + "','"+ nome + "'," + idade + ")";
//            st.execute(SQL);
//        }catch(Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }
}
