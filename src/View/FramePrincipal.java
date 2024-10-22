package View;

import Controller.DataBase;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.*;

public class FramePrincipal extends JFrame {
    private JPanel gradientPanel;
    private JButton cadastrarAluno;
    private JButton sairButton;
    private JButton creditosButton;
    private JButton removerButton;
    private JButton alterarButton;
    private JButton mostrarButton;
    private String ra;
    private String novoNome;
    private int novoCursoId;
    //private JFrame frame;

    public FramePrincipal(){
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            UIManager.put("ComboBox.padding", new Insets(2, 2, 2, 2));
            UIManager.put("ComboBox.minimumWidth", 50);
            UIManager.put("ComboBox.selectionInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ComboBox.buttonSeparatorWidth", 0);
            UIManager.put("ComboBox.editorColumns", 10);

        } catch (Exception e){
            e.printStackTrace();
        }

        // Configurações da Janela Principal
        setTitle("Sistema de Cadastro de Alunos");
        setSize(800, 700);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Painel de Fundo com Degradê
        gradientPanel = new GradientPanel("diagonal");
        gradientPanel.setLayout(new GridBagLayout()); // Layout para centralizar os botões
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Espaçamento

        Font labelFont = new Font("Comic Sans MS", Font.BOLD, 13);

        // Criação e estilizacao dos Botões
        cadastrarAluno = new JButton("Cadastro de Aluno");
        cadastrarAluno.setFont(labelFont);
        cadastrarAluno.setPreferredSize(new Dimension(160, 50));
        cadastrarAluno.setContentAreaFilled(true);              // Garante que a área de conteúdo seja preenchida
        cadastrarAluno.setFocusPainted(false);                  // Remove a borda de foco
        //cadastrarAluno.setBorderPainted(false);                 // Remove a borda padrão

        sairButton = new JButton("Sair");
        sairButton.setFont(labelFont);
        sairButton.setPreferredSize(new Dimension(160, 50));
        sairButton.setContentAreaFilled(true);              // Garante que a área de conteúdo seja preenchida
        sairButton.setFocusPainted(false);                  // Remove a borda de foco

        creditosButton = new JButton("Créditos");
        creditosButton.setFont(labelFont);
        creditosButton.setPreferredSize(new Dimension(160, 50));
        creditosButton.setContentAreaFilled(true);              // Garante que a área de conteúdo seja preenchida
        creditosButton.setFocusPainted(false);                  // Remove a borda de foco

        mostrarButton = new JButton("Listar Alunos");
        mostrarButton.setFont(labelFont);
        mostrarButton.setPreferredSize(new Dimension(160, 50));
        mostrarButton.setContentAreaFilled(true);              // Garante que a área de conteúdo seja preenchida
        mostrarButton.setFocusPainted(false);                  // Remove a borda de foco

        removerButton = new JButton("Remover Aluno");
        removerButton.setFont(labelFont);
        removerButton.setPreferredSize(new Dimension(160, 50));
        removerButton.setContentAreaFilled(true);              // Garante que a área de conteúdo seja preenchida
        removerButton.setFocusPainted(false);                  // Remove a borda de foco

        alterarButton = new JButton("Alterar Cadastro");
        alterarButton.setPreferredSize(new Dimension(160, 50));
        alterarButton.setFont(labelFont);
        alterarButton.setContentAreaFilled(true);
        alterarButton.setFocusPainted(false);

        // Ações dos Botões
        cadastrarAluno.addActionListener(e -> new FormularioAlunos());
        alterarButton.addActionListener(e -> alterarCadastro(ra, novoNome, novoCursoId));
        sairButton.addActionListener(e -> System.exit(0));
        creditosButton.addActionListener(e -> mostrarCreditos());
        removerButton.addActionListener(e -> new TelaRemoverAluno());
        mostrarButton.addActionListener(e -> mostrarTabelaAlunos());

        // Adicionando os Botões no Painel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os botões
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gradientPanel.add(cadastrarAluno, gbc); // Adiciona o botão de cadastro

        gbc.gridy = 1; // Próxima linha
        gradientPanel.add(mostrarButton, gbc); // Adiciona o botão de mostrar tabela

        gbc.gridy = 2; // Próxima linha
        gradientPanel.add(removerButton, gbc); // Adiciona o botão de remover aluno

        gbc.gridy = 3;
        gradientPanel.add(creditosButton, gbc); // Adiciona o botao de creditos

