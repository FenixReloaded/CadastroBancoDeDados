package View;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class FramePrincipal extends JFrame {
    //private JFrame frame;
    private JPanel gradientPanel;
    private JButton cadastrarAluno;
    private JButton sairButton;
    private JButton creditosButton;


    public FramePrincipal(){
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception e){
            e.printStackTrace();
        }

        // Configurações da Janela Principal
        setTitle("Sistema de Cadastro de Alunos");
        setSize(600, 400);
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


        // Ações dos Botões
        cadastrarAluno.addActionListener(e -> new FormularioAlunos());
        sairButton.addActionListener(e -> System.exit(0));
        creditosButton.addActionListener(e -> mostrarCreditos());

        // Adicionando os Botões no Painel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os botões
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gradientPanel.add(cadastrarAluno, gbc); // Adiciona o botão de cadastro

        gbc.gridy = 1;
        gradientPanel.add(creditosButton, gbc); // Adiciona o botao de creditos

        gbc.gridy = 2; // Próxima linha
        gradientPanel.add(sairButton, gbc); // Adiciona o botão de sair

        // Adiciona o painel de fundo ao frame principal
        add(gradientPanel);

        // Exibe a janela
        setVisible(true);
    }

    // Método para mostrar a janela de créditos
    private void mostrarCreditos() {
        JDialog creditosDialog = new JDialog(this, "Créditos", true);
        creditosDialog.setSize(300, 300);
        creditosDialog.setLocationRelativeTo(this);

        // Painel de fundo com degradê
        GradientPanel creditosPanel = new GradientPanel("radial");
        creditosPanel.setLayout(new GridBagLayout()); // Centraliza os nomes

        // Texto dos nomes com estilo
        JLabel nomesLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "<h2 style='color: #f8f8ff;'>Créditos</h2>" +
                        "<p style='color: #f8f8ff;'>Caio Pereira Guimarães</p>" +
                        "<p style='color: #f8f8ff;'>João Pedro do Carmo Ribeiro</p>" +
                        "<p style='color: #f8f8ff;'>Lucas Kenji Hayashi</p>" +
                        "<p style='color: #f8f8ff;'>Gabriel Carlos</p>" +
                        "<p style='color: #f8f8ff;'>Pedro França</p>" +
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
