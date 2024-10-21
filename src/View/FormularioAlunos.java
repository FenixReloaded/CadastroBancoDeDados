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
    private static JComboBox<String> cursoComboBox;
    private JButton inserirBtn;
    private JPanel panel;
    private String raGerado; // Guarda o RA gerado para o aluno
    private JButton confirmarBtn;
    private JButton alterarButton;
    private String ra;
    private String novoNome;
    private int novoCursoId;



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
        carregarCursos(); // Carregar cursos na inicialização
        panel.add(cursoComboBox, constraints);

        // Linha 3: Botão Inserir
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        inserirBtn = new JButton("Inserir");
        inserirBtn.addActionListener(new InserirDadosListener());
        panel.add(inserirBtn, constraints);

        // Linha 4: Botão Alterar Cadastro
        constraints.gridy = 3;
        alterarButton = new JButton("Alterar Cadastro");
        alterarButton.addActionListener(e -> abrirFormularioAlteracao());
        panel.add(alterarButton, constraints);

        add(panel);
        setVisible(true);
    }

    private void abrirFormularioAlteracao() {
        JFrame frame = new JFrame("Atualizar Cadastro");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.add(criarFormularioAlteracao());
        frame.setVisible(true);
    }

    private JPanel criarFormularioAlteracao() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Campos de texto
        JTextField raField = new JTextField();
        JTextField nomeField = new JTextField();

        // Criar ComboBox para cursos
        JComboBox<String> cursoField = new JComboBox<>();
        carregarCursosParaComboBox(cursoField); // Carregar os cursos no ComboBox de alteração

        // Botão de atualizar
        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(e -> {
            String ra = raField.getText();
            String novoNome = nomeField.getText();
            try {
                // Extrair o ID do curso do item selecionado no ComboBox
                int novoCursoId = Integer.parseInt(cursoField.getSelectedItem().toString().split(" - ")[0]);
                atualizarCadastro(ra, novoNome, novoCursoId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID do curso inválido.");
            }
        });

        // Adicionar componentes ao painel
        painel.add(new JLabel("RA:"));
        painel.add(raField);
        painel.add(new JLabel("Novo Nome:"));
        painel.add(nomeField);
        painel.add(new JLabel("Novo Curso:"));
        painel.add(cursoField);
        painel.add(new JLabel());
        painel.add(atualizarButton);

        return painel;
    }

    // Carrega os cursos em qualquer ComboBox
    public void carregarCursosParaComboBox(JComboBox<String> comboBox) {
        try (Connection conexao = DataBase.conectar();
             Statement stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT Cursos_ID, Nomes FROM cursos ORDER BY Cursos_ID ASC")) {

            comboBox.removeAllItems(); // Limpa o comboBox antes de carregar os cursos

            while (rs.next()) {
                int cursoId = rs.getInt("Cursos_ID");
                String cursoNome = rs.getString("Nomes");
                comboBox.addItem(cursoId + " - " + cursoNome); // Exibe "ID - Nome do Curso"
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar cursos: " + e.getMessage());
        }
    }



    private void atualizarCadastro(String ra, String novoNome, int novoCursoId) {
        String sql = "UPDATE alunos SET Nome = ?, Cursos_ID = ? WHERE Ra = ?";

        try (Connection conexao = DataBase.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setInt(2, novoCursoId);
            stmt.setString(3, ra);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null, "Aluno atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Aluno não encontrado!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar aluno: " + e.getMessage());
        }
    }


    private void alterarCadastro(String ra, String novoNome, int novoCursoId){
        JFrame frame = new JFrame("Atualizar Cadastro");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.add(formularioAlterarCadastro());
        frame.setVisible(true);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String sql = "UPDATE alunos SET Nome = ?, Cursos_ID = ? WHERE Ra = ?";

        try (Connection conexao = DataBase.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setInt(2, novoCursoId);
            stmt.setString(3, ra);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null, "Aluno atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Aluno não encontrado!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    public JPanel formularioAlterarCadastro(){
        JPanel painel = new JPanel(new GridLayout(4, 2,10,10));

        JLabel raLabel = new JLabel("RA:");
        JTextField raField = new JTextField();

        JLabel nomeLabel = new JLabel("Novo Nome");
        JTextField nomeField = new JTextField();

        JLabel cursoLabel = new JLabel("Novo Curso ID:");
        JTextField cursoField = new JTextField();

        JButton atualizarButton = new JButton("Atualizar");

        atualizarButton.addActionListener(e -> {
            String ra = raField.getText();
            String novoNome = nomeField.getText();
            int novoCursoId;

            try {
                novoCursoId = Integer.parseInt(cursoField.getText());
                alterarCadastro(ra, novoNome, novoCursoId);
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "ID do curso inválido");
            }
        });

        painel.add(raLabel);
        painel.add(raField);
        painel.add(nomeField);
        painel.add(nomeLabel);
        painel.add(cursoLabel);
        painel.add(cursoField);
        painel.add(new JLabel());
        painel.add(atualizarButton);

        return painel;
    }


    // Método para obter cursos do banco
    public void carregarCursos() {
        try (Connection conexao = DataBase.conectar();
             Statement stmt = conexao.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT Cursos_ID, Nomes FROM cursos ORDER BY Cursos_ID ASC")) {

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

    // Método para obter disciplinas do banco

    private String[] obterDisciplinasDoCurso(int cursoId) {
        String query = "SELECT Nome FROM disciplinas WHERE Cursos_ID = ?";

        try (Connection conexao = DataBase.conectar();
             PreparedStatement stmt = conexao.prepareStatement(query,
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            stmt.setInt(1, cursoId); // Define o ID do curso no parâmetro

            ResultSet rs = stmt.executeQuery();

            // Conta o número de disciplinas
            rs.last();
            String[] disciplinas = new String[rs.getRow()]; // Define o tamanho do array
            rs.beforeFirst(); // Retorna para o início do ResultSet

            int i = 0;
            while (rs.next()) {
                disciplinas[i++] = rs.getString("Nome");
            }
            return disciplinas;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar disciplinas: " + e.getMessage());
            return new String[]{};
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

            String ra = gerarRAUnico(); // RA único gerado

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

                // Passar o RA diretamente ao método de matrícula
                efetuarMatricula(ra, cursoId);

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

    private void efetuarMatricula(String alunoRa, int cursoId) {
        JFrame frame = new JFrame("Efetuar Matrícula");
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Obter disciplinas específicas para o curso selecionado
        String[] disciplinas = obterDisciplinasDoCurso(cursoId);
        JCheckBox[] checkboxes = new JCheckBox[disciplinas.length];

        for (int i = 0; i < disciplinas.length; i++) {
            checkboxes[i] = new JCheckBox(disciplinas[i]);
            panel.add(checkboxes[i]);
        }

        confirmarBtn = new JButton("Confirmar Matrícula");

        confirmarBtn.addActionListener(e -> {
            try (Connection conexao = DataBase.conectar()) {
                String sql = "INSERT INTO Disciplinas_Alunos (Disciplinas_ID, Alunos_Ra, Situacao_Disciplina) " +
                        "VALUES (?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                for (JCheckBox checkbox : checkboxes) {
                    if (checkbox.isSelected()) {
                        int disciplinaId = obterIdDisciplina(checkbox.getText());
                        stmt.setInt(1, disciplinaId);
                        stmt.setString(2, alunoRa); // RA correto sendo usado
                        stmt.setString(3, "Matriculado");
                        stmt.addBatch(); // Adiciona ao batch
                    }
                }

                int[] resultados = stmt.executeBatch(); // Executa todas as inserções
                JOptionPane.showMessageDialog(null, "Matrícula realizada com sucesso! " +
                        "Disciplinas matriculadas: " + resultados.length);
                frame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao realizar matrícula: " + ex.getMessage());
            }
        });

        panel.add(confirmarBtn);
        frame.add(panel);
        frame.setVisible(true);
    }

    private int obterIdDisciplina(String nomeDisciplina) {
        String query = "SELECT Disciplinas_ID FROM disciplinas WHERE Nome = ?";
        try (Connection conexao = DataBase.conectar();
             PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setString(1, nomeDisciplina);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Disciplinas_ID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao obter ID da disciplina: " + e.getMessage());
        }
        return -1;
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