        gbc.gridy = 4; // Próxima linha
        gradientPanel.add(sairButton, gbc); // Adiciona o botão de sair


        // Adiciona o painel de fundo ao frame principal
        add(gradientPanel);

        // Exibe a janela
        setVisible(true);
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

    // Método para mostrar a janela de créditos
    private void mostrarCreditos() {
        JDialog creditosDialog = new JDialog(this, "Créditos", true);
        creditosDialog.setSize(500, 500);
        creditosDialog.setLocationRelativeTo(this);

        // Painel de fundo com degradê
        GradientPanel creditosPanel = new GradientPanel("radial");
        creditosPanel.setLayout(new GridBagLayout()); // Centraliza os nomes

        // Texto dos nomes com estilo
        JLabel nomesLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "<h1 style='color: #f8f8ff;'>Créditos:</h1>" +
                        "<h2 style='color: #f8f8ff;'>Versão 1.0 Sistema de Cadastro de Alunos</h2>" +
                        "<p style='color: #f8f8ff;'>Caio Pereira Guimarães</p>" +
                        "<p style='color: #f8f8ff;'>João Pedro do Carmo Ribeiro</p>" +
                        "<p style='color: #f8f8ff;'>Lucas Kenji Hayashi</p>" +
                        "<p style='color: #f8f8ff;'>Gabriel Carlos Silva</p>" +
                        "<p style='color: #f8f8ff;'>Pedro França de Godoi</p>" +
                        "</div></html>"
        );
        nomesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nomesLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        // Adiciona o rótulo no painel de créditos
        creditosPanel.add(nomesLabel);

        // Adiciona o painel ao diálogo e exibe
        creditosDialog.add(creditosPanel);
        creditosDialog.setVisible(true);
    }

    // Mostra a tabela toda de alunos
    private void mostrarTabelaAlunos(){ // Colocar estilos flatlaf nas tabelas
        JDialog tabelaDialog = new JDialog(this, "Tabela de Alunos", true);
        tabelaDialog.setSize(800, 600);
        tabelaDialog.setLocationRelativeTo(null);

        try (Connection conexao = DataBase.conectar();
             Statement stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT Ra, Nome, Cursos_ID FROM alunos ORDER BY Nome ASC");
        ) {

            // Criacao dos parametros da tabela
            String[] nomesColunas = {"RA", "Nome", "Curso"};
            Object[][] dados = new Object[getQntAlunos()][3];

            // Insere os alunos na tabela
            for(int i=0;rs.next();i++) {
                String ra = rs.getString("Ra");
                String nome = rs.getString("Nome");
                int curso = rs.getInt("Cursos_ID");
                Object[] elemento = {ra, nome, curso};
                dados[i] = elemento;
            }

            // Painel de fundo com degradê
            GradientPanel tabelaPanel = new GradientPanel("radial");
            tabelaPanel.setLayout(null);
            tabelaPanel.setSize(800, 600);

            // Tabela em si
            JTable tabelaTable = new JTable(dados, nomesColunas);
            tabelaTable.getTableHeader().setBounds(50, 30, 700, 20);
            tabelaTable.setBounds(50, 50, 700, 20*getQntAlunos());

            // Adiciona a tabela e o Header dela no Painel de fundo
            tabelaPanel.add(tabelaTable.getTableHeader());
            tabelaPanel.add(tabelaTable);

            // Adiciona a tabela ao diálogo e exibe
            tabelaDialog.add(tabelaPanel);
            tabelaDialog.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar alunos: " + e.getMessage());
        }

    }

    // Retorna a qnt de elementos na lista de Alunos
    private int getQntAlunos(){
        int R = 0;
        try (   Connection conexao = DataBase.conectar();
                Statement stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery("SELECT COUNT(Ra) FROM alunos");
        ) {

            while(rs.next()) {
                R = rs.getInt("COUNT(Ra)");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar alunos: " + e.getMessage());
        }

        return R;
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
                            0, height / 2f, new Color(255, 255, 255, 255),
                            width, height / 2f, new Color(255, 255, 255, 181)
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
                            0, 0, new Color(50, 50, 198),
                            0, height, new Color(50, 60, 123)
                    );
                    g2d.setPaint(defaultGradient);
                    break;
            }
            g2d.fillRect(0, 0, width, height);
        }
    }
}
