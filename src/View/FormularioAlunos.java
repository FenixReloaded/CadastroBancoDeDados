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

public class FormularioAlunos extends JFrame {
    private JTextField nomeField;
    private JComboBox<String> cursoComboBox;
    private JButton inserirBtn;
    private JPanel panel;
    private String raGerado; // Guarda o RA gerado para o aluno
    private JButton confirmarBtn;


    public FormularioAlunos() {
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
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(labelFont);
        panel.add(nomeLabel, constraints);

        constraints.gridx = 1;
        nomeField = new JTextField(15);
        nomeField.putClientProperty("JComponent.roundRect", true);
        panel.add(nomeField, constraints);

        // Linha 2: Curso (ComboBox)
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel cursoLabel = new JLabel("Curso:");
        cursoLabel.setFont(labelFont);
        panel.add(cursoLabel, constraints);

        constraints.gridx = 1;
        cursoComboBox = new JComboBox<>();
        carregarCursos(); // Chamar para carregar os cursos ao iniciar
        panel.add(cursoComboBox, constraints); // Adicionar o comboBox ao layout
        // colocar propriedade do flatlaf


        // Linha 3: Botão Inserir
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        inserirBtn = new JButton("Inserir");
        inserirBtn.setFont(labelFont);
        inserirBtn.addActionListener(new InserirDadosListener());
        inserirBtn.putClientProperty("Button.minimumHeight", 38);
        panel.add(inserirBtn, constraints);

        add(panel);
        setVisible(true);
    }

    // Método para obter cursos do banco
    private void carregarCursos() {
        try (Connection conexao = DataBase.conectar();
             Statement stmt = conexao.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT Cursos_ID, Nomes FROM cursos")) {

            cursoComboBox.removeAllItems(); // Limpa o comboBox

            while (rs.next()) {
                int cursoId = rs.getInt("Cursos_ID");
                String cursoNome = rs.getString("Nomes");
                cursoComboBox.addItem(cursoId + " - " + cursoNome);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar cursos: " + e.getMessage());
        }
    }

    private class InserirDadosListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nome = nomeField.getText();
            String cursoSelecionado = (String) cursoComboBox.getSelectedItem();
            if (cursoSelecionado == null || cursoSelecionado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione um curso válido.");
                return;
            }

            // Extrair o ID do curso a partir do item selecionado
            int cursoId = Integer.parseInt(cursoSelecionado.split(" - ")[0]);

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nome não pode estar vazio.");
                return;
            }

            String ra = gerarRAUnico();
            int matriculaId = gerarMatricula();

            try (Connection conexao = DataBase.conectar()) {
                String sql = "INSERT INTO alunos (Ra, Nome, Cursos_ID, Matriculas_ID) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, ra);
                stmt.setString(2, nome);
                stmt.setInt(3, cursoId);
                stmt.setInt(4, matriculaId);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");

                // Chamar o método de efetuar matrícula com o ID do curso
                efetuarMatricula(matriculaId, cursoId);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir dados: " + ex.getMessage());
            }
        }
    }

    // Método para gerar RA único
    private String gerarRAUnico() {
        Random random = new Random();
        String ra;

        try (Connection conexao = DataBase.conectar()) {
            ResultSet rs;
            do {
                ra = "RA" + String.format("%08d", random.nextInt(100000000));
                String sql = "SELECT Ra FROM alunos WHERE Ra = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, ra);
                rs = stmt.executeQuery();
            } while (rs.next());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar RA: " + e.getMessage());
            return null;
        }
        return ra;
    }

    // Método para gerar número de matrícula incremental
    private int gerarMatricula() {
        int novoRegistroId = 1; // Valor inicial caso a tabela esteja vazia

        try (Connection conexao = DataBase.conectar()) {
            // Recuperar o maior Registro_ID
            String sql = "SELECT MAX(Registro_ID) FROM matriculas";
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                novoRegistroId = rs.getInt(1) + 1;
            }

            // Insere a nova matrícula com campo 'Ativa'
            String insertSql = "INSERT INTO matriculas (Registro_ID, Ativa) VALUES (?, ?)";
            PreparedStatement insertStmt = conexao.prepareStatement(insertSql);
            insertStmt.setInt(1, novoRegistroId);
            insertStmt.setBytes(2, new byte[]{1}); // Ativa como 1 (ativo)
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar matrícula: " + e.getMessage());
        }
        return novoRegistroId;
    }

    private void efetuarMatricula(int matriculaId, int cursoId) {
        JFrame frame = new JFrame("Efetuar Matrícula");
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Obter disciplinas específicas para o curso selecionado
        JComboBox<String> disciplinasBox = new JComboBox<>(obterDisciplinasDoCurso(cursoId));
        panel.add(new JLabel("Selecione a disciplina:", 0));
        panel.add(disciplinasBox);

        confirmarBtn = new JButton("Confirmar Matrícula");
        confirmarBtn.putClientProperty("Button.margin", new Insets(1,1,1,1));
        confirmarBtn.putClientProperty("Button.minimumWidth", 10);
        confirmarBtn.putClientProperty("Button.minimumHeight", 15);

        confirmarBtn.addActionListener(e -> {
            String disciplina = (String) disciplinasBox.getSelectedItem();
            JOptionPane.showMessageDialog(null, "Matrícula realizada com sucesso!\nDisciplina: " + disciplina);
            frame.dispose();
        });


        panel.add(confirmarBtn);
        frame.add(panel);
        frame.setVisible(true);
    }

    // Método para obter disciplinas do banco
    private String[] obterDisciplinasDoCurso(int cursoId) {
        try (Connection conexao = DataBase.conectar();
             Statement stmt = conexao.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT nome FROM disciplinas")) {

            // Navega até o final para contar o número de disciplinas
            rs.last();
            String[] disciplinas = new String[rs.getRow()]; // Define o tamanho do array
            rs.beforeFirst(); // Retorna para o início do ResultSet

            int i = 0;
            while (rs.next()) {
                disciplinas[i++] = rs.getString("nome");
            }
            return disciplinas;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar disciplinas: " + e.getMessage());
            return new String[]{};
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
